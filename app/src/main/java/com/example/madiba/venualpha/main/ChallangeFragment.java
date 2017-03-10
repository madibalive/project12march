package com.example.madiba.venualpha.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.NavigateTo;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.eventpage.WhiteEventPageActivity;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

import static com.example.madiba.venualpha.discover.DiscoverActivity.CATEGORY_DATA;


public class ChallangeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final List<String> DATA = Arrays.asList("Global","Friends");
    private List<ParseObject>   mDatas = new ArrayList<>();
    private RecyclerView mRecyclerview;
    private ChallangesAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ParseObject mMyChallange;
    private View headView;
    private View footer;
    private FrameLayout view_more;
    private TextView mHashtag,mTitle,mDesc;
    private ImageView mImage;
    private RxLoader<List<MdMemoryItem>> listRxLoader;
    private RxLoaderManager loaderManager;
    private Spinner segmentControl;
    private Boolean isGlobal;


    private LinearLayout mChannelsLayout,mInteractionLayout;
    private ViewGroup mChannelsContainer;

    public ChallangeFragment() {
    }

    public static ChallangeFragment newInstance() {
        return new ChallangeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);

        for (int i = 0; i < 5; i++) {
            ParseObject a=new ParseObject("");
            mDatas.add(a);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.container_core, container, false);
        headView = inflater.inflate(R.layout.header_challange, container, false);
        footer = inflater.inflate(R.layout.foote_challange_view_more, container, false);
        view_more = (FrameLayout) footer.findViewById(R.id.view_more_button);
        mTitle = (TextView) headView.findViewById(R.id.title);
        mDesc = (TextView) headView.findViewById(R.id.desc);
        mImage = (ImageView) headView.findViewById(R.id.image);

        mRecyclerview = (RecyclerView) view.findViewById(R.id.core_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.core_swipelayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRefresh() {

        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            listRxLoader.restart();
        }else
            mSwipeRefreshLayout.setRefreshing(false);

    }

    private void initAdapter(List<ParseObject> objects){

        mAdapter = new ChallangesAdapter(R.layout.item_challange, mDatas);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
        });


        view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mAdapter.addHeaderView(headView);
        mAdapter.addFooterView(footer);
        mRecyclerview.setAdapter(mAdapter);
    }



    private void initSpinner(){
        ArrayAdapter<String> eventCategoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, DATA);
        eventCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        segmentControl.setAdapter(eventCategoryAdapter);
        segmentControl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toggle(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void displayChannels(List<ParseObject> mDatas){
        if (CATEGORY_DATA.size()<0) {
            mChannelsContainer.setVisibility(View.GONE);
            return;
        }

        else {
            mChannelsContainer.setVisibility(View.VISIBLE);
            mChannelsLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            for (final String feature : CATEGORY_DATA) {
                ImageView chipView = (ImageView) inflater.inflate(
                        R.layout.draw_layout, mChannelsLayout, false);
//                chipView.setText(feature.getTitle());
//                chipView.setContentDescription(feature.getTitle());
//                chipView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
//                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
//                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        getActivity().startActivity(intent);
//                    }
//                });

                mChannelsLayout.addView(chipView);
            }

            mDesc.setOnClickListener(view -> mInteractionLayout.setVisibility(View.GONE));

        }
    }




    private void displayHeader(String hashTag,String info,String url){

        if (hashTag==null || url==null)
            return;

        new Handler().postDelayed(() -> {
            mInteractionLayout.setVisibility(View.VISIBLE);

            Glide.with(getActivity())
                    .load(url)
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into(mImage);

            mHashtag.setText(String.format("%s Invited you", info));
        },500);


    }


   private void toggle(int index){
        if (index==0 && !isGlobal) {
            isGlobal = true;
            loadData();
        }else if (index==1 && isGlobal) {
            isGlobal = false;
            loadData();
        }
    }




    private void requestPlayer(List<MdMemoryItem> memoryItems,int pos){

    }

    private void requestMore(){

    }


    private void requestChannel(ParseObject parseObject){

    }






    private void loadData(){
        listRxLoader =loaderManager.create(
                LoaderMainFragment.LoadChallange(isGlobal),
                new RxLoaderObserver<List<MdMemoryItem>>() {
                    @Override
                    public void onNext(List<MdMemoryItem> value) {
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

        );
    }


    private class ChallangesAdapter extends BaseQuickAdapter<MdMemoryItem> {

        ChallangesAdapter(int layoutResId, List<MdMemoryItem> data) {
            super(layoutResId, data);
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        protected void convert(BaseViewHolder holder, MdMemoryItem challange) {

//            holder.setText(R.id.challange_name, challange.getParseUser("from").getUsername())
//                    .setText(R.id.challange_name, "sample text")
//                    .setText(R.id.challange_pos, "#" + getData().indexOf(challange)+1)
//                    .setOnClickListener(R.id.challange_avatar, new OnItemChildClickListener());
//
//            Glide.with(mContext)
//                    .load(challange.getParseUser("from").getParseFile("avatar"))
//                    .thumbnail(0.4f)
//                    .placeholder(R.drawable.ic_default_avatar)
//                    .error(R.drawable.placeholder_error_media)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .fallback(R.drawable.ic_default_avatar)
//                    .crossFade()
//                    .into((ImageView) holder.getView(R.id.challange_avatar));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
//            listRxLoader.start();
        }
    }
}
