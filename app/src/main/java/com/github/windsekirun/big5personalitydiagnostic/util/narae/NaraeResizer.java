package com.github.windsekirun.big5personalitydiagnostic.util.narae;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.github.windsekirun.big5personalitydiagnostic.util.narae.util.ImageDecoder;

/**
 * NaraeResizer
 * Class: NaraeResizer
 * Created by WindSekirun on 2015. 7. 1..
 * <p/>
 * Url: http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
 */
@SuppressWarnings("ALL")
public class NaraeResizer {

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap drawableToBitmap5(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static int calcuateResizeWidth(Bitmap bitmap, int per) {
        int sourceWidth = bitmap.getWidth();
        if (per == 50) return (int) (sourceWidth  * 0.5f);
        else if (per == 75) return (int) (sourceWidth * 0.75f);
        else return (int) (sourceWidth * 0.3f);
    }

    public static int calcuateResizeHeight(Bitmap bitmap, int per) {
        int sourceHeight = bitmap.getHeight();
        if (per == 50) return (int) (sourceHeight  * 0.5f);
        else if (per == 75) return (int) (sourceHeight * 0.75f);
        else return (int) (sourceHeight * 0.3f);
    }

    public static Drawable bitmapToDrawable(Resources resources, Bitmap bitmap) {
        return new BitmapDrawable(resources, bitmap);
    }

    public static Bitmap resize(Bitmap bitmap, int width, int height) {
        return resize(bitmap, width, height, ResizeMode.AUTOMATIC);
    }

    public static Bitmap resize(Bitmap sampledSrcBitmap, int width, int height, ResizeMode mode) {
        int sourceWidth = sampledSrcBitmap.getWidth();
        int sourceHeight = sampledSrcBitmap.getHeight();

        if (mode == null || mode == ResizeMode.AUTOMATIC) {
            mode = calculateResizeMode(sourceWidth, sourceHeight);
        }

        if (mode == ResizeMode.FIT_TO_WIDTH) {
            height = calculateHeight(sourceWidth, sourceHeight, width);
        } else if (mode == ResizeMode.FIT_TO_HEIGHT) {
            width = calculateWidth(sourceWidth, sourceHeight, height);
        }
        return Bitmap.createScaledBitmap(sampledSrcBitmap, width, height, true).copy(Bitmap.Config.RGB_565, true);

    }

    public static Bitmap resize(Bitmap sampledSrcBitmap, int width, int height, ResizeMode mode, boolean is565) {
        int sourceWidth = sampledSrcBitmap.getWidth();
        int sourceHeight = sampledSrcBitmap.getHeight();

        if (mode == null || mode == ResizeMode.AUTOMATIC) {
            mode = calculateResizeMode(sourceWidth, sourceHeight);
        }

        if (mode == ResizeMode.FIT_TO_WIDTH) {
            height = calculateHeight(sourceWidth, sourceHeight, width);
        } else if (mode == ResizeMode.FIT_TO_HEIGHT) {
            width = calculateWidth(sourceWidth, sourceHeight, height);
        }
        if (is565) return Bitmap.createScaledBitmap(sampledSrcBitmap, width, height, true).copy(Bitmap.Config.RGB_565, true);
        else return Bitmap.createScaledBitmap(sampledSrcBitmap, width, height, true).copy(Bitmap.Config.ARGB_8888, true);
    }

    private static ResizeMode calculateResizeMode(int width, int height) {
        if (ImageDecoder.ImageOrientation.getOrientation(width, height) == ImageDecoder.ImageOrientation.LANDSCAPE) {
            return ResizeMode.FIT_TO_WIDTH;
        } else {
            return ResizeMode.FIT_TO_HEIGHT;
        }
    }

    private static int calculateWidth(int originalWidth, int originalHeight, int height) {
        return (int) Math.ceil(originalWidth / ((double) originalHeight / height));
    }

    private static int calculateHeight(int originalWidth, int originalHeight, int width) {
        return (int) Math.ceil(originalHeight / ((double) originalWidth / width));
    }

    public enum ResizeMode {
        AUTOMATIC,
        FIT_TO_WIDTH,
        FIT_TO_HEIGHT,
        FIT_EXACT
    }
}
