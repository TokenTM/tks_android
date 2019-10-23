package com.tokentm.sdk.components.cert;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.cert.model.CertParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.CompanyActivityCompanySubmitFileBinding;
import com.tokentm.sdk.source.BasicService;
import com.xxf.arch.XXF;

import java.io.InputStream;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 公司认证 提交文件
 */
public class CompanyCertSubmitFileActivity extends BaseTitleBarActivity {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "certParams";

    public static void launch(Context context, CertParams certParams) {
        context.startActivity(getLauncher(context, certParams));
    }

    public static Intent getLauncher(Context context, CertParams certParams) {
        return new Intent(context, CompanyCertSubmitFileActivity.class)
                .putExtra(KEY_CERT_PARAMS, certParams);
    }

    CompanyActivityCompanySubmitFileBinding binding;
    CertParams certParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CompanyActivityCompanySubmitFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }

    private void initView() {
        setTitle("企业认证");
        certParams = (CertParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);

        binding.companyNameTv.setText(certParams.getCompanyName());
        binding.companyCreditCodeTv.setText(certParams.getCompanyCreditCode());
        binding.legalPersonNameTv.setText(certParams.getLegalPersonName());
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        TokenTmClient.getService(BasicService.class)
                .getOrgLetterImage(certParams.getuDid(), certParams.getCompanyName(), "511324199010090695", certParams.getLegalPersonName())
                .map(new Function<InputStream, Bitmap>() {
                    @Override
                    public Bitmap apply(InputStream inputStream) throws Exception {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 2;
                        options.inJustDecodeBounds = false;
                        return BitmapFactory.decodeStream(inputStream, new Rect(), options);
                    }
                })
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToErrorNotice())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        binding.pdfView.setImageBitmap(bitmap);
                    }
                });
    }
}
