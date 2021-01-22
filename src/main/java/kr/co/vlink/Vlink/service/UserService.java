package kr.co.vlink.Vlink.service;

import kr.co.vlink.Vlink.domain.*;
import kr.co.vlink.Vlink.dto.UserDTO;
import kr.co.vlink.Vlink.persistance.HashTagRepository;
import kr.co.vlink.Vlink.persistance.ReplyRepository;
import kr.co.vlink.Vlink.persistance.UserRepository;
import kr.co.vlink.Vlink.util.AuthCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PartnersService partnersService;
    @Autowired
    private AuthCodeGenerator authCodeGenerator;

    public User findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
        int userProfileIdx = new Random().nextInt(5) + 1;
        if(user != null) {
            return userRepository.save(
                    User.builder()
                            .id(user.getId())
                            .name(userDTO.getName())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .phoneNumber(userDTO.getPhoneNumber())
                            .gender(userDTO.getGender())
                            .address(userDTO.getAddress())
                            .birth(userDTO.getBirth())
                            .profileImageURL("https://vlink-data.s3.ap-northeast-2.amazonaws.com/"+userProfileIdx+".png")
                            .authState(true)
                            .build()
            );
        }
        return null;
    }

    public User updateAuthState(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);

        if(user != null && user.getAuthCode().equals(userDTO.getAuthCode())) {
            user.updateAuthState(true);
            return userRepository.save(user);
        }
        return null;
    }

    public User sendAuthMail(UserDTO userDTO) throws MessagingException {
        String authCode = authCodeGenerator.getRandomCode(6);
        String email = userDTO.getEmail();
        User user =  userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            user = userRepository.save(User.builder()
                    .email(userDTO.getEmail())
                    .authCode(authCode)
                    .authState(false)
                    .authTime(new Date())
                    .build());
        } else {
            user.updateAuthCode(authCode);
        }
        mailService.sendUserAuthMail(email, authCode);
        return user;
    }

    public User findByEmailAndPassword(UserDTO userDTO) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);

        if(user != null && user.getPassword().equals(userDTO.getPassword()) && user.getAuthState()) {
            return user;
        }
        return null;
    }

    public boolean checkEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null && user.getAuthState()) {
            return true;
        }
        return false;
    }

    public User updateUser(UserDTO userDTO, String userId) {
        User user = findUserById(userId);
        if(user != null) {
            //update reply user information
            List<Reply> replyList = videoService.findReplyListByUserId(userId);
            if(userDTO.getName() != null || userDTO.getProfileImageURL() != null && replyList != null) {
                for(Reply reply : replyList) {
                    UserMapping userMapping = UserMapping.builder()
                            .id(reply.getUser().getId())
                            .name((userDTO.getName() != null) ? userDTO.getName() : reply.getUser().getName())
                            .profileImageURL((userDTO.getProfileImageURL() != null) ? userDTO.getProfileImageURL() : reply.getUser().getProfileImageURL())
                            .build();
                    reply.updateUser(userMapping);
                    replyRepository.save(reply);
                }
            }
            user.updateInfo(userDTO);
            return userRepository.save(user);
        }
        return null;
    }

    public User updateFollowList(String partnersId, String userId) {
        User user = findUserById(userId);
        Partners partners = partnersService.findPartnersById(partnersId);

        if(user != null && partners != null) {
            List<PartnersMapping> followerList = user.getFollowList();
            int index = user.indexOfFollower(partnersId);
            if(index == -1) {
                PartnersMapping partnersMapping = PartnersMapping.builder()
                        .id(partners.getId())
                        .name(partners.getName())
                        .profileImageURL(partners.getProfileImageURL())
                        .build();
                followerList.add(partnersMapping);
                user.updateFollowList(followerList);
                partners.updateFollowerCount(partners.getFollowerCount() + 1);
            } else {
                followerList.remove(index);
                int followerCount = (partners.getFollowerCount() - 1 <= 0) ? 0 : partners.getFollowerCount() - 1;
                partners.updateFollowerCount(followerCount);
            }
            user = userRepository.save(user);
            partnersService.savePartners(partners);
            return user;
        }
        return null;
    }

    public User addVideoToSavedVideoList(String videoId, String userId) {
        User user = findUserById(userId);
        VideoMapping video = videoService.findVideoMappingById(videoId);

        if(user != null && video != null) {
            List<VideoMapping> videoList = user.getSavedVideoList();
            int index = user.indexOfVideo(videoId);
            if(index == -1) {
                videoList.add(video);
            } else {
                videoList.remove(index);
            }
            user.updateSavedVideoList(videoList);
            return userRepository.save(user);
        }
        return null;
    }

    public User addProductToSavedProductList(String id, String userId) {
        User user = findUserById(userId);
        Product product = productService.findProductById(id);
        if(user != null && product != null) {
            List<Product> productList = user.getSavedProductList();
            int index = user.indexOfProduct(id);
            if(index == -1) {
                productList.add(product);
            } else {
                productList.remove(index);
            }
            user.updateSavedProductList(productList);
            return userRepository.save(user);
        }
        return null;
    }

    public UserMapping findUserMappingById(String userId) {
        return userRepository.findOneById(userId);
    }

    public void deleteUserById(String userId) {
        userRepository.deleteById(userId);
        List<Reply> replyList = videoService.findReplyListByUserId(userId);
        if(replyList != null) {
            for(Reply reply : replyList) {
                replyRepository.deleteById(reply.getId());
            }
        }
    }

    public User sendUserPassword(String email) throws MessagingException {
        String newPassword = authCodeGenerator.getRandomCode(10);
        User user =  userRepository.findByEmail(email).orElse(null);
        if(user != null) {
            user.updatePassword(newPassword);
            userRepository.save(user);
            mailService.sendUserPasswordMail(email, newPassword);
            return user;
        }
        return null;
    }

    public List<String> getTop5HashTagList() {
        List<String> tagList = new ArrayList<>();
        List<HashTag> hashTagList = hashTagRepository.findTop5ByOrderByCountDesc();
        for(int i = 0; i < hashTagList.size(); i++) {
            tagList.add(hashTagList.get(i).getHashTag());
        }
        return tagList;
    }
}
