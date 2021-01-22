package kr.co.vlink.Vlink.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SearchDTO {
    private String keyword;
    private Integer page;
    private String userId;
    private String partnersId;
}
