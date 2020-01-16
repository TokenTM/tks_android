package com.tokentm.sdk.components.identitypwd.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
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

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.common.CompatUtils;
import com.tokentm.sdk.components.databinding.TksComponentsActivityIdentityPwdSetBinding;
import com.tokentm.sdk.components.identitypwd.UserConfig;
import com.tokentm.sdk.components.identitypwd.model.UDIDResult;
import com.tokentm.sdk.components.identitypwd.model.IdentityLayout;
import com.tokentm.sdk.components.identitypwd.presenter.IdentityPwdSetPresenter;
import com.tokentm.sdk.components.identitypwd.viewmodel.IdentityPwdSetVm;
import com.tokentm.sdk.exceptions.InvalidIdentityPwdException;
import com.tokentm.sdk.model.ChainResult;
import com.tokentm.sdk.model.ChainSignedResult;
import com.tokentm.sdk.model.DIDSignature;
import com.tokentm.sdk.model.IdentityInfoStoreItem;
import com.tokentm.sdk.source.BasicService;
import com.tokentm.sdk.source.ChainService;
import com.tokentm.sdk.source.IdentityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.rxjava.transformer.internal.UILifeTransformerImpl;

import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 用户身份确认页面 返回{@link com.tokentm.sdk.components.identitypwd.model.UDIDResult}
 */
public class IdentityConfirmActivity extends BaseTitleBarActivity implements IdentityPwdSetPresenter {
    //倒计时60秒
    private static final int SMS_DELAY = 60;
    private static final String KEY_PHONE = "phone";
    private static final String KEY_SHOW_INVITATION_CODE = "show_invitation_code";

    public static void launch(Context context, String phone, boolean showInvitationCode) {
        context.startActivity(getLauncher(context, phone, showInvitationCode));
    }

    public static Intent getLauncher(Context context, String phone, boolean showInvitationCode) {
        return new Intent(context, IdentityConfirmActivity.class)
                .putExtra(KEY_PHONE, phone)
                .putExtra(KEY_SHOW_INVITATION_CODE, showInvitationCode);
    }

    TksComponentsActivityIdentityPwdSetBinding binding;
    IdentityPwdSetVm viewModel;
    String phone;
    boolean showInvitationCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityIdentityPwdSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        phone = getIntent().getStringExtra(KEY_PHONE);
        showInvitationCode = getIntent().getBooleanExtra(KEY_SHOW_INVITATION_CODE, false);
        setTitle("身份密码");

        viewModel = ViewModelProviders.of(this).get(IdentityPwdSetVm.class);
        viewModel.phone.set(phone);
        viewModel.showInvitationCode.set(showInvitationCode);
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


