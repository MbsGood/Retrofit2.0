package com.yunnex.merge.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.yunnex.merge.utils.ImageLoader;

public class GJImageView extends ImageView {
    private Context mContext;
    private boolean forceAutoLoadImage = false;
    private float ratio = -1;
    private int imageRes = -1;

    public GJImageView(Context context) {
        this(context, null, 0);
    }

    public GJImageView(Context context, int resDefault) {
        this(context, null, 0);
    }

    public GJImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            mContext = context;
        }
    }

    public GJImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public boolean isForceAutoLoadImage() {
        return forceAutoLoadImage;
    }

    public void setForceAutoLoadImage(boolean forceAutoLoadImage) {
        this.forceAutoLoadImage = forceAutoLoadImage;
    }

    public void setStubImage(int imageRes) {
        this.imageRes = imageRes;
    }

    public void loadImagePath(String imagePath) {
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        ImageLoader.displayWithDefaultImage(mContext, imagePath, this, imageRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (ratio <= 0)
            return;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || heightSpecMode != MeasureSpec.EXACTLY) {
            heightSpecSize = (int) (widthSpecSize / ratio);
        } else if (heightSpecMode == MeasureSpec.EXACTLY || widthSpecMode != MeasureSpec.EXACTLY) {
            widthSpecSize = (int) (heightSpecSize * ratio);
        }
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

}
