package com.tuacy.autoconfigure.email.configure;

import com.tuacy.autoconfigure.email.entity.EmailProperties;
import com.tuacy.autoconfigure.email.service.EmailService;
import com.tuacy.email.EmailMM;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EmailProperties.class)
public class EmailAutoConfigure {

    /**
     * 一些属性配置
     */
    private final EmailProperties emailProperties;

    public EmailAutoConfigure(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    /**
     * 需要自动配置的Bean,这样该Bean就可以在添加了starter包的项目里面直接哪里啊用就可以了
     *
     * @return EmailService
     */
    @Bean
    @ConditionalOnClass(EmailService.class)
    @ConditionalOnMissingBean
    public EmailService emailService() {
        return new EmailService(emailProperties.getSendEmail(), emailProperties.getSendPassword());
    }

    @Bean
    @ConditionalOnClass(EmailMM.class)
    @ConditionalOnMissingBean
    public EmailMM emailMM() {
        return new EmailMM();
    }
}
