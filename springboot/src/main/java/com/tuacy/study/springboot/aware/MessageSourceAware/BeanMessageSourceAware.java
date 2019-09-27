package com.tuacy.study.springboot.aware.MessageSourceAware;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Locale;

@Component
public class BeanMessageSourceAware implements MessageSourceAware {

    /**
     * 一般会通过@Autowired注入MessageSource来替代MessageSourceAware接口的使用
     */
    private MessageSource messageSource;

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.printMessage();
    }

    public void printMessage() {

        String hello = messageSource.getMessage("hello", null, "", Locale.CHINA);

        Object[] arg = new Object[] { "Tuacy", Calendar.getInstance().getTime() };
        String userInfo = messageSource.getMessage("userInfo", arg, "", Locale.CHINA);
    }
}
