package com.tokentm.sdk.components.identitypwd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.databinding.UserActivityIdentityPwdUpdateBinding;
import com.tokentm.sdk.source.DidService;
import com.xxf.arch.utils.ToastUtils;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 修改用户身份密码
 */
public class UserIdentityPwdUpdateActivity extends BaseTitleBarActivity {
    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    public static Intent getLauncher(Context context) {
        return new Intent(context, UserIdentityPwdUpdateActivity.class);
    }

    UserActivityIdentityPwdUpdateBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityIdentityPwdUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        getTitleBar().setTitleBarTitle("修改身份密码");
        //长度限制
        binding.identityOriginalPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.identityPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.identityRepwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});


        //设置密文或者明文显示
        binding.identityOriginalPwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ComponentUtils.showTextForPlain(binding.identityOriginalPwdEt);
                } else {
                    ComponentUtils.showTextForCipher(binding.identityOriginalPwdEt);
                }
            }
        });
        binding.identityPwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ComponentUtils.showTextForPlain(binding.identityPwdEt);
                } else {
                    ComponentUtils.showTextForCipher(binding.identityPwdEt);
                }
            }
        });
        binding.identityRepwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ComponentUtils.showTextForPlain(binding.identityRepwdEt);
                } else {
                    ComponentUtils.showTextForCipher(binding.identityRepwdEt);
                }
            }
        });


        //监听
        binding.identityPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //设置密码校验提示
                binding.identityPwdCheckLengthTv.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                binding.identityPwdCheckCombineTv.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                binding.identityPwdCheckLengthTv.setSelected(s.length() >= UserConfig.MINI_LENTH_PWD && s.length() <= UserConfig.MAX_LENTH_PWD);
                binding.identityPwdCheckCombineTv.setSelected(UserConfig.PATTERN_PWD.matcher(s).matches());
            }
        });


        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private boolean checkInputLegal() {
        if (TextUtils.isEmpty(binding.identityOriginalPwdEt.getText())) {
            ToastUtils.showToast("请输入原身份密码!");
            return false;
        }
        if (TextUtils.isEmpty(binding.identityPwdEt.getText())) {
            ToastUtils.showToast("请输入身份密码!");
            return false;
        }
        if (!UserConfig.PATTERN_PWD.matcher(binding.identityPwdEt.getText()).matches()) {
            ToastUtils.showToast("请输入6-20位数字及字母!");
            return false;
        }
        if (!TextUtils.equals(binding.identityPwdEt.getText(), binding.identityRepwdEt.getText())) {
            ToastUtils.showToast("身份密码与确认身份密码输入不一致!");
            return false;
        }
        return true;
    }

    @SuppressLint("CheckResult")
    private void submit() {
        if (!checkInputLegal()) {
            return;
        }
        TokenTmClient.getService(DidService.class)
                .reset()
        //TODO youxuan
        // 密码 来解密-- 密钥！！！！
//        String originalPwd = binding.identityOriginalPwdEt.getText().toString().trim();
//        String newPwd = binding.identityPwdEt.getText().toString().trim();
//        UserInfoBackupRepositoryImpl
//                .getInstance()
//                .queryIdentityPwdFromServer()
//                .map(new Function<List<BackupPwdSecurityQuestionDTO>, BackupPwdSecurityQuestionDTO>() {
//                    @Override
//                    public BackupPwdSecurityQuestionDTO apply(List<BackupPwdSecurityQuestionDTO> backupPwdSecurityQuestionDTOS) throws Exception {
//                        return backupPwdSecurityQuestionDTOS.get(0);
//                    }
//                })
//                .map(new Function<BackupPwdSecurityQuestionDTO, BackupPwdSecurityQuestionDTO>() {
//                    @Override
//                    public BackupPwdSecurityQuestionDTO apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
//                        //旧密码解密
//                        String decodeSecretKey = EncryptionUtils.decodeString(backupPwdSecurityQuestionDTO.pwdEncryptedSecretKey, originalPwd);
//                        if (TextUtils.isEmpty(decodeSecretKey)) {
//                            throw new RuntimeException("身份密码输入错误!");
//                        }
//                        //用新密码加密密钥
//                        String pwdEncryptedSecretKey = EncryptionUtils.encodeString(decodeSecretKey, newPwd);
//                        backupPwdSecurityQuestionDTO.secretKey = decodeSecretKey;
//                        backupPwdSecurityQuestionDTO.pwdEncryptedSecretKey = pwdEncryptedSecretKey;
//                        return backupPwdSecurityQuestionDTO;
//                    }
//                })
//                .flatMap(new Function<BackupPwdSecurityQuestionDTO, ObservableSource<BackupPwdSecurityQuestionDTO>>() {
//                    @Override
//                    public ObservableSource<BackupPwdSecurityQuestionDTO> apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
//                        //备份到云端 pwdEncryptedSecretKey
//                        return UserInfoBackupRepositoryImpl
//                                .getInstance()
//                                .addIdentityPwdToServerAndLocal(backupPwdSecurityQuestionDTO);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(XXF.<BackupPwdSecurityQuestionDTO>bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
//                .subscribe(new Consumer<BackupPwdSecurityQuestionDTO>() {
//                    @Override
//                    public void accept(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
//                        finish();
//                    }
//                });
    }
}
