package com.tokentm.sdk.components.identitypwd;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsUserDialogGetPhoneCodeBinding;
import com.tokentm.sdk.source.BasicService;
import com.tokentm.sdk.source.IdentityPwdService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 手机号获取验证码对话框
 */
public class UserIdentityPwdDecryptDialog extends BaseAlertDialog<Boolean> implements PhoneCodeGetPresenter {


    /**
     * 倒计时60秒
     */
    private static final int SMS_DELAY = 60;
    private TksComponentsUserDialogGetPhoneCodeBinding binding;
    private String uDid;
    private FragmentActivity mContext;

    public UserIdentityPwdDecryptDialog(@NonNull FragmentActivity context, String uDid, @Nullable BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        super(context, dialogConsumer);
        mContext = context;
        this.uDid = uDid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window dialogWindow = this.getWindow();
        if (dialogWindow != null) {
            dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            //设置window背景透明
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            binding = TksComponentsUserDialogGetPhoneCodeBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            initView();
        }
    }


    private void initView() {
        PhoneCodeGetVM viewModel = ViewModelProviders.of(mContext).get(PhoneCodeGetVM.class);
        binding.setPresenter(this);
        binding.setViewModel(viewModel);
        binding.inputPhoneCodeEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
    }

    @Override
    public void show() {
        super.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 获取验证码
     *
     * @param phone        手机号
     * @param smsCountdown 倒计时
     */
    @Override
    public void onSendSMSCode(ObservableField<String> phone, ObservableLong smsCountdown) {
        if (TextUtils.isEmpty(phone.get())) {
            ToastUtils.showToast("请输入您的手机号");
            return;
        }
        TokenTmClient.getService(BasicService.class)
                .sendSmsCode(phone.get())
                .compose(XXF.<Boolean>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(this)
                                .setLoadingNotice("发送中..."))
                )
                .flatMap(new Function<Boolean, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Boolean aBoolean) throws Exception {
                        return io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS)
                                .take(SMS_DELAY + 1)
                                .map(new Function<Long, Long>() {
                                    @Override
                                    public Long apply(Long aLong) throws Exception {
                                        return SMS_DELAY - aLong;
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        smsCountdown.set(aLong);
                    }
                });
    }

    /**
     * 校验验证码
     *
     * @param phone
     * @param smsCode
     */
    @Override
    public void onCheckPhoneCode(ObservableField<String> phone, ObservableField<String> smsCode) {
        TokenTmClient.getService(IdentityPwdService.class)
                .decryptUDID(uDid, phone.get(), smsCode.get())
                .compose(XXF.bindToLifecycle(mContext))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            setResult(true);
                        } else {
                            ToastUtils.showToast("操作失败");
                        }
                    }
                });
    }
}
