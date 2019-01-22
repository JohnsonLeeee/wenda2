package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Feed;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2019-01-22 17:36
 **/

@Mapper
@Repository
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = "  user_id,  created_date, data, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;


    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{createdDate},#{data},#{type})"})
    int addFeed(Feed feed);

    @Select({"SELECT ", SELECT_FIELDS, "FROM ", TABLE_NAME, "WHERE id=#{id}"})
    Feed getFeedById(@Param("id") int id);

    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
