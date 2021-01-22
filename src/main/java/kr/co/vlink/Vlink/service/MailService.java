package kr.co.vlink.Vlink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    SpringTemplateEngine templateEngine;
    private static final String FROM_ADDRESS = "vlinkKR@gmail.com";

    @Async
    public void sendPartnerAllowMail(String email, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("[승인완료] V-link 파트너스에 선정되었습니다!");
        helper.setTo(email);
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("password", password);

        String html = templateEngine.process("mail-template-partners", context);
        helper.setText(html, true);
        mailSender.send(message);
    }

    @Async
    public void sendUserAuthMail(String email, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("[인증번호] V-link 가입을 축하드립니다.");
        helper.setFrom("V-link");
        helper.setTo(email);
        Context context = new Context();
        context.setVariable("authCode", authCode);

        String html = templateEngine.process("mail-template-user", context);
        helper.setText(html, true);
        mailSender.send(message);
    }

    @Async
    public void sendUserPasswordMail(String email, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("[임시비밀번호] V-link 비밀번호 발급");
        helper.setFrom("V-link");
        helper.setTo(email);
        Context context = new Context();
        context.setVariable("password", password);

        String html = templateEngine.process("mail-template-user-password", context);
        helper.setText(html, true);
        mailSender.send(message);
    }

    @Async
    public void sendPartnersContact(String email, String name, String title, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setSubject("[파트너스 문의]"+ title);
        helper.setFrom("[파트너스]"+name);
        helper.setTo(email);
        Context context = new Context();
        context.setVariable("partners_name", name);
        context.setVariable("contact_context", content);

        String html = templateEngine.process("mail-template-partnerscontact", context);
        helper.setText(html, true);
        mailSender.send(message);
    }
}
