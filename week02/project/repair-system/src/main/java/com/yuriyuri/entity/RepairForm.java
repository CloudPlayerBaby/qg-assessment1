package com.yuriyuri.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RepairForm {
    private int id;
    private int userId;
    private String type;
    private String problem;
    private int status;
    private Timestamp updateTime;
    private String imageUrl;
}
