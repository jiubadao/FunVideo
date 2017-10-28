package com.zw.funvideo.videoplayer;

import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.zw.funvideo.R;
import com.zw.funvideo.app.App;
import com.zw.funvideo.constant.Constant;
import com.zw.funvideo.model.ItemList;
import com.zw.funvideo.utils.NetUtils;

import java.io.File;
import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author zsj
 */

public class PlayActivity extends AppCompatActivity
        implements SurfaceHolder.Callback, IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, CacheListener {

    private static final int FLAG_HIDE_SYSTEM_UI = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    private SurfaceHolder surfaceHolder;
    private VideoController videoController;

    private IjkMediaPlayer mediaPlayer;

    private ItemList item;

    public Toolbar toolbar;
    public ActionBar ab;




    private HttpProxyCacheServer cacheServer() {
        return App.getProxy(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(FLAG_HIDE_SYSTEM_UI);

        setContentView(R.layout.play_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        videoController = (VideoController) findViewById(R.id.video_controller);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);


        item = getIntent().getParcelableExtra("item");
        String playUrl = item.data.playUrl;

        ab = getSupportActionBar();
        if (ab != null){
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(item.data.title);
        }

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);

        playVideo(playUrl);
    }

    private void playVideo(String path) {
        try {
            mediaPlayer = new IjkMediaPlayer();
            videoController.attachPlayer(mediaPlayer, item);
            boolean cacheWithWifi = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constant.CACHE_KEY,true);

            if (cacheWithWifi && NetUtils.isWifiConnected(this)) {
                String proxyPath = cacheServer().getProxyUrl(path);
                cacheServer().registerCacheListener(this, path);
                mediaPlayer.setDataSource(proxyPath);
            } else {
                mediaPlayer.setDataSource(path);
            }
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer.setScreenOnWhilePlaying(true);
            videoController.startVideoPlayback(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        percentsAvailable = percentsAvailable * (int) item.data.duration / 100;
        videoController.onBufferUpdate(percentsAvailable);
    }

    private boolean surfaceCreated;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!surfaceCreated) {
            videoController.surfaceCreated();
            surfaceCreated = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        videoController.calculateVideoWidthAndHeight();
        videoController.startVideoPlayback(surfaceHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        videoController.surfaceDestory();
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer imp, int percent) {
        if (cacheServer().isCached(item.data.playUrl)) {
            videoController.onBufferUpdate((int) item.data.duration);
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        videoController.onInfo(what);
        return true;
    }

    @Override
    public void onCompletion(IMediaPlayer imp) {
        videoController.onCompleted();
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        videoController.onPrepared(surfaceHolder);
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer,
                                   int width, int height, int sar_num, int sar_den) {
        videoController.onVideoChanged(surfaceHolder, width, height);
    }

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mediaPlayer.isPlaying()) {
            videoController.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            videoController.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoController.surfaceDestory();
        cacheServer().unregisterCacheListener(this);
        release();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
