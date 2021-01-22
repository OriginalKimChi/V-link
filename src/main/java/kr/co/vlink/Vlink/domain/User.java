package kr.co.vlink.Vlink.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.vlink.Vlink.dto.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@ToString
@Document("User")
public class User {

    @Id
    private String id;
    private String email;
    private String password;
    private String name;
    private String profileImageURL;
    private String phoneNumber;
    private String address;
    private String gender;
    private Integer birth;

    //mail authentication
    private String authCode;
    private Date authTime;
    private Boolean authState;

    private List<VideoMapping> savedVideoList;
    @DBRef
    private List<Product> savedProductList;

    private List<PartnersMapping> followList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createAt;

    @Builder
    public User(String id, String email, String password, String name, String profileImageURL, String phoneNumber, String address, String gender, Integer birth, String authCode, Date authTime, boolean authState) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImageURL = profileImageURL;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.birth = birth;
        this.authCode = authCode;
        this.authTime = authTime;
        this.authState = authState;
        this.followList = new ArrayList<>();
        this.savedProductList = new ArrayList<>();
        this.savedVideoList = new ArrayList<>();
        this.createAt = new Date();
    }

    public void updatePassword(String password) {
        this.password = password;
    }
    public void updateAuthState(boolean state) {
        this.authState = state;
    }
    public void updateAuthCode(String authCode) {
        this.authCode = authCode;
        this.authTime = new Date();
    }
    public void updateFollowList(List<PartnersMapping> partnersList) { this.followList = followList; }
    public void updateSavedVideoList(List<VideoMapping> videoList) {
        this.savedVideoList = videoList;
    }
    public void updateSavedProductList(List<Product> productList) {
        this.savedProductList = productList;
    }

    public void updateInfo(UserDTO userDTO) {
        if (userDTO.getName() != null) this.name = userDTO.getName();
        if (userDTO.getProfileImageURL() != null) this.profileImageURL = userDTO.getProfileImageURL();
        if (userDTO.getPhoneNumber() != null) this.phoneNumber = userDTO.getPhoneNumber();
        if (userDTO.getAddress() != null) this.address = userDTO.getAddress();
        if (userDTO.getGender() != null) this.gender = userDTO.getGender();
        if (userDTO.getBirth() != null) this.birth = userDTO.getBirth();
    }

    public int indexOfFollower(String partnersId) {
        for(int i = 0; i < this.followList.size(); i++) {
            if(this.followList.get(i).getId().equals(partnersId)) return i;
        }
        return -1;
    }
    public int indexOfVideo(String videoId) {
        for(int i = 0; i < this.savedVideoList.size(); i++) {
            if(this.savedVideoList.get(i).getId().equals(videoId)) return i;
        }
        return -1;
    }
    public int indexOfProduct(String productId) {
        for(int i = 0; i < this.savedProductList.size(); i++) {
            if(this.savedProductList.get(i).getId().equals(productId)) return i;
        }
        return -1;
    }
}
