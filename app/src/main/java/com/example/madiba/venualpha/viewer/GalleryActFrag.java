package com.example.madiba.venualpha.viewer;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.madiba.venualpha.Actions.ActionNetwork;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.util.NotificationUtils;
import com.jaychang.srv.SimpleRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class GalleryActFrag extends FragmentActivity {


    private TextView mTag;
    private Spinner toggleSpinner;
    private View root;
    private SimpleRecyclerView mRecyclerView;

    private int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_act_frag);
        root = getWindow().getDecorView().getRootView();

    }





    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionNetwork action) {
//        String
        if (action.active)
            NotificationUtils.showSnackBar(root,"no network ",getApplicationContext(),action.active);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



}
