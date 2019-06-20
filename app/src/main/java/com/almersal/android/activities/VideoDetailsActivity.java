package com.almersal.android.activities;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.almersal.android.R;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


public class VideoDetailsActivity extends BaseActivity implements IVLCVout.Callback {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static final String VideoDetailsActivity_URL_TAG = "VideoDetailsActivity_URL_TAG";

    @BindView(R.id.surface)
    public SurfaceView mSurface;

    @BindView(R.id.vlc_container)
    public LinearLayout vlcContainer;

    @BindView(R.id.vlc_overlay)
    public FrameLayout vlcOverlay;

    @BindView(R.id.vlc_button_play_pause)
    public ImageView vlcButtonPlayPause;

    @BindView(R.id.fullscreen_button)
    public ImageView vlcbuttonFullScreen;


    @BindView(R.id.channelTitle)
    public TextView channelTitle;

    @BindView(R.id.vlc_seekbar)
    public SeekBar vlcSeekbar;


    public static int position;

    private String mFilePath;

    private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);

    private Handler handlerOverlay;

    private Runnable runnableOverlay;

    private Handler handlerSeekbar;

    private Runnable runnableSeekbar;

    private SurfaceHolder holder;

    private LibVLC libvlc;

    private MediaPlayer mMediaPlayer = null;

    private int mVideoWidth;

    private int mVideoHeight;

    int index;

    private OrientationEventListener orientationEventListener;


    @Override
    public void onBaseCreate(Bundle savedInstanceState) {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_details_layout);
        ButterKnife.bind(this);
        holder = mSurface.getHolder();

        orientationEventListener =
                new OrientationEventListener(this) {
                    @Override
                    public void onOrientationChanged(int orientation) {
                        int epsilon = 10;
                        int leftLandscape = 90;
                        int rightLandscape = 270;
                        if (epsilonCheck(orientation, leftLandscape, epsilon) ||
                                epsilonCheck(orientation, rightLandscape, epsilon)) {
                            VideoDetailsActivity.this.setRequestedOrientation(
                                    ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        }
                    }

                    private boolean epsilonCheck(int a, int b, int epsilon) {
                        return a > b - epsilon && a < b + epsilon;
//                        return Math.abs(b - a) < epsilon && a < b + epsilon;
                    }
                };
        orientationEventListener.enable();

        vlcSeekbar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        vlcSeekbar.getThumb().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
    }

    @Optional
    @OnClick(R.id.fullscreen_button)
    public void onFullScreenButtonClick(View view) {
        toggleFullscreen();
    }

    private void enterFullScreen() {
        vlcContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        toggleFullscreen();
        setSize(mVideoWidth, mVideoHeight);
    }

    private void setupControls() {
        vlcButtonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    vlcButtonPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play_over_video));
                } else {
                    mMediaPlayer.play();
                    vlcButtonPlayPause.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause_over_video));
                }
            }
        });


        // SEEKBAR
        handlerSeekbar = new Handler();
        runnableSeekbar = new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {


                    vlcSeekbar.setProgress((int) (mMediaPlayer.getPosition() * 100));

                }
                handlerSeekbar.postDelayed(runnableSeekbar, 1000);
            }
        };

        runnableSeekbar.run();
        vlcSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.v("NEW POS", "pos is : " + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        // OVERLAY


        // OVERLAY
        handlerOverlay = new Handler();
        runnableOverlay = new Runnable() {
            @Override
            public void run() {
                vlcOverlay.setVisibility(View.GONE);
                // toggleFullscreen(true);
            }
        };
        final long timeToDisappear = 3000;
        handlerOverlay.postDelayed(runnableOverlay, timeToDisappear);
        vlcContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vlcOverlay.setVisibility(View.VISIBLE);

                handlerOverlay.removeCallbacks(runnableOverlay);
                handlerOverlay.postDelayed(runnableOverlay, timeToDisappear);
            }
        });
    }

    private void toggleFullscreen() {
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            vlcbuttonFullScreen.setImageResource(R.drawable.ic_fullscreen_black_24dp);
//            vlcContainer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            vlcbuttonFullScreen.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
        }

    }


    private void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        // get screen size
        int w = getWindow().getDecorView().getWidth();
        int h = getWindow().getDecorView().getHeight();

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;


        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        if (holder != null)
            holder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        ViewGroup.LayoutParams lp = mSurface.getLayoutParams();
        lp.width = w;
        lp.height = h;
        mSurface.setLayoutParams(lp);
        mSurface.invalidate();
    }

    public void playMovie() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            return;
//        vlcContainer.setVisibility(View.VISIBLE);
        holder = mSurface.getHolder();
