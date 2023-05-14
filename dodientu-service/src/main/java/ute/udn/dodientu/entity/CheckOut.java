package ute.udn.dodientu.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Data
public class CheckOut extends BaseEntity {
    private Long userId;
    private BigDecimal totalMoneyCheckOut;
    private String hoTen;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "wardId")
    private Ward ward;
}
