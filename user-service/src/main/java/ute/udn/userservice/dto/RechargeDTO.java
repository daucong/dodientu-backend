package ute.udn.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.udn.userservice.entity.Bank;

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
