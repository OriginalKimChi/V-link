package kr.co.vlink.Vlink.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserMapping {
    private String id;
    private String name;
    private String profileImageURL;

    @Builder
    public UserMapping(String id, String name, String profileImageURL) {
        this.id = id;
        this.name = name;
        this.profileImageURL = profileImageURL;
    }

    public void updateName(String name) { this.name = name; }
    public void updateProfileImageURL(String url) { this.profileImageURL = url; }
}
