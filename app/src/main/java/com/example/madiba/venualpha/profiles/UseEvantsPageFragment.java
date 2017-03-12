package com.example.madiba.venualpha.profiles;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.EventItemCell;
import com.example.madiba.venualpha.models.MdEventItem;
import com.jaychang.srv.OnLoadMoreListener;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class UseEvantsPageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private int mode;
    private int state;
    private MdEventItem eventItem;
    private RxLoader<List<EventItemCell>> eventxLoader;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private Date startDate,endDate;
    RxLoaderManager loaderManager;
    private SimpleRecyclerView recyclerView;
    private String loadClassName,loadId;

    public UseEvantsPageFragment() {
        // Required empty public constructor
    }

    public static UseEvantsPageFragment newInstance(String  id) {
        UseEvantsPageFragment fragment = new UseEvantsPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loadId = getArguments().getString(ARG_PARAM1);

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }



    private void setupView(){

        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(SimpleRecyclerView simpleRecyclerView) {
                eventxLoader.start();
            }
        });
    }



    private void load() {

        eventxLoader=  loaderManager.create(
                LoaderProfileLoader.loadUserEvants(loadId,mCurrentCounter,startDate,endDate),
                new RxLoaderObserver<List<EventItemCell>>() {
                    @Override
                    public void onNext(List<EventItemCell> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            if (value.size()>0) {
                                recyclerView.addCells(value);
                                mCurrentCounter = recyclerView.getItemCount();
                            }
                        },500);
                    }

                    @Override
                    public void onStarted() {
                        super.onStarted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                        recyclerView.setLoadMoreCompleted();

                    }
                }
        );


    }



}
