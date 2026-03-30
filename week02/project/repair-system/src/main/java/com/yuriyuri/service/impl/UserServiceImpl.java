package com.yuriyuri.service.impl;

import com.yuriyuri.common.BusinessException;
import com.yuriyuri.mapper.UserMapper;
import com.yuriyuri.entity.Identity;
import com.yuriyuri.entity.User;
import com.yuriyuri.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
//service进行逻辑处理
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     *
     * @param sid
     * @param password
     * @param identity
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(String sid, String password, Identity identity) {
        //非空检验
        if (sid == null || sid.isEmpty() || password == null || password.isEmpty()) {
            throw new BusinessException(400,"工号和密码不能为空");
        }

        //正则检验
        //工号检验
        if (identity == Identity.STUDENT) {
            //如果是学生，工号应以3125开头
            String regex = "^3125\\d{6}$";
            if (!sid.matches(regex)) {
                throw new BusinessException(400,"学生工号应以3125开头");
            }
        } else if (identity == Identity.ADMIN) {
            //如果是管理员，工号应以0025开头
            String regex = "^0025\\d{6}$";
            if (!sid.matches(regex)) {
                throw new BusinessException(400,"学生工号应以0025开头");
            }
        }

        //密码检验
        String pwdRegex = "^[a-zA-Z0-9]{4,16}$";
        if (!password.matches(pwdRegex)) {
            throw new BusinessException(400,"密码应为4-16位数字和字母");
        }

        if (userMapper.add(sid, password, identity)) {
            return true;
        } else {
            throw new BusinessException(500,"注册失败，系统错误");
        }
    }

    /**
     * 根据工号和密码查询是否存在用户（登陆查询）
     *
     * @param sid
     * @param password
     * @return
     */
    @Override
    public User select(String sid, String password) {
        //非空检验
        if (sid == null || sid.isEmpty() || password == null || password.isEmpty()) {
            throw new BusinessException(400,"工号和密码不能为空");
        }

        return userMapper.select(sid, password);
    }

    /**
     * 根据sid查询用户信息
     *
     * @param sid
     * @return
     */
    @Override
    public User selectBySid(String sid) {
        //非空检验
        if (sid == null || sid.isEmpty()) {
            throw new BusinessException(400,"工号不能为空");
        }

        return userMapper.selectBySid(sid);
    }

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    public User selectById(int id) {
        if (id <= 0) {
            throw new BusinessException(400,"id必须大于0");
        }
        return userMapper.selectById(id);
    }

    /**
     * 绑定宿舍
     *
     * @param dormitory
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindDormitory(String dormitory, int id) {

        //如果有这个id
        User userInfo = userMapper.selectById(id);

        if (userInfo == null) {
            throw new BusinessException(404,"用户不存在");
        }

        if (userInfo.getDormitory() != null) {
            //如果已经有宿舍，就不可以再绑定
            throw new BusinessException(409,"已经绑定宿舍了");
        }

        userMapper.updateDormitory(dormitory, id);
    }

    /**
     * 修改宿舍
     *
     * @param dormitory
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDormitory(String dormitory, int id) {

        //如果有这个id
        User userInfo = userMapper.selectById(id);
        if (userInfo == null) {
            throw new BusinessException(404,"用户不存在");
        }

        if (!userMapper.updateDormitory(dormitory, id)) {
            throw new BusinessException(500,"更新宿舍失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String oldPassword,String newPassword, int id) {
        if(id<=0){
            throw new BusinessException(400,"id必须大于0");
        }

        if (oldPassword == null || oldPassword.isEmpty()) {
            throw new BusinessException(400,"旧密码不能为空");
        }

        if (newPassword == null || newPassword.isEmpty()) {
            throw new BusinessException(400,"新密码不能为空");
        }

        //密码检验
        String pwdRegex = "^[a-zA-Z0-9]{4,16}$";
        if (!newPassword.matches(pwdRegex) || !oldPassword.matches(pwdRegex)) {
            throw new BusinessException(400, "密码应为4-16位数字和字母组成");
        }

        User user = userMapper.selectById(id);
        if(user==null){
            throw new BusinessException(404,"用户不存在");
        }

        if(!user.getPassword().equals(oldPassword)){
            throw new BusinessException(400, "旧密码错误");
        }

        if (!userMapper.updatePassword(newPassword, id)) {
            throw new BusinessException(500,"系统错误");
        }
    }
}
