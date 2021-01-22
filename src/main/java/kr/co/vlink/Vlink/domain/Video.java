package kr.co.vlink.Vlink.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Getter
@ToString
@NoArgsConstructor
@Document("Video")
public class Video {
    @Id
    private String id;
    private String videoName;
    private List<String> tagList;
    private String videoURL;
    private String thumbnailImageURL;
    private List<VideoProduct> productList;
    private Integer playTime;

    private PartnersMapping partners;

    @DBRef
    private List<Reply> replyList;
    private List<String> likeUserList;

    private int viewCount;
    private int likeCount;
    private int replyCount;

    private Map<String, Integer> addressStatics;
    private Map<String, Integer> genderStatics;
    private Map<String, Integer> birthStatics;

    @Transient
    private Boolean isLikedVideo;
    @Transient
    private Boolean isSavedVideo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date modifiedDate;

    @Builder
    public Video(String id, String videoName, List<String> tagList, String videoURL, String thumbnailImageURL, List<VideoProduct> productList, Integer playTime, PartnersMapping partners, Date modifiedDate) {
        this.id = id;
        this.videoName = videoName;
        this.tagList = tagList;
        this.videoURL = videoURL;
        this.thumbnailImageURL = thumbnailImageURL;
        this.productList = productList;
        this.playTime = playTime;
        this.partners = partners;
        this.addressStatics = new HashMap<>();
        this.genderStatics = new HashMap<>();
        this.birthStatics = new HashMap<>();
        this.likeUserList = new ArrayList<>();
        this.replyList = new ArrayList<>();
        this.viewCount = 0;
        this.likeCount = 0;
        this.replyCount = 0;
        this.createAt = new Date();
        this.modifiedDate = modifiedDate;
    }

    public void updateViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void updateLike(List<String> likeUserList) {
        this.likeUserList = likeUserList;
        this.likeCount = likeUserList.size();
    }

    public void updateReplyList(List<Reply> replyList) {
        this.replyList = replyList;
        this.replyCount = replyList.size();
    }

    public void updatePartners(PartnersMapping partners) { this.partners = partners; }
    public void updateProductList(List<VideoProduct> productList) { this.productList = productList; }
    public void updateStatics(String address, String gender, String birth) {
        if(this.addressStatics.containsKey(address)) {
            this.addressStatics.put(address, this.addressStatics.get(address) + 1);
        } else {
            this.addressStatics.put(address, 1);
        }

        if(this.genderStatics.containsKey(gender)) {
            this.genderStatics.put(gender, this.genderStatics.get(gender) + 1);
        } else {
            this.genderStatics.put(gender, 1);
        }

        if(this.birthStatics.containsKey(birth)) {
            this.birthStatics.put(birth, this.birthStatics.get(birth) + 1);
        } else {
            this.birthStatics.put(birth, 1);
        }
    }

    public int indexOfProduct(String id) {
        for(int i = 0; i < this.replyList.size(); i++) {
            if(this.productList.get(i).getProduct().getId().equals(id)) return i;
        }
        return -1;
    }

    public int indexOfReply(String replyId) {
        for(int i = 0; i < this.replyList.size(); i++) {
            if(this.replyList.get(i).getId().equals(replyId)) return i;
        }
        return -1;
    }

    public int indexOfLikeUser(String userId) {
        for(int i = 0; i < this.likeUserList.size(); i++) {
            if(this.likeUserList.get(i).equals(userId)) return i;
        }
        return -1;
    }

    public void updateIsLikedVideo(Boolean flag) {
        this.isLikedVideo = flag;
    }
    public void updateIsSavedVideo(Boolean flag) { this.isSavedVideo = flag; }
}
