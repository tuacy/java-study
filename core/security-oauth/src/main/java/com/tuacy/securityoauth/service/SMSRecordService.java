package com.tuacy.securityoauth.service;

/**
 * @name: SMSRecordService
 * @author: tuacy.
 * @date: 2019/11/29.
 * @version: 1.0
 * @Description:
 */
public interface SMSRecordService {

    /**
     * 保存验证码
     */
    String saveSMSCode(String userName, String smsCode);

    /**
     * 获取验证码
     */
    String getSMSCode(String userName);

    /**
     * 清除验证码
     */
    void clearSMSCode(String userName);

}
