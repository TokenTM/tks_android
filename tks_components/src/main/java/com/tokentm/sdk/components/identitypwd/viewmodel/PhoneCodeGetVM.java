package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxf.arch.viewmodel.XXFViewModel;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 获取验证码vm
 */
public class PhoneCodeGetVM extends XXFViewModel {

    /**
     * 用户手机号
     */
    public ObservableField<String> phone = new ObservableField<>();
    /**
     * 短信倒计时
     */
    public ObservableLong smsCountdown = new ObservableLong();
    /**
     * 验证码
     */
    public ObservableField<String> smsCode = new ObservableField<>();
    /**
     * 是否可以点击提交
     */
    public ObservableBoolean submitable = new ObservableBoolean();

    private Observable.OnPropertyChangedCallback submitableCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            submitable.set(!TextUtils.isEmpty(phone.get())
                    && !TextUtils.isEmpty(smsCode.get()));
        }
    };

    public PhoneCodeGetVM(@NonNull Application application) {
        super(application);
        this.phone.addOnPropertyChangedCallback(submitableCallback);
        this.smsCode.addOnPropertyChangedCallback(submitableCallback);
    }
}
