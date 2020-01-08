package com.tokentm.sdk.components.identitypwd.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokentm.sdk.components.common.BaseActivity;
import com.tokentm.sdk.components.databinding.TksComponentsActivityContactCustomerServiceBinding;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.actiondialog.SystemUtils;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 联系客服
 */
public class ContactCustomerServiceActivity extends BaseActivity {

    private static final String DID = "did";
    private String weChatId = "Blockchain_Union001";
    private String email = "mingxu@staff.token.tm";

    public static void launch(@NonNull Context context, @Nullable String did) {
        context.startActivity(getLauncher(context, did));
    }

    public static Intent getLauncher(Context context, String did) {
        return new Intent(context, ContactCustomerServiceActivity.class)
                .putExtra(DID, did);
    }

    TksComponentsActivityContactCustomerServiceBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsActivityContactCustomerServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }


    protected void initView() {
        binding.tvShowWeChatId.setText(String.format("微信ID:%s", weChatId));
        binding.tvShowEmail.setText(String.format("运营联系邮箱:%s", email));
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvCopyWeChatId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SystemUtils.copyTextToClipboard(weChatId);
                ToastUtils.showToast("已经复制到剪切板了!");
            }
        });
        binding.tvSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发邮件
                Intent data = new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse(String.format("mailto:%s", email)));
                data.putExtra(Intent.EXTRA_SUBJECT, "SDK客服");
                startActivity(data);
            }
        });
    }

}
