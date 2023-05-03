package ute.udn.dodientu.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomDTO {
    private HttpStatus status;
    private String message;
}