package com.tokentm.sdk.components.cert;

import android.databinding.ObservableField;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description
 */
public interface UserCertByIDCardPresenter {

    /**
     * 选择照片
     *
     * @param pic
     */
    void onSelectPic(ObservableField<String> pic);

    /**
     * 身份认证
     *
     * @param userName
     * @param userIDCard
     * @param userIDCardFrontPic
     * @param userIDCardBackPic
     * @param userIDCardHandedPic
     */
    void onUserCert(ObservableField<String> userName,
                    ObservableField<String> userIDCard,
                    ObservableField<String> userIDCardFrontPic,
                    ObservableField<String> userIDCardBackPic,
                    ObservableField<String> userIDCardHandedPic
    );
}