//        holder.addCallback((SurfaceHolder.Callback) ChannelDetailsActivity.this);
//        ChannelEntity entity = channelList.get(position);
//        mFilePath = entity.getDecryptedUrl();
//        mFilePath = "http://almersal.co/videos/d846bfb1-6ebc-42c8-a658-d3169e652f26.mp4";
//        mFilePath = "http://almersal.co/videos/e0d4abb1-83d3-4129-a940-c10bce8293a5.mp4";
        mFilePath = getIntent().getStringExtra(VideoDetailsActivity_URL_TAG);
        if (!mFilePath.startsWith("http://"))
            mFilePath = "http://" + mFilePath;
//        mFilePath = "rtsp://172.16.202.5:1935/vod/test3g640h264.mp4";
//        channelTitle.setText(entity.getName());
        createPlayer(mFilePath);
//        playChannel(mFilePath);
    }

    private void createPlayer(String media) {
        releasePlayer();
        setupControls();
        try {
            boolean islandscape1 = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            if (islandscape1) {
//                vlcContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT));

                ArrayList<String> options = new ArrayList<String>();
                //options.add("--subsdec-encoding <encoding>");
                options.add("--aout=opensles");
                options.add("--audio-time-stretch"); // time stretching
                options.add("-vvv"); // verbosity
                libvlc = new LibVLC(this, options);
                holder.setKeepScreenOn(true);

                // Creating media player
                mMediaPlayer = new MediaPlayer(libvlc);
                mMediaPlayer.setEventListener(mPlayerListener);

                // Seting up video output
                final IVLCVout vout = mMediaPlayer.getVLCVout();
                vout.setVideoView(mSurface);
                //vout.setSubtitlesView(mSurfaceSubtitles);
                vout.addCallback(this);
                vout.attachViews();

                Media m = new Media(libvlc, Uri.parse(media));
                mMediaPlayer.setMedia(m);
                mMediaPlayer.play();
//                mtezarAds.setVisibility(View.GONE);
//                vlcbuttonFullScreen.setVisibility(View.GONE);
            } else {
                ArrayList<String> options = new ArrayList<String>();
                //options.add("--subsdec-encoding <encoding>");
                options.add("--aout=opensles");
                options.add("--audio-time-stretch"); // time stretching
                options.add("-vvv"); // verbosity
                libvlc = new LibVLC(this, options);
                holder.setKeepScreenOn(true);

                // Creating media player
                mMediaPlayer = new MediaPlayer(libvlc);
                mMediaPlayer.setEventListener(mPlayerListener);

                // Seting up video output
                final IVLCVout vout = mMediaPlayer.getVLCVout();
                vout.setVideoView(mSurface);
                vout.addCallback(this);
                vout.attachViews();

                Media m = new Media(libvlc, Uri.parse(media));
                mMediaPlayer.setMedia(m);
                mMediaPlayer.play();

            }

        } catch (Exception e) {
            Toast.makeText(this, "الرجاء تحديث التطبيق من صفحة الرئيسية", Toast.LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        if (libvlc == null)
            return;
        mMediaPlayer.stop();
        final IVLCVout vout = mMediaPlayer.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        holder = null;
        libvlc.release();
        libvlc = null;

        mVideoWidth = 0;
        mVideoHeight = 0;

    }

    private static class MyPlayerListener implements MediaPlayer.EventListener {
        private WeakReference<VideoDetailsActivity> mOwner;

        public MyPlayerListener(VideoDetailsActivity owner) {
            mOwner = new WeakReference<VideoDetailsActivity>(owner);
        }

        @Override
        public void onEvent(MediaPlayer.Event event) {
            VideoDetailsActivity player = mOwner.get();

            switch (event.type) {
                case MediaPlayer.Event.EndReached:
//                    Log.d(TAG, "MediaPlayerEndReached");
                    player.releasePlayer();
                    break;
                case MediaPlayer.Event.Playing:
                case MediaPlayer.Event.Paused:
                case MediaPlayer.Event.Stopped:
                default:
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            try {
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } catch (Exception ex) {
            }
            vlcbuttonFullScreen.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
            setSize(mVideoWidth, mVideoHeight);
        } else {
            try {
                this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } catch (Exception ex) {
            }
            vlcbuttonFullScreen.setImageResource(R.drawable.ic_fullscreen_black_24dp);
            setSize(mVideoWidth, mVideoHeight);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer = null;
        playMovie();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
        releasePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    /**
     * Registering callbacks
     */

    @Override
    public void onNewLayout(IVLCVout vout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
        if (width * height == 0)
            return;

        mVideoWidth = width;
        mVideoHeight = height;
        setSize(mVideoWidth, mVideoHeight);
    }

    @Override
    public void onSurfacesCreated(IVLCVout vout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vout) {

    }

    @Override
    public void onHardwareAccelerationError(IVLCVout vlcVout) {
        this.releasePlayer();
        Toast.makeText(this, "Error with hardware acceleration", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        releasePlayer();
        super.onBackPressed();

    }

}
