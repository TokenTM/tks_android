package com.tokentm.sdk.components.cert;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.UserActivityCertByIdcardBinding;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.actiondialog.BottomPicSelectDialog;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 用户身份证认证
 */
public class UserCertByIDCardActivity extends BaseTitleBarActivity implements UserCertByIDCardPresenter {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "userCertParams";

    public static void launch(Context context, UserCertByIDCardParams userCertByIDCardParams) {
        context.startActivity(getLauncher(context, userCertByIDCardParams));
    }

    public static Intent getLauncher(Context context, UserCertByIDCardParams userCertByIDCardParams) {
        return new Intent(context, UserCertByIDCardActivity.class)
                .putExtra(KEY_CERT_PARAMS, userCertByIDCardParams);
    }


    UserActivityCertByIdcardBinding binding;
    UserCertByIDCardVM viewModel;
    UserCertByIDCardParams certByIDCardParams;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityCertByIdcardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        certByIDCardParams = (UserCertByIDCardParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);
        setTitle("身份证认证");
        viewModel = ViewModelProviders.of(this).get(UserCertByIDCardVM.class);
        binding.setViewModel(viewModel);
        binding.setPresenter(this);
        viewModel.userName.set(certByIDCardParams.getUserName());
        viewModel.userIDCard.set(certByIDCardParams.getUserIDCard());
    }

    @Override
    public void onSelectPic(ObservableField<String> pic) {
        new BottomPicSelectDialog(this, new Consumer<String>() {
            @Override
            public void accept(String url) throws Exception {
                pic.set(url);
            }
        }).show();
    }

    @Override
    public void onUserCert(ObservableField<String> userName, ObservableField<String> userIDCard, ObservableField<String> userIDCardFrontPic, ObservableField<String> userIDCardBackPic, ObservableField<String> userIDCardHandedPic) {
        ToastUtils.showToast("TODO");
    }
}
