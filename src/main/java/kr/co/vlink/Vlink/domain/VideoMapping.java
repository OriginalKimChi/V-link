package kr.co.vlink.Vlink.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Getter
@NoArgsConstructor
public class VideoMapping {
    private String id;
    private String videoName;
    private List<String> tagList;
    private String videoURL;
    private String thumbnailImageURL;
    private Integer playTime;

    private PartnersMapping partners;

    private int viewCount;
    private int likeCount;
    private int replyCount;

    @Transient
    private boolean isLikedVideo;

    public void updateIsLikedVideo(boolean state) { this.isLikedVideo = state; }
}
