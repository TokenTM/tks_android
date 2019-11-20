package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokentm.sdk.components.identitypwd.UserConfig;
import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 设置身份密码vm
 */
public class IdentityPwdUpdateVM extends XXFViewModel {

    public ObservableField<String> identityPwd = new ObservableField<>();
    public ObservableField<String> reIdentityPwd = new ObservableField<>();
    public ObservableBoolean submitable = new ObservableBoolean();

    private Observable.OnPropertyChangedCallback submitableCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            submitable.set(
                    !TextUtils.isEmpty(identityPwd.get())
                            && UserConfig.PATTERN_PWD.matcher(identityPwd.get()).matches()
                            && TextUtils.equals(identityPwd.get(), reIdentityPwd.get())
            );
        }
    };

    public IdentityPwdUpdateVM(@NonNull Application application) {
        super(application);
        this.reIdentityPwd.addOnPropertyChangedCallback(submitableCallback);
        this.identityPwd.addOnPropertyChangedCallback(submitableCallback);
    }
}
