package com.tokentm.sdk.components.identitypwd.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.R;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.TksComponentsUserActivityIdentityPwdDecryptBinding;
import com.tokentm.sdk.components.identitypwd.model.StepModel;
import com.tokentm.sdk.components.identitypwd.presenter.IdentityPwdDecryptPresenter;
import com.tokentm.sdk.components.identitypwd.viewmodel.IdentityPwdDecryptVM;
import com.tokentm.sdk.model.NodeServiceDecryptedPartItem;
import com.tokentm.sdk.model.NodeServiceEncryptedPartItem;
import com.tokentm.sdk.source.IdentityService;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 重置用户身份密码设置
 */
public class UserIdentityPwdResetActivity extends BaseTitleBarActivity implements IdentityPwdDecryptPresenter {

    private static final String KEY_DID = "did";
    private static final String KEY_PHONE = "phone";


    public static void launch(@NonNull Context context, @NonNull String did, @Nullable String phone) {
        context.startActivity(getLauncher(context, did, phone));
    }

    public static Intent getLauncher(@NonNull Context context, @NonNull String did, @Nullable String phone) {
        return new Intent(context, UserIdentityPwdResetActivity.class)
                .putExtra(KEY_DID, did)
                .putExtra(KEY_PHONE, phone);
    }

    private String did;
    private String phone;
    TksComponentsUserActivityIdentityPwdDecryptBinding binding;
    StepAdapter stepAdapter;
    List<List<NodeServiceEncryptedPartItem>> encryptIdentityPwdParts = new ArrayList<>();
    final SparseArray<NodeServiceDecryptedPartItem> decryptedPartItemSparseArray = new SparseArray<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsUserActivityIdentityPwdDecryptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }


    private void initView() {
        setTitle("重置身份密码");
        did = getIntent().getStringExtra(KEY_DID);
        phone = getIntent().getStringExtra(KEY_PHONE);

        binding.setViewModel(ViewModelProviders.of(this).get(IdentityPwdDecryptVM.class));
        binding.setPresenter(this);

        binding.getViewModel().phone.set(phone);

        binding.recyclerView.setAdapter(stepAdapter = new StepAdapter());
        stepAdapter.bindData(true, generateSteps(3));


        binding.getViewModel().step.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                stepAdapter.setStep(binding.getViewModel().step.get());
            }
        });
    }

    private List<StepModel> generateSteps(int stepCount) {
        List<StepModel> stepModels = new ArrayList<>();
        for (int i = 0; i < stepCount; i++) {
            stepModels.add(new StepModel(R.drawable.tks_components_step_cheked, R.drawable.tks_components_step_uncheked));
        }
        return stepModels;
    }


    private void loadData() {


    }

    @Override
    public void onSendSMSCode(ObservableField<String> phone, ObservableLong smsCountdown) {

    }

    @SuppressLint("CheckResult")
    @Override
    public void onIdentityPwdDecrypt(ObservableInt step, ObservableField<String> phone, ObservableField<String> smsCode) {
        io.reactivex.Observable
                .defer(new Callable<ObservableSource<List<List<NodeServiceEncryptedPartItem>>>>() {
                    @Override
                    public ObservableSource<List<List<NodeServiceEncryptedPartItem>>> call() throws Exception {
                        if (encryptIdentityPwdParts == null || encryptIdentityPwdParts.isEmpty()) {
                            return TokenTmClient.getService(IdentityService.class)
                                    .getEncryptIdentityPwdParts(did, phone.get(), smsCode.get())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnNext(new Consumer<List<List<NodeServiceEncryptedPartItem>>>() {
                                        @Override
                                        public void accept(List<List<NodeServiceEncryptedPartItem>> lists) throws Exception {
                                            encryptIdentityPwdParts = lists;

                                            //刷新UI
                                            stepAdapter.clearData();
                                            stepAdapter.setStep(0);
                                            stepAdapter.bindData(true, generateSteps(lists.get(0).size()));
                                        }
                                    });
                        }
                        return io.reactivex.Observable
                                .just(encryptIdentityPwdParts);


                    }
                })
                .flatMap(new Function<List<List<NodeServiceEncryptedPartItem>>, ObservableSource<NodeServiceDecryptedPartItem>>() {
                    @Override
                    public ObservableSource<NodeServiceDecryptedPartItem> apply(List<List<NodeServiceEncryptedPartItem>> lists) throws Exception {
                        NodeServiceEncryptedPartItem nodeServiceEncryptedPartItem = lists.get(0).get(step.get());
                        NodeServiceEncryptedPartItem nodeServiceEncryptedPartItemCopy = lists.get(1).get(step.get());

                        return decryptIdentityPwdPart(nodeServiceEncryptedPartItem, nodeServiceEncryptedPartItemCopy, phone.get(), smsCode.get());
                    }
                })
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<NodeServiceDecryptedPartItem>() {
                    @Override
                    public void accept(NodeServiceDecryptedPartItem nodeServiceDecryptedPartItem) throws Exception {
                        decryptedPartItemSparseArray.put(step.get(), nodeServiceDecryptedPartItem);
                        step.set(step.get() + 1);

                        if (decryptedPartItemSparseArray.size() == encryptIdentityPwdParts.get(0).size()) {
                            List<NodeServiceDecryptedPartItem> decryptedPartItems = new ArrayList<>();
                            for (int i = 0; i < decryptedPartItemSparseArray.size(); i++) {
                                decryptedPartItems.add(decryptedPartItemSparseArray.get(i));
                            }
                            String pwd = TokenTmClient.getService(IdentityService.class)
                                    .decryptIdentityPwd(did, decryptedPartItems);
                            gotoPwdUpdatePage(pwd);

                        }
                    }
                });
    }

    private void gotoPwdUpdatePage(String oldPwd) {
        XXF.startActivityForResult(this, UserIdentityPwdUpdateActivity.getLauncher(this, did, oldPwd), 2001)
                .filter(new Predicate<ActivityResult>() {
                    @Override
                    public boolean test(ActivityResult activityResult) throws Exception {
                        return activityResult.isOk();
                    }
                })
                .take(1)
                .subscribe(new Consumer<ActivityResult>() {
                    @Override
                    public void accept(ActivityResult activityResult) throws Exception {
                        setResult(Activity.RESULT_OK, getIntent().putExtra(KEY_ACTIVITY_RESULT, true));
                        finish();
                    }
                });
    }

    /**
     * @param encryptedPartItem
     * @param encryptedPartItemCopy 第二备份
     * @param phone
     * @param smsCode
     * @return
     */
    private io.reactivex.Observable<NodeServiceDecryptedPartItem> decryptIdentityPwdPart(@NonNull NodeServiceEncryptedPartItem encryptedPartItem,
                                                                                         @Nullable NodeServiceEncryptedPartItem encryptedPartItemCopy,
                                                                                         @NonNull String phone, @NonNull String smsCode) {
        return TokenTmClient.getService(IdentityService.class)
                .decryptIdentityPwdPart(did, encryptedPartItem, phone, smsCode)
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends NodeServiceDecryptedPartItem>>() {
                    @Override
                    public ObservableSource<? extends NodeServiceDecryptedPartItem> apply(Throwable throwable) throws Exception {
                        if (encryptedPartItemCopy != null) {
                            return TokenTmClient.getService(IdentityService.class)
                                    .decryptIdentityPwdPart(did, encryptedPartItemCopy, phone, smsCode);
                        }
                        return io.reactivex.Observable.error(throwable);
                    }
                });
    }
}
