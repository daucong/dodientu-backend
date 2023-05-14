package ute.udn.userservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentNotOtpDTO {
    private String email;

    private BigDecimal money;
}
