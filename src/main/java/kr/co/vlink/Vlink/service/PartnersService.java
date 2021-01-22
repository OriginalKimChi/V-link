package kr.co.vlink.Vlink.service;

import kr.co.vlink.Vlink.domain.Partners;
import kr.co.vlink.Vlink.domain.PartnersMapping;
import kr.co.vlink.Vlink.domain.User;
import kr.co.vlink.Vlink.domain.Video;
import kr.co.vlink.Vlink.dto.PartnersDTO;
import kr.co.vlink.Vlink.dto.SearchDTO;
import kr.co.vlink.Vlink.persistance.PartnersRepository;
import kr.co.vlink.Vlink.persistance.UserRepository;
import kr.co.vlink.Vlink.persistance.VideoRepository;
import kr.co.vlink.Vlink.util.AuthCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class PartnersService {

    @Autowired
    private PartnersRepository partnersRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private VideoService videoService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthCodeGenerator authCodeGenerator;
    @Autowired
    private MailService mailService;
    @Autowired
    private S3Service s3Service;

    public Partners createPartners(PartnersDTO partnersDTO) {
        int partnersProfileIdx = new Random().nextInt(5) + 1;
        return partnersRepository.save(
                Partners.builder()
                        .email(partnersDTO.getEmail())
                        .name(partnersDTO.getName())
                        .profileImage("https://vlink-data.s3.ap-northeast-2.amazonaws.com/"+partnersProfileIdx+".png") // 자동 등록
                        .snsAccount(partnersDTO.getSnsAccount())
                        .blogURL(partnersDTO.getBlogURL())
                        .interestField(partnersDTO.getInterestField())
                        .authState(false)
                        .firstLogin(true)
                        .build()
        );
    }

    public Partners findPartnersByEmailAndPassword(PartnersDTO partnersDTO) {
        Partners partners = partnersRepository.findByEmailAndAuthState(partnersDTO.getEmail(), true).orElse(null);

        if (partners != null && partners.getPassword().equals(partnersDTO.getPassword())) {
            return partners;
        }
        return null;
    }

    public Partners updatePassword(PartnersDTO partnersDTO, String partnersId) {
        Partners partners = partnersRepository.findById(partnersId).orElse(null);

        if(partners != null && partners.getPassword().equals(partnersDTO.getPassword())) {
            partners.updatePassword(partnersDTO.getNewPassword());
            partners.updateFirstLogin(false);
            return partnersRepository.save(partners);
        }
        return null;
    }

    public Partners updateName(String name, String partnersId) {
        Partners partners = partnersRepository.findById(partnersId).orElse(null);

        if(partners != null) {
            partners.updateName(name);

            // update partners image in video
            List<Video> videoList = partners.getVideoList();
            if(videoList != null && videoList.size() != 0) {
                for(Video video : videoList) {
                    PartnersMapping partnersMapping = PartnersMapping.builder()
                            .id(video.getPartners().getId())
                            .name(name)
                            .profileImageURL(video.getPartners().getProfileImageURL())
                            .build();
                    video.updatePartners(partnersMapping);
                    videoService.saveVideo(video);
                }
                partners = partnersRepository.save(partners);
            }

            // update partner image in followList
            List<User> userList = userRepository.findByFollowList_Id(partners.getId());
            if(userList != null && userList.size() != 0) {
                for(User user : userList) {
                    List<PartnersMapping> followList = user.getFollowList();
                    int index = user.indexOfFollower(partners.getId());
                    if(index != -1) {
                        PartnersMapping partnersMapping = PartnersMapping.builder()
                                .id(user.getFollowList().get(index).getId())
                                .name(name)
                                .profileImageURL(user.getFollowList().get(index).getProfileImageURL())
                                .build();
                        followList.set(index,partnersMapping);
                        user.updateFollowList(followList);
                        userRepository.save(user);
                    }
                }
            }
            return partners;
        }
        return null;
    }

    public Partners updateProfileImage(MultipartFile image, String partnersId) throws IOException {
        Partners partners = partnersRepository.findById(partnersId).orElse(null);

        if(partners != null) {
            s3Service.deleteFile(partners.getProfileImageURL());
            String url = s3Service.upload(image, "partners");
            partners.updateProfileImageURL(url);

            // update partners image in video
            List<Video> videoList = partners.getVideoList();
            if(videoList != null && videoList.size() != 0) {
                for(Video video : videoList) {
                    PartnersMapping partnersMapping = PartnersMapping.builder()
                            .id(video.getPartners().getId())
                            .name(video.getPartners().getName())
                            .profileImageURL(url)
                            .build();
                    video.updatePartners(partnersMapping);
                    videoService.saveVideo(video);
                }
                partners = partnersRepository.save(partners);
            }

            // update partner image in followList
            List<User> userList = userRepository.findByFollowList_Id(partners.getId());
            if(userList != null && userList.size() != 0) {
                for(User user : userList) {
                    List<PartnersMapping> followList = user.getFollowList();
                    int index = user.indexOfFollower(partners.getId());
                    if(index != -1) {
                        PartnersMapping partnersMapping = PartnersMapping.builder()
                                .id(user.getFollowList().get(index).getId())
                                .name(user.getFollowList().get(index).getName())
                                .profileImageURL(url)
                                .build();
                        followList.set(index,partnersMapping);
                        user.updateFollowList(followList);
                        userRepository.save(user);
                    }
                 }
            }
            return partners;
        }
        return null;
    }

    public Partners findPartnersByEmail(String email) {
        return partnersRepository.findByEmail(email).orElse(null);
    }

    public Partners findPartnersById(String id) {
        return partnersRepository.findById(id).orElse(null);
    }

    public Partners allowPartners(String partnerId) throws MessagingException {
        Partners partners = partnersRepository.findById(partnerId).orElse(null);

        if(partners != null) {
            String password = authCodeGenerator.getRandomCode(10);
            partners.updatePassword(password);
            partners.updateAuthState(true);
            mailService.sendPartnerAllowMail(partners.getEmail(), password);
            return partnersRepository.save(partners);
        }
        return null;
    }

    public List<Partners> findPartnersListByAuthState() {
        return partnersRepository.findByAuthState(false);
    }

    public List<Partners> findPartnersList() { return partnersRepository.findAll(); }

    public Partners updateVideoList(Partners partners, List<Video> videoList) {
        partners.updateVideoList(videoList);
        return partnersRepository.save(partners);
    }

    public Partners savePartners(Partners partners) {
        return partnersRepository.save(partners);
    }

    public Page<PartnersMapping> findPartnersListByKeyword(SearchDTO searchDTO) {
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
        return partnersRepository.findByNameRegexOrderByCreateAtDesc(keyword, pageable);
    }

    public List<PartnersMapping> findTop3ByPartnersName(SearchDTO searchDTO) {
        String partnersName = ".*.*";
        int page = 0;

        if(searchDTO.getKeyword() != null) {
            String keywordString = searchDTO.getKeyword().replace(" ", ".*)(.*");
            partnersName = "(?i)(.*" + keywordString + ".*)";
        }
        return partnersRepository.findTop3ByNameRegex(partnersName);
    }

    public PartnersMapping findPartnersMappingById(String partnersId) {
        return partnersRepository.findOneById(partnersId);
    }

    public Map<String, Object> findPartnersVideoInfo(String partnersId) {
        List<Video> videoList = videoRepository.findByPartners_Id(partnersId);
        Map<String, Object> partnersVideoInfo = new HashMap<>();

        if(videoList.size() > 0 && videoList != null) {
            int viewCount = 0;
            int likeCount = 0;
            int replyCount = 0;
            Map<String, Integer> addressStatics = new HashMap<>();
            Map<String, Integer> genderStatics = new HashMap<>();
            Map<String, Integer> birthStatics = new HashMap<>();
            for(Video video : videoList) {
                viewCount += video.getViewCount();
                likeCount += video.getLikeCount();
                replyCount += video.getReplyCount();

                video.getAddressStatics().forEach((key, value) -> {
                    if(addressStatics.containsKey(key)) addressStatics.put(key, addressStatics.get(key) + value);
                    else addressStatics.put(key, value);
                });
                video.getGenderStatics().forEach((key, value) -> {
                    if(genderStatics.containsKey(key)) genderStatics.put(key, genderStatics.get(key) + value);
                    else genderStatics.put(key, value);
                });
                video.getBirthStatics().forEach((key, value) -> {
                    if(birthStatics.containsKey(key)) birthStatics.put(key, birthStatics.get(key) + value);
                    else birthStatics.put(key, value);
                });
            }
            partnersVideoInfo.put("videoCount", videoList.size());
            partnersVideoInfo.put("viewCount", viewCount);
            partnersVideoInfo.put("likeCount", likeCount);
            partnersVideoInfo.put("replyCount", replyCount);
            partnersVideoInfo.put("addressStatics", addressStatics);
            partnersVideoInfo.put("genderStatics", genderStatics);
            partnersVideoInfo.put("birthStatics", birthStatics);

            return partnersVideoInfo;
        }
        return null;
    }
}
