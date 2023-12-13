package top.kuoer.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.kuoer.parameter.Message;

import java.util.List;

@Mapper
@Repository
public interface ChatGPTMapper {

    @Select("SELECT * FROM message WHERE id=#{id}")
    List<Message> getDialogueMessageList(String id);

    @Insert("INSERT INTO message (id, role, content) values (#{id}, #{role}, #{content})")
    int addDialogueMessage(@Param("id") String id, @Param("role") String role, @Param("content") String content);

    @Delete("DELETE FROM message where id=#{id}")
    int removeDialogue(@Param("id") String id);

}
