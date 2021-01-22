package kr.co.vlink.Vlink.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Document("Partners")
public class Partners {
    @Id
    private String id;
    private String email;
    private String name;
    private String profileImageURL;
    private List<ExternalAccount> snsAccount;
    private ExternalAccount blogURL;
    private List<String> interestField;

    private String password;
    private boolean authState;
    private boolean firstLogin;

    private Integer followerCount;

    @DBRef
    private List<Video> videoList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createAt;

    @Builder
    public Partners(String email, String name, String profileImage, List<ExternalAccount> snsAccount, ExternalAccount blogURL, List<String> interestField, boolean authState, boolean firstLogin) {
        this.email = email;
        this.name = name;
        this.profileImageURL = profileImage;
        this.snsAccount = snsAccount;
        this.blogURL = blogURL;
        this.interestField = interestField;
        this.authState = authState;
        this.firstLogin = firstLogin;
        this.followerCount = 0;
        this.createAt = new Date();
    }

    public void updateName(String name) { this.name = name; }
    public void updatePassword(String password) {
        this.password = password;
    }
    public void updateAuthState(boolean state) { this.authState = state; }
    public void updateFirstLogin(boolean state) { this.firstLogin = state; }
    public void updateVideoList(List<Video> videoList) { this.videoList = videoList; }
    public void updateProfileImageURL(String url) { this.profileImageURL = url; }
    public void updateFollowerCount(int count) { this.followerCount = count; }

    public int indexOfVideoList(String videoId) {
        for(int i = 0; i < this.videoList.size(); i++) {
            if(this.videoList.get(i).getId().equals(videoId)) return i;
        }
        return -1;
    }
}
