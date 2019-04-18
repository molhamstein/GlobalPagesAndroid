package com.almersal.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.almersal.android.listeners.OnSeekChangeListener;

/**
 * Created by Adhamkh on 2019-04-18.
 */

public class FullScreenVideoView extends VideoView {

    private OnSeekChangeListener onSeekChangeListener;

    public OnSeekChangeListener getOnSeekChangeListener() {
        return onSeekChangeListener;
    }

    public void setOnSeekChangeListener(OnSeekChangeListener onSeekChangeListener) {
        this.onSeekChangeListener = onSeekChangeListener;
    }

    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    public void setFixedVideoSize(int width, int height) {
        getHolder().setFixedSize(width, height);
    }

    @Override
    public void seekTo(int msec) {
        super.seekTo(msec);
        if (onSeekChangeListener != null)
            onSeekChangeListener.onSeekChange(msec);
    }




}
