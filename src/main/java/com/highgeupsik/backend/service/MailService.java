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
                body.append("<html><body><p><strong>" + username + "님 HighGeupSik에 오신것을 환영합니다! "
                        + "하단링크 접속 및 로그아웃 후 다시 로그인해주세요</strong></p><br/>")
                    .append("<a href = 'https://higk.o-r.kr/'>하이급식 접속하기</a></body></html>");
            } else {
                subject = "HighGeupSik 회원가입에 실패했습니다";
                body.append("<html><body><p><strong>" + username + "님 가입이 거부되었습니다.<br/>")
                    .append( "하단링크 접속 후 학생증을 다시 제출해 주세요</strong></p><br/>")
                    .append("<<a href = 'https://higk.o-r.kr/'>하이급식 접속하기</a></body></html>");
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
