package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

@Mapper
@Repository
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 把一条comment插入到数据库中
     * @param comment comment类
     * @return 返回值为插入的记录数目
     */
    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    /**
     * 对于多个参数来说，每个参数之前都要加上@Param注解，要不然会找不到对应的参数进而报错
     * issue : mybatis中#和$的区别
     * @param entityId 实体的id，比如question，comment的id
     * @param entityType 实体类型，question,comment等
     * @return 返回对应的comment的List
     */
    @Select({"select ", SELECT_FIELDS, " FROM ", TABLE_NAME,
            " WHERE entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    /**
     *
     * @param entityId 实体的id，比如question，comment的id
     * @param entityType 实体类型，question,comment等
     * @return 返回相关问题/评论下comment的数量
     */
    @Select({" select count(id) from ", TABLE_NAME,
            " WHERE entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCountByEntityId(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from", TABLE_NAME,
            " WHERE user_id=#{userId} and entity_type=#{entityType}"})
    int getCommentCountByUserId(@Param("userId") int userId, @Param("entityType")int entityType);

    /**
     *
     * @param id comment的id
     * @param status 默认0，0表示未删除
     * @return 返回boolean，更新status是否成功
     */
    @Update({"update ", TABLE_NAME, "set status=#{status} where id=#{id}"})
    boolean updateComment(@Param("id") int id, @Param("status") int status);

    @Select({"select ", SELECT_FIELDS, "FROM", TABLE_NAME, "where id=#{id}"})
    Comment getCommentById(int id);
}
