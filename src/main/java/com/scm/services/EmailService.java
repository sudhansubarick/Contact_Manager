package com.scm.services;

public interface EmailService {

    
    void sendEmail(String to,String subject, String body);

    void senEmailWithHtml();

    void sendEmailWithAttachment();

}
