package com.tokentm.sdk.crop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.tokentm.sdk.crop.R;
import com.tokentm.sdk.crop.callback.CropBoundsChangeListener;
import com.tokentm.sdk.crop.callback.OverlayViewChangeListener;


public class CropView extends FrameLayout {

    private final GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;

    public CropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.crop_view, this, true);
        mGestureCropImageView = (GestureCropImageView) findViewById(R.id.image_view_crop);
        mViewOverlay = (OverlayView) findViewById(R.id.view_overlay);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.crop_CropView);
        mViewOverlay.processStyledAttributes(a);
        mGestureCropImageView.processStyledAttributes(a);
        a.recycle();


        mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
            @Override
            public void onCropAspectRatioChanged(float cropRatio) {
                mViewOverlay.setTargetAspectRatio(cropRatio);
            }
        });
        mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
            @Override
            public void onCropRectUpdated(RectF cropRect) {
                mGestureCropImageView.setCropRect(cropRect);
            }
        });
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return mViewOverlay;
    }

}