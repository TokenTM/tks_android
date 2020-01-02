package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 认证说明VM
 */
public class CompanyCertificationDetailsVm extends CompanyCertificationInstructionsVm {

    /**
     * VM初始化
     *
     * @param application
     */
    public CompanyCertificationDetailsVm(@NonNull Application application) {
        super(application);
        isShowTop.set(false);
    }

}
