package com.yuriyuri.dto;

import lombok.Data;

@Data
public class FormDTO {
    private int id;
    private int userId;
    private String type;
    private String problem;
    private int status;
    private String imageUrl;
}
