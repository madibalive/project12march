package com.example.madiba.venualpha.channels;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.channel.ChVenuHighCell;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


/**
 * A placeholder fragment containing a simple view.
 */
public class ChannelSportFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    private SimpleRecyclerView mRecyclerview;
    private ImageView imageView;
    private RoundCornerImageView avatar;
    private ImageButton close;
    private TextView mTitle;
    RxLoaderManager loaderManager;
    private View mProgressView;
    private View mView;
    private SwipeRefreshLayout refreshLayout;

    RxLoader<List<ChVenuHighCell>> highLoader;

    public ChannelSportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_base_channel, container, false);
        mRecyclerview = (SimpleRecyclerView) view.findViewById(R.id.core_recyclerview);
        mView = view.findViewById(R.id.mview);
        avatar = (RoundCornerImageView) view.findViewById(R.id.avatar);
        imageView = (ImageView) view.findViewById(R.id.background_image_view);
        close = (ImageButton) view.findViewById(R.id.close);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        mTitle = (TextView) view.findViewById(R.id.title);
        mProgressView = view.findViewById(R.id.login_progress);

       return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout.setOnRefreshListener(this);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void setupHeader(){
        mTitle.setText("");
        Glide.with(this)
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


    private void Query(){
        highLoader = loaderManager.create(
                LoaderChannel.loadHighlights("","",3),
                new RxLoaderObserver<List<ChVenuHighCell>>() {
                    @Override
                    public void onNext(List<ChVenuHighCell> value) {
                        new Handler().postDelayed(() -> {
//                            mSwipeRefreshLayout.setRefreshing(false);
                            if (value.size()>0){
                                mRecyclerview.addCells(value);

                            }
                        },500);
                    }
                    @Override
                    public void onStarted() {
                        Timber.d("stated");
                        super.onStarted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                    }
                }

        );
    }

    @Override
    public void onRefresh() {

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

}
