package ute.udn.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;

    private String firstName;

    private String lastName;

    private String email;

    private Boolean emailVerified = false;

    private String phone;

    private String address;

    private String password;

    private int status = 0;

    private String avatar;

    private BigDecimal surplus;

    @ManyToOne
    @JoinColumn(name = "typeUserId")
    private TypeUser typeUserId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userRole",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    private String resetPasswordToken;

    public User(String userName, String firstName, String lastName, String email, String phone, String password, TypeUser typeUserId) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.typeUserId = typeUserId;
    }

    public User(Long id, String userName, String firstName, String lastName,  String address, String email, String phone, int status, String password, TypeUser typeUserId) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.password = password;
        this.typeUserId = typeUserId;
    }
}
