package com.sdt.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyEmailPaymentDTO {
    @NotBlank
    @Email
    private String email;

    private Integer otpNo;

    private BigDecimal money;
}
