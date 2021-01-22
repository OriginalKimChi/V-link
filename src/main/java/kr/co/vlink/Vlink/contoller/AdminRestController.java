package kr.co.vlink.Vlink.contoller;

import kr.co.vlink.Vlink.domain.Partners;
import kr.co.vlink.Vlink.dto.PartnersDTO;
import kr.co.vlink.Vlink.dto.ResultDTO;
import kr.co.vlink.Vlink.service.PartnersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    private PartnersService partnersService;

    @Value("${vlink.admin.id}")
    private String adminId;

    @Value("${vlink.admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public boolean adminLogin(@RequestParam String id, @RequestParam String password, HttpSession session) {
        if(id.equals(adminId) && password.equals(adminPassword)) {
            session.setAttribute("admin", true);
            return true;
        }
        return false;
    }

    @PostMapping("/allow-partners")
    public boolean allowPartners(@RequestBody PartnersDTO partnersDTO, HttpSession session) throws MessagingException {
        Partners partners = null;
        if((Boolean) session.getAttribute("admin")) {
            partners = partnersService.allowPartners(partnersDTO.getId());
        }
        return (partners != null) ? true : false;
    }

    @GetMapping("/all-partners")
    public ResultDTO findAllPartners(HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        List<Partners> partnersList = partnersService.findPartnersList();
        if(partnersList != null) {
//        if(partnersList != null && (Boolean) session.getAttribute("admin")) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partnersList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("not found");
            resultDTO.setData(null);
        }
        return resultDTO;
    }

    @GetMapping("/waiting-partners")
    public ResultDTO findNotAllowedPartners(HttpSession session) {
        ResultDTO resultDTO = new ResultDTO();
        List<Partners> partnersList = partnersService.findPartnersListByAuthState();
        if(partnersList != null && (Boolean) session.getAttribute("admin")) {
            resultDTO.setCode(200);
            resultDTO.setMessage("success");
            resultDTO.setData(partnersList);
        } else {
            resultDTO.setCode(500);
            resultDTO.setMessage("not found");
            resultDTO.setData(null);
        }
        return resultDTO;
    }
}
