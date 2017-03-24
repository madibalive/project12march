package com.example.madiba.venualpha.eventmanager.userEvents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class UserEventsFragment extends Fragment {

    private int mode;
    private EventPager mAdapter;

    private View mProgressView;
    private ViewPager mViewPager;
    List<MdEventItem> eventItems;

    public UserEventsFragment() {
    }


    public static UserEventsFragment newInstance(int number) {
        UserEventsFragment fragment = new UserEventsFragment();
        Bundle args = new Bundle();
        args.putInt("mode", number);
        fragment.setArguments(args);
        return fragment;    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("SELECTED", "MY EVENT ");
        if (getArguments() != null) {
            mode = getArguments().getInt("mode");
       }

        eventItems= new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            eventItems.add(new MdEventItem());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_event_container, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            setupPagerAdapter(eventItems);

    }


    private void setupPagerAdapter(List<MdEventItem> mDatas){
        Timber.i(String.valueOf(mDatas.size()));
        mAdapter = new EventPager(getChildFragmentManager(),mDatas);
        mViewPager.setAdapter(mAdapter);
    }


    private class EventPager extends FragmentStatePagerAdapter {
        private List<MdEventItem> mDatas;

        public EventPager(FragmentManager fm, List<MdEventItem> data) {
            super(fm);
            mDatas = data;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            return ViewMyEventFragment.newInstance();
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
            mViewPager.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




}
