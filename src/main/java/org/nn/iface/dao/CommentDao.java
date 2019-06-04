package org.nn.iface.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.nn.iface.domain.Comment;

import java.util.List;

public interface CommentDao {

    @Select("select * from comment where starId = #{starId}")
    List<Comment> findCommentByStarId(@Param("starId") Long id);

    @Select("select * from comment where userId = #{userId}")
    List<Comment> findCommentByUserId(@Param("userId") Long id);

    @Insert("insert into comment (starId, userId, context, picture) values(#{starId}, #{userId}, #{context}, #{picture})")
    int insertComment(Comment comment);

}
