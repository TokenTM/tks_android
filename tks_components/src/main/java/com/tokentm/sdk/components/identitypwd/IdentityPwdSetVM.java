package com.tokentm.sdk.components.identitypwd;

import android.app.Application;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 设置身份密码vm
 */
public class IdentityPwdSetVM extends XXFViewModel {

    public ObservableField<String> phone = new ObservableField<>();
    public ObservableLong smsCountdown = new ObservableLong();
    public ObservableField<String> smsCode = new ObservableField<>();
    public ObservableField<String> identityPwd = new ObservableField<>();
    public ObservableField<String> reIdentityPwd = new ObservableField<>();
    public ObservableBoolean submitable = new ObservableBoolean();


    public IdentityPwdSetVM(@NonNull Application application) {
        super(application);
        this.reIdentityPwd.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                submitable.set(!TextUtils.isEmpty(phone.get())
                        && !TextUtils.isEmpty(smsCode.get())
                        && !TextUtils.isEmpty(reIdentityPwd.get())
                        && TextUtils.equals(identityPwd.get(), reIdentityPwd.get())
                        && reIdentityPwd.get().length() >= UserConfig.MINI_LENTH_PWD
                        && reIdentityPwd.get().length() <= UserConfig.MAX_LENTH_PWD
                        && UserConfig.PATTERN_PWD.matcher(reIdentityPwd.get()).matches());
            }
        });
    }
}
