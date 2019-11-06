package com.tokentm.sdk.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tokentm.sdk.components.cert.PropertyRightsTransferRecordsActivity;
import com.tokentm.sdk.demo.databinding.WineToTreasureBinding;

/**
 * @author lqx  E-mail:herolqx@126.com
 * @Description 酒来宝测试页面
 */
public class WineToTreasureActivity extends FragmentActivity {


    WineToTreasureBinding binding;

    public static Intent getLauncher(Context context) {
        return new Intent(context, WineToTreasureActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WineToTreasureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        //开启物权转移记录
        binding.btCreatePropertyRightsTransferRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(PropertyRightsTransferRecordsActivity.getLauncher(v.getContext()));
            }
        });
        //开启企业认证弹窗
        binding.btCreateEnterpriseCertificationDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                v.getContext().startActivity(new Intent(v.getContext(), PropertyRightsTransferRecordsActivity.class));
            }
        });
    }
}
