package kr.co.vlink.Vlink.contoller;

import kr.co.vlink.Vlink.domain.*;
import kr.co.vlink.Vlink.dto.ContactDTO;
import kr.co.vlink.Vlink.dto.PartnersDTO;
import kr.co.vlink.Vlink.dto.ResultDTO;
import kr.co.vlink.Vlink.dto.SearchDTO;
import kr.co.vlink.Vlink.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/partners")
public class PartnersRestController {

    @Autowired
    private PartnersService partnersService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ProductService productService;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private MailService mailService;

    @PostMapping("/sign-up")
    public ResultDTO createPartners(@RequestBody PartnersDTO partnersDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            Partners partners = partnersService.createPartners(partnersDTO);
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partners);
        } catch (Exception e) {
            resultDTO.setCode(500);
            resultDTO.setMessage(e.toString());
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/sign-in")
    public ResultDTO partnersLogin(@RequestBody PartnersDTO partnersDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Partners partners = partnersService.findPartnersByEmailAndPassword(partnersDTO);
        if(partners != null) {
            session.setAttribute("partnersId", partners.getId());
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partners);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("Login failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/logout")
    public void logout(HttpSession session) {
        session.removeAttribute("partnersId");
    }

    @PostMapping("/password-change")
    public ResultDTO updatePassword(@RequestBody PartnersDTO partnersDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Partners partners = partnersService.updatePassword(partnersDTO, session.getAttribute("partnersId").toString());
        if(partners != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partners);
        } else {
            resultDTO.setCode(400);
            resultDTO.setMessage("Password not correct");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/update-name")
    public ResultDTO updateName(@RequestParam String partnersId, @RequestParam String name, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Partners partners = partnersService.updateName(name, partnersId);
        if(partners != null && (boolean) session.getAttribute("admin")) { // only admin
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partners);
        } else {
            resultDTO.setCode(400);
            resultDTO.setMessage("Update Failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/update-profileImg")
    public ResultDTO updateProfileImage(@RequestParam MultipartFile image, HttpSession session) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        Partners partners = partnersService.updateProfileImage(image, session.getAttribute("partnersId").toString());
        if(partners != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partners);
        } else {
            resultDTO.setCode(400);
            resultDTO.setMessage("Update Failed");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/search")
    public ResultDTO findPartners(@RequestBody SearchDTO searchDTO) {
        ResultDTO resultDTO = new ResultDTO();
        try {
            Page<PartnersMapping> partnerList = partnersService.findPartnersListByKeyword(searchDTO);
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partnerList);
        } catch (Exception e) {
            resultDTO.setCode(500);
            resultDTO.setMessage(e.toString());
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/upload")
    public ResultDTO upload(MultipartFile file) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        String videoURL = s3Service.upload(file, "partners");
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

    @GetMapping("/top-video")
    public ResultDTO findTop3VideoByViewCount(HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        List<Video> videoList = videoService.findTop3VideoByPartnersId(session.getAttribute("partnersId").toString());
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

    @PostMapping("/video-list")
    public ResultDTO findVideoListByPartnersId(@RequestBody SearchDTO searchDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Page<Video> videoList = videoService.findVideoListByPartnersId(searchDTO,session.getAttribute("partnersId").toString());
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

    @PostMapping("/find")
    public ResultDTO findPartnersByPartnersId(@RequestBody SearchDTO searchDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Page<Video> videoList = videoService.findVideoListByPartnersId(searchDTO, searchDTO.getPartnersId());
//        Page<Video> videoList = videoService.findVideoListByPartnersId(searchDTO,session.getAttribute("partnersId").toString());
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

    @GetMapping("/product-list")
    public ResultDTO findProductListByPartnersId(HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        HashMap<String, Product> productList = productService.findTop3ProductByPartnerIdAndViewCount(session.getAttribute("partnersId").toString());
        if (productList != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(productList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("No data");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/videos-info")
    public ResultDTO findVideoListInfoByPartnersId(HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Map<String, Object> partnersVideoInfo = partnersService.findPartnersVideoInfo(session.getAttribute("partnersId").toString());
        if (partnersVideoInfo != null) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partnersVideoInfo);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("No data");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @PostMapping("/contact")
    public ResultDTO requestContact(@RequestBody ContactDTO contactDTO, HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        Partners partners = partnersService.findPartnersById(session.getAttribute("partnersId").toString());
        try {
            mailService.sendPartnersContact(partners.getEmail(), partners.getName(), contactDTO.getTitle(), contactDTO.getContent());
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(true);
        } catch (MessagingException e) {
            resultDTO.setCode(500);
            resultDTO.setMessage("No data");
            resultDTO.setData(null);
        }
        return resultDTO;
    }
}
