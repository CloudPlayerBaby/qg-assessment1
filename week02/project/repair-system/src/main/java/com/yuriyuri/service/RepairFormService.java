package com.yuriyuri.service;

import com.yuriyuri.entity.RepairForm;

import java.util.List;

public interface RepairFormService {
    void createForm(int userId, String type, String problem,String imageUrl);
    List<RepairForm> selectFormsByUid(int userId);
    boolean updateStatus(int status, int id);
    RepairForm selectFormsById(int id);
    List<RepairForm> selectAllForms();
    List<RepairForm> selectFormsByStatus(int status);
    boolean deleteById(int id);
}
