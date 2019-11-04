package com.tokentm.sdk.components.identitypwd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.common.CompatUtils;
import com.tokentm.sdk.components.databinding.UserActivityIdentityPwdSetBinding;
import com.tokentm.sdk.source.BasicService;
import com.tokentm.sdk.source.IdentityPwdService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 用户身份密码设置
 */
public class UserIdentityPwdSetActivity extends BaseTitleBarActivity implements IdentityPwdSetPresenter {
    //倒计时60秒
    private static final int SMS_DELAY = 60;
    private static final String KEY_PHONE = "phone";

    public static void launch(Context context, String phone) {
        context.startActivity(getLauncher(context, phone));
    }

    public static Intent getLauncher(Context context, String phone) {
        return new Intent(context, UserIdentityPwdSetActivity.class)
                .putExtra(KEY_PHONE, phone);
    }

    UserActivityIdentityPwdSetBinding binding;
    IdentityPwdSetVM viewModel;
    String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityIdentityPwdSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        phone = getIntent().getStringExtra(KEY_PHONE);
        setTitle("设置身份密码");

        viewModel = ViewModelProviders.of(this).get(IdentityPwdSetVM.class);
        viewModel.phone.set(phone);

        binding.setViewModel(viewModel);
        binding.setPresenter(this);

        //长度限制
        binding.identityPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.identityRepwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});


        //设置密文或者明文显示
        binding.identityPwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    CompatUtils.showTextForPlain(binding.identityPwdEt);
                } else {
                    CompatUtils.showTextForCipher(binding.identityPwdEt);
                }
            }
        });
        binding.identityRepwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    CompatUtils.showTextForPlain(binding.identityRepwdEt);
                } else {
                    CompatUtils.showTextForCipher(binding.identityRepwdEt);
                }
            }
        });


        viewModel.identityPwd.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String identityPwd = viewModel.identityPwd.get();
                //设置密码校验提示
                binding.identityPwdCheckLengthTv.setVisibility(TextUtils.isEmpty(identityPwd) ? View.GONE : View.VISIBLE);
                binding.identityPwdCheckCombineTv.setVisibility(TextUtils.isEmpty(identityPwd) ? View.GONE : View.VISIBLE);
                binding.identityPwdCheckLengthTv.setSelected(identityPwd.length() >= UserConfig.MINI_LENTH_PWD && identityPwd.length() <= UserConfig.MAX_LENTH_PWD);
                binding.identityPwdCheckCombineTv.setSelected(UserConfig.PATTERN_PWD.matcher(identityPwd).matches());
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSendSMSCode(ObservableField<String> phone, ObservableLong smsCountdown) {
        TokenTmClient.getService(BasicService.class)
                .sendSmsCode(phone.get())
                .compose(XXF.bindToLifecycle(getActivity()))
                .compose(XXF.<Boolean>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(UserIdentityPwdSetActivity.this)
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
                .compose(XXF.bindToLifecycle(getActivity()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        smsCountdown.set(aLong);
                    }
                });
    }

    @Override
    public void onIdentityPwdSet(ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd) {
        TokenTmClient.getService(IdentityPwdService.class)
                .createUDID(phone.get(), smsCode.get(), identityPwd.get())
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String uDid) throws Exception {
                        //返回uDid
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, uDid));
                        finish();
                    }
                });
    }
}
