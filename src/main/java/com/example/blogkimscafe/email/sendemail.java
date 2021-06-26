package com.example.blogkimscafe.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class sendemail  {

    @Autowired
    private JavaMailSender sender;//자체적으로 제공해주는 기능

    public void sendEmail(String toAddress, String subject, String body) {

        System.out.println(toAddress+"보낼주소");

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
          helper.setTo(toAddress);///보낼주소
          helper.setSubject(subject);///제목
          helper.setText(body);//내용
        } catch (MessagingException e) {
          e.printStackTrace();
          throw new RuntimeException("이메일 전송에 실패했습니다 잠시후 다시시도 바랍니다");
        }
        sender.send(message);
        System.out.println(toAddress+"에게 메일전송");
    }
        
       
}
