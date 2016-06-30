package com.yunnex.merge.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by leo
 * on 2016/5/20.
 */
public class ImageLoader {

    public static void displayImage(Context context, String uri, ImageView imageView) {
        displayWithDefaultImage(context, uri, imageView, -1);
    }

    public static void displayWithDefaultImage(Context context, String uri, ImageView imageView, int imageRes) {
        Glide.with(context)
                .load(uri)
                .placeholder(imageRes)
                .dontAnimate()
                .into(imageView);
    }
}
