package ute.udn.dodientu.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@Entity
public class Cart extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private int qualityPay;

    private BigDecimal totalPrice;

    private Long userId;

    private String message;
}