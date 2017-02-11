package com.wulei.funnypicture.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by wulei on 2017/2/10.
 */

public class ImageLoaderUtils {
    /**
     * 封装图片加载库，glide
     */
    public static void display(Context context, ImageView imageView, String url
            , int errorImage, int placeHolder) {
        Glide.with(context)
                .load(url)
                .placeholder(placeHolder)
                .error(errorImage)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void display(Context context, ImageView imageView, String url,
                               Drawable errordrawable, Drawable placeHolder) {
        Glide.with(context)
                .load(url)
                .placeholder(placeHolder)
                .error(errordrawable)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }
}
