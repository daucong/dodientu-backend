package com.sdt.userservice.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenDTO {
    @NotBlank
    private String refreshToken;
}
