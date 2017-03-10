package com.example.madiba.venualpha.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.trends.TrendingAdapter;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.TrendingModel;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

import static com.example.madiba.venualpha.discover.DiscoverActivity.CATEGORY_DATA;


public class TrendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerview;
    private TrendingAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<TrendingModel>  mDatas = new ArrayList<>();
    private RxLoader<List<TrendingModel>> listRxLoader;
    private RxLoaderManager loaderManager;
    private View headView;
    private TextView mHeaderText;
    private SpannableString header;
    private ViewPager mPager;
    private EventsPagerAdapter mPagerAdapter;
    List<ParseObject> topEvendData=new ArrayList<>();


    private LinearLayout mLiveNowLayout,mInteractionLayout;
    private ViewGroup mLiveNowContainer;


    public TrendFragment() {
    }

    public static TrendFragment newInstance() {
        return new TrendFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 6; i++) {
            ParseObject a=new ParseObject("");
            topEvendData.add(a);
        }

        TrendingModel livenow = new TrendingModel("Live Now",TrendingModel.LIVE_NOW);
        TrendingModel mGossip = new TrendingModel("Trending Gossip",TrendingModel.GOSSIP);
        TrendingModel media = new TrendingModel("Top Event",TrendingModel.MEDIA);
        List<TrendingModel> mdata = new ArrayList<>();
        mdata.add(livenow);
        mdata.add(media);
        mdata.add(mGossip);

        mAdapter = new TrendingAdapter(R.layout.container_recycler, mdata);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.container_core, container, false);
        headView = inflater.inflate(R.layout.header_trend, container, false);
        mPager = (ViewPager)headView.findViewById(R.id.container);
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
        mAdapter.addHeaderView(headView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);

    }


    private void displayLiveNow(List<MdEventItem> mDatas){
        if (CATEGORY_DATA.size()<0) {
            mLiveNowContainer.setVisibility(View.GONE);
            return;
        }

        else {
            mLiveNowContainer.setVisibility(View.VISIBLE);
            mLiveNowLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            for (final String feature : CATEGORY_DATA) {
                ImageView chipView = (ImageView) inflater.inflate(
                        R.layout.draw_layout, mLiveNowLayout, false);
//                chipView.setText(feature.getTitle());
//                chipView.setContentDescription(feature.getTitle());
//                chipView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
//                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
//                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        getActivity().startActivity(intent);
//                    }
//                });

                mLiveNowLayout.addView(chipView);
            }

        }
    }



    private void displayMems(List<MdMemoryItem> memoryItems){

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
