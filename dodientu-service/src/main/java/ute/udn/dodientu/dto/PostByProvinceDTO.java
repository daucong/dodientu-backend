package ute.udn.dodientu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostByProvinceDTO {
    private int year;
    private int month;
    private String provinceName;
    private long totalQuantity;
}
