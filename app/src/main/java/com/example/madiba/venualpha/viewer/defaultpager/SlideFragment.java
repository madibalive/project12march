package com.example.madiba.venualpha.viewer.defaultpager;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdMediaItem;
import com.example.madiba.venualpha.ui.AutoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SlideFragment extends Fragment {

    private static final String ARG_CURRENT_ITEM = "param1";
    private static final String ARG_DATA_SET = "param2";

    // TODO: Rename and change types of parameters
    private int currentItem;
    private List<MdMediaItem> DATA;

    private AutoScrollViewPager viewPager;
    private ProgressBar progressBar;
    private PagerAdapter adapter;

    private OnSlideFragmentInteractionListener mListener;

    public SlideFragment() {
        // Required empty public constructor
    }

    public static SlideFragment newInstance(List<MdMediaItem> dataSet, int position) {
        SlideFragment fragment = new SlideFragment();
        Bundle args = new Bundle();
        Parcelable listParcelable = Parcels.wrap(dataSet);

        args.putParcelable(ARG_DATA_SET, listParcelable);
        args.putInt(ARG_CURRENT_ITEM, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        if (Build.VERSION.SDK_INT > 10) {
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        } else {
            getActivity().getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (getArguments() != null) {
            currentItem = getArguments().getInt(ARG_CURRENT_ITEM);
            DATA = Parcels.unwrap(getArguments().getParcelable(ARG_DATA_SET));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPager);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void setupViewPager(){
        adapter= new PagerAdapter(getChildFragmentManager(), DATA,getApplicationContext());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                // TODO: 3/5/2017 send message to handler  
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(currentItem);

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



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSlideFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSlideFragmentInteractionListener) {
            mListener = (OnSlideFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSlideFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSlideFragmentInteractionListener {
        void onSlideFragmentInteraction(Uri uri);
    }
}
