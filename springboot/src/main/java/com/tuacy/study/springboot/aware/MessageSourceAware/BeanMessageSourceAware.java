package com.tuacy.study.springboot.aware.MessageSourceAware;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BeanMessageSourceAware implements MessageSourceAware {

    /**
     * 如此,当前Bean能够获得国际化和本地化消息支持
     */
    private MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.printMessage();
    }

    public void printMessage() {

//        String hello = messageSource.getMessage("hello", null, "", null);
//
//        String name = messageSource.getMessage("hello",
//                new Object[]{28, "http://www.yiibai.com"}, Locale.US);
//
//        System.out.println("Customer name (English) : " + name);
//
//        String namechinese = messageSource.getMessage("customer.name",
//                new Object[]{28, "http://www.yiibai.com"},
//                Locale.SIMPLIFIED_CHINESE);
//
//        System.out.println("Customer name (Chinese) : " + namechinese);
    }
}
