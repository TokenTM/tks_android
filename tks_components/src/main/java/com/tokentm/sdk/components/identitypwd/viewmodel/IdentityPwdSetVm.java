package com.tokentm.sdk.components.identitypwd.viewmodel;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tokentm.sdk.components.identitypwd.UserConfig;
import com.tokentm.sdk.components.identitypwd.model.IdentityLayout;
import com.tokentm.sdk.model.IdentityInfoStoreItem;
import com.xxf.arch.XXF;
import com.xxf.arch.viewmodel.XXFViewModel;

import static com.tokentm.sdk.components.identitypwd.model.IdentityLayout.OLD_USER_HAVE_CACHE;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 设置身份密码vm
 */
public class IdentityPwdSetVm extends XXFViewModel {

    public ObservableField<String> phone = new ObservableField<>();
    public ObservableLong smsCountdown = new ObservableLong();
    public ObservableField<String> smsCode = new ObservableField<>();

    /**
     * 6-20位字符 指示是否显示
     */
    public ObservableBoolean identityPwdLengthRightful = new ObservableBoolean();
    /**
     * 字母及数字组合 指示是否显示
     */
    public ObservableBoolean identityPwdContentRightful = new ObservableBoolean();
    /**
     * 输入身份密码
     */
    public ObservableField<String> identityPwd = new ObservableField<>();
    /**
     * 再次确认输入身份密码
     */
    public ObservableField<String> reIdentityPwd = new ObservableField<>();
    /**
     * 确认按钮是否可以点击
     */
    public ObservableBoolean submitable = new ObservableBoolean();
    /**
     * 是否显示忘记密码
     */
    public ObservableBoolean isShowForgetIdentity = new ObservableBoolean();
    /**
     * 是否显示确认身份密码布局
     */
    public ObservableBoolean isShowConfirmIdentityPassword = new ObservableBoolean();
    /**
     * 是否显示 身份密码模块是否显示
     */
    public ObservableBoolean isShowIdentityPassword = new ObservableBoolean();
    public ObservableField<String> did = new ObservableField<>();
    public ObservableField<IdentityInfoStoreItem> identityInfo = new ObservableField<>();
    /**
     * 登录布局显示的三种类型
     */
    public ObservableField<IdentityLayout> identityLayout = new ObservableField<>();
    /**
     * 验证 验证码loading是否显示
     */
    public ObservableBoolean checkDidProgressVisible = new ObservableBoolean();

    private Observable.OnPropertyChangedCallback submitableCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            XXF.getLogger().d(identityLayout.get() + "identityLayout.get()变化了");
            switch (identityLayout.get()) {
                case NEW_USER:
                    submitable.set(!TextUtils.isEmpty(phone.get())
                            && !TextUtils.isEmpty(smsCode.get())
                            && smsCode.get().length() >= 6
                            && !TextUtils.isEmpty(identityPwd.get())
                            && UserConfig.PATTERN_PWD.matcher(identityPwd.get()).matches()
                            && TextUtils.equals(identityPwd.get(), reIdentityPwd.get()));
                    break;
                case OLD_USER_NO_CACHE:
                    submitable.set(!TextUtils.isEmpty(phone.get())
                            && !TextUtils.isEmpty(smsCode.get())
                            && smsCode.get().length() >= 6
                            && !TextUtils.isEmpty(identityPwd.get())
                            && UserConfig.PATTERN_PWD.matcher(identityPwd.get()).matches());
                    break;
                case OLD_USER_HAVE_CACHE:
                    submitable.set(!TextUtils.isEmpty(phone.get())
                            && !TextUtils.isEmpty(smsCode.get())
                            && smsCode.get().length() >= 6);
                    break;
                default:
                    break;
            }
        }
    };

    public IdentityPwdSetVm(@NonNull Application application) {
        super(application);
        this.reIdentityPwd.addOnPropertyChangedCallback(submitableCallback);
        this.identityPwd.addOnPropertyChangedCallback(submitableCallback);
        this.phone.addOnPropertyChangedCallback(submitableCallback);
        this.smsCode.addOnPropertyChangedCallback(submitableCallback);
        this.identityLayout.addOnPropertyChangedCallback(submitableCallback);

        identityLayout.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                XXF.getLogger().d("identityLayout变化了");
                switch (identityLayout.get()) {
                    case NEW_USER:
                        isShowIdentityPassword.set(true);
                        isShowForgetIdentity.set(false);
                        isShowConfirmIdentityPassword.set(true);
                        break;
                    case OLD_USER_NO_CACHE:
                        isShowIdentityPassword.set(true);
                        isShowForgetIdentity.set(true);
                        isShowConfirmIdentityPassword.set(false);
                        break;
                    case OLD_USER_HAVE_CACHE:
                        isShowIdentityPassword.set(false);
                        isShowForgetIdentity.set(false);
                        isShowConfirmIdentityPassword.set(false);
                        break;
                    default:
                        break;
                }
            }
        });
        this.identityInfo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                did.set(identityInfo.get() != null && identityInfo.get().available() ? identityInfo.get().getDid() : null);
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
                isShowIdentityPassword.set(false);
                smsCode.set(null);
            }
        });
        identityLayout.set(OLD_USER_HAVE_CACHE);
    }
}
