package com.example.madiba.venualpha.contact;


import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.ModelOtherContact;
import com.example.madiba.venualpha.models.PhoneContact;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;


public class LocalContactFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener ,EasyPermissions.PermissionCallbacks{
    private static final int RC_LOCATION_CONTACTS_PERM = 124;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerview;
    private List<ModelOtherContact> mDatas = new ArrayList<>();
    private MainAdapter mAdapter;
    private View facebookHeader;
    private View LocalContactHeader;
    private RxLoaderManager loaderManager;
    private Boolean loadFollowers;
    private String userId;

    public LocalContactFragment() {
    }

    public static LocalContactFragment newInstance() {
        return new LocalContactFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ModelOtherContact mGossip = new ModelOtherContact();
        ModelOtherContact event = new ModelOtherContact();

        mGossip.setType(ModelOtherContact.TYPE_PARSE);
        event.setType(ModelOtherContact.TYPE_LOCAL);

        mDatas.add(mGossip);
        mDatas.add(event);
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
        initAdapter();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            load();
        }
    }

    private void initAdapter(){
        mAdapter=new MainAdapter(R.layout.item_person,mDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
        });
    }

    @Override
    public void onRefresh() {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            load();
        }else
            mSwipeRefreshLayout.setRefreshing(false);
    }

    private void initload(){
        Timber.e("starting network call ");
        loaderManager.create(LoaderGeneral.ContactLoadOnce(getActivity()),
                new RxLoaderObserver<List<ModelOtherContact>>() {
                    @Override
                    public void onNext(List<ModelOtherContact> value) {
                        new Handler().postDelayed(() -> {
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (value.size()>0)
                                mAdapter.setNewData(value);
                        },500);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Timber.e(e.getMessage());

                    }

                    @Override
                    public void onCompleted() {

                        super.onCompleted();
                        Timber.e("return complete ");
                    }
                }).start();
    }

    private class LocalAdapter
            extends BaseQuickAdapter<PhoneContact> {

        LocalAdapter(int layoutResId, List<PhoneContact> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final PhoneContact data) {
//
//            holder.setText(R.id.cc_i_name, data.getUsername())
//                    .setVisible(R.id.cc_i_unfollow,false)
//                    .setOnClickListener(R.id.cc_i_avatar,new OnItemChildClickListener());

        }
    }

    private class ParseAdapter
            extends BaseQuickAdapter<ParseUser> {

        List<ParseUser> data;

        ParseAdapter(int layoutResId, List<ParseUser> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final ParseUser data) {

//            holder.setText(R.id.cc_i_name, data.getUsername())
//                    .setOnClickListener(R.id.cc_i_unfollow,new OnItemChildClickListener());

//            Glide.with(mContext).load(data.getParseFile("avatar").getUrl())
//                    .thumbnail(0.1f).dontAnimate().into((ImageView) holder.getView(R.id.cc_i_avatar));

        }

    }


    public class MainAdapter extends BaseQuickAdapter<ModelOtherContact> {


        private List<ParseUser> datas = new ArrayList<>();

        public MainAdapter(int layoutResId, List<ModelOtherContact> data) {
            super(layoutResId, data);

            for (int i = 0; i < 5; i++) {
                ParseUser a=new ParseUser();
                datas.add(a);
            }
        }

        @Override
        protected int getDefItemViewType(int position) {
        if (getItem(position).getType() == ModelOtherContact.TYPE_PARSE)
                return ModelOtherContact.TYPE_PARSE;

            else if (getItem(position).getType() == ModelOtherContact.TYPE_LOCAL)
                return ModelOtherContact.TYPE_LOCAL;

            return super.getDefItemViewType(position);
        }

        @Override
        protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
           if (viewType == ModelOtherContact.TYPE_PARSE)
                return new ParseContactViewHolder(getItemView(R.layout.container_box_header, parent));

            else if (viewType == ModelOtherContact.TYPE_LOCAL)
                return new LocalContactViewHolder(getItemView(R.layout.container_box_header, parent));

            return super.onCreateDefViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            ModelOtherContact n;
            try {
                n = getItem(position);

               if (holder instanceof ParseContactViewHolder) {
                    RecyclerView rview = ((ParseContactViewHolder) holder).getView(R.id.box_recyclerview);
                    TextView title = ((ParseContactViewHolder) holder).getView(R.id.box_title);
                    title.setText("Local Contact");
                    ParseAdapter mAdapter = new ParseAdapter (R.layout.item_person,datas);
//                    ParseAdapter mAdapter = new ParseAdapter (R.layout.item_person, n.getParseContacts());
                    rview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    rview.setHasFixedSize(true);

                    rview.setAdapter(mAdapter);
                    mAdapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
                        if (view.getId() == R.id.cc_i_unfollow){
                            // TODO: 12/29/2016  call follow here
                        }
                    });
                }
                else if (holder instanceof LocalContactViewHolder) {
                    RecyclerView rview = ((LocalContactViewHolder) holder).getView(R.id.box_recyclerview);
                    TextView title = ((LocalContactViewHolder) holder).getView(R.id.box_title);
                    title.setText("Facebook here");
//                    LocalAdapter mAdapter = new LocalAdapter(R.layout.item_person, datas);
                    rview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    rview.setHasFixedSize(true);

                    rview.setAdapter(mAdapter);
                    mAdapter.setOnRecyclerViewItemChildClickListener((baseQuickAdapter, view, i) -> {
                        if (view.getId() == R.id.cc_i_unfollow){
                            // TODO: 12/29/2016  call sms invite here
                        }
                    });
                }

                super.onBindViewHolder(holder, position, payloads);

            }catch (Exception e){
                Timber.e(e.getMessage());
            }
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, ModelOtherContact discoverModel) {

        }

        private class ParseContactViewHolder extends BaseViewHolder {
            public ParseContactViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class LocalContactViewHolder extends BaseViewHolder {
            public LocalContactViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void load() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.READ_CONTACTS)) {
                initload();
            } else {
                EasyPermissions.requestPermissions(getActivity(), getString(R.string.permission_contact),
                        RC_LOCATION_CONTACTS_PERM, Manifest.permission.READ_CONTACTS);
            }
        }else {
            initload();
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
