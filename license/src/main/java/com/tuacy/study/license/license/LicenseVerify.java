package com.tuacy.study.license.license;

import de.schlichtherle.license.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.prefs.Preferences;

/**
 * License校验类
 */
public class LicenseVerify {

    private static Logger logger = LogManager.getLogger(LicenseVerify.class);
    /**
     * 证书subject
     */
    private String subject;
    /**
     * 公钥别称
     */
    private String publicAlias;
    /**
     * 访问公钥库的密码
     */
    private String storePass;
    /**
     * 证书生成路径
     */
    private String licensePath;
    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;
    /**
     * LicenseManager
     */
    private LicenseManager licenseManager;
    /**
     * 标识证书是否安装成功
     */
    private boolean installSuccess;

    public LicenseVerify(String subject, String publicAlias, String storePass, String licensePath, String publicKeysStorePath) {
        this.subject = subject;
        this.publicAlias = publicAlias;
        this.storePass = storePass;
        this.licensePath = licensePath;
        this.publicKeysStorePath = publicKeysStorePath;
    }

    /**
     * 安装License证书，读取证书相关的信息, 在bean注册的时候调用该函数
     */
    public void installLicense() {
        try {
            Preferences preferences = Preferences.userNodeForPackage(LicenseVerify.class);

            CipherParam cipherParam = new DefaultCipherParam(storePass);

            KeyStoreParam publicStoreParam = new CustomKeyStoreParam(LicenseVerify.class,
                    publicKeysStorePath,
                    publicAlias,
                    storePass,
                    null);
            LicenseParam licenseParam = new DefaultLicenseParam(subject, preferences, publicStoreParam, cipherParam);

            licenseManager = new CustomLicenseManager(licenseParam);
            licenseManager.uninstall();
            LicenseContent licenseContent = licenseManager.install(new File(licensePath));
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            installSuccess = true;
            logger.info("------------------------------- 证书安装成功 -------------------------------");
            logger.info(MessageFormat.format("证书有效期：{0} - {1}", format.format(licenseContent.getNotBefore()), format.format(licenseContent.getNotAfter())));
        } catch (Exception e) {
            installSuccess = false;
            logger.error("------------------------------- 证书安装成功 -------------------------------");
            logger.error(e);
        }
    }

    /**
     * 卸载证书
     */
    public void unInstallLicense() {
        if (installSuccess) {
            try {
                licenseManager.uninstall();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    /**
     * 校验License证书
     */
    public boolean verify() {
        try {
            LicenseContent licenseContent = licenseManager.verify();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
