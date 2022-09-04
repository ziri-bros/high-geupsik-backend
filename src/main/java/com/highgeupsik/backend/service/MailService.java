package com.highgeupsik.backend.service;

import com.highgeupsik.backend.exception.MailException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String username, String receiverEmail, boolean accept) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            String subject = "";
            StringBuilder body = new StringBuilder();
            if (accept) {
                subject = "HighGeupSik 회원가입을 축하드립니다";
                body.append("<html><p><strong>" + username + "님 HighGeupSik에 오신것을 환영합니다!</strong></p><br/>");
            } else {
                subject = "HighGeupSik 회원가입에 실패했습니다";
                body.append("<html><p><strong>" + username + "님 가입이 거부되었습니다. 학생증을 다시 제출해 주세요</strong></p><br/>");
            }
            messageHelper.setTo(receiverEmail);
            messageHelper.setSubject(subject);
            messageHelper.setText(body.toString(), true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailException(e.getMessage());
        }
    }
}
