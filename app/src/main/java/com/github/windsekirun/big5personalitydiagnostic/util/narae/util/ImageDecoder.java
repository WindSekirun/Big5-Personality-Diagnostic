package com.github.windsekirun.big5personalitydiagnostic.util.narae.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * NaraeResizer
 * Class: ImageDecoder
 * Created by WindSekirun on 2015. 7. 1..
 * <p/>
 * Url: http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 */
@SuppressWarnings("ALL")
public class ImageDecoder {
    public static Bitmap decodeFile(File bitmapFile) {
        return decodeFile(bitmapFile, -1, -1);
    }

    public static Bitmap decodeFile(File bitmapFile, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
    }

    public static Bitmap decodeResource(Resources res, int resId) {
        return decodeResource(res, resId, -1, -1);
    }

    public static Bitmap decodeResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    public static Bitmap decodeByteArray(byte[] byteArray) {
        return decodeByteArray(byteArray, -1, -1);
    }

    public static Bitmap decodeByteArray(byte[] byteArray, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (reqWidth == -1) {
            reqWidth = options.outWidth;
        }

        if (reqHeight == -1) {
            reqHeight = options.outHeight;
        }

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

    public enum ImageOrientation {

        PORTRAIT,
        LANDSCAPE;

        public static ImageOrientation getOrientation(int width, int height) {
            if (width >= height) {
                return ImageOrientation.LANDSCAPE;
            } else {
                return ImageOrientation.PORTRAIT;
            }
        }

    }
}
