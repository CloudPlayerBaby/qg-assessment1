package com.yuriyuri.service;

import com.yuriyuri.pojo.Identity;
import com.yuriyuri.pojo.User;

public interface UserService {
    boolean add(String sid, String password, Identity identity);
    User select(String sid, String password);
    User selectBySid(String sid);
    User selectById(int id);
    void bindDormitory(String dormitory, int id);
    void updateDormitory(String dormitory, int id);
    boolean updatePassword(String password, int id);

}
