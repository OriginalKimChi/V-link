package kr.co.vlink.Vlink.contoller;

import kr.co.vlink.Vlink.domain.Partners;
import kr.co.vlink.Vlink.service.PartnersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@CrossOrigin
public class WebController {

    @Autowired
    private PartnersService partnersService;

    @GetMapping("/dashboard")
    public String test(Model model, HttpSession session) {
        if(session.getAttribute("partnersId") != null) {
            Partners partners = partnersService.findPartnersById(session.getAttribute("partnersId").toString());
            model.addAttribute("partnersName", partners.getName());
            model.addAttribute("partnersImage", partners.getProfileImageURL());
            return "dashboard";
        }
        return "login";
    }

    @GetMapping("/video-list")
    public String test2(Model model, HttpSession session) {
        if(session.getAttribute("partnersId") != null) {
            Partners partners = partnersService.findPartnersById(session.getAttribute("partnersId").toString());
            model.addAttribute("partnersName", partners.getName());
            model.addAttribute("partnersImage", partners.getProfileImageURL());
            model.addAttribute("partnersEmail", partners.getEmail());
            return "video-list";
        }
        return "login";
    }

    @GetMapping("/video-upload")
    public String test3(Model model, HttpSession session) {
        if(session.getAttribute("partnersId") != null) {
            Partners partners = partnersService.findPartnersById(session.getAttribute("partnersId").toString());
            model.addAttribute("partnersName", partners.getName());
            model.addAttribute("partnersImage", partners.getProfileImageURL());
            return "video-upload";
        }
        return "login";
    }

    @GetMapping("/mail-contact")
    public String test4(Model model, HttpSession session) {
        if(session.getAttribute("partnersId") != null) {
            Partners partners = partnersService.findPartnersById(session.getAttribute("partnersId").toString());
            model.addAttribute("partnersName", partners.getName());
            model.addAttribute("partnersImage", partners.getProfileImageURL());
            return "mail-contact";
        }
        return "login";
    }

    @GetMapping("/change-password")
    public String test5(Model model, HttpSession session) {
        if(session.getAttribute("partnersId") != null) {
            Partners partners = partnersService.findPartnersById(session.getAttribute("partnersId").toString());
            model.addAttribute("partners", partners);
            return "change-password";
        }
        return "login";
    }

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("userId", "test");
        return "login";
    }

    @GetMapping("/partners-Apply")
    public String partnersApply2() {return "partnersApply"; }

    @GetMapping("/admin-login")
    public String adminLogin() {return "admin_login"; }

    @GetMapping("/admin-list")
    public String adminList() {return "admin_list"; }

    @GetMapping("/admin-ap")
    public String adminAp() {return "admin_ap"; }


}
