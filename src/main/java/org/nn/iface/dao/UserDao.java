package org.nn.iface.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nn.iface.domain.User;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {

    @Select("select * from user where name = #{name}")
    User findPasswordByName(@Param("name") String name);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Long id);

    @Insert("insert into user (name, password, pic, sex, sign) values(#{name}, #{password}, #{pic}, #{sex}, #{sign})")
    int insertUser(User user);

}
