package com.yuriyuri.dto;

import com.yuriyuri.entity.Identity;
import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String sid;
    private String password;
    private Identity identity;
    private String dormitory;
}
