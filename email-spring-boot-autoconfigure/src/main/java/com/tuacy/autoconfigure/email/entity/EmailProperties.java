package com.tuacy.autoconfigure.email.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("email")
public class EmailProperties {

    private String sendEmail;
    private String sendPassword;

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getSendPassword() {
        return sendPassword;
    }

    public void setSendPassword(String sendPassword) {
        this.sendPassword = sendPassword;
    }
}
