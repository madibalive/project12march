package com.example.madiba.venualpha.main.feed;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendEventCell;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendEventCellHolder;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCell;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCellHolder;
import com.example.madiba.venualpha.main.feed.Feedversion2.FeedEventCell;
import com.example.madiba.venualpha.main.feed.Feedversion2.FeedGossipCell;
import com.example.madiba.venualpha.main.feed.Feedversion2.FeedMemoryCell;
import com.example.madiba.venualpha.chateu.ChateuGossipFragAct;
import com.example.madiba.venualpha.eventpage.WhiteEventPageActivity;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdGossip;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.MdTrendEvents;
import com.example.madiba.venualpha.models.MdTrendMemory;
import com.example.madiba.venualpha.viewer.player.BaseMemoryActivity;
import com.jaychang.srv.OnLoadMoreListener;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;


public class MainFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SimpleRecyclerView mRecyclerview;
    private View root;
    private RxLoaderManager loaderManager;

    private RxLoader<List<SimpleCell>> listRxLoader;
    private int delayMillis = 500;
    private int mCurrentCounter = 0;
    private Date upperBoundDate,lowerBoundDate;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_main0, container, false);
        root = view;
        mRecyclerview = (SimpleRecyclerView) view.findViewById(R.id.recyclerView);

//        refreshLayout = (RecyclerRefreshLayout) view.findViewById(R.id.refresh_layout);
//        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setRefreshStyle(RecyclerRefreshLayout.RefreshStyle.PINNED);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindSample();

        mRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(SimpleRecyclerView simpleRecyclerView) {
                if (mCurrentCounter<5){
                    bindSample();
                    mCurrentCounter++;
                }
            }
        });

    }



    void bind(){
        List<SimpleCell> cells = new ArrayList<>();


        FeedEventCell eventCell = new FeedEventCell(new MdEventItem());
        FeedMemoryCell memoryCell = new FeedMemoryCell(new MdMemoryItem());
        FeedGossipCell gossipCell = new FeedGossipCell(new MdGossip());
        eventCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
            @Override
            public void onCellClicked(Object o, Object o2, Object o3) {
                startActivity(new Intent(getActivity(), WhiteEventPageActivity.class));
            }
        });
        memoryCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
            @Override
            public void onCellClicked(Object o, Object o2, Object o3) {
                startActivity(new Intent(getActivity(), BaseMemoryActivity.class));
            }
        });
        gossipCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
            @Override
            public void onCellClicked(Object o, Object o2, Object o3) {
                startActivity(new Intent(getActivity(), ChateuGossipFragAct.class));
            }
        });



        cells.add(eventCell);
        cells.add(memoryCell);

        cells.add(gossipCell);
        mRecyclerview.addCells(cells);
        mRecyclerview.setLoadMoreCompleted();

    }
    void bindSample(){
        List<SimpleCell> cells = new ArrayList<>();


        FeedEventCell eventCell = new FeedEventCell(new MdEventItem());
        FeedMemoryCell memoryCell = new FeedMemoryCell(new MdMemoryItem());
        FeedGossipCell gossipCell = new FeedGossipCell(new MdGossip());
        eventCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
            @Override
            public void onCellClicked(Object o, Object o2, Object o3) {
                startActivity(new Intent(getActivity(), WhiteEventPageActivity.class));
            }
        });
        memoryCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
            @Override
            public void onCellClicked(Object o, Object o2, Object o3) {
                startActivity(new Intent(getActivity(), BaseMemoryActivity.class));
            }
        });
        gossipCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
            @Override
            public void onCellClicked(Object o, Object o2, Object o3) {
                startActivity(new Intent(getActivity(), ChateuGossipFragAct.class));
            }
        });

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

        cells.add(eventCellHolder);


        cells.add(eventCell);
        cells.add(memoryCell);
        cells.add(memoryCellHolder);

        cells.add(gossipCell);

        mRecyclerview.addCells(cells);

    }

//    @Override
//    public void onRefresh() {
//
//        new Handler().postDelayed(() -> {
//            bindSample();
//
//            refreshLayout.setRefreshing(false);
//
//        },1000);
//
//    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
