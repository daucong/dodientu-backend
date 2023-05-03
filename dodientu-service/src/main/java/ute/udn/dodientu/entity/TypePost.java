package ute.udn.dodientu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.*;
import java.util.List;

@Data
@Entity
public class TypePost extends BaseEntity {

    private String typePostName;

    @Column(precision = 9, scale = 3)
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal typePostPrice;

    public void pricePrecisionConvertion() {
        this.typePostPrice = this.typePostPrice.setScale(3, RoundingMode.HALF_UP);
    }

    @PostLoad
    public void priceLoadConvertion() {
        this.typePostPrice = this.typePostPrice.setScale(3, RoundingMode.HALF_UP);
    }

    @OneToMany(mappedBy = "typePost")
    @JsonIgnore
    private List<Post> posts;
}