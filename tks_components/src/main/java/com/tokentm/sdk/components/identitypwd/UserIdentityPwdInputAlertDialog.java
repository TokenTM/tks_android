package com.tokentm.sdk.components.identitypwd;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsUserDialogDecryptedByPwdBinding;
import com.tokentm.sdk.source.IdentityPwdService;
import com.xxf.arch.XXF;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 身份密码输入对话框
 */
public class UserIdentityPwdInputAlertDialog extends BaseAlertDialog<String> {


    private TksComponentsUserDialogDecryptedByPwdBinding binding;
    String uDid;

    public UserIdentityPwdInputAlertDialog(@NonNull Context context, String uDid, @Nullable BiConsumer<DialogInterface, String> dialogConsumer) {
        super(context, dialogConsumer);
        this.uDid = uDid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window dialogWindow = this.getWindow();
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //设置window背景透明
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        binding = TksComponentsUserDialogDecryptedByPwdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    private void initView() {
        binding.identityPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        //忘记密码
        binding.identityPwdForgetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotIdentityPwd();
            }
        });
        //延时一段时间在执行动画,因为进来先弹键盘
        binding.tksComponentsUserDialogDecryptedWithRedChapterIv.postDelayed(new Runnable() {
            @Override
            public void run() {
                startPropertyAnim();
            }
        },800);
    }

    /**
     * 动画实际执行
     */
    private void startPropertyAnim() {
        // X轴方向上的坐标
        float translationX = binding.tksComponentsUserDialogDecryptedByReadChapter.getTranslationX();
        float translationY = binding.tksComponentsUserDialogDecryptedByReadChapter.getTranslationY();

        //这个还小
        float left = binding.tksComponentsUserDialogDecryptedWithRedChapterIv.getLeft();
        float left1 = binding.tksComponentsUserDialogDecryptedByReadChapter.getLeft();

        //这个还小
        int top = binding.tksComponentsUserDialogDecryptedWithRedChapterIv.getTop();
        int top1 = binding.tksComponentsUserDialogDecryptedByReadChapter.getTop();

        ObjectAnimator animator = ObjectAnimator.ofFloat(binding.tksComponentsUserDialogDecryptedByReadChapter, "scaleY", 1f, 0.7f, 1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(binding.tksComponentsUserDialogDecryptedByReadChapter, "scaleX", 1f, 0.7f, 1f);
        AnimatorSet animSet1 = new AnimatorSet();
        animSet1.play(animator).with(animator1);
        animSet1.setDuration(900);
        animSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                binding.tksComponentsUserDialogDecryptedByReadChapter.setVisibility(View.INVISIBLE);
                binding.tksComponentsUserDialogDecryptedWithRedChapterIv.setVisibility(View.VISIBLE);
            }
        });

        // 向右移动500pix，然后再移动到原来的位置复原。
        // 参数“translationX”指明在x坐标轴位移，即水平位移。
        ObjectAnimator anim = ObjectAnimator.ofFloat(binding.tksComponentsUserDialogDecryptedByReadChapter, "translationX", translationX, -(left1-left));
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(binding.tksComponentsUserDialogDecryptedByReadChapter, "translationY", translationY, -(top1-top));

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim).with(anim1);
        animSet.setDuration(900);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animSet1.start();
            }
        });
        animSet.start();
    }

    private void forgotIdentityPwd() {
        UserIdentityPwdReSetActivity.launch(getContext(), null);
    }

    @Override
    public void show() {
        super.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void submit() {
        if (TextUtils.isEmpty(binding.identityPwdEt.getText())) {
            ToastUtils.showToast("请输入身份密码");
            return;
        }
        String pwd = binding.identityPwdEt.getText().toString().trim();
        TokenTmClient.getService(IdentityPwdService.class)
                .validateIdentityPwd(uDid, pwd)
                .compose(XXF.bindToErrorNotice())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            setResult(pwd);
                        } else {
                            ToastUtils.showToast("密码不正确");
                        }
                    }
                });
    }
}
