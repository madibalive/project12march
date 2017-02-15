package com.example.madiba.venualpha.contact;


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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class FollowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private List<ParseObject> mDatas = new ArrayList<>();
    private List<ParseObject> mSortedList = new ArrayList<>();
    private MainAdapter mAdapter;

    RxLoaderManager loaderManager;
    private Boolean loadFollowers;
    private String userId;

    public FollowFragment() {
    }

    public static FollowFragment newInstance(String id, Boolean followers) {
        FollowFragment frg = new FollowFragment();
//        Bundle extras= new Bundle();
//        extras.putBoolean("loadFollowers",followers);
//        extras.putString(GlobalConstants.INTENT_ID,id);
//
//        frg.setArguments(extras);

        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 5; i++) {
            ParseObject a=new ParseObject("");
            mDatas.add(a);
        }

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
        if (getArguments() != null){
            userId = getArguments().getString(GlobalConstants.INTENT_ID);
            loadFollowers = getArguments().getBoolean("followers");
        }else {
        }
        initAdapter();
    }

    private void initAdapter(){
        mAdapter=new MainAdapter(R.layout.item_person,mDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
//            startActivity(new Intent(getActivity(), UserPageActivity.class));
        });


    }

    @Override
    public void onRefresh() {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            initload();
        }else
            mSwipeRefreshLayout.setRefreshing(false);
    }

    void initload(){
        loaderManager.create(
                LoaderGeneral.loadUsersContacts(userId,1),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
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

        ).start();
    }


    private class MainAdapter
            extends BaseQuickAdapter<ParseObject> {






        MainAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder holder, final ParseObject data) {
//
//            if (loadFollowers){
//                holder.setText(R.id.cc_i_name, data.getParseUser("from").getUsername());
//                Glide.with(mContext).load(data.getParseUser("from").getParseFile("avatar").getUrl())
//                        .thumbnail(0.1f).dontAnimate().into((ImageView) holder.getView(R.id.cc_i_avatar));
//
//            }else {
//                holder.setText(R.id.cc_i_name, data.getParseUser("to").getUsername());
//                Glide.with(mContext).load(data.getParseUser("to").getParseFile("avatar").getUrl())
//                        .thumbnail(0.1f).dontAnimate().into((ImageView) holder.getView(R.id.cc_i_avatar));
//
//            }


        }



        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        public boolean areContentsTheSame(ParseObject oldItem, ParseObject newItem) {
            return oldItem.equals(newItem);
        }

        public boolean areItemsTheSame(ParseObject item1, ParseObject item2) {
            return item1.getObjectId() == item2.getObjectId();
        }

        public void replaceAll(List<ParseObject> models) {
            for (int i = models.size() - 1; i >= 0; i--) {
                final ParseObject model = models.get(i);
                if (!models.contains(model)) {
                    models.remove(model);
                }
            }
            mSortedList.addAll(models);
        }



    }

    private static List<ParseObject> filter(List<ParseObject> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<ParseObject> filteredModelList = new ArrayList<>();
        for (ParseObject model : models) {
            final String text = model.getParseUser("").getUsername().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            initload();
        }
    }
}
