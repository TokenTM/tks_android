package com.tokentm.sdk.components.identitypwd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.databinding.UserActivityIdentityPwdDecryptedBySecurityQuestionBinding;
import com.tokentm.sdk.model.BackupPwdSecurityQuestionDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.xxf.arch.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 回答上次选择的问题
 */
public class UserIdentityPwdDecryptedBySecurityQuestionActivity extends BaseTitleBarActivity {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    public static Intent getLauncher(Context context) {
        return new Intent(context, UserIdentityPwdDecryptedBySecurityQuestionActivity.class);
    }

    private UserActivityIdentityPwdDecryptedBySecurityQuestionBinding binding;
    final List<SecurityQuestionDTO> securityQuestionDTOList = new ArrayList<>();
    private BackupPwdSecurityQuestionDTO lastBackupPwdSecurityQuestionDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityIdentityPwdDecryptedBySecurityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
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
        if (lastBackupPwdSecurityQuestionDTO == null) {
            return;
        }
        if (checkInputLegal()) {
            //问题答案加密
            String securityQuestions = new StringBuilder()
                    .append(binding.identitySecurityQuestionQuestionAnswer1.getText().toString().trim())
                    .append(binding.identitySecurityQuestionQuestionAnswer2.getText().toString().trim())
                    .append(binding.identitySecurityQuestionQuestionAnswer3.getText().toString().trim())
                    .toString()
                    .trim();
            String secretKey = EncryptionUtils.decodeString(lastBackupPwdSecurityQuestionDTO.securityQuestionEncryptedSecretKey, securityQuestions);
            if (TextUtils.isEmpty(secretKey)) {
                ToastUtils.showToast("问题回答错误,请重新输入");
                return;
            }
            //解密校验成功
            // !!注意必须返回 secretKey
            setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, secretKey));
            finish();
        }
    }

    private boolean checkInputLegal() {
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
        //TODO youxuan
//        Observable.zip(
//                TokenTmClient
//                        .getService(ConfigDataSource.class)
//                        .getSecurityQuestionTemplate(),
//
//                UserInfoBackupRepositoryImpl
//                        .getInstance()
//                        .queryIdentityPwdFromServer()
//                        .map(new Function<List<BackupPwdSecurityQuestionDTO>, BackupPwdSecurityQuestionDTO>() {
//                            @Override
//                            public BackupPwdSecurityQuestionDTO apply(List<BackupPwdSecurityQuestionDTO> backupPwdSecurityQuestionDTOS) throws Exception {
//                                return backupPwdSecurityQuestionDTOS.get(0);
//                            }
//                        }),
//                new BiFunction<List<SecurityQuestionDTO>, BackupPwdSecurityQuestionDTO, Object>() {
//                    @Override
//                    public Object apply(List<SecurityQuestionDTO> securityQuestionDTOS, BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) throws Exception {
//                        securityQuestionDTOList.clear();
//                        securityQuestionDTOList.addAll(securityQuestionDTOS);
//                        lastBackupPwdSecurityQuestionDTO = backupPwdSecurityQuestionDTO;
//                        return backupPwdSecurityQuestionDTO;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        List<Long> securityQuestionIds = lastBackupPwdSecurityQuestionDTO.securityQuestionIds;
//                        Map<Long, SecurityQuestionDTO> securityQuestionDTOMap = new HashMap<>();
//                        for (SecurityQuestionDTO securityQuestionDTO : securityQuestionDTOList) {
//                            securityQuestionDTOMap.put(securityQuestionDTO.id, securityQuestionDTO);
//                        }
//
//                        SecurityQuestionDTO[] questionArray = new SecurityQuestionDTO[UserConfig.IDENTITY_PWD_SET_QUESTION_COUNT];
//                        for (int i = 0; i < questionArray.length; i++) {
//                            questionArray[i] = getUnsafeSecurityQuestion(securityQuestionDTOMap, securityQuestionIds.get(i));
//                        }
//                        binding.identitySecuritySpinnerQuestion1.setText(questionArray[0].question);
//                        binding.identitySecuritySpinnerQuestion2.setText(questionArray[1].question);
//                        binding.identitySecuritySpinnerQuestion3.setText(questionArray[2].question);
//                    }
//                });
    }

    @UiThread
    private SecurityQuestionDTO getUnsafeSecurityQuestion(Map<Long, SecurityQuestionDTO> securityQuestionDTOMap, long id)
            throws RuntimeException {
        SecurityQuestionDTO securityQuestionDTO = securityQuestionDTOMap.get(id);
        if (securityQuestionDTO == null) {
            String log = "questionId=" + id + " is not found!";
            ToastUtils.showToast(log);
            throw new RuntimeException(log);
        }
        return securityQuestionDTO;
    }
}
