package ute.udn.userservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class VerifyEmailDTO {
    @NotBlank
    @Email
    private String email;

    private Integer otpNo;
}