        viewModel.smsCode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!TextUtils.isEmpty(viewModel.smsCode.get()) && viewModel.smsCode.get().length() >= 6) {
                    //检查
                    checkDid(viewModel.phone, viewModel.smsCode);
                }
            }
        });
    }

    /**
     * 检查did是否存在
     *
     * @param phone
     * @param smsCode
     */
    private void checkDid(ObservableField<String> phone, ObservableField<String> smsCode) {
        TokenTmClient.getService(IdentityService.class)
                .getUDIDStoreInfo(phone.get(), smsCode.get())
                .compose(new UILifeTransformerImpl<IdentityInfoStoreItem>() {

                    @Override
                    public void onSubscribe() {
                        viewModel.submitable.set(false);
                        viewModel.checkDidProgressVisible.set(true);
                    }

                    @Override
                    public void onNext(IdentityInfoStoreItem identityInfoStoreItem) {
                        //老用户
                        if (identityInfoStoreItem != null && identityInfoStoreItem.available()) {
                            TokenTmClient.getService(IdentityService.class)
                                    .isLogined(identityInfoStoreItem.getDid())
                                    .subscribe(new Consumer<Boolean>() {
                                        @Override
                                        public void accept(Boolean aBoolean) throws Exception {
                                            //有缓存记录
                                            if (aBoolean) {
                                                viewModel.identityLayout.set(IdentityLayout.OLD_USER_HAVE_CACHE);
                                            } else {
                                                //没有缓存记录
                                                viewModel.identityLayout.set(IdentityLayout.OLD_USER_NO_CACHE);
                                            }
                                        }
                                    });
                        } else {
                            //新用户
                            viewModel.identityLayout.set(IdentityLayout.NEW_USER);
                        }
                        viewModel.identityLayout.notifyChange();
                        viewModel.identityInfo.set(identityInfoStoreItem);
                        viewModel.checkDidProgressVisible.set(false);
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        viewModel.submitable.set(false);
                        viewModel.checkDidProgressVisible.set(false);
                    }

                    @Override
                    public void onCancel() {
                        viewModel.submitable.set(false);
                        viewModel.checkDidProgressVisible.set(false);
                    }
                })
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToErrorNotice())
                .subscribe();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onSendSMSCode(ObservableField<String> phone, ObservableLong smsCountdown) {
        TokenTmClient.getService(BasicService.class)
                .sendSmsCode(phone.get())
                .compose(XXF.bindToLifecycle(getActivity()))
                .compose(XXF.<Boolean>bindToProgressHud(
                        new ProgressHUDTransformerImpl.Builder(IdentityConfirmActivity.this)
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
    public void onForgetIdentity(ObservableField<String> phone, ObservableField<String> uDID) {
        XXF.startActivityForResult(this,
                IdentityPwdDecryptActivity.getLauncher(
                        this,
                        viewModel.identityInfo.get(),
                        phone.get()
                ),
                7001)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .map(new Function<ActivityResult, Boolean>() {
                    @Override
                    public Boolean apply(ActivityResult activityResult) throws Exception {
                        return activityResult.getData().getBooleanExtra(BaseTitleBarActivity.KEY_ACTIVITY_RESULT, false);
                    }
                })
                .take(1)
                .compose(XXF.bindUntilEvent(this, Lifecycle.Event.ON_DESTROY))
                .compose(XXF.bindToErrorNotice())
                .subscribe();
    }


    /**
     * 新用户
     *
     * @param phone
     * @param smsCode
     * @param identityPwd
     */
    @SuppressLint("CheckResult")
    @Override
    public void onIdentitySet(ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd, ObservableField<String> invitationCode) {
        TokenTmClient.getService(IdentityService.class)
                .createUDID(phone.get(), smsCode.get(), identityPwd.get(), false, invitationCode.get())
                .map(new Function<ChainSignedResult<DIDSignature>, UDIDResult>() {
                    @Override
                    public UDIDResult apply(ChainSignedResult<DIDSignature> didSignatureChainSignedResult) throws Exception {
                        return new UDIDResult(phone.get(), didSignatureChainSignedResult);
                    }
                })
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<UDIDResult>() {
                    @Override
                    public void accept(UDIDResult udidResult) throws Exception {
                        //返回uDid
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, udidResult));
                        finish();
                    }
                });
    }

    /**
     * 老用户无缓存
     *
     * @param uDID
     * @param phone
     * @param smsCode
     * @param identityPwd
     */
    @Override
    public void onIdentityDecrypt(ObservableField<String> uDID, ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd) {
        TokenTmClient.getService(IdentityService.class)
                .loginUDID(uDID.get(), phone.get(), smsCode.get(), identityPwd.get())
                .map(new Function<ChainSignedResult<DIDSignature>, UDIDResult>() {
                    @Override
                    public UDIDResult apply(ChainSignedResult<DIDSignature> didSignatureChainSignedResult) throws Exception {
                        return new UDIDResult(phone.get(), didSignatureChainSignedResult);
                    }
                })
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<UDIDResult>() {
                    @Override
                    public void accept(UDIDResult udidResult) throws Exception {
                        //返回uDid
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, udidResult));
                        finish();
                    }
                });
    }

    /**
     * 老用户的有缓存记录登录
     *
     * @param did
     * @param phone
     */
    private void oldUsersHaveCacheLogin(String did, String phone) {
        TokenTmClient.getService(IdentityService.class)
                .getLoginCache(did)
                .map(new Function<ChainSignedResult<DIDSignature>, UDIDResult>() {
                    @Override
                    public UDIDResult apply(ChainSignedResult<DIDSignature> didSignatureChainSignedResult) throws Exception {
                        return new UDIDResult(phone, didSignatureChainSignedResult);
                    }
                })
                .compose(XXF.bindToErrorNotice())
                .compose(XXF.bindToLifecycle(this))
                .subscribe(new Consumer<UDIDResult>() {
                    @Override
                    public void accept(UDIDResult udidResult) throws Exception {
                        //返回uDid
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, udidResult));
                        finish();
                    }
                });
    }

    @Override
    public void onIdentityChoice(ObservableField<IdentityLayout> identityLayout, ObservableField<String> uDID, ObservableField<String> phone, ObservableField<String> smsCode, ObservableField<String> identityPwd, ObservableField<String> invitationCode) {
        switch (identityLayout.get()) {
            case NEW_USER:
                onIdentitySet(phone, smsCode, identityPwd, invitationCode);
                break;
            case OLD_USER_NO_CACHE:
                onIdentityDecrypt(uDID, phone, smsCode, identityPwd);
                break;
            case OLD_USER_HAVE_CACHE:
                oldUsersHaveCacheLogin(uDID.get(), phone.get());
                break;
            default:
                break;
        }
    }
}
