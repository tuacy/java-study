package com.tuacy.security.cache;

import com.tuacy.security.service.SMSRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class CacheTest {

    private SMSRecordService smsRecordService;

    @Autowired
    public void setSmsRecordService(SMSRecordService smsRecordService) {
        this.smsRecordService = smsRecordService;
    }

    @Test
    public void saveCache() {
        smsRecordService.saveSMSCode("tuacy", "12689");
        String code = smsRecordService.getSMSCode("tuacy");
        smsRecordService.clearSMSCode("tuacy");
        String code1 = smsRecordService.getSMSCode("tuacy");
        System.out.println(code);
    }

    @Test
    public void getCache() {

    }

    @Test
    public void clearCache() {

    }

}
