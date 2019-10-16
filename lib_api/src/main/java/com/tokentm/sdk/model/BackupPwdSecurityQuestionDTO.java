package com.tokentm.sdk.model;

import java.util.List;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class BackupPwdSecurityQuestionDTO {

    private String pwdEncryptedSecretKey;
    private String securityQuestionEncryptedSecretKey;
    private List<Long> securityQuestionIds;

    public BackupPwdSecurityQuestionDTO() {
    }

    public BackupPwdSecurityQuestionDTO(String pwdEncryptedSecretKey,
                                        String securityQuestionEncryptedSecretKey,
                                        List<Long> securityQuestionIds) {
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

    public void setPwdEncryptedSecretKey(String pwdEncryptedSecretKey) {
        this.pwdEncryptedSecretKey = pwdEncryptedSecretKey;
    }

    public void setSecurityQuestionEncryptedSecretKey(String securityQuestionEncryptedSecretKey) {
        this.securityQuestionEncryptedSecretKey = securityQuestionEncryptedSecretKey;
    }

    public void setSecurityQuestionIds(List<Long> securityQuestionIds) {
        this.securityQuestionIds = securityQuestionIds;
    }
}
