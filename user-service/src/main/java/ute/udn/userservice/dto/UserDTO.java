package com.sdt.userservice.dto;

import com.sdt.userservice.entity.TypeUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private String address;

    private int status;

    private TypeUser typeUser;

    private Set<String> role;

    public UserDTO(Long id, String email, String password, int status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
    }
}
