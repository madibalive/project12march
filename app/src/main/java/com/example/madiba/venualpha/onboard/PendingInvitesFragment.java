package com.example.madiba.venualpha.onboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.ui.AnimateCheckBox;
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


public class PendingInvitesFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private MainAdapter mAdapter;
    private List<ParseObject> mDatas=new ArrayList<>();

    RxLoaderManager loaderManager;

    public PendingInvitesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.container_core, container, false);
        mRecyclerview = (RecyclerView) root.findViewById(R.id.core_recyclerview);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        initAdapter();


    }
    private void initAdapter() {
        mAdapter = new MainAdapter(R.layout.item_ontap, mDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
//            if (view.getId()== R.id.action_delete){
//                accept(i);
//            }
        });

    }

    private void accept(int pos){
        try {
//            GeneralService.startActionSetGoing(getActivity(),mAdapter.getItem(pos).getObjectId(),mAdapter.getItem(pos).getClassName());
            mAdapter.remove(pos);
            mAdapter.notifyItemRemoved(pos);

        }catch (IndexOutOfBoundsException e){
            Timber.e("error deleting object %s",e.getMessage());
        }

    }

    void initload(){
        loaderManager.create(
                LoaderGeneral.loadPendingInvites(),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
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
        private Set<ParseObject> checkedSet = new HashSet<>();

        public MainAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final ParseObject data) {
            holder.setText(R.id.ot_i_location, data.getString("categoryName"))
                    .setText(R.id.ot_i_order_item,data.getString("number"))
                    .setText(R.id.ot_i_location,data.getObjectId());


            final AnimateCheckBox checkBox = ((AnimateCheckBox) holder.getView(R.id.checkbox));

            if(checkedSet.contains(data)){
                checkBox.setChecked(true);
            }else {
                //checkBox.setChecked(false); //has animation
                checkBox.setUncheckStatus();
            }
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedSet.add(data);
                } else {
                    checkedSet.remove(data);
                }
            });
        }

        public Set<ParseObject> returnData(){
            return checkedSet;
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
            initload();
        }
    }
}
