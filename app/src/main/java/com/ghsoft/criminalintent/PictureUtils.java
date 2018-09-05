package com.ghsoft.criminalintent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PictureUtils {
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
}
