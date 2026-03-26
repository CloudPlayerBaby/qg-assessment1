package com.yuriyuri.dto;

import com.yuriyuri.entity.Identity;
import lombok.Data;

@Data
public class RegisterDTO {
    private String sid;
    private String password;
    private Identity identity;
}
