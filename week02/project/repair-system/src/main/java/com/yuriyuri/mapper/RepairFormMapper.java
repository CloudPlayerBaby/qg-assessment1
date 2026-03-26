package com.yuriyuri.mapper;

import com.yuriyuri.entity.RepairForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RepairFormMapper {
    @Insert("insert into repair_form values(null,#{user_id},#{type},#{problem},0,now(),#{image_url})")
    void createForm(@Param("user_id")int userId,@Param("type")String type, @Param("problem")String problem,@Param("image_url")String imageUrl);

    @Select("select * from repair_form where user_id=#{user_id}")
    List<RepairForm> selectFormsByUid(int userId);

    @Update("update repair_form set status=#{status} where id=#{id}")
    boolean updateStatus(@Param("status")int status,@Param("id")int id);

    @Select("select * from repair_form where id=#{id}")
    RepairForm selectFormsById(int id);

    @Select("select * from repair_form")
    List<RepairForm> selectAllForms();

    @Select("select * from repair_form where status=#{status}")
    List<RepairForm> selectFormsByStatus(int status);

    @Delete("delete from repair_form where id=#{id}")
    boolean deleteById(int id);
}
