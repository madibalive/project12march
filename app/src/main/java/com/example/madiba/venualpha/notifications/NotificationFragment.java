package com.example.madiba.venualpha.notifications;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.notif.NotificationAdapter;
import com.example.madiba.venualpha.models.ModelNotification;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import timber.log.Timber;


public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerview;
    private NotificationAdapter mAdapter;
    private List<ModelNotification> mDatas = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View notLoadingView;
    private int mCurrentCounter = 0;
    private RxLoaderManager loaderManager;
    private RxLoader<List<ParseObject>> listRxLoader;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mDatas = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            ParseObject a=new ParseObject("");
//            mDatas.add(a);
//        }

        ModelNotification mGossip = new ModelNotification(ModelNotification.TYPE_EXTENDED);
        ModelNotification livenow = new ModelNotification(ModelNotification.TYPE_DEFAULT);
        ModelNotification media = new ModelNotification(ModelNotification.TYPE_SUGGESTTED);
        mDatas.add(mGossip);
        mDatas.add(livenow);
        mDatas.add(media);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.container_core, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.core_swipelayout);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.core_recyclerview);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        initAdapter();
    }

    private void initAdapter(){
        mAdapter = new NotificationAdapter(R.layout.item_notif_extended, mDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext()))
            Timber.i("as");
//            initialLoad();
        else
            mSwipeRefreshLayout.setRefreshing(false);
    }



//    private void initialLoad(){
//        loaderManager.create(
//                LoaderGeneral.loadNotifications(mCurrentCounter),
//                new RxLoaderObserver<List<ParseObject>>() {
//                    @Override
//                    public void onNext(List<ParseObject> value) {
//                        Timber.d("onnext");
//                        if (value.size() <= 0) {
//                            mAdapter.notifyDataChangedAfterLoadMore(false);
//                            if (notLoadingView == null) {
//                                notLoadingView = getActivity().getLayoutInflater().inflate(R.layout.view_no_more_data, (ViewGroup) mRecyclerview.getParent(), false);
//                            }
//                            mAdapter.addFooterView(notLoadingView);
//
//                        } else {
//                            new Handler().postDelayed(() -> {
//                                mAdapter.removeAllFooterView();
//                                if (mSwipeRefreshLayout.isRefreshing()) {
//                                    mSwipeRefreshLayout.setRefreshing(false);
//                                    mAdapter.setNewData(value);
//                                    mAdapter.openLoadMore(20, true);
//                                    mAdapter.removeAllFooterView();
//                                }else {
//                                    mAdapter.notifyDataChangedAfterLoadMore(value, true);
//                                    mCurrentCounter = mAdapter.getData().size();
//                                }
//                                mCurrentCounter = mAdapter.getData().size();
//                                ParseObject.unpinAllInBackground(GlobalConstants.CLASS_NOTIFICATION, e1 -> {
//                                    ParseObject.pinAllInBackground(GlobalConstants.CLASS_NOTIFICATION, value);
//                                });
//                            }, 500);
//
//                        }
//                    }
//
//                    @Override
//                    public void onStarted() {
//                        Timber.d("stated");
//                        super.onStarted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.d("stated error %s",e.getMessage());
//                        super.onError(e);
//                        mSwipeRefreshLayout.setRefreshing(false);
//
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("completed");
//                        super.onCompleted();
//                    }
//                }
//
//        ).start();
//    }

//    private class NotifAdapter
//            extends BaseQuickAdapter<ParseObject> {
//
//        NotifAdapter(int layoutResId, List<ParseObject> data) {
//            super(layoutResId, data);
//        }
////
//
//        @Override
//        protected void convert(BaseViewHolder holder, final ParseObject notif) {

//
//            String msg ;
//
//            switch (notif.getInt("type")){
//
//                case 1:
//                    msg = "liked your post";
//                    break;
//                case 2:
//                    msg="Commented on your post";
//                    break;
//                case 3:
//                    msg="Followed you";
//                    break;
//                case 4:
//                    msg="Invited to an event";
//                    break;
//
//                default:
//                    msg="";
//                    break;
//            }
//            holder.setText(R.id.notif_i_name, notif.getParseUser("from").getUsername())
//                    .setText(R.id.notif_i_name,msg);
//
//            Glide.with(mContext)
//                    .load(notif.getParseUser("from").getParseFile("avatar"))
//                    .crossFade()
//                    .placeholder(R.drawable.ic_default_avatar)
//                    .error(R.drawable.placeholder_error_media)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .fallback(R.drawable.ic_default_avatar)
//                    .thumbnail(0.4f)
//                    .into((CircleImageView) holder.getView(R.id.notif_i_avatar));
//
//            if (notif.getInt("type")==4){
////                holder.setOnClickListener(R.id.notif_i_btn,new OnItemChildClickListener())
////                        .setVisible(R.id.notif_i_btn,true);
//            }
//
//            if (notif.getInt("type") != 4){
//                Glide.with(mContext)
//                        .load(notif.getParseObject("object").getParseFile("url"))
//                        .crossFade()
//                        .placeholder(R.drawable.ic_default_avatar)
//                        .error(R.drawable.placeholder_error_media)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .centerCrop()
//                        .fallback(R.drawable.ic_default_avatar)
//                        .thumbnail(0.4f)
//                        .into((RoundCornerImageView) holder.getView(R.id.notif_i_rightimage));
//            }
//
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            initialLoad();
        }
    }

}
