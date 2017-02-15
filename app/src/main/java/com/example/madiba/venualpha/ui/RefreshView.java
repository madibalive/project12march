package com.example.madiba.venualpha.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.madiba.venualpha.R;


public class RefreshView extends FrameLayout implements View.OnClickListener {
    private Context context;
    private ImageView refreshIcon;
    private RotateLoading progressBar;
    private Boolean refresh =false;

    private RefreshViewSwitchListener refreshViewSwitchListener;

    public interface RefreshViewSwitchListener {
        void onRefreshViewModeChanged(Boolean mode);
    }

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    private void init() {
        LayoutInflater.from(context).inflate(R.layout.view_refreshview, this);

        refreshIcon = (ImageView) findViewById(R.id.refresh_imageview);
        progressBar = (RotateLoading) findViewById(R.id.progress_bar);

        setOnClickListener(this);
        setProgressBar();


    }

    private void setRefreshBtn(){
        if (refreshIcon.getVisibility()!=VISIBLE){
            refreshIcon.setVisibility(VISIBLE);

            if (progressBar.getVisibility()==VISIBLE){
                progressBar.setVisibility(GONE);
            }

        }
        progressBar.stop();

    }
    private void setProgressBar(){
        if (progressBar.getVisibility()!=VISIBLE){
            progressBar.setVisibility(VISIBLE);


            if (refreshIcon.getVisibility()==VISIBLE){
                refreshIcon.setVisibility(GONE);
            }
        }
        progressBar.start();

    }
    private void setup(){
        if (refresh){
            setRefreshBtn();
        }else {
            setProgressBar();
        }
    }


    @Override
    public void onClick(View view) {
        toggle();
    }

    private void setPrivateSwitchListener(@NonNull RefreshViewSwitchListener listener){
        this.refreshViewSwitchListener = listener;
    }

    public void toggle() {
        refresh = !refresh;

        setup();
        if (refreshViewSwitchListener != null) {
            refreshViewSwitchListener.onRefreshViewModeChanged(refresh);
        }

    }

}
