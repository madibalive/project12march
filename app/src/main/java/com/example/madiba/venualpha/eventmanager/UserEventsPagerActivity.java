package com.example.madiba.venualpha.eventmanager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class UserEventsPagerActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private List<ParseObject> mDatas = new ArrayList<>();
    private TextView totalCount;
    private UserEventPagetAdapter mAdapter;
    private ViewPager mPager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    RxLoaderManager loaderManager;
    RxLoader<List<ParseObject>> load;
    public UserEventsPagerActivity() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPager = (ViewPager) findViewById(R.id.container);
        loaderManager = RxLoaderManagerCompat.get(this);
        initload();
//        setupPagerAdapter(mDatas);
        load.start();
    }

    private void setupPagerAdapter(List<ParseObject> mDatas){
        Timber.i(String.valueOf(mDatas.size()));
        mAdapter = new UserEventPagetAdapter(getSupportFragmentManager(),mDatas);
        mPager.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        if (NetUtils.hasInternetConnection(getApplicationContext()))
            reload();
        else
            mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initload(){

        load =loaderManager.create(
                LoaderEventManager.loadOnMyEvents(),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
//                            mSwipeRefreshLayout.setRefreshing(false);
                            if (value.size()>0)
                                setupPagerAdapter(value);
                        },500);
                    }

                    @Override
                    public void onStarted() {
                        Timber.d("stated");
                        super.onStarted();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Timber.d("stated error %s",e.getMessage());
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

    private void reload(){
        load.restart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (NetUtils.hasInternetConnection(getApplicationContext()))
//            load.start();
    }

    private class UserEventPagetAdapter extends FragmentStatePagerAdapter {
        private List<ParseObject> mDatas;

        public UserEventPagetAdapter(FragmentManager fm, List<ParseObject> data) {
            super(fm);
            mDatas = data;
            Timber.i("in MyEventPagerAdapter %s",String.valueOf(mDatas.size()));
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            Timber.e("post %s",position);
            return UserEventsFragment.newInstance();
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }
    }
}
