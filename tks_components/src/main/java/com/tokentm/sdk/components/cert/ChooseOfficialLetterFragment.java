package com.tokentm.sdk.components.cert;

import android.annotation.SuppressLint;
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
import com.tokentm.sdk.components.databinding.TksComponentsFragmentUploadOfficeLetterBinding;
import com.tokentm.sdk.crop.Crop;
import com.tokentm.sdk.crop.util.CropUtils;
import com.tokentm.sdk.model.CertUserInfoStoreItem;
import com.tokentm.sdk.source.BasicService;
import com.tokentm.sdk.source.CertService;
import com.tokentm.sdk.source.TokenTmClient;
import com.xxf.arch.XXF;
import com.xxf.arch.core.activityresult.ActivityResult;
import com.xxf.arch.rxjava.transformer.ProgressHUDTransformerImpl;
import com.xxf.arch.utils.ToastUtils;
import com.xxf.view.actiondialog.SystemUtils;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author youxuan  E-mail:xuanyouwu@163.com
 * @Description 上传公函
 */
public class ChooseOfficialLetterFragment extends BaseFragment implements PicSelectPresenter {
    /**
     * 认证参数
     */
    private static final String KEY_CERT_PARAMS = "companyCertParams";

    public static ChooseOfficialLetterFragment newInstance(CompanyCertParams companyCertParams) {
        ChooseOfficialLetterFragment fragment = new ChooseOfficialLetterFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_CERT_PARAMS, companyCertParams);
        fragment.setArguments(args);
        return fragment;
    }

    TksComponentsFragmentUploadOfficeLetterBinding binding;
    CompanyCertParams companyCertParams;
    String localFilePath;

    /**
     * 获取本地选择的图片路径
     *
     * @return
     */
    public String getLocalFilePath() {
        return localFilePath;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TksComponentsFragmentUploadOfficeLetterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        loadData();
    }

    private void initView() {
        companyCertParams = (CompanyCertParams) getArguments().getSerializable(KEY_CERT_PARAMS);
        binding.downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPic(binding.pdfView);
            }
        });
        binding.uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPicSelectDialog();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void downloadPic(View bitmapView) {
        Observable
                .fromCallable(new Callable<Bitmap>() {
                    @Override
                    public Bitmap call() throws Exception {
                        return getViewBitmap(bitmapView);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Bitmap, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(Bitmap bitmap) throws Exception {
                        return SystemUtils.saveImageToAlbum(
                                getActivity(),
                                String.format("%s.png", companyCertParams.getCompanyName()), getViewBitmap(bitmapView)
                        );
                    }
                })
                .subscribeOn(Schedulers.io())
                .compose(XXF.bindToLifecycle(this))
                .compose(XXF.bindToProgressHud(new ProgressHUDTransformerImpl.Builder(this)))
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        ToastUtils.showToast("已经保存到相册:" + file.getAbsolutePath());
                    }
                });
    }

    private static Bitmap getViewBitmap(View view) {
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            view.destroyDrawingCache();
        }

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        } else {
            Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
            view.destroyDrawingCache();
            view.setWillNotCacheDrawing(willNotCache);
            view.setDrawingCacheBackgroundColor(color);
            return bitmap;
        }
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
                                        certUserInfoStoreItem.getName())
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

    private void showPicSelectDialog() {
        //直接进行拍照
        SystemUtils.doTakePhoto(getActivity(), new Consumer<String>() {
            @Override
            public void accept(String imgPath) throws Exception {
                XXF.startActivityForResult(getActivity(), CropUtils.getUCropLauncher(getActivity(), imgPath), Crop.REQUEST_CROP)
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
