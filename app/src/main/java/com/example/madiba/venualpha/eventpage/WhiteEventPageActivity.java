package com.example.madiba.venualpha.eventpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.Actions.ActionEvantPageBuy;
import com.example.madiba.venualpha.Actions.ActionEventGoing;
import com.example.madiba.venualpha.Actions.ActionMediaCheckIslike;
import com.example.madiba.venualpha.NavigateTo;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.dailogs.CommentActivityFragment;
import com.example.madiba.venualpha.dailogs.EventReactionFragment;
import com.example.madiba.venualpha.dailogs.RequestFragment;
import com.example.madiba.venualpha.models.ModelEventFeature;
import com.example.madiba.venualpha.models.ModelFeedItem;
import com.example.madiba.venualpha.services.GeneralService;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.ui.venubutton.AllAngleExpandableButton;
import com.example.madiba.venualpha.ui.venubutton.ButtonEventListener;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.TimeUitls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class WhiteEventPageActivity extends FragmentActivity {
    private static final String MAP_FRAGMENT_TAG = "map";


    RxLoaderManager loaderManager;
    private RecyclerView mGoingRcview;
    private RecyclerView mMediaRcview;
    private EventGoingAdapter mGoingAdapter;
    private EventMediaAdapter mMediaAdapter;
    private List<ParseObject> mAttendeeDatas=new ArrayList<>();
    private List<ParseObject> mMediaDatas=new ArrayList<>();
    private ParseObject mCurrentEvent;
    private TextView mTime,mDate,mActionMsg,mActionType,mInviteName;
    private TextView mTitle,mDesc,mName,mLocation,mAttendeeCnt,mMediaMore,mFavBtn,mCmtBtn;
    private FloatingActionButton actionBtn;
    private CircleImageView mAvatar,mInviteAvatar;
    private StateButton mActionBtn;
    private ModelFeedItem dataItem;
    private AllAngleExpandableButton mMoreButton;
    private FrameLayout mapContainer;
    private LinearLayout mInviteBar;
    private Button mFirstBtn,mSecondBtn;
    private ProgressDialog progressDialog;
    private Boolean waiting;
    private Drawable icon ;
    private ImageView imageView;
    private StateButton stateButton;
    private LinearLayout mTags;
    private ViewGroup mTagsContainer;
    private LinearLayout mTagsPeopl;
    private ViewGroup mTagsPeoplContainer;


    private SupportMapFragment mapFragment;


    public WhiteEventPageActivity() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_event_page);

        //        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
//        mMoreButton= (AllAngleExpandableButton) root.findViewById(R.id.button_expandable_90_180);
//        mTitle = (TextView) root.findViewById(R.id.event_page_title);
//        mFavBtn = (TextView) root.findViewById(R.id.event_page_favs);
        mCmtBtn = (TextView) findViewById(R.id.event_page_cmts);
//        mName = (TextView) root.findViewById(R.id.event_page_name);
//        mTime = (TextView) root.findViewById(R.id.event_page_time);
//        mDate = (TextView) root.findViewById(R.id.event_page_date);
//        mLocation = (TextView) root.findViewById(R.id.event_page_location);
//        mDesc = (TextView) root.findViewById(R.id.event_page_desc);
        imageView= (ImageView) findViewById(R.id.session_photo);
//        mActionMsg= (TextView) root.findViewById(R.id.event_page_action_msg);
//        mFirstBtn = (Button) root.findViewById(R.id.event_page_action_btn);
//        mGoingRcview = (RecyclerView) root.findViewById(R.id.recycler_no_frame);
//        mMediaRcview = (RecyclerView) root.findViewById(R.id.recycler_no_frame);
        stateButton = (StateButton) findViewById(R.id.stateButton);
        mTags = (LinearLayout) findViewById(R.id.session_tags);
        mTagsContainer = (ViewGroup) findViewById(R.id.session_tags_container);
        mTagsPeopl = (LinearLayout) findViewById(R.id.session_tags);
        mTagsPeoplContainer = (ViewGroup) findViewById(R.id.session_tags_container);
        mapContainer = (FrameLayout) findViewById(R.id.container);

