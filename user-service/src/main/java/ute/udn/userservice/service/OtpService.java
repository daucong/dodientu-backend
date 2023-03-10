package com.sdt.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private OtpGenerator otpGenerator;

    @Autowired
    private EmailSenderService emailService;

    public Boolean generateOtp(String key) {
        Integer otpValue = otpGenerator.generateOTP(key);
        if (otpValue == -1) {
            return false;
        }
        String message = "Your One Time Password is " + otpValue + ". This OTP is valid for 5 minutes.";

        return emailService.sendSimpleMail(key, "Password Reset", message);
    }

    public Boolean generateOtpAcceptPayment(String key) {
        Integer otpValue = otpGenerator.generateOTP(key);
        if (otpValue == -1) {
            return false;
        }
        String message = "Your OTP is " + otpValue + ". This OTP is valid for 5 minutes.";

        return emailService.sendSimpleMail(key, "OTP", message);
    }

    public Boolean validateOTP(String key, Integer otpNumber) {
        Integer cacheOTP = otpGenerator.getOtp(key);
        if (cacheOTP.equals(otpNumber)) {
            otpGenerator.clearOTP(key);
            return true;
        }
        return false;
    }
}