package com.sdt.userservice.dto;

import com.sdt.userservice.entity.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RechargeDTO {

    private Long id;

    private int cardNumber;

    private String cardName;

    private String email;

    private String yearMonth;

    private Bank bankId;
}
