package com.yuriyuri.mapper;

import com.yuriyuri.entity.Identity;
import com.yuriyuri.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {
    @Insert("insert into user values(null,#{sid},#{password},#{identity},null)")
    boolean add(@Param("sid")String sid, @Param("password")String password, @Param("identity") Identity identity);

    @Select("select * from user where sid=#{sid} and password=#{password}")
    User select(@Param("sid")String sid, @Param("password")String password);

    @Select("select * from user where sid=#{sid}")
    User selectBySid(String sid);

    @Select("select * from user where id=#{id}")
    User selectById(int id);

/*    @Update("update user set dormitory=#{dormitory} where id=#{id}")
    boolean bindDormitory(@Param("dormitory")String dormitory,@Param("id")int id);*/

    @Update("update user set dormitory=#{dormitory} where id=#{id}")
    boolean updateDormitory(@Param("dormitory")String dormitory,@Param("id")int id);

    @Update("update user set password=#{password} where id=#{id}")
    boolean updatePassword(@Param("password")String password,@Param("id")int id);
}
