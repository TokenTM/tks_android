package com.tokentm.sdk.uidemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tokentm.sdk.components.identitypwd.model.BindUDID;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.uidemo.databinding.ActivityLoginBinding;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 登陆或者注册页面
 */
public class LoginOrRegisterActivity extends AppCompatActivity {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    private static Intent getLauncher(Context context) {
        return new Intent(context, LoginOrRegisterActivity.class);
    }

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册 登陆 都需要
                ComponentUtils.launchUserIdentityConfirmActivity(
                        LoginOrRegisterActivity.this,
                        binding.loginPhoneEt.getText().toString(),
                        new Consumer<BindUDID>() {
                            @Override
                            public void accept(BindUDID bindUDID) throws Exception {
                                //TODO 中心化系统和userId进行绑定

                                DemoSp.getInstance().login(binding.loginPhoneEt.getText().toString(), bindUDID.getDid());
                                startActivity(new Intent(LoginOrRegisterActivity.this, MainActivity.class));
                                finish();
                            }
                        });
            }
        });
    }
}
