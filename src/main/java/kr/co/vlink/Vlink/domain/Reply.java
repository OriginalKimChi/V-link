package kr.co.vlink.Vlink.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@ToString
@Document("Reply")
public class Reply {
    @Id
    private String id;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createAt;

    private UserMapping user;

    @Builder
    public Reply(String id, String content, UserMapping user) {
        this.id = id;
        this.content = content;
        this.createAt = new Date();
        this.user = user;
    }

    public void updateUser(UserMapping user) {this.user = user;}
    public void updateContent(String content) {this.content = content; }
}
