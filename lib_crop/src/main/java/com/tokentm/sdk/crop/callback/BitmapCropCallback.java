package com.tokentm.sdk.crop.callback;

import android.support.annotation.NonNull;

/**
 * @author lqx
 */
public interface BitmapCropCallback {

    void onBitmapCropped();

    void onCropFailure(@NonNull Exception bitmapCropException);

}