//        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
//        toolbar.setNavigationOnClickListener(view -> this.finish());
//        fab.setOnClickListener(view -> openRequest());


        loaderManager = RxLoaderManagerCompat.get(this);

        //avatar
        Glide.with(this)
                .load(SingletonDataSource.getInstance().getEventPageEvent().getParseFile("image100").getUrl())
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(imageView);
//        initAdapter();

        displayTags(SingletonDataSource.getInstance().getEventPageEvent());
        isMapAvailabe(SingletonDataSource.getInstance().getEventPageEvent());


        stateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = EventReactionFragment.newInstance();
                newFragment.show(getSupportFragmentManager(), "comment");
            }
        });

        mCmtBtn.setOnClickListener(view -> {
            DialogFragment newFragment = CommentActivityFragment.newInstance(SingletonDataSource.getInstance().getEventPageEvent().getObjectId(),SingletonDataSource.getInstance().getEventPageEvent().getClassName(),true);
            newFragment.show(getSupportFragmentManager(), "comment");
        });
    }



    private void initView(){

        Intent data = this.getIntent();
        if (data.hasExtra("fromFeed")){


            dataItem = SingletonDataSource.getInstance().getCurrentFeedItem();
            if (dataItem !=null) {

                mTitle.setText(String.format("%s%s", dataItem.getEvTitle(), dataItem.getHashtag()));
//            mDesc.setText(dataItem.getpa());
                mLocation.setText(dataItem.getEvLocation());
                mName.setText(dataItem.getName());

                mDate.setText(dataItem.getEvDateToString());
                mTime.setText(dataItem.getEvTimeToString());

                mFavBtn.setText(dataItem.getReactions());
                mCmtBtn.setText(dataItem.getComment());


                //avatar
                Glide.with(this)
                        .load(dataItem.getAvatar())
                        .crossFade()
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.placeholder_error_media)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .fallback(R.drawable.ic_default_avatar)
                        .thumbnail(0.4f)
                        .into(mAvatar);


            }else {
                mCurrentEvent =SingletonDataSource.getInstance().getCurrentEvent();
                mTitle.setText(String.format("%s%s", mCurrentEvent.getString("title"), mCurrentEvent.getString("hashTag")));
                mDesc.setText(mCurrentEvent.getString("desc"));
                mLocation.setText(mCurrentEvent.getString("location"));
                mName.setText(mCurrentEvent.getParseUser("from").getUsername());

                mDate.setText(TimeUitls.Format_dayOnly(mCurrentEvent.getDate("time")));
                mTime.setText(TimeUitls.getRelativeTime(mCurrentEvent.getDate("time")));

                mFavBtn.setText(String.valueOf(mCurrentEvent.getInt("comment")));
                mCmtBtn.setText(String.valueOf(mCurrentEvent.getInt("reactions")));


                //avatar
                Glide.with(this)
                        .load(mCurrentEvent.getParseUser("from").getParseFile("avatar"))
                        .crossFade()
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.placeholder_error_media)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .fallback(R.drawable.ic_default_avatar)
                        .thumbnail(0.4f)
                        .into(mAvatar);


//                isMapAvailabe(mCurrentEvent);



            }
            // initialise views
        }

    }

    private void isMapAvailabe(ParseObject parseObject) {
        FragmentTransaction fragmentTransaction;

        mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);

        // We only create a fragment if it doesn't already exist.
        if (mapFragment == null) {

            GoogleMapOptions options = new GoogleMapOptions();
            options.liteMode(true);
            options.maxZoomPreference(13.5f);
            options.minZoomPreference(13.5f);
            // To programmatically add the map, we first create a SupportMapFragment.
            mapFragment = SupportMapFragment.newInstance(options);

            // Then we add it using a FragmentTransaction.
            fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, mapFragment, MAP_FRAGMENT_TAG);
            fragmentTransaction.commit();


        }

        if (parseObject.getParseGeoPoint("cordinate") != null) {

//            getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
            mapFragment.getMapAsync(googleMap -> {
                LatLng latLng = new LatLng(parseObject.getParseGeoPoint("cordinate").getLatitude(), parseObject.getParseGeoPoint("cordinate").getLongitude());
                CameraPosition selfLoc = CameraPosition.builder()
                        .target(latLng)
                        .zoom(13)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(selfLoc));

                googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet("SAMPLE LOCATION")
//                        .icon(BitmapDescriptorFactory
//                                .fromResource(R.drawable.ic_panorama))
                        .title("Event Location!"));
            });
        } else {
//            getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
        }
    }

    private void setReactionButton(Boolean aBoolean){
        // get eventype
        // get if going record exist
        //if yes negate



        // if   true - cancel -- unRegister -- not going
        if (aBoolean){

        }else {

        }
        // else if false -- Accept Invite -- Register -- Attend --Buy



    }


    private void setAvailableAction(ActionEvantPageBuy action){
        new Handler().postDelayed(() -> {
            if (action.getInvited()){
                // show liniear layout

                Glide.with(this)
                        .load(mCurrentEvent.getParseUser("from").getParseFile("avatar"))
                        .crossFade()
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.placeholder_error_media)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .fallback(R.drawable.ic_default_avatar)
                        .thumbnail(0.4f)
                        .into(mAvatar);

                mName.setText(String.format("%s Invited you", mCurrentEvent.getParseUser("from").getUsername()));


                mInviteBar.setVisibility(View.VISIBLE);

            }

            if (action.isGoing()){

                // set button cancel
                mFirstBtn.setText(action.getMsg());
                mFirstBtn.setOnClickListener(view -> {
                    waiting =true;
                    progressDialog.show();
                    GeneralService.startActionCancelGoing(this.getApplicationContext(),mCurrentEvent.getObjectId(),mCurrentEvent.getClassName());

                });


            }else {
                // set button cancel
                mFirstBtn.setText(action.getMsg());
                mFirstBtn.setOnClickListener(view -> {

                });
                if (action.getSecondButton()){
                    // set second buton
                    mSecondBtn.setText(action.getMsg());
//                    mSecondBtn.setOnClickListener(view -> GeneralService.startActionSetGoing(this.getApplicationContext(),mCurrentEvent.getObjectId(),mCurrentEvent.getClassName()));
                }
            }

            if (waiting){
                progressDialog.dismiss();
            }

        },500);
    }



    private void setListeners(){
        mCmtBtn.setOnClickListener(view -> {
            DialogFragment newFragment = CommentActivityFragment.newInstance(dataItem.getParseId(),dataItem.getClassName(),true);
            newFragment.show(getSupportFragmentManager(), "comment");
        });

        mFavBtn.setOnClickListener(view -> {

        });

        mMoreButton.setButtonEventListener(new ButtonEventListener() {
            @Override
            public void onButtonClicked(int index) {

                switch (index){
                    case 4:

                        break;
                    case 2:
                        break;

                    case 3:
                        break;
                    case 1:
                        break;
                    default:
                        return;                }
            }

            @Override
            public void onExpand() {
//                showToast("onExpand");
            }

            @Override
            public void onCollapse() {
//                showToast("onCollapse");
            }
        });

    }

    private void checkFavs(){

    }

    private void setListener(AllAngleExpandableButton button) {


        mMoreButton.setOnClickListener(view -> openMedia());


    }


    private void initAdapter(){

        for (int i = 0; i < 5; i++) {
            ParseObject a=new ParseObject("");
            mMediaDatas.add(a);
        }
//
//        final List<ButtonData> buttonDatas = new ArrayList<>();
//        int[] drawable = {R.drawable.ic_add, R.drawable.ic_add, R.drawable.ic_add, R.drawable.ic_add,R.drawable.ic_add};
//        int[] color = {R.color.venu_blue, R.color.venu_red, R.color.venu_green, R.color.venu_yellow,R.color.venu_orange};
//
//        for (int i = 0; i < 5; i++) {
//            ButtonData buttonData;
//            if (i == 0) {
//                buttonData = ButtonData.buildIconButton(this, drawable[i], 15);
//            } else {
//                buttonData = ButtonData.buildIconButton(this, drawable[i], 0);
//            }
//            buttonData.setBackgroundColorId(this, color[i]);
//            buttonDatas.add(buttonData);
//        }


//        mGoingAdapter=new EventGoingAdapter(R.layout.item_ontap,mAttendeeDatas);
//        mGoingRcview.setLayoutManager(new LinearLayoutManager(this));
//        mGoingRcview.setHasFixedSize(true);
//        mGoingRcview.setAdapter(mGoingAdapter);

        mMediaAdapter=new EventMediaAdapter(R.layout.item_ontap,mMediaDatas);
        mMediaRcview.setLayoutManager(new LinearLayoutManager(this));
        mMediaRcview.setHasFixedSize(true);
        mMediaRcview.setAdapter(mMediaAdapter);


//        mGoingAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
//
//            NavigateTo.gotoGoingList(this,mCurrentEvent.getObjectId(),mCurrentEvent.getClassName(),getSupportFragmentManager());
//
//        });

        mMediaAdapter.setOnRecyclerViewItemClickListener((view, i) -> {

            SingletonDataSource.getInstance().setCurrentEvent(mCurrentEvent);
            NavigateTo.goToHashtagGallery(this,mCurrentEvent.getObjectId(),mCurrentEvent.getClassName(),mCurrentEvent.getString("hashTag"));

        });
    }

    private void openGoing(){

    }

    private void openMedia(){

    }

    private void openMap(){

    }

    private void openRequest(){
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            DialogFragment requestDialog = new RequestFragment();
            requestDialog.show(getSupportFragmentManager(),"request");
        }
    }

    private void displayTags(ParseObject data) {
        if (data.getString("features") == null) {
            mTagsContainer.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(data.getString("features"))) {
            mTagsContainer.setVisibility(View.GONE);
        } else {
            mTagsContainer.setVisibility(View.VISIBLE);
            mTags.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            String[] tagIds = data.getString("features").split(",");

            List<ModelEventFeature> tags = new ArrayList<>();
            for (String tagId : tagIds) {

                ModelEventFeature feature = new ModelEventFeature(tagId);

                if (ModelEventFeature.FEATURE_BUS.equals(tagId)) {
                    feature.setResourceId("");
                }else if(ModelEventFeature.FEATURE_FOOD_AND_DRINKS.equals(tagId)){
                    feature.setResourceId("");
                }else if (ModelEventFeature.FEATURE_WIFI.equals(tagId)){
                    feature.setResourceId("");
                }else if (ModelEventFeature.FEATURE_BUS.equals(tagId)){
                    feature.setResourceId("");
                }
                tags.add(feature);
            }

            if (tags.size() == 0) {
                mTagsContainer.setVisibility(View.GONE);
                return;
            }


            for (final ModelEventFeature feature : tags) {
//                ImageView chipView = (ImageView) inflater.inflate(
//                        R.layout.draw_layout, mTags, false);
////                chipView.setText(feature.getTitle());
////                chipView.setContentDescription(feature.getTitle());
////                chipView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
////                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
////                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        getActivity().startActivity(intent);
////                    }
////                });
//
//                mTags.addView(chipView);
            }
        }
    }


    private void displayPeople(List<ParseObject> data) {
        if (data.size()<0) {
            mTagsPeoplContainer.setVisibility(View.GONE);
            return;
        }

        else {
            mTagsPeoplContainer.setVisibility(View.VISIBLE);
            mTags.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

//            for (final ParseObject feature : data) {
//                ImageView chipView = (ImageView) inflater.inflate(
//                        R.layout.draw_layout, mTags, false);
////                chipView.setText(feature.getTitle());
////                chipView.setContentDescription(feature.getTitle());
////                chipView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
////                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
////                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        getActivity().startActivity(intent);
////                    }
////                });
//
//                mTags.addView(chipView);
//            }
        }
    }

    private void initload(){
        loaderManager.create(
                LoaderEventPage.loadMedia(mCurrentEvent.getObjectId(),mCurrentEvent.getClassName()),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            if (value.size()>0){
                                mMediaAdapter.setNewData(value);
                            }
//                                mAdapter.setNewData(value);
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

//        loaderManager.create(
//                LoaderEventPage.loadGoingFull(mCurrentEvent.getObjectId(),mCurrentEvent.getClassName()),
//                new RxLoaderObserver<List<ParseObject>>() {
//                    @Override
//                    public void onNext(List<ParseObject> value) {
//                        Timber.d("onnext");
//                        new Handler().postDelayed(() -> {
//                            if (value.size()>0){
//                                mGoingAdapter.setNewData(value);
//                            }
//                        },500);
//                    }
//                    @Override
//                    public void onStarted() {
//                        super.onStarted();
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.d("stated error %s",e.getMessage());
//                        super.onError(e);
//                    }
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("completed");
//                        super.onCompleted();
//                    }
//                }
//        ).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionMediaCheckIslike action) {
        if (action.status) {
            icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart_active);
            mFavBtn.setCompoundDrawables(icon, null, null, null);
        } else {
            icon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
            mFavBtn.setCompoundDrawables(icon, null, null, null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionEventGoing action) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionEvantPageBuy action) {

    }

    @Override
    public void onResume() {
        if (mapFragment !=null)
            mapFragment.onResume();
        super.onResume();
        if (NetUtils.hasInternetConnection(getApplicationContext())) {
//            initload();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class EventGoingAdapter
            extends BaseQuickAdapter<ParseObject> {

        public EventGoingAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final ParseObject request) {
            if (holder.getAdapterPosition()>3){
                holder.setText(R.id.going_count,"+"+request.getString("number"))
                        .setOnClickListener(R.id.going_bgrnd,new OnItemChildClickListener());
            }else {
//                Glide.with(mContext)
//                        .load(request.getString("avatar"))
//                        .crossFade()
//                        .placeholder(R.drawable.ic_default_avatar)
//                        .error(R.drawable.placeholder_error_media)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .centerCrop()
//                        .fallback(R.drawable.ic_default_avatar)
//                        .thumbnail(0.4f)
//                        .into(((RoundCornerImageView) holder.getView(R.id.going_imageview)));
            }
        }
    }

    private class EventMediaAdapter
            extends BaseQuickAdapter<ParseObject> {

        public EventMediaAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final ParseObject request) {

            if (holder.getAdapterPosition()>3){
                holder.setText(R.id.going_count,"+"+request.getString("number"))
                        .setOnClickListener(R.id.going_bgrnd,new OnItemChildClickListener());
            }else {
//                Glide.with(mContext)
//                        .load(request.getString("url"))
//                        .crossFade()
//                        .placeholder(R.drawable.ic_default_avatar)
//                        .error(R.drawable.placeholder_error_media)
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .centerCrop()
//                        .fallback(R.drawable.ic_default_avatar)
//                        .thumbnail(0.4f)
//                        .into(((RoundCornerImageView) holder.getView(R.id.going_imageview)));
            }
        }
    }
}
