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
import com.tokentm.sdk.components.databinding.TksComponentsFragmentUploadBusinessLicenseBinding;
import com.tokentm.sdk.components.utils.compress_picture.CompressPictureUtil;
import com.tokentm.sdk.crop.Crop;
import com.tokentm.sdk.crop.util.CropUtils;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.view.actiondialog.SystemUtils;

import java.io.File;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

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

    TksComponentsFragmentUploadBusinessLicenseBinding binding;
    CompanyCertParams companyCertParams;
    String localFilePath;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsFragmentUploadBusinessLicenseBinding.inflate(getLayoutInflater());
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
                File compressPictureWithSize = CompressPictureUtil.getCompressPictureWithSize(imgPath, 1024 * 1000);
                XXF.startActivityForResult(getActivity(), CropUtils.getUCropLauncher(getActivity(), compressPictureWithSize.getAbsolutePath()), Crop.REQUEST_CROP)
                        .filter(new Predicate<ActivityResult>() {
                            @Override
                            public boolean test(ActivityResult activityResult) throws Exception {
                                return activityResult.isOk();
                            }
                        }).
                        take(1)
                        .subscribe(new Consumer<ActivityResult>() {
                            @Override
                            public void accept(ActivityResult activityResult) throws Exception {
                                Uri resultUri = Crop.getOutput(activityResult.getData());
                                if (resultUri != null) {
                                    localFilePath = resultUri.getPath();

                                    Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                                    //高度填满
                                    float height = binding.pdfView.getMeasuredWidth() * (bitmap.getHeight() * 1.0f / bitmap.getWidth());
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
                                    binding.pdfView.setLayoutParams(layoutParams);
                                    bitmap.recycle();

                                    binding.pdfView.setImageURI(Uri.fromFile(new File(imgPath)));
                                }
                            }
                        });

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
