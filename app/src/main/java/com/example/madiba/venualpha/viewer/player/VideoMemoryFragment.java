package com.example.madiba.venualpha.viewer.player;


import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.danikula.videocache.CacheListener;
import com.danikula.videocache.HttpProxyCacheServer;
import com.example.madiba.venualpha.Actions.ActionMemoryStart;
import com.example.madiba.venualpha.Actions.ActionMemoryStop;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.comment.CommentDialog;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMediaItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.profiles.UserPageActivity;
import com.example.madiba.venualpha.ui.AspectFrameLayout;
import com.example.madiba.venualpha.util.ProxyFactory;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.io.File;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.WINDOW_SERVICE;


public class VideoMemoryFragment extends Fragment implements
        VideoView.OnTouchListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnBufferingUpdateListener,
        CacheListener {
    private MdMediaItem mediaItem;

    private TextView mCommets,mRxns,mHashtag,mName;
    private RoundCornerImageView mAvatar;

    private ProgressBar progressBar;
    private VideoView videoView;
    private AspectFrameLayout aspectFrameLayout;
    private AudioManager audioManager;
    private GestureDetectorCompat detector;
    private LinearLayout topLinearLayout,bottomLinearLayout;
    private boolean isOffline = false;
    private boolean isViewShown = false;
    private int screenY;
    private int screenX;
    private float volumeInFloat;
    private boolean isOperating;
    private boolean isScrollingX;
    private boolean isScrollingY;
    private int totalTimeMilli, maxVolume;

    private int waitingTime;

    private Boolean playing=false;

    public static final String TAG = "VIDEOFRAGMENT";

    public VideoMemoryFragment() {
        // Required empty public constructor
    }
    public static VideoMemoryFragment newInstance() {
        return new VideoMemoryFragment();
    }
    public static VideoMemoryFragment newInstance(MdMediaItem param1) {
        VideoMemoryFragment fragment = new VideoMemoryFragment();
        Bundle args = new Bundle();
        args.putParcelable("item", Parcels.wrap(param1));
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mediaItem = Parcels.unwrap(getArguments().getParcelable("item"));
        }else {
//            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_player, container, false);
        mHashtag = (TextView) view.findViewById(R.id.hashtag);
        mCommets = (TextView) view.findViewById(R.id.comment);
        mRxns = (TextView) view.findViewById(R.id.reaction);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        aspectFrameLayout = (AspectFrameLayout) view.findViewById(R.id.video_view_container);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRxns.setOnClickListener(view1 -> requestChat());

        new Handler().postDelayed(() -> {
            EventBus.getDefault().post(new ActionMemoryStart(10000));

        },500);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible())
        {
            if (!isVisibleToUser)   // If we are becoming invisible, then...
            {
                //pause or stop video
//                videoView.stopPlayback();
            }

            if (isVisibleToUser) // If we are becoming visible, then...
            {
                //play your video
//                videoView.start();
            }
        }
    }




    private void displayVideo(String url){

        videoView.setOnPreparedListener(this);
        videoView.setOnErrorListener(this);
        videoView.setOnCompletionListener(this);
//        Glide.with(this)
//                .load(url)
//                .crossFade()
//                .placeholder(R.drawable.ic_default_avatar)
//                .error(R.drawable.placeholder_error_media)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .centerCrop()
//                .fallback(R.drawable.ic_default_avatar)
//                .thumbnail(0.4f)
//                .into(photoView);
    }

    private void displayHeader(String url,String name){

        mName.setText("");

        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(mAvatar);
    }

    private void displayFooter(String tag,String comment,String rxn){
        mName.setText("");
        mCommets.setText("");
        mRxns.setText("");

    }


    private void requestChat(){
        setWaitingTime();
      CommentDialog.newInstance().show(getChildFragmentManager(), "dialog");

    }

    private void requestEvent(MdEventItem eventItem){

    }
    private void requestUser(MdUserItem event){

        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(new Intent(getActivity(), UserPageActivity.class)));
            }
        });
    }


    //    BACKGROUND OPERATIONS ///////////////////////////////////////////////////////////////////////////
