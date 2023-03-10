package com.sdt.userservice.dto;

import com.sdt.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnUserAccountChangeDTO {
    private User user;
    private String action;
    private String actionStatus;
}
