package kr.co.vlink.Vlink.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultDTO {
    private int code;
    private String message;
    private Object data;
}
