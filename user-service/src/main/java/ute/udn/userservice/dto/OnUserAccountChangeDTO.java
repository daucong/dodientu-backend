package ute.udn.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.udn.userservice.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnUserAccountChangeDTO {
    private User user;
    private String action;
    private String actionStatus;
}
