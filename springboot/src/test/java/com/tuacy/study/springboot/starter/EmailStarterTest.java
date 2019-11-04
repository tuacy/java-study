package com.tuacy.study.springboot.starter;

import com.tuacy.autoconfigure.email.service.EmailService;
import com.tuacy.email.EmailMM;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class EmailStarterTest {

    private EmailService emailService;
    private EmailMM emailMM;

    @Autowired
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    public void setEmailMM(EmailMM emailMM) {
        this.emailMM = emailMM;
    }

    @Test
    public void sendEmail() {
        emailService.sendEmail("1007@qq.com", "测试一哈子");
    }

    @Test
    public void sendEmail1() {
        emailMM.sendEmail();
    }
}
