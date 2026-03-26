package com.yuriyuri.service;

import com.yuriyuri.entity.Identity;
import com.yuriyuri.entity.User;

public interface UserService {
    boolean add(String sid, String password, Identity identity);
    User select(String sid, String password);
    User selectBySid(String sid);
    User selectById(int id);
    void bindDormitory(String dormitory, int id);
    void updateDormitory(String dormitory, int id);
    void updatePassword(String oldPassword,String newPassword, int id);
}
