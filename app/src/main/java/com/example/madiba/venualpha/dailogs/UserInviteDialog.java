package com.example.madiba.venualpha.dailogs;


import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.Actions.ActionInvitesList;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.ui.RotateLoading;
import com.example.madiba.venualpha.ui.StateButton;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;



public class UserInviteDialog extends DialogFragment {

    private MainAdapter mAdapter;
    private ArrayList<String> userIds;
    private RotateLoading progressBar;
    private ImageView close;
    private RxLoaderManager loaderManager;
    private RecyclerView mRecyclerview;
    private List<ParseUser> mDatas=new ArrayList<>();
    private Boolean isActive=false;
    private AppCompatCheckBox mSelectAll;
    private StateButton stateButton;

    public UserInviteDialog() {
    }

    public static UserInviteDialog newInstance() {
        return new UserInviteDialog();
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

//            SingletonDataSource.getInstance().setInviteIdList(mAdapter.getSelectedAllUsersID());
//            SingletonDataSource.getInstance().setInviteUserList(mAdapter.getSelectedAllUsers());
            SingletonDataSource.getInstance().setInviteIdList(mAdapter.getSelectedUsersID());
            SingletonDataSource.getInstance().setInviteUserList(mAdapter.getSelectedUsers());
//        }

        EventBus.getDefault().post(new ActionInvitesList(mAdapter.getSelectedUsers().size()));

        dismiss();
    }


    private void setupAdapter(){
//        mAdapter=new MainAdapter(R.layout.view_linearlayout,mDatas);
//        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerview.setHasFixedSize(true);
//        mRecyclerview.setAdapter(mAdapter);
//        progressBar.start();
    }

    private void initload(){
        loaderManager.create(
                LoaderGeneral.loadUsersContacts(),
                new RxLoaderObserver<List<ParseUser>>() {
                    @Override
                    public void onNext(List<ParseUser> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {

                            if (value.size()>0)
                                mAdapter.setNewData(value);
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



    private class MainAdapter
            extends BaseQuickAdapter<ParseUser> {

        private SparseBooleanArray mSelections = new SparseBooleanArray();
        private static final String SELECTION_POSITIONS = "position";
        private Drawable checkRs;

        public MainAdapter(int layoutResId, List<ParseUser> data) {
            super(layoutResId, data);
            checkRs = ContextCompat.getDrawable(getContext(), R.drawable.ic_check_black_24dp);

        }

        @Override
        protected void convert(BaseViewHolder holder, final ParseUser user) {
            holder.setText(R.id.name, user.getUsername());

            Glide.with(mContext)
                    .load(user.getParseFile("avatarSmall").getUrl())
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into((RoundCornerImageView)holder.getView(R.id.avatar));

            if (isSelected(holder.getAdapterPosition())){
                holder.setVisible(R.id.check_imageview,true);
            }else {
                holder.setVisible(R.id.check_imageview,false);

            }

            LinearLayout linearLayout = ( holder).getView(R.id.container);
            linearLayout.setOnClickListener(view -> tapSelection(holder.getAdapterPosition()));

        }


        public void setSelected(int position, boolean isSelected) {
            mSelections.put(position, isSelected);
            refreshHolder(position);
        }

        private void refreshHolder(int index) {
            if (mData.get(index) == null) {
                return;
            }
            notifyItemChanged(index);

        }

        public void restoreSelectionStates(Bundle savedStates) {
            List<Integer> selectedPositions = savedStates.getIntegerArrayList(SELECTION_POSITIONS);
            restoreSelections(selectedPositions);

        }

        private void restoreSelections(List<Integer> selected) {
            if (selected == null) return;
            int position;
            mSelections.clear();
            for (int i = 0; i < selected.size(); i++) {
                position = selected.get(i);
                mSelections.put(position, true);
            }
            notifyDataSetChanged();
        }


        public boolean isSelected(int position) {
            return mSelections.get(position);
        }

        public List<Integer> getSelectedPositions() {
            List<Integer> positions = new ArrayList<Integer>();

            for (int i = 0; i < mSelections.size(); i++) {
                if (mSelections.valueAt(i)) {
                    positions.add(mSelections.keyAt(i));
                }
            }

            return positions;
        }

        public List<ParseUser> getSelectedUsers() {
            List<ParseUser> users = new ArrayList<>();

            for (int i = 0; i < mSelections.size(); i++) {
                if (mSelections.valueAt(i)) {
                    users.add(mData.get(mSelections.keyAt(i)));
                }
            }
            return users;
        }

        public List<String> getSelectedUsersID() {
            List<String> users = new ArrayList<>();

            for (int i = 0; i < mSelections.size(); i++) {
                if (mSelections.valueAt(i)) {
                    users.add(mData.get(mSelections.keyAt(i)).getObjectId());
                }
            }
            return users;
        }


        public void checkAll(){
            for (int i = 0; i < mData.size(); i++) {
                setSelected(i,true);
            }
            notifyDataSetChanged();

        }

        private void unCheckAll(){
            for (int i = 0; i < mData.size(); i++) {
                setSelected(i,false);
            }
            notifyDataSetChanged();
        }



        public void tapSelection(int position) {
            boolean isSelected = isSelected(position);
            setSelected(position,  !isSelected);
        }

        public void clearSelections() {
            mSelections.clear();
            notifyDataSetChanged();
        }
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