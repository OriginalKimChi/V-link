package kr.co.vlink.Vlink.contoller;

import kr.co.vlink.Vlink.domain.*;
import kr.co.vlink.Vlink.dto.*;
import kr.co.vlink.Vlink.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PartnersService partnersService;
    @Autowired
    private S3Service s3Service;

    @PostMapping("/sign-up")
    public ResultDTO signUp(@RequestBody UserDTO userDTO) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.createUser(userDTO);
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Create failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/sign-in")
    public ResultDTO signIn(@RequestBody UserDTO userDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.findByEmailAndPassword(userDTO);
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
            session.setAttribute("userId", user.getId());
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Create failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/logout")
    public boolean logout(HttpSession session) {
        session.removeAttribute("userId");
        return true;
    }

    @PostMapping("/mail-check")
    public ResultDTO checkUserEmail(@RequestBody UserDTO userDTO) throws MessagingException {
        ResultDTO resultDTO = new ResultDTO();

        if(userService.checkEmail(userDTO.getEmail())) {
            resultDTO.setCode(400);
            resultDTO.setMessage("email exits");
            resultDTO.setData(null);
            return resultDTO;
        }

        User user = userService.sendAuthMail(userDTO);
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Create failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/auth")
    public ResultDTO auth(@RequestBody UserDTO userDTO) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.updateAuthState(userDTO);
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Auth failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PutMapping("/update-info")
    public ResultDTO updateUser(@RequestBody UserDTO userDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.updateUser(userDTO, userDTO.getId());
//        User user = userService.updateUser(userDTO, session.getAttribute("userId").toString());
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Update failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/search")
    public ResultDTO search(@RequestBody SearchDTO searchDTO) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> searchData = new HashMap<>();

        List<VideoMapping> videoList = videoService.findTop3VideoByVideoName(searchDTO);
        searchData.put("video", videoList);
        List<Product> productList = productService.findTop3ProductByProductName(searchDTO);
        searchData.put("product", productList);
        List<PartnersMapping> partnersList = partnersService.findTop3ByPartnersName(searchDTO);
        searchData.put("partners", partnersList);;

        if(searchData.size() != 0) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(searchData);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Search failed : No search result");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/follow")
    public ResultDTO follow(@RequestBody PartnersDTO partnersDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.updateFollowList(partnersDTO.getId(), session.getAttribute("userId").toString());
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Update failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/save-video")
    public ResultDTO saveVideo(@RequestBody VideoDTO videoDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.addVideoToSavedVideoList(videoDTO.getId(), videoDTO.getUserId());
//        User user = userService.addVideoToSavedVideoList(videoDTO.getId(), session.getAttribute("userId").toString());
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Update failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/save-product")
    public ResultDTO saveProduct(@RequestBody ProductDTO productDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.addProductToSavedProductList(productDTO.getId(), productDTO.getUserId());
//        User user = userService.addProductToSavedProductList(productDTO.getId(), session.getAttribute("userId").toString());
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Update failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/withdrawal")
    public boolean deleteUser(HttpSession session) {
        try {
            userService.deleteUserById(session.getAttribute("userId").toString());
        } catch (Exception e) {
            System.out.println(e.toString());
            return false;
        }
        return true;
    }

    @PostMapping("/upload")
    public ResultDTO upload(MultipartFile file) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        String videoURL = s3Service.upload(file, "user");
        if(videoURL != null) {
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

    @PostMapping("/my-page")
    public ResultDTO myPage(@RequestBody UserDTO userDTO) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.findUserById(userDTO.getId());
//        User user = userService.findUserById(session.getAttribute("userId").toString());
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Upload failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/user-info")
    public ResultDTO userInfo(@RequestBody UserDTO userDTO) {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.findUserById(userDTO.getId());
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Upload failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/find-password")
    public ResultDTO findPassword(@RequestBody UserDTO userDTO) throws MessagingException {
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.sendUserPassword(userDTO.getEmail());
        if(user != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(user);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Can't find email");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/top-hashtag")
    public ResultDTO findTopHashTagList() {
        ResultDTO resultDTO = new ResultDTO();
        List<String> tagList = userService.getTop5HashTagList();
        if(tagList.size() != 0) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(tagList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Can't find tag");
            resultDTO.setData(null);
        }
        return resultDTO;
    }
}
