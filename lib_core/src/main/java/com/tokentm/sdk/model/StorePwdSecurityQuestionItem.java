package com.tokentm.sdk.model;

import com.tokentm.sdk.Config;
import com.tokentm.sdk.common.encrypt.EncryptionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public class StorePwdSecurityQuestionItem extends StoreItem<BackupPwdSecurityQuestionDTO> {
    public StorePwdSecurityQuestionItem(String DID, BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) {
        this.setDid(DID);
        this.setDataId(DID);
        this.setDataType(Config.BackupType.TYPE_BACK_UP_SECRETKEY.getValue());
        this.setData(backupPwdSecurityQuestionDTO);
    }

    public StorePwdSecurityQuestionItem(String DID, String dpk, String identityPwd, LinkedHashMap<Long, String> securityQuestionAnswers) {
        this.setDid(DID);
        this.setDataId(DID);
        this.setDataType(Config.BackupType.TYPE_BACK_UP_SECRETKEY.getValue());
        this.setData(generate(dpk, identityPwd, securityQuestionAnswers));
    }

    private BackupPwdSecurityQuestionDTO generate(String secretKey, String identityPwd, LinkedHashMap<Long, String> securityQuestionAnswers) {
        String pwdEncryptedSecretKey = EncryptionUtils.encodeString(secretKey, identityPwd);

        StringBuffer questionAnswers = new StringBuffer();
        List<Long> securityQuestionIds = new ArrayList<>();
        for (Map.Entry<Long, String> entry : securityQuestionAnswers.entrySet()) {
            securityQuestionIds.add(entry.getKey());
            questionAnswers.append(entry.getValue());
        }
        String securityQuestionEncryptedSecretKey = EncryptionUtils.encodeString(secretKey, questionAnswers.toString());

        return new BackupPwdSecurityQuestionDTO(
                pwdEncryptedSecretKey,
                securityQuestionEncryptedSecretKey,
                securityQuestionIds);
    }
}
