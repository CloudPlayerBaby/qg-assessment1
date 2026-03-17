package com.yuriyuri.service.impl;

import com.yuriyuri.mapper.UserMapper;
import com.yuriyuri.pojo.Identity;
import com.yuriyuri.pojo.User;
import com.yuriyuri.service.UserService;
import com.yuriyuri.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

//service进行逻辑处理
public class UserServiceImpl implements UserService {
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

    /**
     * 注册
     * @param sid
     * @param password
     * @param identity
     * @return
     */
    @Override
    public boolean add(String sid, String password, Identity identity) {
        //非空检验
        if (sid == null || sid.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("工号密码不能为空");
            return false;
        }

        //正则检验
        //工号检验
        if (identity == Identity.STUDENT) {
            //如果是学生，工号应以3125开头
            String regex = "^3125\\d{6}$";
            if (!sid.matches(regex)) {
                System.out.println("学生工号应以3125开头");
                return false;
            }
        } else if (identity == Identity.ADMIN) {
            //如果是管理员，工号应以0025开头
            String regex = "^0025\\d{6}$";
            if (!sid.matches(regex)) {
                System.out.println("管理员工号应以0025开头");
                return false;
            }
        }

        //密码检验
        String pwdRegex = "^[a-zA-Z0-9]{4,16}$";
        if (!password.matches(pwdRegex)) {
            System.out.println("密码应为4-16位数字和字母");
            return false;
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.add(sid, password, identity);
            sqlSession.commit();
            return true;
        }

    }

    /**
     * 根据工号和密码查询是否存在用户（登陆查询）
     * @param sid
     * @param password
     * @return
     */
    @Override
    public User select(String sid, String password) {
        //非空检验
        if (sid == null || sid.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("工号密码不能为空");
            return new User();
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.select(sid, password);
            sqlSession.commit();
            return user;
        }

    }

    /**
     * 根据sid查询用户信息
     * @param sid
     * @return
     */
    @Override
    public User selectBySid(String sid) {
        //非空检验
        if (sid == null || sid.isEmpty()) {
            return new User();
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.selectBySid(sid);
            sqlSession.commit();
            return user;
        }
    }

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @Override
    public User selectById(int id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.selectById(id);
            sqlSession.commit();
            return user;
        }
    }

    /**
     * 绑定宿舍
     *
     * @param dormitory
     * @param id
     */
    @Override
    public void bindDormitory(String dormitory, int id) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //如果有这个id
            User userInfo = mapper.selectById(id);
            if(userInfo==null) {
                //对应到cookie，这里应该是失效了，输出语句就无所谓
                System.out.println("用户不存在");
                return;
            }

            if(userInfo.getDormitory()!=null){
                //如果已经有宿舍，就不可以再绑定
                return;
            }

            if(mapper.updateDormitory(dormitory,id)){
                sqlSession.commit();
            }
        }
    }

    /**
     * 修改宿舍
     *
     * @param dormitory
     * @param id
     */
    @Override
    public void updateDormitory(String dormitory, int id) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            //如果有这个id
            User userInfo = mapper.selectById(id);
            if(userInfo==null) {
                //对应到cookie，这里应该是失效了，输出语句就无所谓
                System.out.println("用户不存在");
                return;
            }

            if(mapper.updateDormitory(dormitory,id)){
                sqlSession.commit();
            }
        }
    }

    @Override
    public boolean updatePassword(String password, int id) {
        if(password==null||password.isEmpty()){
            return false;
        }

        //密码检验
        String pwdRegex = "^[a-zA-Z0-9]{4,16}$";
        if (!password.matches(pwdRegex)) {
            System.out.println("密码应为4-16位数字和字母");
            return false;
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.updatePassword(password, id);
            sqlSession.commit();
            return true;
        }
    }


}