//    /////////////////////////////////////////////////////////////////////////////////////////////////
//    /////////////////////////////////////////////////////////////////////////////////////////////////


    private void checkCachedState(String url) {
        HttpProxyCacheServer proxy = ProxyFactory.getProxy(getActivity().getApplicationContext());
        boolean fullyCached = proxy.isCached(url);
        if (fullyCached) {
            progressBar.setProgress(100);
        }
    }

    private void initSound(){
        audioManager = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeInFloat = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Display display = ((WindowManager)getActivity().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        screenY = display.getHeight();
        screenX = display.getWidth();
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        progressBar.setProgress(percentsAvailable);

    }


    private void initGestureListinner() {
        detector = new GestureDetectorCompat(getActivity(), new MyGestureListener());
    }

    private void backward() {
        if (videoView.canSeekBackward()) {
            videoView.pause();
            videoView.seekTo(videoView.getCurrentPosition() - 30000);
            videoView.start();
        }
    }

    private void forward() {
        if (videoView.canSeekForward()) {
            videoView.pause();
            videoView.seekTo(videoView.getCurrentPosition() + 10000);
            videoView.start();
        }
    }

    private void setProgress(float percent) {
        int position = (int)(videoView.getCurrentPosition() - totalTimeMilli * percent);
        if (position < 0) {
            position = 0;
        }else if (position > totalTimeMilli) {
            position = totalTimeMilli;
        }
        videoView.seekTo(position);
    }

    private void setVolume(float percent) {
        volumeInFloat = volumeInFloat + maxVolume * percent;
        if (volumeInFloat < 0) {
            volumeInFloat = 0;
        }else if (volumeInFloat > maxVolume) {
            volumeInFloat = maxVolume;
        }
        Log.d(TAG, "maxVolume:" + maxVolume);
        Log.d(TAG, "deltColume:" + (int)(maxVolume * percent));
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)volumeInFloat, AudioManager.FLAG_SHOW_UI);
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        // TODO: 3/15/2017 skip
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(getActivity(), mediaPlayer.toString(), Toast.LENGTH_SHORT).show();
        // TODO: 3/15/2017 skip
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        totalTimeMilli = mediaPlayer.getDuration();
        int milliseconds = videoView.getDuration();

        // TODO: 3/15/2017 parse to autoscroll here

        progressBar.setVisibility(View.GONE);
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        aspectFrameLayout.setAspectRatio((double) videoWidth / videoHeight);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        this.detector.onTouchEvent(motionEvent);
        return false;
    }



    @Override
    public void onPause() {
        if (videoView != null) {
            videoView.stopPlayback();
        }
        super.onPause();

    }

    @Override
    public void onResume() {
        if (videoView != null) {
            videoView.start();
        }

        EventBus.getDefault().post(new ActionMemoryStart(waitingTime));

        super.onResume();
    }

    @Override
    public void onStop() {
//        changeProgress(false);

        super.onStop();

    }



    @Override
    public void onDestroy() {
        try {
            videoView.stopPlayback();
            ProxyFactory.getProxy(getActivity().getApplicationContext()).unregisterCacheListener(this);
        }catch (Exception e){}

        super.onDestroy();
    }


    private void updatePosition() {
        int position = (int)( totalTimeMilli -videoView.getCurrentPosition());
        if (position < 0) {
            position = 0;
        }else if (position > totalTimeMilli) {
            position = totalTimeMilli;
        }

        // TODO: 3/15/2017 update state
        EventBus.getDefault().post(new ActionMemoryStart(position));



    }

    private void setWaitingTime(){

        waitingTime=2000;
        EventBus.getDefault().post(new ActionMemoryStop());

    }





    //    CLASS STATOC  ///////////////////////////////////////////////////////////////////////////
//    /////////////////////////////////////////////////////////////////////////////////////////////////
//    /////////////////////////////////////////////////////////////////////////////////////////////////


    private  void showUI() {
        if (!isViewShown) {
            bottomLinearLayout.setVisibility(View.VISIBLE);
            topLinearLayout.setVisibility(View.VISIBLE);
            isViewShown = true;
        }
    }

    private  void hideUI() {
        if (isViewShown) {
            isViewShown = false;
            bottomLinearLayout.setVisibility(View.GONE);
            topLinearLayout.setVisibility(View.GONE);
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float distanceX, float distanceY) {
            Log.d(TAG, "onScroll_v1:" + distanceX);
            Log.d(TAG, "onScroll_v2:" + distanceY);
            Log.d(TAG, "onScroll_v2_divide:" + distanceY/screenY);
            isOperating = true;
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                if (!isScrollingX) {
                    isScrollingY = true;
                    setVolume(distanceY / screenY);
                    return true;
                } else {
                    return false;
                }
            } else {
                if (!isScrollingY && isOffline) {
                    isScrollingX = true;
                    setProgress(distanceX / screenX);

                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isViewShown) {
                hideUI();
            } else {
                showUI();
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (videoView.isPlaying()) {
                videoView.pause();
                // TODO: 3/15/2017 stop
                EventBus.getDefault().post(new ActionMemoryStop());
            } else {
                videoView.start();
                videoView.getDuration();
                // TODO: 3/15/2017 start +time
                updatePosition();

            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v_x, float v_y) {
            Log.d(TAG, "onFling_v1:" + v_x);
            Log.d(TAG, "onFling_v2:" + v_y);
            if (Math.abs(v_x) > Math.abs(v_y)) {//上下滑动
                if (v_x > 0) {
                    forward();
                } else {
                    backward();
                }
                return true;
            } else {//左右滑动
                return false;
            }
        }
    }


}
