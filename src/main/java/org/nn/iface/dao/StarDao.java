package org.nn.iface.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nn.iface.domain.Star;
import org.springframework.stereotype.Component;

@Component
public interface StarDao {

    @Insert("insert into star(name, introduce, contentPhotos, works) values(#{name}, #{introduce}, #{contentPhotos}, #{works})")
    @Options(useGeneratedKeys = true)
    int insertStar(Star star);


    @Select("select * from star where name = #{name}")
    Star findStarByName(@Param("name")String name);

}
