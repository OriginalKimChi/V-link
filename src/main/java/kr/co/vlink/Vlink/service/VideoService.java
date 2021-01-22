package kr.co.vlink.Vlink.service;

import kr.co.vlink.Vlink.domain.*;
import kr.co.vlink.Vlink.dto.SearchDTO;
import kr.co.vlink.Vlink.dto.VideoDTO;
import kr.co.vlink.Vlink.persistance.HashTagRepository;
import kr.co.vlink.Vlink.persistance.ProductRepository;
import kr.co.vlink.Vlink.persistance.ReplyRepository;
import kr.co.vlink.Vlink.persistance.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private PartnersService partnersService;

    public Video findVideoById(String id) { return videoRepository.findById(id).orElse(null); }

    public VideoMapping findVideoMappingById(String id) { return videoRepository.findOneById(id).orElse(null); }

    public Page<VideoMapping> findVideoListByKeyword(SearchDTO searchDTO) {
        String keyword = ".*.*";
        int page = 0;

        if(searchDTO.getKeyword() != null) {
            String keywordString = searchDTO.getKeyword().replace(" ", ".*)(.*");
            keyword = "(?i)(.*" + keywordString + ".*)";
        }
        if(searchDTO.getPage() != null && searchDTO.getPage() > 0) {
            page = searchDTO.getPage() - 1;
        }
        Pageable pageable = PageRequest.of(page, 10);
        return videoRepository.findByVideoNameRegexOrderByCreateAtDesc(keyword, pageable);
    }

    public Page<VideoMapping> findVideoListByTag(SearchDTO searchDTO) {
        int page = 0;
        String keyword = searchDTO.getKeyword();

        if(searchDTO.getPage() != null && searchDTO.getPage() > 0) {
            page = searchDTO.getPage() - 1;
        }
        Pageable pageable = PageRequest.of(page, 10);
        return videoRepository.findByTagListOrderByViewCountDesc(keyword, pageable);
    }

    public List<VideoMapping> findTop3VideoByVideoName(SearchDTO searchDTO) {
        String videoName = ".*.*";
        int page = 0;
        if(searchDTO.getKeyword() != null) {
            String keywordString = searchDTO.getKeyword().replace(" ", ".*)(.*");
            videoName = "(?i)(.*" + keywordString + ".*)";
        }
        return videoRepository.findTop3ByVideoNameRegexOrderByViewCountDesc(videoName);
    }

    public Page<VideoMapping> findFollowVideoList(SearchDTO searchDTO, String userId) {
        User user = userService.findUserById(userId);
        if(user != null) {
            int page = 0;

            if(searchDTO.getPage() != null && searchDTO.getPage() > 0) {
                page = searchDTO.getPage() - 1;
            }
            Pageable pageable = PageRequest.of(page, 10);

            List<PartnersMapping> partnersList = user.getFollowList();
            if(partnersList != null && partnersList.size() != 0) {
                List<String> partnersIdList = new ArrayList<>();
                for(PartnersMapping partners : partnersList) {
                    partnersIdList.add(partners.getId());
                }
                return videoRepository.findByPartners_IdInOrderByCreateAtDesc(partnersIdList, pageable);
            }
        }
        return null;
    }

    public boolean findIsLikedVideo(Video video, String userId) {
        if(video.indexOfLikeUser(userId) == -1) {
            return false;
        }
        return true;
    }

    public boolean findIsSavedVideo(Video video, String userId) {
        User user = userService.findUserById(userId);
        if(user != null) {
            if(user.indexOfVideo(video.getId()) == -1) {
                return false;
            }
            return true;
        }
        return false;
    }

    public Video addViewCount(Video video) {
        video.updateViewCount(video.getViewCount() + 1);
        return videoRepository.save(video);
    }

    public Video addViewCountAndStatics(Video video, String userId) {
        User user = userService.findUserById(userId);
        if(user != null) {
            video.updateStatics(user.getAddress(), user.getGender(), user.getBirth().toString());
            video.updateViewCount(video.getViewCount() + 1);
            return videoRepository.save(video);
        } else {
            return addViewCount(video);
        }
    }

    public Video userViewVideo(String id, String userId) {
        Video video = findVideoById(id);
        video.updateIsLikedVideo(findIsLikedVideo(video, userId));
        video.updateIsSavedVideo(findIsSavedVideo(video, userId));
        return addViewCountAndStatics(video, userId);
    }

    public Video viewVideo(String id) {
        Video video = findVideoById(id);
        return addViewCount(video);
    }

    public Video createVideo(VideoDTO videoDTO, String partnersId) {
        Partners partners = partnersService.findPartnersById(partnersId);
        if(partners != null) {
            PartnersMapping partnersMapping = partnersService.findPartnersMappingById(partnersId);
            Video video = Video.builder()
                    .videoName(videoDTO.getVideoName())
                    .tagList(videoDTO.getTagList())
                    .videoURL(videoDTO.getVideoURL())
                    .thumbnailImageURL(videoDTO.getThumbnailImageURL())
                    .playTime(videoDTO.getPlayTime())
                    .productList(productService.createOrUpdateProductByVideoProductList(videoDTO))
                    .partners(partnersMapping)
                    .build();
            List<Video> videoList = (partners.getVideoList() != null) ? partners.getVideoList() : new ArrayList<>();
            video = videoRepository.save(video);
            videoList.add(video);
            partnersService.updateVideoList(partners, videoList);
            return video;
        }
        return null;
    }

    public Video saveVideo(VideoDTO videoDTO) {
        Video video = findVideoById(videoDTO.getId());
        if(video != null) {
            Video updateVideo = Video.builder()
                    .id(video.getId())
                    .videoName(videoDTO.getVideoName())
                    .tagList(videoDTO.getTagList())
                    .videoURL(video.getVideoURL())
                    .thumbnailImageURL(videoDTO.getThumbnailImageURL())
                    .playTime(videoDTO.getPlayTime())
                    .productList(productService.createOrUpdateProductByVideoProductList(videoDTO))
                    .build();
            return videoRepository.save(updateVideo);
        }
        return null;
    }

    public Video updateLikeCountById(String videoId, String userId) {
        User user = userService.findUserById(userId);
        Video video = findVideoById(videoId);

        if(video != null && user != null) {
            List<String> likeUserList = video.getLikeUserList();
            int index = video.indexOfLikeUser(userId);
            if(index == -1) {
                likeUserList.add(user.getId());
                video.updateIsLikedVideo(true);
            } else {
                likeUserList.remove(index);
                video.updateIsLikedVideo(false);
            }
            video.updateLike(likeUserList);
            return videoRepository.save(video);
        }
        return null;
    }

    public boolean deleteVideoById(String id, String partnersId) {
        Partners partners = partnersService.findPartnersById(partnersId);
        Video video = findVideoById(id);
        if(video != null && partners != null) {
            List<Video> videoList = partners.getVideoList();
            videoList.remove(partners.indexOfVideoList(video.getId()));
            partners.updateVideoList(videoList);
            partnersService.savePartners(partners);

            // delete all reply in video
            List<Reply> replyList = video.getReplyList();
            if(replyList.size() != 0 && replyList != null) {
                for(Reply reply : replyList) {
                    deleteReply(id, reply.getId());
                }
            }
            videoRepository.deleteById(video.getId());
            return true;
        }
        return false;
    }

    public Video addReply(String videoId, String content, String userId) {
        Video video = findVideoById(videoId);
        UserMapping user = userService.findUserMappingById(userId);
        if(video != null && user != null) {
            List<Reply> replyList = video.getReplyList();
            replyList.add(replyRepository.save(Reply.builder().content(content).user(user).build()));
            video.updateReplyList(replyList);
            return videoRepository.save(video);
        }
        return null;
    }

    public Video updateReply(String videoId, String replyId,String content) {
        Video video = findVideoById(videoId);
        Reply reply = replyRepository.findById(replyId).get();
        if(video != null && reply != null) {
            List<Reply> replyList = video.getReplyList();
            int index = video.indexOfReply(replyId);
            reply.updateContent(content);
            replyList.set(index, replyRepository.save(reply));
            video.updateReplyList(replyList);
            return videoRepository.save(video);
        }
        return null;
    }

    public Video deleteReply(String videoId, String replyId) {
        Video video = findVideoById(videoId);
        if(video != null) {
            List<Reply> replyList = video.getReplyList();
            int index = video.indexOfReply(replyId);
            replyList.remove(index);
            video.updateReplyList(replyList);
            return videoRepository.save(video);
        }
        return null;
    }

    public List<Reply> findReplyListByUserId(String userId) {
        return replyRepository.findByUser_Id(userId);
    }

    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    public Product findProductInVideo(String videoId, String productId) {
        Video video = findVideoById(videoId);
        int index = video.indexOfProduct(productId);

        List<VideoProduct> videoProductList = video.getProductList();
        VideoProduct videoProduct = videoProductList.get(index);
        videoProduct.addCount();
        videoProductList.set(index, videoProduct);
        video.updateProductList(videoProductList);
        videoRepository.save(video);

        Product product = productService.findProductById(productId);
        product.addViewCount();
        return productRepository.save(product);
    }

    public List<VideoMapping> findTop3VideoByViewCount() {
        return videoRepository.findTop3ByOrderByViewCountDesc();
    }

    public List<Video> findTop3VideoByPartnersId(String partnersId) {
        return videoRepository.findTop3ByPartners_IdOrderByViewCountDesc(partnersId);
    }

    public List<VideoMapping> findTop3VideoByProductId(String productId) {
        return videoRepository.findTop3ByProductList_Product_IdOrderByViewCountDesc(productId);
    }

    public Page<Video> findVideoListByPartnersId(SearchDTO searchDTO, String partnersId) {
        int page = 0;

        if(searchDTO.getPage() != null && searchDTO.getPage() > 0) {
            page = searchDTO.getPage() - 1;
        }
        Pageable pageable = PageRequest.of(page, 10);

        return videoRepository.findByPartners_IdOrderByCreateAtDesc(partnersId, pageable);
    }

    public List<Video> findProductListByPartnersId(String partnersId) {
        return videoRepository.findByPartners_Id(partnersId);
    }

    public void updateVideoHashTag(Video video) {
        List<String> tagList = video.getTagList();
        for(String tagName : tagList) {
            HashTag hashTag = hashTagRepository.findByHashTag(tagName).orElse(null);
            if(hashTag != null) {
                hashTag.updateCount();
                hashTagRepository.save(hashTag);
            } else {
                hashTagRepository.save(HashTag.builder().hashTag(tagName).build());
            }
        }
    }
}
