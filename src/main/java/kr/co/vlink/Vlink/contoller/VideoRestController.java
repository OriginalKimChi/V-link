package kr.co.vlink.Vlink.contoller;

import kr.co.vlink.Vlink.domain.Product;
import kr.co.vlink.Vlink.domain.Video;
import kr.co.vlink.Vlink.domain.VideoMapping;
import kr.co.vlink.Vlink.dto.*;
import kr.co.vlink.Vlink.service.S3Service;
import kr.co.vlink.Vlink.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoRestController {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private VideoService videoService;

    @GetMapping(value = {"/findAll", "/search"})
    public ResultDTO findVideos(@RequestBody SearchDTO searchDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            Page<VideoMapping> videoList = videoService.findVideoListByKeyword(searchDTO);
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(videoList);
        } catch (Exception e) {
            resultDTO.setCode(500);
            resultDTO.setMessage(e.toString());
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/find")
    public ResultDTO findVideo(@RequestBody VideoDTO videoDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Video video;
        if(videoDTO.getUserId() == null) {
            video = videoService.viewVideo(videoDTO.getId());
        } else {
            video = videoService.userViewVideo(videoDTO.getId(), videoDTO.getUserId());
        }
//        if(session.getAttribute("userId") == null) {
//            video = videoService.viewVideo(videoDTO.getId());
//        } else{
//            video = videoService.userViewVideo(videoDTO.getId(), session.getAttribute("userId").toString());
//        }
        if(video != null) {
            videoService.updateVideoHashTag(video);
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(video);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Find failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/feed")
    public ResultDTO findVideoFeed(@RequestBody SearchDTO searchDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Page<VideoMapping> videoList = null;
        videoList = videoService.findVideoListByKeyword(searchDTO);
//        if(searchDTO.getUserId() == null) {
//            videoList = videoService.findVideoListByKeyword(searchDTO);
//        } else {
//            videoList = videoService.findFollowVideoList(searchDTO, searchDTO.getUserId());
//        }
//        if(session.getAttribute("userId") == null) {
//            videoList = videoService.findVideoListByKeyword(searchDTO);
//        } else {
//            videoList = videoService.findFollowVideoList(searchDTO, session.getAttribute("userId").toString());
//        }
        if (videoList != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(videoList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Find failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/tag-video")
    public ResultDTO findTagVideo(@RequestBody SearchDTO searchDTO) {
        ResultDTO resultDTO = new ResultDTO();
        Page<VideoMapping> videoList = videoService.findVideoListByTag(searchDTO);
        if (videoList != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(videoList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Find failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/save")
    public ResultDTO createVideo(@RequestBody VideoDTO videoDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Video video = videoService.createVideo(videoDTO, session.getAttribute("partnersId").toString());
        if (video != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(video);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Save failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PutMapping("/update")
    public ResultDTO updateVideo(@RequestBody VideoDTO videoDTO) {
        ResultDTO resultDTO = new ResultDTO();
        Video video = videoService.saveVideo(videoDTO);
        if (video != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(video);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Update failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/upload")
    public ResultDTO uploadVideo(MultipartFile file) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        String videoURL = s3Service.upload(file, "video");
        if (videoURL != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(videoURL);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Upload failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PutMapping("/reUpload")
    public ResultDTO resultDTO(@RequestParam String fileURL, MultipartFile file) {
        ResultDTO resultDTO = new ResultDTO();
        s3Service.deleteFile(fileURL);
        String videoURL = s3Service.upload(file, "video");
        if (videoURL != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(videoURL);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Upload failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @DeleteMapping("/delete")
    public ResultDTO deleteVideoById(@RequestBody VideoDTO videoDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        boolean deleteState = videoService.deleteVideoById(videoDTO.getId(), session.getAttribute("partnersId").toString());
        if (deleteState) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(true);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Delete failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/like")
    public ResultDTO likeVideoById(@RequestBody VideoDTO videoDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
//        Video video = videoService.updateLikeCountById(videoId, session.getAttribute("userId").toString());
        Video video = videoService.updateLikeCountById(videoDTO.getId(), videoDTO.getUserId());
        if (video != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(video);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Like failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/add-reply")
    public ResultDTO addReply(@RequestBody ReplyDTO replyDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
//        Video video = videoService.addReply(replyDTO.getVideoId(), replyDTO.getContent(), session.getAttribute("userId").toString());
        Video video = videoService.addReply(replyDTO.getVideoId(), replyDTO.getContent(), replyDTO.getUserId());
        if (video != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(video);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Add reply failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PutMapping("/update-reply")
    public ResultDTO updateReply(@RequestBody ReplyDTO replyDTO) {
        ResultDTO resultDTO = new ResultDTO();
        Video video = videoService.updateReply(replyDTO.getVideoId(), replyDTO.getReplyId(), replyDTO.getContent());
        if (video != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(video);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Update failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/remove-reply")
    public ResultDTO removeReply(@RequestBody ReplyDTO replyDTO) {
        ResultDTO resultDTO = new ResultDTO();
        Video video = videoService.deleteReply(replyDTO.getVideoId(), replyDTO.getReplyId());
        if (video != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(video);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Delete failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/find-product")
    public ResultDTO findProductById(@RequestParam String videoId, @RequestParam String productId) {
        ResultDTO resultDTO = new ResultDTO();
        Product product = videoService.findProductInVideo(videoId, productId);
        if (product != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(product);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Delete failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/top-view")
    public ResultDTO findTop3VideoByViewCount() {
        ResultDTO resultDTO = new ResultDTO();
        List<VideoMapping> videoList = videoService.findTop3VideoByViewCount();
        if (videoList != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(videoList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("No data");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/product-related")
    public ResultDTO findTopVideoByProductId(@RequestBody ProductDTO productDTO) {
        ResultDTO resultDTO = new ResultDTO();
        List<VideoMapping> videoList = videoService.findTop3VideoByProductId(productDTO.getId());
        if (videoList != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(videoList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("No data");
            resultDTO.setData(null);
        }
        return resultDTO;
    }
}
