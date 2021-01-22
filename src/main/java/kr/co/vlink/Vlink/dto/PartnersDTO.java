package kr.co.vlink.Vlink.dto;

import kr.co.vlink.Vlink.domain.ExternalAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PartnersDTO {
    private String id;
    private String email;
    private String name;
    private String profileImageURL;
    private List<ExternalAccount> snsAccount;
    private ExternalAccount blogURL;
    private List<String> interestField;

    private String password;
    private String newPassword;
}
