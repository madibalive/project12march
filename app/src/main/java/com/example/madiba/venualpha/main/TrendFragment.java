package com.example.madiba.venualpha.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.trends.TrendingAdapter;
import com.example.madiba.venualpha.models.TrendingModel;
import com.example.madiba.venualpha.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class TrendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerview;
    private TrendingAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<TrendingModel>  mDatas = new ArrayList<>();
    private RxLoader<List<TrendingModel>> listRxLoader;
    private RxLoaderManager loaderManager;

    public TrendFragment() {
    }

    public static TrendFragment newInstance() {
        return new TrendFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        TrendingModel livenow = new TrendingModel("Live Now",TrendingModel.LIVE_NOW);
        TrendingModel mGossip = new TrendingModel("Trending Gossip",TrendingModel.TRENDING);
        List<TrendingModel> mdata = new ArrayList<>();
        mdata.add(livenow);
        mdata.add(mGossip);

        mAdapter = new TrendingAdapter(R.layout.container_box_trend, mdata);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.container_core, container, false);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.core_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.core_swipelayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        initAdapter();
        initload();
    }

    @Override
    public void onRefresh() {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            listRxLoader.restart();
        }else
            mSwipeRefreshLayout.setRefreshing(false);}


    private void initAdapter(){
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
    }

    private void initload(){
        listRxLoader =loaderManager.create(
                LoaderMainFragment.loadrend(),
                new RxLoaderObserver<List<TrendingModel>>() {
                    @Override
                    public void onNext(List<TrendingModel> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (value.size()>0)
                                mAdapter.setNewData(value);
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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                    }
                }
        );
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            listRxLoader.start();
        }
    }

}
