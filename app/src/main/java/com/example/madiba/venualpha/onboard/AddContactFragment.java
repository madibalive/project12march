package com.example.madiba.venualpha.onboard;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.Actions.ActionPostComplete;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.services.GeneralService;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.ui.AnimateCheckBox;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AddContactFragment extends Fragment {
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
     private static final int RC_SETTINGS_SCREEN = 125;
    private RecyclerView mRecyclerview;
    private MainAdapter mAdapter;
    private List<ParseUser> mDatas=new ArrayList<>();
    private ProgressDialog progress;
    private Boolean mEnableFb=false;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
        add(" publish_actions");
    }};

    RxLoaderManager loaderManager;

    public AddContactFragment() {
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
        mAdapter = new MainAdapter(R.layout.item_person_selectable, mDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
    }


    private void showBanner(){

    }

    private void linkFacebook(){
        if (!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseFacebookUtils.linkWithReadPermissionsInBackground(ParseUser.getCurrentUser(), this, mPermissions, new SaveCallback() {
                @Override
                public void done(ParseException ex) {
                    if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                        processLoad();
                    }
                }
            });
        }
    }

    private void addNewUsers(){
        SingletonDataSource.getInstance().setOnboardUserList(mAdapter.returnData());
        GeneralService.startActionOnboardAddUsers(getActivity().getApplicationContext());

        // TODO: 3/1/2017 goto categories

    }
    //    ///////////////////////////////////////////////////////
    //    NETWORKING OPERATION HERE //////////////////////////////
    //////////////////////////////////////////////////////////////

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void checkPermissionV2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_CONTACTS)) {
                progress = ProgressDialog.show(getActivity(), null,
                        "finding users in your contacts", true);
                if (mEnableFb)
                    processLoad();
                else
                    linkFacebook();
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.permission_contact),
                        RC_LOCATION_CONTACTS_PERM, Manifest.permission.READ_CONTACTS);
            }
        }else {
            if (mEnableFb)
                processLoad();
            else
                linkFacebook();        }

    }

    private void processLoad() {

        loaderManager.create(
                LoaderGeneral.onboardFindUser(getApplicationContext(),mEnableFb),
                new RxLoaderObserver<List<ParseUser>>() {
                    @Override
                    public void onNext(List<ParseUser> value) {
                        Timber.d("onnext");
                        progress.dismiss();
                        new Handler().postDelayed(() -> {
                            if (value.size()>0) {
//                                mContactsFound.setText(String.format("%s found", String.valueOf(value.size())));
                                mAdapter.setNewData(value);
                            }

                        },500);
                    }

                    @Override
                    public void onCompleted() {
                        progress.dismiss();

                        super.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {

                        super.onError(e);
                        progress.dismiss();

                    }
                }

        ).start();
    }

    private class MainAdapter
            extends BaseQuickAdapter<ParseUser> {
        private Set<ParseUser> checkedSet = new HashSet<>();

        MainAdapter(int layoutResId, List<ParseUser> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final ParseUser data) {
            holder.setText(R.id.name, data.getUsername());
            if (data.getParseFile("avatarSmall")!=null ){
                Glide.with(mContext)
                        .load(data.getParseFile("avatarSmall").getUrl())
                        .crossFade()
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.placeholder_error_media)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .fallback(R.drawable.ic_default_avatar)
                        .thumbnail(0.4f)
                        .into(((RoundCornerImageView) holder.getView(R.id.avatar)));
            }
            final AnimateCheckBox checkBox = holder.getView(R.id.checkbox);

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

        Set<ParseUser> returnData(){
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

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionPostComplete action) {
        if (!action.success){
            if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
                checkPermissionV2();
            }
        }
    }



    @Override
    public void onStop() {

        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

}
