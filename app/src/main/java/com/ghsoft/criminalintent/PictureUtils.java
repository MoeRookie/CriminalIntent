package com.ghsoft.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    /**
     * 获取缩放后的位图对象
     * @param path 原图片文件
     * @param destWidth 目标宽度
     * @param destHeight 目标高度
     * @return 缩放后的位图对象
     */
    public static Bitmap getScaledBitmap(String path,int destWidth,int destHeight){
        // 读取外存中图片的尺寸
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        // 计算缩放比例
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            }else{
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // 读取并创建最终的bitmap
        return BitmapFactory.decodeFile(path,options);
    }
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager()
                .getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }
}
