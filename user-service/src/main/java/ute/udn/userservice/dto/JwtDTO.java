package com.sdt.userservice.dto;

import com.sdt.userservice.entity.TypeUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JwtDTO implements Serializable {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private int status;
    private TypeUser typeUser;

    private List<String> roles;

    public JwtDTO(String accessToken, String refreshToken, Long id, String userName, String email,
                  String firstName, String lastName, String phone, int status, TypeUser typeUser, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.status = status;
        this.typeUser = typeUser;
        this.roles = roles;
    }

    public JwtDTO(int status){
        this.status = status;
    }
}