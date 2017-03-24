package com.example.madiba.venualpha.eventmanager.userEvents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ontap.RequestFragment;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class TrackingEventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private View root;
    private MainAdapter mAdapter;
    private List<ParseObject> mDatas=new ArrayList<>();
    RxLoaderManager loaderManager;
    private View mProgressView;

    public TrackingEventFragment() {
    }

    public static TrackingEventFragment newInstance() {
        return new TrackingEventFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < 5; i++) {
            ParseObject a=new ParseObject("");
            mDatas.add(a);
        }
        Log.e("SELECTED", "TRACKING EVENT ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_tracking_container, container, false);
        mRecyclerview = (RecyclerView) root.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.core_swipelayout);
        mProgressView = root.findViewById(R.id.progress_bar);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        loaderManager = RxLoaderManagerCompat.get(this);
        initAdapter();


    }
    private void initAdapter(){
        mAdapter=new MainAdapter(R.layout.item_ontap,mDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {

        });

    }

    private void openRequest(){
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            DialogFragment requestDialog = new RequestFragment();
            requestDialog.show(getChildFragmentManager(),"request");
        }
    }

    @Override
    public void onRefresh() {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            initload();
        }else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void Toggle(){

    }


    private void delete(int pos){
        try {
            mAdapter.getItem(pos).deleteInBackground(e -> {
                if (e == null ){
                    if (root != null)
                        Snackbar.make(root, "Delete Successful", Snackbar.LENGTH_SHORT).show();

                }else{
                    if (root != null)
                        Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            });
            mAdapter.remove(pos);
            mAdapter.notifyItemRemoved(pos);

        }catch (IndexOutOfBoundsException e){
            Timber.e("error deleting object %s",e.getMessage());
        }

    }

    void initload(){
        loaderManager.create(
                LoaderGeneral.loadOnTap(),
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

        private Boolean selectionMode=false;
        private Set<ParseObject> checkedSet = new HashSet<>();

        public MainAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final ParseObject category) {
            holder.setText(R.id.ot_i_location, category.getString("categoryName"))
                    .setText(R.id.ot_i_order_item,category.getString("number"))
                    .setText(R.id.ot_i_location,category.getObjectId());

        }


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            initload();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRecyclerview.setVisibility(show ? View.GONE : View.VISIBLE);
            mRecyclerview.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRecyclerview.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRecyclerview.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



}
