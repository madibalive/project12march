package com.example.madiba.venualpha.channels;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.channel.ChVenuEventCell;
import com.example.madiba.venualpha.adapter.channel.ChVenuHighCell;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.MdChannelSports;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainChannelActivity extends FragmentActivity {


    private SimpleRecyclerView mRecyclerview;
    private ImageView imageView;
    private RoundCornerImageView avatar;
    private ImageButton close;
    private TextView mTitle;
    private Spinner spinner;
    private int Type;
    RxLoaderManager loaderManager;
    private View mProgressView;
    private View mView;

    RxLoader<List<SimpleCell>> sportsLoader;
    RxLoader<List<ChVenuEventCell>> eventsLoader;
    RxLoader<List<ChVenuHighCell>> highLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_channel);

        mRecyclerview = (SimpleRecyclerView) findViewById(R.id.core_recyclerview);
        mView = findViewById(R.id.mview);
        avatar = (RoundCornerImageView) findViewById(R.id.avatar);
        imageView = (ImageView) findViewById(R.id.background_image_view);
        mProgressView = findViewById(R.id.login_progress);

    }

    private void toggle(int type){
        switch (type){
            case GlobalConstants.CHANNEL_TYPE_VENU_EVENTS:
                modeEvents();
            case GlobalConstants.CHANNEL_TYPE_VENU_HIGHLIST:
                modeHighlight();
            case GlobalConstants.CHANNEL_TYPE_VENU_SPORTS:
                modeSports();
                break;
            default:
                modeTwo();
        }
    }
    private void modeHighlight(){

        // TODO: 2/28/2017 setupload data
    }

    private void modeEvents(){

        // TODO: 2/28/2017 setupload data
    }
    private void modeSports(){
        // TODO: 2/28/2017 twoHighway data
    }
    private void modeTwo(){
        spinner.setVisibility(View.VISIBLE);
    }


    private void requestOpen(int type){
        switch (type){
            case GlobalConstants.CHANNEL_TYPE_VENU_EVENTS:
                break;
            default:
                modeTwo();
        }
    }

    private void setSize(View view){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        lp.setMargins(10, 20, 10, 20);
        view.setLayoutParams(lp);
    }


    private void ModeSports(List<MdChannelSports> data){


//        mRecyclerview.addCells(cells);
    }



    private void setupImageView(){
        mTitle.setText("");
        Glide.with(MainChannelActivity.this)
                .load("")
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(avatar);

        Glide.with(this)
                .load("")
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(imageView);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mView.setVisibility(show ? View.GONE : View.VISIBLE);
            mView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


//    private void loadHighlight(){
//
//
//        highLoader = loaderManager.create(
//                LoaderChannel.loadHighlights(),
//                new RxLoaderObserver<List<ChVenuHighCell>>() {
//                    @Override
//                    public void onNext(List<ChVenuHighCell> value) {
//                        new Handler().postDelayed(() -> {
////                            mSwipeRefreshLayout.setRefreshing(false);
//                            if (value.size()>0){
//                                mRecyclerview.addCells(value);
//                            }
//                        },500);
//                    }
//                    @Override
//                    public void onStarted() {
//                        Timber.d("stated");
//                        super.onStarted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
////                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("completed");
//                        super.onCompleted();
//                    }
//                }
//
//        );
//
//        eventsLoader = loaderManager.create(
//                LoaderChannel.loadEvent(),
//                new RxLoaderObserver<List<ChVenuEventCell>>() {
//                    @Override
//                    public void onNext(List<ChVenuEventCell> value) {
//                        new Handler().postDelayed(() -> {
////                            mSwipeRefreshLayout.setRefreshing(false);
//                            if (value.size()>0){
//                                mRecyclerview.addCells(value);
//                            }
//                        },500);
//                    }
//                    @Override
//                    public void onStarted() {
//                        Timber.d("stated");
//                        super.onStarted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
////                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("completed");
//                        super.onCompleted();
//                    }
//                }
//
//        );
//    }



}
