package com.yuriyuri.pojo;

public class User {
    private int id;
    private String sid;
    private String password;
    private Identity identity;
    private String dormitory;

    public User() {
    }

    public User(int id, String sid, String password, Identity identity,String dormitory) {
        this.id = id;
        this.sid = sid;
        this.password = password;
        this.identity = identity;
        this.dormitory = dormitory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }


    public String getDormitory() {
        return dormitory;
    }

    public void setDormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", sid='" + sid + '\'' +
                ", password='" + password + '\'' +
                ", identity=" + identity +
                ", dormitory='" + dormitory + '\'' +
                '}';
    }
}
