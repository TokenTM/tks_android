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

import com.tokentm.sdk.components.databinding.UserActivityIdentityPwdResetBinding;
import com.xxf.arch.utils.ToastUtils;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 重置用户身份密码
 */
public class UserIdentityPwdReSetActivity extends BaseTitleBarActivity {


    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    public static Intent getLauncher(Context context) {
        return new Intent(context, UserIdentityPwdReSetActivity.class);
    }

    UserActivityIdentityPwdResetBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityIdentityPwdResetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        getTitleBar().setTitleBarTitle("重置身份密码");
        //长度限制
        binding.identityPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.identityRepwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});


        //设置密文或者明文显示
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
        binding.resetSecurityQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserIdentityPwdAndSecurityQuestionReSetActivity
                        .launch(getActivity());
                finish();
            }
        });
    }

    private boolean checkInputLegal() {
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
        //TODO YX
//        // 密码 来解密-- 密钥！！！！
//        String pwd = binding.identityPwdEt.getText().toString().trim();
//        //重置身份密码,本地一定有身份密钥
//        UserInfoBackupRepositoryImpl.getInstance()
//                .queryIdentityPwdFromLocal()
//                .map(new Function<BackupPwdSecurityQuestionDTO, BackupPwdSecurityQuestionDTO>() {
//                    @Override
//                    public BackupPwdSecurityQuestionDTO apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
//                        if (TextUtils.isEmpty(backupPwdSecurityQuestionDTO.secretKey)) {
//                            throw new RuntimeException("本地 secretKey 为null");
//                        }
//                        return backupPwdSecurityQuestionDTO;
//                    }
//                })
//                .map(new Function<BackupPwdSecurityQuestionDTO, BackupPwdSecurityQuestionDTO>() {
//                    @Override
//                    public BackupPwdSecurityQuestionDTO apply(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
//                        //用新密码加密密钥
//                        String pwdEncryptedSecretKey = EncryptionUtils.encodeString(backupPwdSecurityQuestionDTO.secretKey, pwd);
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
//                        setResult(Activity.RESULT_OK);
//                        finish();
//                    }
//                });
    }
}
