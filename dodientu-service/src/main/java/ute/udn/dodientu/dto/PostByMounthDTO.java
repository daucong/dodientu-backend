package ute.udn.dodientu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostByMounthDTO {
    private int year;
    private int month;
    private long totalQuantity;
}
