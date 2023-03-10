package com.sdt.userservice.dto;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class LoginDTO {
    @NotBlank
    private String userName;

    private String password;

    private String typeAccount;
}
