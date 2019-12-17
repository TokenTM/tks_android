package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokentm.sdk.components.identitypwd.UserConfig;
import com.tokentm.sdk.model.IdentityInfoStoreItem;
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

    public ObservableBoolean identityPwdLengthRightful = new ObservableBoolean();
    public ObservableBoolean identityPwdContentRightful = new ObservableBoolean();

    public ObservableField<String> reIdentityPwd = new ObservableField<>();
    public ObservableBoolean submitable = new ObservableBoolean();

    public ObservableBoolean isRegisterMode = new ObservableBoolean();
    public ObservableBoolean checkDIDData = new ObservableBoolean();
    public ObservableField<String> did = new ObservableField<String>();
    public ObservableField<IdentityInfoStoreItem> identityInfo = new ObservableField<>();
    public ObservableBoolean checkDIDProgressVisible = new ObservableBoolean();

    private Observable.OnPropertyChangedCallback submitableCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            //注册模式
            if (isRegisterMode.get()) {
                submitable.set(!TextUtils.isEmpty(phone.get())
                        && !TextUtils.isEmpty(smsCode.get())
                        && !TextUtils.isEmpty(identityPwd.get())
                        && UserConfig.PATTERN_PWD.matcher(identityPwd.get()).matches()
                        && TextUtils.equals(identityPwd.get(), reIdentityPwd.get())
                );
            } else {
                submitable.set(!TextUtils.isEmpty(phone.get())
                        && !TextUtils.isEmpty(smsCode.get())
                        && !TextUtils.isEmpty(identityPwd.get())
                );
            }
        }
    };

    public IdentityPwdSetVM(@NonNull Application application) {
        super(application);
        this.reIdentityPwd.addOnPropertyChangedCallback(submitableCallback);
        this.identityPwd.addOnPropertyChangedCallback(submitableCallback);
        this.phone.addOnPropertyChangedCallback(submitableCallback);
        this.smsCode.addOnPropertyChangedCallback(submitableCallback);

        this.identityInfo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                did.set(identityInfo.get() != null && identityInfo.get().available() ? identityInfo.get().getDid() : null);
                isRegisterMode.set(TextUtils.isEmpty(did.get()));
            }
        });

        identityPwd.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                identityPwdLengthRightful.set(!TextUtils.isEmpty(identityPwd.get()) && identityPwd.get().length() >= UserConfig.MINI_LENTH_PWD && identityPwd.get().length() <= UserConfig.MAX_LENTH_PWD);
                identityPwdContentRightful.set(UserConfig.PATTERN_PWD.matcher(identityPwd.get()).matches());
            }
        });

        phone.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                smsCode.set(null);
            }
        });
    }
}
