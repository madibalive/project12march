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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.channels.MainChannelActivity;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.ViewUtils;
import com.example.madiba.venualpha.viewer.gallery.GalleryActivity;
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
    private List<MdMemoryItem>   mDatas = new ArrayList<>();
    private RecyclerView mRecyclerview;
    private ChallangesAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ParseObject mMyChallange;
    private View footer;
    private FrameLayout view_more;
    private TextView mHashtag,mTitle;
    private RoundCornerImageView mImage;
    private RxLoader<List<MdMemoryItem>> listRxLoader;
    private RxLoaderManager loaderManager;
    private Spinner segmentControl;
    private Boolean isGlobal=false;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_challange, container, false);
        footer = inflater.inflate(R.layout.view_more, container, false);
        view_more = (FrameLayout) footer.findViewById(R.id.view_more_button);
        mTitle = (TextView) view.findViewById(R.id.title);
        mChannelsContainer = (ViewGroup) view.findViewById(R.id.channel_layout);
        mChannelsLayout = (LinearLayout) view.findViewById(R.id.channel_container);
        mTitle = (TextView) view.findViewById(R.id.title);
        mHashtag = (TextView) view.findViewById(R.id.hashtag);
        mImage = (RoundCornerImageView) view.findViewById(R.id.image);
        segmentControl = (Spinner) view.findViewById(R.id.spinner);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<ParseObject> parseObjects = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            parseObjects.add(new ParseObject(""));
        }

        List<MdMemoryItem> memoryItems = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            memoryItems.add(new MdMemoryItem());
        }
        initSpinner();
        displayChannels(parseObjects);
        initAdapter(memoryItems);
    }

    @Override
    public void onRefresh() {

        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            listRxLoader.restart();
        }else
            mSwipeRefreshLayout.setRefreshing(false);

    }

    private void initAdapter(List<MdMemoryItem> objects){
//        List<ChallangeCell> cells = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            ChallangeCell cell = new ChallangeCell(new MdMemoryItem());
//            mRecyclerview.addCell(cell);
//        }

//        mRecyclerview.addCells(cells);
//
        mAdapter = new ChallangesAdapter(R.layout.item_challange, objects);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
        });


        view_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GalleryActivity.class));
            }
        });

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
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            for (final ParseObject channel : mDatas) {
                View view = inflater.inflate(R.layout.item_channel, mChannelsLayout, false);
//                view.setLayoutParams (new ViewGroup.LayoutParams (
//                        140,90));

//                ViewUtils.setHeightAndWidth(view,140,90);
                ViewUtils.setMarginRight(view,10);

                RoundCornerImageView imageView = (RoundCornerImageView) view.findViewById(R.id.image_view);
                ImageButton update = (ImageButton) view.findViewById(R.id.updated);
                TextView title = (TextView) view.findViewById(R.id.title);

//                if (true)
//                    update.setVisibility(View.VISIBLE);

//                Glide.with(getActivity())
//                        .load(channel.getParseFile("avatarSmall").getUrl())
//                        .crossFade()
//                        .placeholder(R.drawable.ic_default_avatar)
//                        .error(R.drawable.placeholder_error_media)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .centerCrop()
//                        .fallback(R.drawable.ic_default_avatar)
//                        .thumbnail(0.4f)
//                        .into(imageView);
//
//                title.setText(channel.getString("title"));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), MainChannelActivity.class));
                    }
                });

                mChannelsLayout.addView(view);
            }

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

            mTitle.setText(String.format("%s Invited you", info));
            mHashtag.setText(String.format("%s Invited you", info));
        },500);


    }


    private void toggle(int index){
        if (index==0 && !isGlobal) {
            isGlobal = true;
//            loadData();
        }else if (index==1 && isGlobal) {
            isGlobal = false;
//            loadData();
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
