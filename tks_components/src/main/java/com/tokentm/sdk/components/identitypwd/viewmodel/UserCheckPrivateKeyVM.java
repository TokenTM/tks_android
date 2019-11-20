package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 校验私钥VM
 */
public class UserCheckPrivateKeyVM extends XXFViewModel {

    /**
     * 用户输入的私钥
     */
    public ObservableField<String> privateKey = new ObservableField<>();

    public ObservableBoolean submitable = new ObservableBoolean();

    private Observable.OnPropertyChangedCallback submitableCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            submitable.set(!TextUtils.isEmpty(privateKey.get()));
        }
    };

    public UserCheckPrivateKeyVM(@NonNull Application application) {
        super(application);
        this.privateKey.addOnPropertyChangedCallback(submitableCallback);
    }
}
