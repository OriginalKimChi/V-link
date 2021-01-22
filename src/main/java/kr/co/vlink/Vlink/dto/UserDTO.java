package kr.co.vlink.Vlink.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String email;
    private String password;
    private String name;
    private String profileImageURL;
    private String phoneNumber;
    private String address;
    private String gender;
    private Integer birth;

    private String authCode;
}
