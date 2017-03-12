package com.example.madiba.venualpha.eventmanager.other;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.ui.customviewpager.EnchantedViewPager;

import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;

public class OtherEventFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private int mParam1;
    EnchantedViewPager mViewPager;
    RxLoaderManager loaderManager;


    public OtherEventFragment() {
    }


    public static OtherEventFragment newInstance() {
        OtherEventFragment fragment = new OtherEventFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onboard_add_details, container, false);
        mViewPager = (EnchantedViewPager) view.findViewById(R.id.viewPager);
        return view;
    }


    private void modeEvents(){

    }

    private void setupEvents(List<MdEventItem> data){
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        final OtherPagerAdapter adapter = new OtherPagerAdapter(getActivity(), data,display);
        mViewPager.setAdapter(adapter);
        mViewPager.useAlpha();
        mViewPager.useScale();
        mViewPager.addSwipeToDismiss(position -> {
            adapter.removePosition(position);
            removeItem(data.get(position));
        });


    }

    private void removeItem(MdEventItem mdEventItem) {
    }


}
