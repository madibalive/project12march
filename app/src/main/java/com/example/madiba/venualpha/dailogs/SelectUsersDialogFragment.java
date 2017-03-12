package com.example.madiba.venualpha.dailogs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.ui.AnimateCheckBox;
import com.example.madiba.venualpha.ui.RotateLoading;
import com.example.madiba.venualpha.ui.StateButton;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class SelectUsersDialogFragment extends DialogFragment {

    private AddNewAdapter mAdapter;
    private ArrayList<String> userIds;
    private RotateLoading progressBar;
    private ImageView close;
    private RxLoaderManager loaderManager;
    private RecyclerView mRecyclerview;
    private List<ParseUser> mDatas=new ArrayList<>();
    private Boolean isActive=false;
    private AppCompatCheckBox mSelectAll;
    private StateButton stateButton;
    private int mode;

    private Listener mListener;

    public SelectUsersDialogFragment() {
    }

    public static SelectUsersDialogFragment newInstance() {
        return new SelectUsersDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_invite, container, false);
        close = (ImageView) view.findViewById(R.id.close);
        mSelectAll = (AppCompatCheckBox) view.findViewById(R.id.select_all);
        progressBar = (RotateLoading) view.findViewById(R.id.rotateloading);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        stateButton = (StateButton) view.findViewById(R.id.submit);
        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        close.setOnClickListener(view1 -> dismiss());
        stateButton.setOnClickListener(view1 -> submit());
        mSelectAll.setOnCheckedChangeListener((compoundButton, b) -> toggle(b));
        setupAdapter();

        initload();
    }


    private void toggle(Boolean aBoolean){
        isActive = aBoolean;
        enableViews();
    }

    private void enableViews(){
        Timber.e("boolean" + isActive );
        if (isActive){
            mAdapter.checkAll();
        }else {
            mAdapter.unCheckAll();
        }
    }
    private void submit() {

//        }
//            avatar.setOnClickListener(v -> {
//                if (mListener != null) {
//                    mListener.onUserClicked(getAdapterPosition());
//                    dismiss();
//                }
//            });
    }


    private void setupAdapter(){
//        mAdapter=new AddNewAdapter(R.layout.view_linearlayout,mDatas);
//        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerview.setHasFixedSize(true);
//        mRecyclerview.setAdapter(mAdapter);
//        progressBar.start();
    }



    private class AddNewAdapter
            extends BaseQuickAdapter<ParseObject> {

        private Boolean selectionMode=false;
        private Set<ParseObject> checkedSet = new HashSet<>();

        public AddNewAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final ParseObject category) {
            holder.setText(R.id.ot_i_location, category.getString("categoryName"))
                    .setText(R.id.ot_i_order_item,category.getString("number"))
                    .setText(R.id.ot_i_location,category.getObjectId());



            final AnimateCheckBox checkBox = ((AnimateCheckBox) holder.getView(R.id.checkbox));
            if (!selectionMode){
                checkBox.setVisibility(View.GONE);
                // TODO: 3/8/2017 add remove button
            }else {

                if (checkedSet.contains(category)) {
                    checkBox.setChecked(true);
                } else {
                    //checkBox.setChecked(false); //has animation
                    checkBox.setUncheckStatus();
                }
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        checkedSet.add(category);
                    } else {
                        checkedSet.remove(category);
                    }
                });
            }
        }

        public void toggle(){
            selectionMode = !selectionMode;
            notifyDataSetChanged();
        }

        public void checkAll(){

        }

        public void unCheckAll(){

        }

        public Boolean getMode(){
            return selectionMode;
        }

        public Set<ParseObject> returnData(){
            return checkedSet;
        }
    }


    private void initload(){
        loaderManager.create(
                LoaderGeneral.loadUsersContacts(),
                new RxLoaderObserver<List<ParseUser>>() {
                    @Override
                    public void onNext(List<ParseUser> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {

//                            if (value.size()>0)
//                                mAdapter.setNewData(value);
                        },500);
                    }

                    @Override
                    public void onStarted() {
                        super.onStarted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("stated error %s",e.getMessage());
                        super.onError(e);

                    }

                    @Override
                    public void onCompleted() {
                        progressBar.stop();
                        progressBar.setVisibility(View.GONE);
                        super.onCompleted();
                    }
                }

        ).start();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onUserClicked(MdUserItem position);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {

        super.onStop();
    }
}