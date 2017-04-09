package com.example.madiba.venualpha.profiles.mypage;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.profiles.LoaderProfileLoader;
import com.example.madiba.venualpha.profiles.adapter.ProfileEventCell;
import com.example.madiba.venualpha.profiles.adapter.ProfileGalleryCell;
import com.jaychang.srv.OnLoadMoreListener;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class MyUserGalleryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_PARAM1 = "param1";

    private RxLoader<List<ProfileGalleryCell>> galleryRxLoader;
    private RxLoader<List<ProfileEventCell>> eventxLoader;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private Date startDate,endDate;
    RxLoaderManager loaderManager;
    private SimpleRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String loadId;

    public MyUserGalleryFragment() {
        // Required empty public constructor
    }

    public static MyUserGalleryFragment newInstance(String  id) {
        MyUserGalleryFragment fragment = new MyUserGalleryFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            loadId = getArguments().getString(ARG_PARAM1);

        }
        Log.e("PAGE","user gallery listview");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_gallery, container, false);

        recyclerView = (SimpleRecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.core_swipelayout);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);

        for (int i = 0; i < 10; i++) {
            recyclerView.addCells(new ProfileGalleryCell(new MdMemoryItem()));
        }
    }

    private void setupView(){

        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(SimpleRecyclerView simpleRecyclerView) {
                load();
            }
        });
    }


    @Override
    public void onRefresh() {

    }

    private void load() {

        galleryRxLoader=  loaderManager.create(
                LoaderProfileLoader.loadUserGallery(loadId,mCurrentCounter,startDate,endDate),
                new RxLoaderObserver<List<ProfileGalleryCell>>() {
                    @Override
                    public void onNext(List<ProfileGalleryCell> value) {
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
