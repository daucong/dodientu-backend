package ute.udn.userservice.dto;

import lombok.Data;
import ute.udn.userservice.entity.TypeUser;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class SignUpDTO {

    @NotBlank
    private String userName;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;

    private TypeUser typeUser;

    private Set<String> role;
}
