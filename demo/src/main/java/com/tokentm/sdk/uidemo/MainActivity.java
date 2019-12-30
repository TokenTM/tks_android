package com.tokentm.sdk.uidemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.cert.model.UserCertByIDCardParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.utils.ComponentUtils;
import com.tokentm.sdk.model.StoreItem;
import com.tokentm.sdk.source.StoreService;
import com.tokentm.sdk.source.TokenTmClient;
import com.tokentm.sdk.uidemo.databinding.ActivityMainBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.utils.ToastUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 入口
 */
public class MainActivity extends BaseTitleBarActivity {


    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    String did;

    @Override
    protected void onResume() {
        super.onResume();
        did = DemoSp.getInstance().getLoginDID();
    }

    private void initView() {
        setTitle("tks_demo");
        binding.btCreateDid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DidDemoActivity.launch(getActivity());
            }
        });
        binding.btBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StoreItem<String> storeItem = new StoreItem<>();
                storeItem.setDid(did);
                storeItem.setDataId(did);
                storeItem.setDataType("testType");
                storeItem.setData(String.format("android_x_%s", System.currentTimeMillis()));
                TokenTmClient.getService(StoreService.class)
                        .store(storeItem)
                        .compose(XXF.bindToErrorNotice())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                ToastUtils.showToast("备份成功");
                            }
                        });
            }
        });
        binding.btGetBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TokenTmClient.getService(StoreService.class)
                        .getStore(did, "testType", did)
                        .compose(XXF.bindToErrorNotice())
                        .subscribe(new Consumer<StoreItem<String>>() {
                            @Override
                            public void accept(StoreItem<String> stringStoreItem) throws Exception {
                                ToastUtils.showToast("备份获取1:" + stringStoreItem.getData());
                            }
                        });
                TokenTmClient.getService(StoreService.class)
                        .getStore(did, "testType")
                        .compose(XXF.bindToErrorNotice())
                        .subscribe(new Consumer<List<StoreItem<String>>>() {
                            @Override
                            public void accept(List<StoreItem<String>> storeItems) throws Exception {
                                ToastUtils.showToast("备份获取2:" + storeItems.size());
                            }
                        });
            }
        });
        binding.btIdentityAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentUtils.launchUserCertActivity(
                        getActivity()
                        , new UserCertByIDCardParams.Builder(did)
                                .setUserName("小炫风")
                                .setUserIDCard("511324198901090148")
                                .build());
            }
        });
        binding.btCertCompany.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                ComponentUtils.launchCompanyCertActivity(getActivity(),
                        new CompanyCertParams.Builder(did, "北京百度科技有限公司").build());
            }
        });
        //开启认证详情
        binding.btCertificationDetails.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View v) {
                CertificationDetailsActivity.launch(v.getContext());
            }
        });

        binding.btLoginRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoSp.getInstance().logout();
                LoginOrRegisterActivity.launch(getActivity());
                finish();
            }
        });

        binding.btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoSp.getInstance().logout();
                LoginOrRegisterActivity.launch(getActivity());
                finish();
            }
        });
    }
}
