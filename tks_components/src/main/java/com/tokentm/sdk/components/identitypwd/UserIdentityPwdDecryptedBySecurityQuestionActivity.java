package com.tokentm.sdk.components.identitypwd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.UserActivityIdentityPwdDecryptedBySecurityQuestionBinding;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.tokentm.sdk.source.DidService;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 回答上次选择的问题
 */
public class UserIdentityPwdDecryptedBySecurityQuestionActivity extends BaseTitleBarActivity {

    private static final String KEY_DID = "did";

    public static void launch(Context context, String did) {
        context.startActivity(getLauncher(context, did));
    }

    public static Intent getLauncher(Context context, String did) {
        return new Intent(context, UserIdentityPwdDecryptedBySecurityQuestionActivity.class)
                .putExtra(KEY_DID, did);
    }

    private UserActivityIdentityPwdDecryptedBySecurityQuestionBinding binding;
    final List<SecurityQuestionDTO> securityQuestionDTOList = new ArrayList<>();
    String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityIdentityPwdDecryptedBySecurityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        did = getIntent().getStringExtra(KEY_DID);
        getTitleBar().setTitleBarTitle("安全提示问题");
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }


    @SuppressLint("CheckResult")
    private void submit() {
        if (checkInputLegal()) {
            LinkedHashMap<Long, String> oldSecurityQuestionAnswers = new LinkedHashMap<>();
            oldSecurityQuestionAnswers.put(securityQuestionDTOList.get(0).id, binding.identitySecurityQuestionQuestionAnswer1.getText().toString().trim());
            oldSecurityQuestionAnswers.put(securityQuestionDTOList.get(1).id, binding.identitySecurityQuestionQuestionAnswer2.getText().toString().trim());
            oldSecurityQuestionAnswers.put(securityQuestionDTOList.get(2).id, binding.identitySecurityQuestionQuestionAnswer3.getText().toString().trim());

            TokenTmClient.getService(DidService.class)
                    .decrypt(did, oldSecurityQuestionAnswers)
                    .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                    .compose(XXF.bindToLifecycle(this))
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean decrypted) throws Exception {
                            if (decrypted) {
                                // !!注意必须返回
                                setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, oldSecurityQuestionAnswers));
                                finish();
                            } else {
                                ToastUtils.showToast("安全找回问题回答错误!");
                            }
                        }
                    });
        }
    }

    private boolean checkInputLegal() {
        if (securityQuestionDTOList.size() < UserConfig.IDENTITY_PWD_SET_QUESTION_COUNT) {
            ToastUtils.showToast(String.format("问题数目小于%s!", UserConfig.IDENTITY_PWD_SET_QUESTION_COUNT));
            return false;
        }
        if (TextUtils.isEmpty(binding.identitySecurityQuestionQuestionAnswer1.getText())
                || TextUtils.isEmpty(binding.identitySecurityQuestionQuestionAnswer2.getText())
                || TextUtils.isEmpty(binding.identitySecurityQuestionQuestionAnswer3.getText())) {
            ToastUtils.showToast("问题回答不能为空!");
            return false;
        }
        return true;
    }


    @SuppressLint("CheckResult")
    private void loadData() {
        TokenTmClient.getService(DidService.class)
                .getSecurityQuestions(did)
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .compose(XXF.bindToLifecycle(getActivity()))
                .subscribe(new Consumer<List<SecurityQuestionDTO>>() {
                    @Override
                    public void accept(List<SecurityQuestionDTO> securityQuestionDTOS) throws Exception {
                        securityQuestionDTOList.clear();
                        securityQuestionDTOList.addAll(securityQuestionDTOS);

                        binding.identitySecuritySpinnerQuestion1.setText(securityQuestionDTOS.get(0).question);
                        binding.identitySecuritySpinnerQuestion2.setText(securityQuestionDTOS.get(1).question);
                        binding.identitySecuritySpinnerQuestion3.setText(securityQuestionDTOS.get(2).question);
                    }
                });
    }
}
