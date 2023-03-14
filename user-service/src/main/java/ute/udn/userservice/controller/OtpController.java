package ute.udn.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ute.udn.userservice.dto.ApiPassDTO;
import ute.udn.userservice.dto.JwtDTO;
import ute.udn.userservice.dto.VerifyEmailDTO;
import ute.udn.userservice.entity.User;
import ute.udn.userservice.exception.BadRequestException;
import ute.udn.userservice.service.OtpService;
import ute.udn.userservice.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/v1")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @PostMapping("/generateotp")
    public ResponseEntity<?> generateOtp(@Valid @RequestBody
                                                 VerifyEmailDTO emailRequest) {
        if (userService.existsByEmail(emailRequest.getEmail())) {
            User user = userService.findByEmail(emailRequest.getEmail());
            if (user.getStatus() == 1){
                return ResponseEntity.ok(new JwtDTO(user.getStatus()));
            }
            else {
                if (otpService.generateOtp(emailRequest.getEmail())) {
                    return ResponseEntity.ok(new ApiPassDTO(true, "Otp sent on email account"));
                } else {
                    throw new BadRequestException("Unable to send OTP. try again");
                }
            }
        } else {
            throw new BadRequestException("Email is not associated with any account.");
        }
    }

    @PostMapping("/validateotp")
    public ResponseEntity<?> validateOtp(@Valid @RequestBody VerifyEmailDTO emailRequest) {
        if (emailRequest.getOtpNo() != null) {
            if (otpService.validateOTP(emailRequest.getEmail(), emailRequest.getOtpNo())) {
                return ResponseEntity.ok(new ApiPassDTO(true, "OTP verified successfully"));
            }
        }
        return ResponseEntity.badRequest().body("Invalid OTP");
    }
}
