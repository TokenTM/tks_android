package com.tokentm.sdk.components.cert;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokentm.sdk.TokenTmClient;
import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseTitleBarActivity;
import com.tokentm.sdk.components.databinding.CompanyActivityCompanySubmitFileBinding;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.source.BasicService;
import com.tokentm.sdk.source.CertService;
import com.xxf.arch.XXF;

import java.io.InputStream;

import io.reactivex.ObservableSource;
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
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    public static void launch(Context context, CompanyCertParams companyCertParams) {
        context.startActivity(getLauncher(context, companyCertParams));
    }

    public static Intent getLauncher(Context context, CompanyCertParams companyCertParams) {
        return new Intent(context, CompanyCertSubmitFileActivity.class)
                .putExtra(KEY_CERT_PARAMS, companyCertParams);
    }

    CompanyActivityCompanySubmitFileBinding binding;
    CompanyCertParams companyCertParams;

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
        companyCertParams = (CompanyCertParams) getIntent().getSerializableExtra(KEY_CERT_PARAMS);

        binding.companyNameTv.setText(companyCertParams.getCompanyName());
        binding.companyCreditCodeTv.setText(companyCertParams.getCompanyCreditCode());
        binding.legalPersonNameTv.setText(companyCertParams.getLegalPersonName());
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        TokenTmClient.getService(CertService.class)
                .getUserCertByIDCardInfo(companyCertParams.getuDid())
                .flatMap(new Function<CertUserInfoStoreItem, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(CertUserInfoStoreItem certUserInfoStoreItem) throws Exception {
                        return TokenTmClient.getService(BasicService.class)
                                .getOrgLetterImage(
                                        companyCertParams.getuDid(),
                                        companyCertParams.getCompanyName(),
                                        certUserInfoStoreItem.getIdentityCode(),
                                        companyCertParams.getLegalPersonName())
                                .map(new Function<InputStream, Bitmap>() {
                                    @Override
                                    public Bitmap apply(InputStream inputStream) throws Exception {
                                        return BitmapFactory.decodeStream(inputStream);
                                    }
                                });
                    }
                })
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToErrorNotice())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        //高度填满
                        float height = binding.pdfView.getMeasuredWidth() * (bitmap.getHeight() * 1.0f / bitmap.getWidth());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
                        binding.pdfView.setLayoutParams(layoutParams);

                        binding.pdfView.setImageBitmap(bitmap);
                    }
                });
    }
}
