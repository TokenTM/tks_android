package com.tokentm.sdk.components.identitypwd.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsDialogDecryptedByPwdWithStampAnimBinding;
import com.tokentm.sdk.components.identitypwd.UserConfig;
import com.tokentm.sdk.components.identitypwd.view.IdentityPwdDecryptActivity;
import com.tokentm.sdk.components.utils.KeyboardUtils;
import com.tokentm.sdk.components.utils.PropertyAnimUtils;
import com.tokentm.sdk.model.IdentityInfoStoreItem;
import com.tokentm.sdk.source.IdentityService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.utils.RAUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 身份密码输入对话框
 */
public class IdentityPwdInputDialog extends BaseAlertDialog<String> {

    private final boolean isWithStampAnim;
    private TksComponentsDialogDecryptedByPwdWithStampAnimBinding binding;
    private String uDid;

    /**
     * 带盖章效果
     */
    public static IdentityPwdInputDialog showUserIdentityPwdInputDialogWithStampAnim(@NonNull Context context, String uDid, @Nullable BiConsumer<DialogInterface, String> dialogConsumer) {
        return new IdentityPwdInputDialog(context, uDid, true, dialogConsumer);
    }

    /**
     * 无盖章效果
     */
    public static IdentityPwdInputDialog showUserIdentityPwdInputDialogNoStampAnim(@NonNull Context context, String uDid, @Nullable BiConsumer<DialogInterface, String> dialogConsumer) {
        return new IdentityPwdInputDialog(context, uDid, dialogConsumer);
    }

    private IdentityPwdInputDialog(@NonNull Context context, String uDid, @Nullable BiConsumer<DialogInterface, String> dialogConsumer) {
        this(context, uDid, false, dialogConsumer);
    }

    public IdentityPwdInputDialog(@NonNull Context context, String uDid, boolean isWithStampAnim, @Nullable BiConsumer<DialogInterface, String> dialogConsumer) {
        super(context, dialogConsumer);
        this.uDid = uDid;
        this.isWithStampAnim = isWithStampAnim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsDialogDecryptedByPwdWithStampAnimBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    @Override
    protected double getWindowScale() {
        return 0.72;
    }

    private void initView() {
        binding.identityPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                hideSoftInput();
                dismiss();
            }
        });
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                hideSoftInput();
                submit();
            }
        });
        //忘记密码
        binding.identityPwdForgetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!RAUtils.isLegalDefault()) {
                    return;
                }
                forgotIdentityPwd();
            }
        });

    }

    /**
     * 隐藏输入法
     */
    private void hideSoftInput() {
        KeyboardUtils.hideSoftInput(binding.identityPwdEt.getContext(), binding.identityPwdEt);
    }

    private void forgotIdentityPwd() {
        TokenTmClient.getService(IdentityService.class)
                .getUDIDStoreInfo(uDid)
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<IdentityInfoStoreItem>() {
                    @Override
                    public void accept(IdentityInfoStoreItem identityInfoStoreItem) throws Exception {
                        IdentityPwdDecryptActivity.launch(getContext(), identityInfoStoreItem, null);
                    }
                });
    }

//    @Override
//    public void show() {
//        super.show();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//    }

    private void submit() {
        if (TextUtils.isEmpty(binding.identityPwdEt.getText())) {
            ToastUtils.showToast("请输入身份密码");
            return;
        }
        String pwd = binding.identityPwdEt.getText().toString().trim();
        TokenTmClient.getService(IdentityService.class)
                .validateIdentityPwd(uDid, pwd)
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        //校验密码成功
                        if (aBoolean) {
                            if (isWithStampAnim) {
                                binding.tksComponentsUserDialogDecryptedWithRedChapterIv.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        PropertyAnimUtils.startStampAnim(binding.tksComponentsUserDialogDecryptedWithRedChapterIv
                                                , binding.tksComponentsUserDialogDecryptedByReadChapter, new AnimatorListenerAdapter() {
                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {
                                                        super.onAnimationEnd(animation);
                                                        binding.tksComponentsUserDialogDecryptedWithRedChapterIv.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dismiss();
                                                                setResult(pwd);
                                                            }
                                                        }, 800);
                                                    }
                                                });
                                    }
                                });
                            } else {
                                dismiss();
                                setResult(pwd);
                            }
                        } else {
                            ToastUtils.showToast("密码不正确");
                        }
                    }
                });
    }
}
