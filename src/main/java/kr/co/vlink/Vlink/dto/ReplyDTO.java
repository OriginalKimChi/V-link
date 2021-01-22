package kr.co.vlink.Vlink.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReplyDTO {
    private String replyId;
    private String userId;
    private String videoId;
    private String content;
}
