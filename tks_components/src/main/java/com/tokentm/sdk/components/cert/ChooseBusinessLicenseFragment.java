package com.tokentm.sdk.components.cert;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokentm.sdk.components.cert.model.CompanyCertParams;
import com.tokentm.sdk.components.common.BaseFragment;
import com.tokentm.sdk.components.databinding.TksComponentsCompanyFragmentUploadBusinessLicenseBinding;
import com.xxf.view.actiondialog.SystemUtils;

import java.io.File;

import io.reactivex.functions.Consumer;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 上传营业执照
 */
public class ChooseBusinessLicenseFragment extends BaseFragment implements PicSelectPresenter {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    public static ChooseBusinessLicenseFragment newInstance(CompanyCertParams companyCertParams) {
        ChooseBusinessLicenseFragment fragment = new ChooseBusinessLicenseFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CERT_PARAMS, companyCertParams);
        fragment.setArguments(args);
        return fragment;
    }

    TksComponentsCompanyFragmentUploadBusinessLicenseBinding binding;
    CompanyCertParams companyCertParams;
    String localFilePath;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsCompanyFragmentUploadBusinessLicenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        companyCertParams = (CompanyCertParams) getArguments().getSerializable(KEY_CERT_PARAMS);
        binding.pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicSelectDialog();
            }
        });
    }

    private void showPicSelectDialog() {

        //直接进行拍照
        SystemUtils.doTakePhoto(getActivity(), new Consumer<String>() {
            @Override
            public void accept(String imgPath) throws Exception {
                localFilePath = imgPath;

                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                //高度填满
                float height = binding.pdfView.getMeasuredWidth() * (bitmap.getHeight() * 1.0f / bitmap.getWidth());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
                binding.pdfView.setLayoutParams(layoutParams);
                bitmap.recycle();

                binding.pdfView.setImageURI(Uri.fromFile(new File(imgPath)));
            }
        });
//        new BottomPicSelectDialog(getActivity(), new Consumer<String>() {
//            @Override
//            public void accept(String imgPath) throws Exception {
//                localFilePath = imgPath;
//
//                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
//                //高度填满
//                float height = binding.pdfView.getMeasuredWidth() * (bitmap.getHeight() * 1.0f / bitmap.getWidth());
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
//                binding.pdfView.setLayoutParams(layoutParams);
//                bitmap.recycle();
//
//                binding.pdfView.setImageURI(Uri.fromFile(new File(imgPath)));
//            }
//        }).show();
    }

    @Override
    public String getSelectedPic() {
        return localFilePath;
    }
}
