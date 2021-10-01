package com.highgeupsik.backend.service;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String username, String receiverEmail, boolean accept) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        StringBuilder body = new StringBuilder();
        if(accept) {
            body.append("<html><p><strong>" + username + "님 HighGeupSik에 오신것을 환영합니다!</strong></p><br/>");
        }else{
            body.append("<html><p><strong>" + username + "님 가입이 거부되었습니다. 학생증을 다시 제출해 주세요</strong></p><br/>");
        }
        messageHelper.setTo(receiverEmail);
        messageHelper.setSubject("HighGeupSik 회원가입을 축하드립니다.");
        messageHelper.setText(body.toString(), true);

        javaMailSender.send(message);

    }


}
