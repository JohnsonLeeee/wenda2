package com.nowcoder.dao;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Mapper
@Repository
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    // issue : 这里返回值为什么为int?
    // 初步解决：insert，返回值是：新插入行的主键（primary key）；需要包含<selectKey>语句，才会返回主键，否则返回值为null。
    // update/delete，返回值是：更新或删除的行数；无需指明resultClass；但如果有约束异常而删除失败，只能去捕捉异常。
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Select({"select", SELECT_FIELDS, "FROM", TABLE_NAME, "WHERE id=#{questionId}"})
    Question selectQuestionById(@Param("questionId") int questionId);

    @Update({"update ", TABLE_NAME, " SET comment_count=#{count} WHERE id=#{questionId}"})
    boolean updateCommentCount(@Param("questionId") int questionId,@Param("count") int count);



}
