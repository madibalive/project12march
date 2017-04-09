package com.example.madiba.venualpha.eventmanager.userEvents;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.eventmanager.MyPagerAdapter;
import com.example.madiba.venualpha.eventmanager.OtherPagerAdapter;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.ui.customviewpager.EnchantedViewPager;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;

public class MyEventFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    EnchantedViewPager mViewPager;
    RxLoaderManager loaderManager;
    List<MdEventItem> eventItems;

    private View mProgressView;

    public MyEventFragment() {
    }


    public static MyEventFragment newInstance() {
        MyEventFragment fragment = new MyEventFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
        eventItems= new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            eventItems.add(new MdEventItem());
        }
        Log.e("SELECTED", "OTHR EVENT ");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_oth_event_container, container, false);
        mViewPager = (EnchantedViewPager) view.findViewById(R.id.viewPager);
        mProgressView = view.findViewById(R.id.progress_bar);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            setupEvents(eventItems);


    }

    private void modeEvents(){

    }

    private void setupEvents(List<MdEventItem> data){
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        final MyPagerAdapter adapter = new MyPagerAdapter(getActivity(), data,display);
        mViewPager.setAdapter(adapter);
        mViewPager.useAlpha();
        mViewPager.useScale();
        mViewPager.setCurrentItem(1);


    }

    private void removeItem(MdEventItem mdEventItem) {
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
