package com.example.madiba.venualpha.viewusers;


import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.util.NetUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.SimpleViewHolder;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class ViewUserListDialog extends DialogFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SimpleRecyclerView mRecyclerview;
    private List<MdUserItem> mDatas = new ArrayList<>();
    RxLoaderManager loaderManager;
    private int type;
    private String userId;
    private ImageButton close;


    public ViewUserListDialog() {
    }

    public static ViewUserListDialog newInstance(String id, Boolean followers) {
        ViewUserListDialog frg = new ViewUserListDialog();
        Bundle extras= new Bundle();
        if (followers)
            extras.putInt(GlobalConstants.INTENT_ID,0);
        else
            extras.putInt(GlobalConstants.INTENT_ID,1);

        frg.setArguments(extras);

        return frg;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
            d.getWindow().setBackgroundDrawable(null);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 5; i++) {
            MdUserItem a=new MdUserItem();
            mDatas.add(a);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.dialog_user_list, container, false);
        mRecyclerview = (SimpleRecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshlayout);
        close = (ImageButton) view.findViewById(R.id.close);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        if (getArguments() != null){
            userId = getArguments().getString(GlobalConstants.INTENT_ID);
            type = getArguments().getInt("followers");
        }else {
        }
        initAdapter();

        close.setOnClickListener(view1 -> dismiss());
    }

    private void initAdapter(){

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
                ViewUserLoader.loadUsersContacts(userId,type),
                new RxLoaderObserver<List<ViewUserCell>>() {
                    @Override
                    public void onNext(List<ViewUserCell> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (value.size()>0)
                                mRecyclerview.addCells(value);
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


    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
        }
    }
}
