package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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




}
