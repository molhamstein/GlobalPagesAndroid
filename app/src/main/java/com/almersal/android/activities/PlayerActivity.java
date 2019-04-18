package com.almersal.android.activities;

/**
 * Created by Adhamkh on 2019-04-18.
 */


import android.media.MediaPlayer;
import android.net.Uri;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.almersal.android.R;
import com.almersal.android.views.FullScreenVideoView;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends BaseActivity {

    public static String PlayerActivity_URL_TAG = "PlayerActivity_URL_TAG";

    private static final String SELECTED_ITEM_POSITION = "ItemPosition";

    public static int mPosition = 0;

    @BindView(R.id.video_view)
    public FullScreenVideoView videoview;

    @BindView(R.id.progress)
    public ProgressBar progress;

    String url;

    @Override
    public void onBaseCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.player_layout);
        ButterKnife.bind(this);
        videoInit();

    }

    MediaPlayer.OnErrorListener listener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(PlayerActivity.this, R.string.cantPlayThisVideo, Toast.LENGTH_LONG).show();
            return true;
        }
    };

    void videoInit() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) videoview.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoview.setLayoutParams(params);
        url = getIntent().getStringExtra(PlayerActivity_URL_TAG);

        videoview.setMediaController(new MediaController(this));
//        url="rtsp://172.16.202.5:1935/vod/sample.mp4";
        videoview.setVideoURI(Uri.parse(url));
        videoview.setOnErrorListener(listener);
//        videoview.setOnSeekChangeListener(new OnSeekChangeListener() {
//            @Override
//            public void onSeekChange(int msec) {
//                mPosition = msec;
//            }
//        });

        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progress.setVisibility(View.GONE);
                if (mPosition > 0) {
                    videoview.seekTo(mPosition);
                } else {
                    videoview.seekTo(1);
                }
                videoview.start();
            }
        });

        videoview.start();

    }

//    @Override
//    protected void onSaveInstanceState(final Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (outState != null)
//            outState.putInt(SELECTED_ITEM_POSITION, mPosition);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState != null)
//            mPosition = savedInstanceState.getInt(SELECTED_ITEM_POSITION);
//    }

    @Override
    protected void onPause() {
        int pos = videoview.getCurrentPosition();
        int duration = videoview.getDuration();
        if (duration > 0)
            mPosition = pos;
        super.onPause();
        try {
            videoview.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
//            videoview.seekTo(mPosition);
            videoview.resume();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
