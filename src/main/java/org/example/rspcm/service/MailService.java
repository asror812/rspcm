package org.example.rspcm.service;

import org.example.rspcm.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;

    public void sendOtp(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(appProperties.getMail().getFrom());
        message.setTo(to);
        message.setSubject("RSPCM OTP Verification");
        message.setText("Tasdiqlash kodi: " + code + ". Kod 10 daqiqa davomida amal qiladi.");
        mailSender.send(message);
    }
}
