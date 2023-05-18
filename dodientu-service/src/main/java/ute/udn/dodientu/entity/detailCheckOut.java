package ute.udn.dodientu.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class detailCheckOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "checkOut_id")
    private CheckOut checkOut;

    private Long postId;

    private int qualityPay;

    private BigDecimal totalPrice;

    private Long userId;

    private int status;

    private String type;

    private String messageDestroy;

}
