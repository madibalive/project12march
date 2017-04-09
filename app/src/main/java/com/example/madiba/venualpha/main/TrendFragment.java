package com.example.madiba.venualpha.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendEventCell;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendEventCellHolder;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCell;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCellHolder;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.MdTrendEvents;
import com.example.madiba.venualpha.models.MdTrendMemory;
import com.example.madiba.venualpha.models.TrendingModel;
import com.example.madiba.venualpha.util.NetUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class TrendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SimpleRecyclerView mRecyclerview;
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.container_core_simple, container, false);
        mRecyclerview = (SimpleRecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.core_swipelayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        List<TrendEventCell> eventItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            eventItems.add(new TrendEventCell(new MdEventItem()));
        }

        List<TrendMemoryCell> memoryCells = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            memoryCells.add(new TrendMemoryCell(new MdMemoryItem()));
        }

        TrendEventCellHolder eventCellHolder = new TrendEventCellHolder(new MdTrendEvents(eventItems));
        TrendMemoryCellHolder memoryCellHolder = new TrendMemoryCellHolder(new MdTrendMemory(memoryCells));

        List<SimpleCell> cells = new ArrayList<>();
        cells.add(eventCellHolder);
        cells.add(eventCellHolder);
        cells.add(memoryCellHolder);

        mRecyclerview.addCells(cells);

    }

    @Override
    public void onRefresh() {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            listRxLoader.restart();
        }else
            mSwipeRefreshLayout.setRefreshing(false);}




    private void initload(){
        listRxLoader =loaderManager.create(
                LoaderMainFragment.loadrend(),
                new RxLoaderObserver<List<TrendingModel>>() {
                    @Override
                    public void onNext(List<TrendingModel> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            mSwipeRefreshLayout.setRefreshing(false);
//                            if (value.size()>0)
//                                mAdapter.setNewData(value);
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
