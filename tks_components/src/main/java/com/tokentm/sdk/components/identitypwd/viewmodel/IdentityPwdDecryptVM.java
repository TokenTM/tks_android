package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 设置身份密码vm
 */
public class IdentityPwdDecryptVM extends XXFViewModel {

    public ObservableField<String> phone = new ObservableField<>();
    public ObservableLong smsCountdown = new ObservableLong();
    public ObservableField<String> smsCode = new ObservableField<>();
    public ObservableInt step = new ObservableInt();
    public ObservableField<String> decryptNodeName = new ObservableField<String>();
    public ObservableBoolean submitable = new ObservableBoolean();
    private Observable.OnPropertyChangedCallback submitableCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            submitable.set(!TextUtils.isEmpty(phone.get())
                    && !TextUtils.isEmpty(smsCode.get())
            );
        }
    };

    public IdentityPwdDecryptVM(@NonNull Application application) {
        super(application);
        phone.addOnPropertyChangedCallback(submitableCallback);
        smsCode.addOnPropertyChangedCallback(submitableCallback);
        phone.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                smsCode.set(null);
            }
        });
    }
}
