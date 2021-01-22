package kr.co.vlink.Vlink.dto;

import kr.co.vlink.Vlink.domain.VideoProduct;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class VideoDTO {
    private String id;
    private String userId;
    private String videoName;
    private List<String> tagList;
    private String videoURL;
    private String thumbnailImageURL;
    private Integer playTime;
    private List<VideoProduct> productList;

    //Product detect
    private MultipartFile frameImage;
    private String frameImageURL;
}
