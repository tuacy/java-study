package com.tuacy.autoconfigure.email.service;

public class EmailService {

    private String sendEmail;
    private String sendPassword;

    public EmailService(String sendEmail, String sendPassword) {
        this.sendEmail = sendEmail;
        this.sendPassword = sendPassword;
    }

    /**
     * 模拟发送邮件
     *
     * @param toEmail 发送邮件的目的地址
     * @param content 发送邮件的内容
     */
    public void sendEmail(String toEmail, String content) {
        // 这里我们就不做具体的逻辑了,我们就简单的打印
        System.out.println("发送邮件成功");
        System.out.println("从 " + sendEmail + "发送给" + toEmail);
        System.out.println("邮件内容:" + content);
    }
}
