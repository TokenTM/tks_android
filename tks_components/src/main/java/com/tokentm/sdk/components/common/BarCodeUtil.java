package com.tokentm.sdk.components.common;

import android.graphics.Bitmap;

import com.tokentm.sdk.components.common.barcode.BitMatrix;
import com.tokentm.sdk.components.common.barcode.CodeWriter;

public class BarCodeUtil {

    /**
     * 根据字符串生成条形码图片并显示在界面上，第二个参数为图片的大小
     * @param contents
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    public static Bitmap createBarcode(String contents, int desiredWidth, int desiredHeight) {
        CodeWriter codeWriter = new CodeWriter();
        boolean[] encode = codeWriter.encode(contents);
        BitMatrix bitMatrix = renderResult(encode, desiredWidth, desiredHeight);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                //0xff000000 黑   0xFFFFFFFF 白
                pixels[offset + x] = bitMatrix.get(x, y) ? 0xff000000 : 0x00FFFFFF;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * @return a byte array of horizontal pixels (0 = white, 1 = black)
     */
    private static BitMatrix renderResult(boolean[] code, int width, int height) {
        int inputWidth = code.length;
        // Add quiet zone on both sides.
        int outputWidth = Math.max(width, inputWidth);
        int outputHeight = Math.max(1, height);

        int multiple = outputWidth / inputWidth;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
        for (int inputX = 0, outputX = 0; inputX < inputWidth; inputX++, outputX += multiple) {
            if (code[inputX]) {
                output.setRegion(outputX, 0, multiple, outputHeight);
            }
        }
        return output;
    }
}
