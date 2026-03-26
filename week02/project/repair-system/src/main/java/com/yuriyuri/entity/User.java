package com.yuriyuri.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {
    private int id;
    private String sid;
    @JsonIgnore
    private String password;
    private Identity identity;
    private String dormitory;
}
