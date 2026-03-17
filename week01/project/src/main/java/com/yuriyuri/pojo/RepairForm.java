package com.yuriyuri.pojo;

import java.sql.Timestamp;

public class RepairForm {
    private int id;
    private int userId;
    private String type;
    private String problem;
    private int status;
    private Timestamp updateTime;

    public RepairForm() {
    }

    public RepairForm(int id, int userId, String type, String problem, int status, Timestamp updateTime) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.problem = problem;
        this.status = status;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "RepairForm{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", problem='" + problem + '\'' +
                ", status=" + status +
                ", updateTime=" + updateTime +
                '}';
    }
}
