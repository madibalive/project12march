package com.example.madiba.venualpha.viewer.player;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.madiba.venualpha.Actions.ActionMemoryStart;
import com.example.madiba.venualpha.Actions.ActionMemoryStop;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdMediaItem;
import com.example.madiba.venualpha.ui.autoscrollviewpager.AutoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class BaseMemoryActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ProgressBar progressBar;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private AutoScrollViewPager mViewPager;
    private Boolean stopped=false;
    private Handler handler;
    List<MdMediaItem> mdMediaItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_memory);
        mViewPager = (AutoScrollViewPager) findViewById(R.id.viewPager);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mdMediaItems=new ArrayList<>();
        displayViewpager(mdMediaItems);
    }


    private void displayViewpager(List<MdMediaItem> mediaItems){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),mediaItems);
        mSectionsPagerAdapter.setLisiter(new PagerListener() {
            @Override
            public void onPagerSelected(int position, int type) {
                if (type==1){
                    Log.i("PAGER", "onPagerSelected: type video");
                    mViewPager.stopAutoScroll();
                }else {
                    mViewPager.startAutoScroll(5000);
                }

            }
        });
        progressBar.setMax(9);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.startAutoScroll(5000);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                progressBar.setProgress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private PagerListener listener;
        private List<MdMediaItem> mediaItems;

        public SectionsPagerAdapter(FragmentManager fm,List<MdMediaItem> mediaItems) {
            super(fm);
            this.mediaItems = mediaItems;
        }

        @Override
        public Fragment getItem(int position) {

            Log.i("PAGER", "onPagerSelected:"+position);

            if (position%2==0){
                if (listener != null )
                    listener.onPagerSelected(position,1);
                return VideoMemoryFragment.newInstance();
            }
            else {
                if (listener != null)
                    listener.onPagerSelected(position, 0);
                return PhotoMemoryFragment.newInstance();

            }

        }

        void setLisiter(PagerListener listener) {
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return 10;
        }
    }

    public interface PagerListener {
        void onPagerSelected(int position, int type);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionMemoryStart action) {
     try {
         mViewPager.startAutoScroll(action.time);
     }catch (Exception e){

     }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionMemoryStop action) {
        try {
            mViewPager.stopAutoScroll();
        }catch (Exception e){

        }
    }


    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



}
