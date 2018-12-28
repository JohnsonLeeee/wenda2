package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wenda
 * @description: 增加消息，查询消息
 * @author: Li Shuai
 * @create: 2018-12-25 09:44
 **/

/*
 * 解释：目的就是为了不再写mapper映射文件（那个xml写的是真的蛋疼。。。）。
 * 添加了@Mapper注解之后这个接口在编译时会生成相应的实现类
 *
 * 需要注意的是：这个接口中不可以定义同名的方法，因为会生成相同的id
 * 也就是说这个接口是不支持重载的
 */
@Mapper
@Repository
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     *
     * @param message message
     * @return 插入的条数，成功为1， 不成功为0
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") VALUES (#{fromId}, #{toId}, #{content}, #{createdDate}, #{hasRead}, #{conversationId})"})
    int addMessage(Message message);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME,
            "where conversation_id=#{conversationId} order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    /**
     *  issue: sql语句这块还要多学习；

     *
     *  统计分类,如在分组内的最大值，最小值，平均值，个数等：select count(id) from table_name group by conversation_id;
     *  分页： 后边加limit 0, 10
     * @param userId userId
     * @param offset offset
     * @param limit limit
     * @return Message的列表，其中message的id是每个会话的数量
     */
    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  " +
                    "order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME,
            "where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId,
                                   @Param("conversationId") String conversationId);

    @Update({"update", TABLE_NAME, "SET has_read=1 where to_id=#{localUserId} and conversation_id=#{conversationId}"})
    boolean setHasReadTrue(@Param("localUserId") int localUserId,
                           @Param("conversationId") String conversationId);


}
