package com.yuriyuri.mapper;

import com.yuriyuri.pojo.RepairForm;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface RepairFormMapper {
    @Insert("insert into repair_form values(null,#{user_id},#{type},#{problem},0,now())")
    boolean createForm(@Param("user_id")int userId,@Param("type")String type, @Param("problem")String problem);

    @Select("select * from repair_form where user_id=#{user_id}")
    @ResultMap("repairFormResultMap")
    List<RepairForm> selectFormsByUid(int userId);

    @Update("update repair_form set status=#{status} where id=#{id}")
    boolean updateStatus(@Param("status")int status,@Param("id")int id);

    @Select("select * from repair_form where id=#{id}")
    @ResultMap("repairFormResultMap")
    RepairForm selectFormsById(int id);

    @Select("select * from repair_form")
    @ResultMap("repairFormResultMap")
    List<RepairForm> selectAllForms();

    @Select("select * from repair_form where status=#{status}")
    @ResultMap("repairFormResultMap")
    List<RepairForm> selectFormsByStatus(int status);

    @Delete("delete from repair_form where id=#{id}")
    boolean deleteById(int id);
}
