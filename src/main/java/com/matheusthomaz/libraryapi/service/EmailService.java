package com.matheusthomaz.libraryapi.service;

import java.util.List;

public interface EmailService {
    void sendMails(String message, List<String> mailList);
}
