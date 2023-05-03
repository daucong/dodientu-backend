package ute.udn.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayPostDTO {

    private Long userId;

    private BigDecimal moneyPost;

    private Integer statusPlus;
}
