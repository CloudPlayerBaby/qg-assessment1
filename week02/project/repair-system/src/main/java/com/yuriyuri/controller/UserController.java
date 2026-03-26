package com.yuriyuri.controller;

import com.yuriyuri.common.Result;
import com.yuriyuri.dto.*;
import com.yuriyuri.entity.User;
import com.yuriyuri.service.UserService;
import com.yuriyuri.util.JwtUtil;
import com.yuriyuri.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody RegisterDTO dto) {
        boolean success = userService.add(dto.getSid(), dto.getPassword(), dto.getIdentity());
        return Result.success(success);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginDTO dto) {
        User user = userService.select(dto.getSid(), dto.getPassword());
        if (user == null) return Result.fail("用户名或密码错误");

        if (!dto.getPassword().equals(user.getPassword())) {
            return Result.fail("密码错误");
        }
        //生成 token并发送给前端，发送id sid identity
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("sid", user.getSid());
        claims.put("identity",user.getIdentity().toString());
        String token = JwtUtil.genToken(claims);
        return Result.success(token);
    }

    @GetMapping("/sid")
    public Result<User> selectBySid() {
        //从 token获取用户sid
        Map<String,Object> map = ThreadLocalUtil.get();
        String sid = (String) map.get("sid");
        User user = userService.selectBySid(sid);
        return Result.success(user);
    }

    @GetMapping("/id")
    public Result<User> selectById() {
        //从 token获取用户id
        Map<String,Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        User user = userService.selectById(id);
        return Result.success(user);
    }

    @PostMapping("/dormitory")
    public Result<Void> bindDormitory(@RequestBody DormitoryDTO dto) {
        //从 token获取用户id
        Map<String,Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        userService.bindDormitory(dto.getDormitory(), id);
        return Result.success();
    }

    @PatchMapping("/dormitory")
    public Result<Void> updateDormitory(@RequestBody DormitoryDTO dto) {
        //从 token获取用户id
        Map<String,Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        userService.updateDormitory(dto.getDormitory(), id);
        return Result.success();
    }

    @PatchMapping("/password")
    public Result<String> updatePassword(@RequestBody PasswordDTO dto) {
        String oldPassword = dto.getOldPassword();
        String newPassword = dto.getNewPassword();
        String confirmPassword = dto.getConfirmPassword();

        if(confirmPassword==null||confirmPassword.isEmpty()){
            return Result.fail("确认密码不能为空");
        }

        if(!confirmPassword.equals(newPassword)){
            return Result.fail("两次密码不一致");
        }

        //从 token获取用户id
        Map<String,Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");

        userService.updatePassword(oldPassword,newPassword,id);
        return Result.success();
    }
}
