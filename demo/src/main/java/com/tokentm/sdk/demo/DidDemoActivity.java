package com.tokentm.sdk.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdInputAlertDialog;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdReSetActivity;
import com.tokentm.sdk.components.identitypwd.UserIdentityPwdSetActivity;
import com.tokentm.sdk.demo.databinding.DidActivityBinding;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.utils.ToastUtils;

import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class DidDemoActivity extends FragmentActivity {
    DidActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DidActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    String did;

    @Override
    protected void onResume() {
        super.onResume();
        did = DemoSp.getInstance().getString("did");
        binding.didText.setText(TextUtils.isEmpty(did) ? "" : "已创建did:\n" + did);
    }

    private void initView() {

        binding.didBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XXF.startActivityForResult(DidDemoActivity.this, UserIdentityPwdSetActivity.getLauncher(v.getContext(), "17611639080"), 101)
                        .filter(new Predicate<ActivityResult>() {
                            @Override
                            public boolean test(ActivityResult activityResult) throws Exception {
                                return activityResult.isOk();
                            }
                        })
                        .take(1)
                        .map(new Function<ActivityResult, String>() {
                            @Override
                            public String apply(ActivityResult activityResult) throws Exception {
                                return activityResult.getData().getStringExtra(BaseTitleBarActivity.KEY_ACTIVITY_RESULT);
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String did) throws Exception {
                                DemoSp.getInstance().putString("did", did);
                                binding.didText.setText("did:" + did);
                            }
                        });
            }
        });

        binding.updatePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("先创建did");
                    return;
                }
                UserIdentityPwdReSetActivity.launch(v.getContext(), did, "17611639080");
            }
        });

        binding.checkPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(did)) {
                    ToastUtils.showToast("先创建did");
                    return;
                }
                new UserIdentityPwdInputAlertDialog(v.getContext(), did, new BiConsumer<DialogInterface, String>() {
                    @Override
                    public void accept(DialogInterface dialogInterface, String s) throws Exception {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
    }
}
