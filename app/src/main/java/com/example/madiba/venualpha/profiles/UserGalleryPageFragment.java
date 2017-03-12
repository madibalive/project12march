package com.example.madiba.venualpha.profiles;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.EventItemCell;
import com.example.madiba.venualpha.adapter.GalleryItemCell;
import com.jaychang.srv.OnLoadMoreListener;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class UserGalleryPageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private RxLoader<List<GalleryItemCell>> galleryRxLoader;
    private RxLoader<List<EventItemCell>> eventxLoader;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private Date startDate,endDate;
    RxLoaderManager loaderManager;
    private SimpleRecyclerView recyclerView;

    private String loadId;

    public UserGalleryPageFragment() {
        // Required empty public constructor
    }

    public static UserGalleryPageFragment newInstance(String  id) {
        UserGalleryPageFragment fragment = new UserGalleryPageFragment();
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
                load();
            }
        });
    }



    private void load() {

        galleryRxLoader=  loaderManager.create(
                LoaderProfileLoader.loadUserGallery(loadId,mCurrentCounter,startDate,endDate),
                new RxLoaderObserver<List<GalleryItemCell>>() {
                    @Override
                    public void onNext(List<GalleryItemCell> value) {
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
