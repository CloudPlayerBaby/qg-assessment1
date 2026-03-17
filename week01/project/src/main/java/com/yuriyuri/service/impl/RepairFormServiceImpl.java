package com.yuriyuri.service.impl;

import com.yuriyuri.mapper.RepairFormMapper;
import com.yuriyuri.pojo.RepairForm;
import com.yuriyuri.service.RepairFormService;
import com.yuriyuri.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class RepairFormServiceImpl implements RepairFormService {
    SqlSessionFactory sqlSessionFactory = SqlSessionFactoryUtil.getSqlSessionFactory();

    @Override
    public boolean createForm(int userId, String type, String problem) {
        if (type == null || problem == null || type.isEmpty() || problem.isEmpty()) {
            return false;
        }

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RepairFormMapper mapper = sqlSession.getMapper(RepairFormMapper.class);
            if (mapper.createForm(userId, type, problem)) {
                sqlSession.commit();
                return true;
            } else {
                System.out.println("创建失败，未知错误");
                return false;
            }
        }

    }

    @Override
    public List<RepairForm> selectFormsByUid(int userId) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RepairFormMapper mapper = sqlSession.getMapper(RepairFormMapper.class);
            List<RepairForm> repairForm = mapper.selectFormsByUid(userId);

            if (repairForm == null) {
                System.out.println("还没有报修单哦");
                return null;
            }

            return repairForm;
        }
    }

    @Override
    public boolean updateStatus(int status, int id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RepairFormMapper mapper = sqlSession.getMapper(RepairFormMapper.class);
            if (mapper.updateStatus(status, id)) {
                sqlSession.commit();
                return true;
            } else {
                System.out.println("更新失败，请确认单号");
                return false;
            }
        }
    }

    @Override
    public RepairForm selectFormsById(int id) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            RepairFormMapper mapper = sqlSession.getMapper(RepairFormMapper.class);
            RepairForm repairForm = mapper.selectFormsById(id);

            if (repairForm == null) {
                return null;
            }

            return repairForm;
        }
    }

    @Override
    public List<RepairForm> selectAllForms() {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            RepairFormMapper mapper = sqlSession.getMapper(RepairFormMapper.class);
            return mapper.selectAllForms();
        }
    }

    @Override
    public List<RepairForm> selectFormsByStatus(int status) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            RepairFormMapper mapper = sqlSession.getMapper(RepairFormMapper.class);
            return mapper.selectFormsByStatus(status);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try(SqlSession sqlSession = sqlSessionFactory.openSession()){
            RepairFormMapper mapper = sqlSession.getMapper(RepairFormMapper.class);
            if(mapper.deleteById(id)){
                sqlSession.commit();
                return true;
            }
        }
        return false;
    }


}
