package com.sdt.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cardNumber;

    private String cardName;

    private BigDecimal money;

    private String email;

    private String date;

    @ManyToOne
    @JoinColumn(name = "bankId")
    private Bank bankId;
}
