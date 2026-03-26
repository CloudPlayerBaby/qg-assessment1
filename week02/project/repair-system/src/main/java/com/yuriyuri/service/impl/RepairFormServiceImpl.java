package com.yuriyuri.service.impl;

import com.yuriyuri.mapper.RepairFormMapper;
import com.yuriyuri.entity.RepairForm;
import com.yuriyuri.service.RepairFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class RepairFormServiceImpl implements RepairFormService {
    @Autowired
    private RepairFormMapper repairFormMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createForm(int userId, String type, String problem,String imageUrl) {
        if (type == null || problem == null || type.isEmpty() || problem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"类型和问题不能为空");
        }

        repairFormMapper.createForm(userId, type, problem,imageUrl);
    }

    @Override
    public List<RepairForm> selectFormsByUid(int userId) {
        List<RepairForm> list = repairFormMapper.selectFormsByUid(userId);
        return list == null ? new ArrayList<>() : list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(int status, int id) {
        if (repairFormMapper.updateStatus(status, id)) {
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"更新失败，单号不存在");
        }
    }

    @Override
    public RepairForm selectFormsById(int id) {
        RepairForm repairForm = repairFormMapper.selectFormsById(id);

        if (repairForm == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"未找到报修单");
        }

        return repairForm;
    }

    @Override
    public List<RepairForm> selectAllForms() {
        List<RepairForm> list = repairFormMapper.selectAllForms();
        return list == null ? new ArrayList<>() : list;
    }

    @Override
    public List<RepairForm> selectFormsByStatus(int status) {
        List<RepairForm> list = repairFormMapper.selectFormsByStatus(status);
        return list == null ? new ArrayList<>() : list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(int id) {
        if (repairFormMapper.deleteById(id)) {
            return true;
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"删除失败");
    }

}
