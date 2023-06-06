package ute.udn.dodientu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostByCategoryDTO {
    private int year;
    private int month;
    private String categoryName;
    private long totalQuantity;
}
