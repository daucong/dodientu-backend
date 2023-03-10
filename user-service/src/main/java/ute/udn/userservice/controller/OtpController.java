package com.sdt.userservice.controller;

import com.sdt.userservice.dto.ApiPassDTO;
import com.sdt.userservice.dto.JwtDTO;
import com.sdt.userservice.dto.VerifyEmailDTO;
import com.sdt.userservice.entity.User;
import com.sdt.userservice.exception.BadRequestException;
import com.sdt.userservice.service.OtpService;
import com.sdt.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
