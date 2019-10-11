package com.tokentm.sdk.components.identitypwd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.databinding.UserActivityIdentityPwdSetBinding;
import com.tokentm.sdk.model.BackupPwdSecurityQuestionDTO;
import com.tokentm.sdk.model.SecurityQuestionDTO;
import com.tokentm.sdk.source.DidDataSource;
import com.xxf.arch.XXF;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 设置身份密码和安全问题
 */
public class UserIdentityPwdAndSecurityQuestionSetActivity extends BaseTitleBarActivity {

    public static void launch(Context context) {
        context.startActivity(getLauncher(context));
    }

    public static Intent getLauncher(Context context) {
        return new Intent(context, UserIdentityPwdAndSecurityQuestionSetActivity.class);
    }

    private UserActivityIdentityPwdSetBinding binding;
    private ArrayAdapter<SecurityQuestionDTO> questionAdapter_1;
    private ArrayAdapter<SecurityQuestionDTO> questionAdapter_2;
    private ArrayAdapter<SecurityQuestionDTO> questionAdapter_3;

    private final List<SecurityQuestionDTO> securityQuestions = new ArrayList<SecurityQuestionDTO>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityIdentityPwdSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        getTitleBar().setTitleBarTitle("设置身份密码");
        binding.backupPwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ComponentUtils.showTextForPlain(binding.backupPwdEt);
                } else {
                    ComponentUtils.showTextForCipher(binding.backupPwdEt);
                }
            }
        });
        //长度限制
        binding.backupPwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        binding.backupRepwdEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(UserConfig.MAX_LENTH_PWD)});
        //监听
        binding.backupPwdEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //设置密码校验提示
                binding.backupPwdCheckLengthTv.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                binding.backupPwdCheckCombineTv.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                binding.backupPwdCheckLengthTv.setSelected(s.length() >= UserConfig.MINI_LENTH_PWD && s.length() <= UserConfig.MAX_LENTH_PWD);
                binding.backupPwdCheckCombineTv.setSelected(UserConfig.PATTERN_PWD.matcher(s).matches());
            }
        });
        binding.backupRepwdHideIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    ComponentUtils.showTextForPlain(binding.backupRepwdEt);
                } else {
                    ComponentUtils.showTextForCipher(binding.backupRepwdEt);
                }
            }
        });
        binding.backupSpinnerQuestion1.setAdapter(questionAdapter_1 = createAdapter());
        binding.backupSpinnerQuestion2.setAdapter(questionAdapter_2 = createAdapter());
        binding.backupSpinnerQuestion3.setAdapter(questionAdapter_3 = createAdapter());
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    /**
     * 创建身份密钥
     * 随机生成
     *
     * @return
     */
    protected String onCreateIdentitySecretKey() {
        return UUIDUtils.generateUUID();
    }

    @SuppressLint("CheckResult")
    private void submit() {
        if (!checkInputLegal()) {
            return;
        }
        String secretKey = onCreateIdentitySecretKey();
        //密码加密
        String pwd = binding.backupPwdEt.getText().toString().trim();
        String pwdEncryptedSecretKey = EncryptionUtils.encodeString(secretKey, pwd);
        //问题答案加密
        String securityQuestions = new StringBuilder()
                .append(binding.backUpQuestionAnswer1.getText().toString().trim())
                .append(binding.backUpQuestionAnswer2.getText().toString().trim())
                .append(binding.backUpQuestionAnswer3.getText().toString().trim())
                .toString()
                .trim();
        String securityQuestionEncryptedSecretKey = EncryptionUtils.encodeString(secretKey, securityQuestions);

        List<Long> ids = Arrays.asList(
                ((SecurityQuestionDTO) binding.backupSpinnerQuestion1.getSelectedItem()).id,
                ((SecurityQuestionDTO) binding.backupSpinnerQuestion2.getSelectedItem()).id,
                ((SecurityQuestionDTO) binding.backupSpinnerQuestion3.getSelectedItem()).id);

        add2ServerAndLocal(new BackupPwdSecurityQuestionDTO(secretKey, pwdEncryptedSecretKey, securityQuestionEncryptedSecretKey, ids));
    }

    /**
     * 更新到云端 并插入本地
     *
     * @param backupPwdSecurityQuestionDTO
     */
    @SuppressLint("CheckResult")
    private void add2ServerAndLocal(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO) {
        //TODO 备份到云端
//        UserInfoBackupRepositoryImpl
//                .getInstance()
//                .addIdentityPwdToServerAndLocal(backupPwdSecurityQuestionDTO)
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(XXF.<BackupPwdSecurityQuestionDTO>bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
//                .subscribe(new Consumer<BackupPwdSecurityQuestionDTO>() {
//                    @Override
//                    public void accept(BackupPwdSecurityQuestionDTO backupPwdSecurityQuestionDTO1) throws Exception {
//                        setResult(Activity.RESULT_OK);
//                        finish();
//                    }
//                });
    }

    private boolean checkInputLegal() {
        if (TextUtils.isEmpty(binding.backupPwdEt.getText())) {
            ToastUtils.showToast("请输入身份密码!");
            return false;
        }
        if (!UserConfig.PATTERN_PWD.matcher(binding.backupPwdEt.getText()).matches()) {
            ToastUtils.showToast("请输入6-20位数字及字母!");
            return false;
        }
        if (!TextUtils.equals(binding.backupPwdEt.getText(), binding.backupRepwdEt.getText())) {
            ToastUtils.showToast("身份密码与确认身份密码输入不一致!");
            return false;
        }

        if (TextUtils.isEmpty(binding.backUpQuestionAnswer1.getText())
                || TextUtils.isEmpty(binding.backUpQuestionAnswer2.getText())
                || TextUtils.isEmpty(binding.backUpQuestionAnswer3.getText())) {
            ToastUtils.showToast("问题回答不能为空!");
            return false;
        }

        HashSet<Object> backupSpinnerQuestion = new HashSet<>(Arrays.asList(binding.backupSpinnerQuestion1.getSelectedItem(), binding.backupSpinnerQuestion2.getSelectedItem(), binding.backupSpinnerQuestion3.getSelectedItem()));
        if (backupSpinnerQuestion.size() < UserConfig.IDENTITY_PWD_SET_QUESTION_COUNT) {
            ToastUtils.showToast("问题选择重复!");
            return false;
        }
        return true;
    }

    private ArrayAdapter<SecurityQuestionDTO> createAdapter() {
        return new ArrayAdapter<SecurityQuestionDTO>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<SecurityQuestionDTO>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View itemView = super.getView(position, convertView, parent);
                initText(itemView, position);
                return itemView;
            }

            private void initText(View itemView, int pos) {
                final TextView text = itemView.findViewById(android.R.id.text1);
                SecurityQuestionDTO item = getItem(pos);
                text.setText(item != null ? item.question : "null");
                text.setTextColor(0xff737785);
                text.setTextSize(15);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View itemView = super.getDropDownView(position, convertView, parent);
                initText(itemView, position);
                return itemView;
            }
        };
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        TokenTmClient.getService(DidDataSource.class)
                .getSecurityQuestionTemplate()
                .map(new Function<List<SecurityQuestionDTO>, List<SecurityQuestionDTO>>() {
                    @Override
                    public List<SecurityQuestionDTO> apply(List<SecurityQuestionDTO> securityQuestionDTOS) throws Exception {
                        if (securityQuestionDTOS.size() < UserConfig.IDENTITY_PWD_SET_QUESTION_COUNT) {
                            throw new RuntimeException("服务器返回的安全问题数量少于3!");
                        }
                        return securityQuestionDTOS;
                    }
                })
                //加载失败 重试几次
                .retry(3)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(XXF.<List<SecurityQuestionDTO>>bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<List<SecurityQuestionDTO>>() {
                    @Override
                    public void accept(List<SecurityQuestionDTO> securityQuestionDTOS) throws Exception {


                        securityQuestions.clear();
                        securityQuestions.addAll(securityQuestionDTOS);

                        questionAdapter_1.clear();
                        questionAdapter_1.addAll(securityQuestionDTOS);
                        binding.backupSpinnerQuestion1.setSelection(0);

                        questionAdapter_2.clear();
                        questionAdapter_2.addAll(securityQuestionDTOS);
                        binding.backupSpinnerQuestion2.setSelection(1);

                        questionAdapter_3.clear();
                        questionAdapter_3.addAll(securityQuestionDTOS);
                        binding.backupSpinnerQuestion3.setSelection(2);
                    }
                });
    }
}
