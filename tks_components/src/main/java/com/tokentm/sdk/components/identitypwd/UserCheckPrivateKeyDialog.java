package com.tokentm.sdk.components.identitypwd;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.view.Window;
import android.view.WindowManager;

import com.tokentm.sdk.components.common.BaseAlertDialog;
import com.tokentm.sdk.components.databinding.TksComponentsUserDialogCheckPrivateKeyBinding;

import io.reactivex.functions.BiConsumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 校验私钥弹窗
 */
public class UserCheckPrivateKeyDialog extends BaseAlertDialog<Boolean> implements UserCheckPrivateKeyPresenter {


    private TksComponentsUserDialogCheckPrivateKeyBinding binding;
    private FragmentActivity mContext;

    public UserCheckPrivateKeyDialog(@NonNull FragmentActivity context, @Nullable BiConsumer<DialogInterface, Boolean> dialogConsumer) {
        super(context, dialogConsumer);
        mContext = context;
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
            binding = TksComponentsUserDialogCheckPrivateKeyBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            initView();
        }
    }


    private void initView() {
        UserCheckPrivateKeyVM viewModel = ViewModelProviders.of(mContext).get(UserCheckPrivateKeyVM.class);
        binding.setPresenter(this);
        binding.setViewModel(viewModel);
        binding.inputPrivateKeyEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
    }

    @Override
    public void show() {
        super.show();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void onCheckPrivateKey(ObservableField<String> privateKey) {
        setResult(true);
    }

    @Override
    public void onCancelCheckPrivateKey(ObservableField<String> privateKey) {
        setResult(false);
    }
}
