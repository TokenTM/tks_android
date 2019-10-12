package com.tokentm.sdk.model;

import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BackupPwdSecurityQuestionDTO {
    /**
     * 不传递到网络上去
     */
    public String pwdEncryptedSecretKey;
    public String securityQuestionEncryptedSecretKey;
    public List<Long> securityQuestionIds;

    public BackupPwdSecurityQuestionDTO() {
    }

    public BackupPwdSecurityQuestionDTO(String pwdEncryptedSecretKey, String securityQuestionEncryptedSecretKey, List<Long> securityQuestionIds) {
        this.pwdEncryptedSecretKey = pwdEncryptedSecretKey;
        this.securityQuestionEncryptedSecretKey = securityQuestionEncryptedSecretKey;
        this.securityQuestionIds = securityQuestionIds;
    }
    

    public String getPwdEncryptedSecretKey() {
        return pwdEncryptedSecretKey;
    }


    public String getSecurityQuestionEncryptedSecretKey() {
        return securityQuestionEncryptedSecretKey;
    }

    public List<Long> getSecurityQuestionIds() {
        return securityQuestionIds;
    }
}
