package com.example.demo.service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

public interface EmailService {
    public void sendMessage(String to, String subject, Map<String, Object> templateModel)
            throws IOException, MessagingException;
}
