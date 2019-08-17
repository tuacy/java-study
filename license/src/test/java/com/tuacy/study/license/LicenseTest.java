package com.tuacy.study.license;

import com.tuacy.study.license.license.LicenseVerify;
import com.tuacy.study.license.license.creator.LicenseCreator;
import com.tuacy.study.license.license.creator.LicenseCreatorParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;


/**
 * @name: LicenseTest
 * @author: tuacy.
 * @date: 2019/8/17.
 * @version: 1.0
 * @Description:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LicenseTest {

    private LicenseVerify licenseVerify;

    @Autowired
    public void setLicenseVerify(LicenseVerify licenseVerify) {
        this.licenseVerify = licenseVerify;
    }

    @Test
    public void licenseCreate() {
        // 生成license需要的一些参数
        LicenseCreatorParam param = new LicenseCreatorParam();
        param.setSubject("ioserver");
        param.setPrivateAlias("privateKey");
        param.setKeyPass("a123456");
        param.setStorePass("a123456");
        param.setLicensePath("D:\\licenseTest\\license.lic");
        param.setPrivateKeysStorePath("D:\\licenseTest\\privateKeys.keystore");
        Calendar issueCalendar = Calendar.getInstance();
        param.setIssuedTime(issueCalendar.getTime());
        Calendar expiryCalendar = Calendar.getInstance();
        expiryCalendar.set(2020, Calendar.DECEMBER, 31, 23, 59, 59);
        param.setExpiryTime(expiryCalendar.getTime());
        param.setConsumerType("user");
        param.setConsumerAmount(1);
        param.setDescription("测试");
        LicenseCreator licenseCreator = new LicenseCreator(param);
        // 生成license
        licenseCreator.generateLicense();
    }

    @Test
    public void licenseVerify() {
       System.out.println("licese是否有效：" + licenseVerify.verify());
    }

}
