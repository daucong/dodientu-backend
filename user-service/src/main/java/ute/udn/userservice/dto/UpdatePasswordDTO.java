package ute.udn.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDTO {
    private Long id;

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}
