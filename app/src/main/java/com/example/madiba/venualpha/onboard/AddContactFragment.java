package com.example.madiba.venualpha.onboard;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceAdapter;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceRecyclerView;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
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


public class AddContactFragment extends Fragment implements FbLinkDailog.OnFragmentInteractionListener {
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final int RC_SETTINGS_SCREEN = 125;
    private MultiChoiceRecyclerView mRecyclerview;
    private AddContactMultiAdapter mAdapter;
    private List<ParseUser> mDatas=new ArrayList<>();
    private ProgressDialog progress;
    private Boolean mEnableFb=false;
    public static final int index =3;
    private Button close;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
        add(" publish_actions");
    }};


    private OnFragmentInteractionListener mListener;

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
        View root=inflater.inflate(R.layout.fragment_onboard_addusers, container, false);
        mRecyclerview = (MultiChoiceRecyclerView) root.findViewById(R.id.recyclerView);
        close = (Button) root.findViewById(R.id.go);
        return root;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showBanner();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(5);
            }
        });

        loaderManager = RxLoaderManagerCompat.get(this);
        initAdapter();


    }
    private void initAdapter() {
        mAdapter=new AddContactMultiAdapter(mDatas,getActivity());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onFragmentInteraction(Boolean uri) {

    }

    private void showBanner(){
        FbLinkDailog.newInstance().show(getChildFragmentManager(), "dialog");
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
//                                mAdapter.setNewData(value);
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



    private class AddContactMultiAdapter extends MultiChoiceAdapter<AddContactMultiAdapter.SampleCustomViewHolder> {

        List<ParseUser> messageV0s;
        Context mContext;

        public AddContactMultiAdapter(List<ParseUser> messageV0s, Context context) {
            this.messageV0s = messageV0s;
            this.mContext = context;
        }

        @Override
        public SampleCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SampleCustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_checkable, parent, false));
        }

        @Override
        public void onBindViewHolder(SampleCustomViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
//
//        ParseUser currentItem = messageV0s.get(position);
//        holder.name.setText(currentItem.getName());


        }

        @Override
        public int getItemCount() {
            return messageV0s.size();
        }

        @Override
        protected void setActive(View view, boolean state) {


            ImageView imageView = (ImageView) view.findViewById(R.id.add);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.container);
            if(state){
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackgroundLight));
//            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                imageView.setVisibility(View.VISIBLE);
            }else{
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
//            imageView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
                imageView.setVisibility(View.GONE);

            }
        }

        public class SampleCustomViewHolder extends RecyclerView.ViewHolder{

            public TextView name;
            public ImageView radio;


            public SampleCustomViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.title);
                radio = (ImageView) itemView.findViewById(R.id.add);
            }
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int uri) {
        if (mListener != null) {
            mListener.onGoCategories();
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
        // TODO: Update argument type and name
        void onGoCategories();
    }

}
