package com.sdt.userservice.controller;

import com.sdt.userservice.dto.*;
import com.sdt.userservice.entity.*;
import com.sdt.userservice.exception.BadRequestException;
import com.sdt.userservice.exception.UpdatePasswordException;
import com.sdt.userservice.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> list() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        Set<String> strRoles = userDTO.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = userService.getOneByRoleName(ERole.ROLE_USER);
            if (userRole == null) throw new RuntimeException("Error: Role is not found.");
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = userService.getOneByRoleName(ERole.ROLE_ADMIN);
                        if (adminRole == null) throw new RuntimeException("Error: Role is not found.");
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = userService.getOneByRoleName(ERole.ROLE_USER);
                        if (userRole == null) throw new RuntimeException("Error: Role is not found.");
                        roles.add(userRole);
                }
            });
        }

        User oldUser = userService.getOneById(userDTO.getId());
        if (userDTO.getPassword() == null || userDTO.getPassword().equals("")) {
            userDTO.setPassword(oldUser.getPassword());
        } else {
            userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        }

        userDTO.setUserName(oldUser.getUserName());
        userDTO.setStatus(oldUser.getStatus());

        User user = new User(userDTO.getId(), userDTO.getUserName(), userDTO.getFirstName(),
                userDTO.getLastName(), userDTO.getAddress(),
                userDTO.getEmail(), userDTO.getPhone(),
                userDTO.getStatus(),
                userDTO.getPassword(), userDTO.getTypeUser());
        user.setRoles(roles);
        userService.saveUser(user);

        return ResponseEntity.ok(new MessageDTO("User update successfully!"));
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") Long id) {
        return userService.getOneById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @PostMapping("/password")
    public ResponseEntity updateUserPassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        return authService.updatePassword(updatePasswordDTO)
                .map(updatedUser -> {
                    OnUserAccountChangeDTO onUserPasswordChangeEvent = new OnUserAccountChangeDTO(updatedUser, "Update Password", "Change successful");
                    applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);
                    return ResponseEntity.ok(new ApiPassDTO(true, "Password changed successfully"));
                })
                .orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));
    }

    @PostMapping("/checkRecharge")
    public ResponseEntity<?> recharge(@RequestBody RechargeDTO rechargeDTO) {
        if (rechargeDTO.getEmail() != null) {

            AccountBank accountBank = accountBankService.getAllByEmail(rechargeDTO.getEmail());
            if (Objects.isNull(accountBank)) {
                return ResponseEntity.ok(new MessageDTO("Email incorrect!!"));
            }
            if (!rechargeDTO.getBankId().getId().equals(accountBank.getBankId().getId())) {
                return ResponseEntity.ok(new MessageDTO("Your Bank account does not exist!!"));
            }
            if (!rechargeDTO.getCardName().equals(accountBank.getCardName())) {
                return ResponseEntity.ok(new MessageDTO("Card name incorrect!!"));
            }
            if (rechargeDTO.getCardNumber() != accountBank.getCardNumber()) {
                return ResponseEntity.ok(new MessageDTO("Card number incorrect!!"));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth date = YearMonth.parse(rechargeDTO.getYearMonth(), formatter);
            YearMonth dateOld = YearMonth.parse(accountBank.getDate(), formatter);

            if (!date.equals(dateOld)) {
                return ResponseEntity.ok(new MessageDTO("Month/Year incorrect!!"));
            }
            if (otpService.generateOtpAcceptPayment(rechargeDTO.getEmail())) {
                return ResponseEntity.ok(new ApiPassDTO(true, "Otp sent on email account"));
            } else {
                throw new BadRequestException("Unable to send OTP. try again");
            }

        } else {
            return ResponseEntity.ok(new MessageDTO("Email null!!"));
        }
    }

    @PostMapping("/payment")
    public ResponseEntity payment(@RequestBody VerifyEmailPaymentDTO emailPaymentDTO) {
        User user = userService.findByEmail(emailPaymentDTO.getEmail());
        if (Objects.isNull(user)) {
            return ResponseEntity.ok(new MessageDTO("Email incorrect!!"));
        }
        if (Objects.isNull(user.getSurplus())){
            user.setSurplus(BigDecimal.valueOf(0));
        }
        AccountBank accountBank = accountBankService.getAllByEmail(user.getEmail());

        if (emailPaymentDTO.getOtpNo() != null) {
            if (otpService.validateOTP(emailPaymentDTO.getEmail(), emailPaymentDTO.getOtpNo())) {
                BigDecimal money = accountBank.getMoney().subtract(emailPaymentDTO.getMoney());
                accountBank.setMoney(money);
                user.setSurplus(emailPaymentDTO.getMoney().add(user.getSurplus()));

                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                TransactionHistory transactionHistory = new TransactionHistory();
                transactionHistory.setName("On the " + formatter.format(date) + " you have recharge with amount of money: "
                        + emailPaymentDTO.getMoney() + ". Surplus currently still: " + money);
                transactionHistory.setUserId(user);

                transactionHistoryService.saveOrUpdate(transactionHistory);
                userService.saveUser(user);
                accountBankService.saveOrUpdate(accountBank);
            } else {
                return ResponseEntity.ok(new MessageDTO("Otp incorrect!!"));
            }
        } else {
            return ResponseEntity.ok(new MessageDTO("Otp can't null!"));
        }
        return ResponseEntity.ok(new MessageDTO("Payment success"));
    }

    @PostMapping("/payPost")
    public ResponseEntity<?> payPost(@RequestBody PayPostDTO payPostDTO) {
        User user = userService.getOneById(payPostDTO.getUserId());
        BigDecimal money = user.getSurplus().subtract(payPostDTO.getMoneyPost());
        user.setSurplus(money);
        userService.saveUser(user);
        return ResponseEntity.ok(new MessageDTO("Payment success"));
    }

    @PostMapping("/plushPost")
    public ResponseEntity<?> plushPost(@RequestBody PayPostDTO payPostDTO) {
        User user = userService.getOneById(payPostDTO.getUserId());
        BigDecimal money = user.getSurplus().add(payPostDTO.getMoneyPost());
        user.setSurplus(money);
        userService.saveUser(user);
        return ResponseEntity.ok(new MessageDTO("Success"));
    }
}
