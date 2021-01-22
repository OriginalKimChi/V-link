package kr.co.vlink.Vlink.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PartnersMapping {
    private String id;
    private String name;
    private String profileImageURL;

    @Builder
    public PartnersMapping(String id, String name, String profileImageURL) {
        this.id = id;
        this.name = name;
        this.profileImageURL = profileImageURL;
    }
}
