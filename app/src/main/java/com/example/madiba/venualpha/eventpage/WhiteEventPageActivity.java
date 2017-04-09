package com.example.madiba.venualpha.eventpage;

import android.app.ProgressDialog;
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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
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
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCell;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCellHolder;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMediaItem;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.MdTrendMemory;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.ontap.RequestFragment;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.ui.SwitchTextView;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.ViewUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaychang.srv.SimpleRecyclerView;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

import static com.example.madiba.venualpha.R.id.event_action_btn;

public class WhiteEventPageActivity extends FragmentActivity {
    private static final String MAP_FRAGMENT_TAG = "map";

    RxLoaderManager loaderManager;
    private SimpleRecyclerView mMediaRecyler;
    private EventMediaAdapter mMediaAdapter;
    private Drawable mMapIcon;
    private TextView mTitle,mDesc,mName,mCmtBtn,mLocation,mInteractionName,
            mAttendeeCount,mMutualAttendee,mDay,mDate,mActionMsg,mActionType,mVerifiedBar;

    private SwitchTextView mFavBtn;

    private ProgressDialog progressDialog;
    private ImageView mMainImage,mInteractionClose;
    private ImageButton mFullScreenBtn,mMoreBtn;
    private PopupMenu popupMenu;
    private StateButton mActionBtn;
    private RoundCornerImageView mAvatar,mInteractionAvatar;
    private FloatingActionButton mUberBtn;
    private FrameLayout mapContainer;

    private Boolean waiting;
    private ParseObject currentEvent;
    private MdEventItem eventItem;
    private List<MdMediaItem> memeoryDatas=new ArrayList<>();
    private List<ParseUser> attendeeDatas=new ArrayList<>();

    private LinearLayout mAttendeesLayout,mInteractionLayout;
    private ViewGroup mAttendeesContainers;
    private LinearLayout mFeaturesLayout;
    private ViewGroup mFeaturesContainer;
    private SupportMapFragment mapFragment;


    public WhiteEventPageActivity() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(view -> this.finish());
        loaderManager = RxLoaderManagerCompat.get(this);
        initVariables();

        mActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = EventReactionFragment.newInstance();
                newFragment.show(getSupportFragmentManager(), "reaction");
            }
        });


        List<ParseUser> eventItems = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            eventItems.add(new ParseUser());
//        }


        displayAttendees(eventItems,"4 mutual friends",true);
        displayFeatures("stata");
        initAdapter();


        mFavBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked()==MotionEvent.ACTION_UP){

                    mFavBtn.toggleSwitch();
                }

                return true;
            }
        });


    }


    private void initVariables(){
        mTitle = (TextView) findViewById(R.id.event_title);
        mFavBtn = (SwitchTextView) findViewById(R.id.event_trackin);
        mCmtBtn = (TextView) findViewById(R.id.event_cmts);
        mAttendeeCount = (TextView) findViewById(R.id.attendees_count);
        mMutualAttendee = (TextView) findViewById(R.id.mutual_attendees);
        mName = (TextView) findViewById(R.id.org_name);
        mDay = (TextView) findViewById(R.id.event_day);
        mDate = (TextView) findViewById(R.id.event_date);
        mLocation = (TextView) findViewById(R.id.event_location_name);
        mDesc = (TextView) findViewById(R.id.event_desc);
        mMainImage= (ImageView) findViewById(R.id.main_image);
        mMediaRecyler = (SimpleRecyclerView) findViewById(R.id.recyclerView);
        mActionBtn = (StateButton) findViewById(event_action_btn);
        mMoreBtn = (ImageButton) findViewById(R.id.event_more_btn);
        mFullScreenBtn = (ImageButton) findViewById(R.id.full_screen);

        mFeaturesLayout = (LinearLayout) findViewById(R.id.feature_layout);
        mFeaturesContainer = (ViewGroup) findViewById(R.id.feature_container);
        mAttendeesLayout = (LinearLayout) findViewById(R.id.attendees_layout);
        mAttendeesContainers = (ViewGroup) findViewById(R.id.attendees_container);
        mapContainer = (FrameLayout) findViewById(R.id.container);
    }

    private void setMainImage(String url){

        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(mMainImage);

        mFullScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/5/2017 toogle fullscreen
            }
        });
    }



    private void displayToolbar(){

        mTitle.setText("");
        mCmtBtn.setText("");
        mFavBtn.setText("");

        mCmtBtn.setOnClickListener(view -> {
//            DialogFragment newFragment = CommentDialogFragment.newInstance(dataItem.getParseId(),dataItem.getClassName(),true);
//            newFragment.show(getSupportFragmentManager(), "comment");
        });

        mFavBtn.setOnClickListener(view -> {

        });

        mMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(WhiteEventPageActivity.this, view);
                popupMenu.getMenu().add(Menu.NONE, 1, 1, "share");
                popupMenu.getMenu().add(Menu.NONE, 2, 2, "share");
                popupMenu.getMenu().add(Menu.NONE, 3, 3, "share");
                popupMenu.setOnMenuItemClickListener(item -> {

                    int i = item.getItemId();

                    return false;

                });
            }
        });

        mActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showActionView();
            }
        });
    }

    private void showActionView() {

        // TODO: 3/5/2017 add actionview
    }


    private void displayOrganiser(MdUserItem userItem){

        if (userItem ==null){
            return;
        }
        //avatar
        Glide.with(this)
                .load(currentEvent.getParseUser("from").getParseFile("avatar"))
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(mAvatar);
        mName.setText("");

        // TODO: 3/5/2017 goto profile
    }

    private void displayAttendees(List<ParseUser> attendeeDatas,@Nullable String mutualState,Boolean isMutualExist){

//        if (isMutualExist){
//            mMutualAttendee.setText(mutualState);
//        }
//        if (attendeeDatas.size()<0) {
//            mAttendeesContainers.setVisibility(View.GONE);
//            return;
//        }
//
//        else {
            mAttendeesContainers.setVisibility(View.VISIBLE);
            mAttendeesLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

//            for (final ParseObject feature : attendeeDatas) {
        for (int i = 0; i < 5; i++) {

            RoundCornerImageView chipView = (RoundCornerImageView) inflater.inflate(
                    R.layout.item_attende, mAttendeesLayout, false);
            ViewUtils.setMargins(chipView,0,4,4,4);

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
            mAttendeesLayout.addView(chipView);
            }
        //}

    }
    private void displayFeatures(@Nullable String data) {

        if (TextUtils.isEmpty(data)) {
            mFeaturesContainer.setVisibility(View.GONE);
        } else {
            mFeaturesContainer.setVisibility(View.VISIBLE);
            mFeaturesLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
//            String[] mFeatures = data.split(",");
//
//            List<ModelEventFeature> tags = new ArrayList<>();
//            for (String featureItem : mFeatures) {
//
//                ModelEventFeature feature = new ModelEventFeature(featureItem);
//
//                if (ModelEventFeature.FEATURE_BUS.equals(featureItem)) {
//                    feature.setResourceId("");
//                }else if(ModelEventFeature.FEATURE_FOOD_AND_DRINKS.equals(featureItem)){
//                    feature.setResourceId("");
//                }else if (ModelEventFeature.FEATURE_WIFI.equals(featureItem)){
//                    feature.setResourceId("");
//                }else if (ModelEventFeature.FEATURE_BUS.equals(featureItem)){
//                    feature.setResourceId("");
//                }
//                tags.add(feature);
//            }
//
//            if (tags.size() == 0) {
//                mFeaturesLayout.setVisibility(View.GONE);
//                return;
//            }


            for (int i = 0; i < 5; i++) {
                ImageView chipView = (ImageView) inflater.inflate(
                        R.layout.item_features, mFeaturesLayout, false);
                mFeaturesLayout.addView(chipView);

            }


//            for (final ModelEventFeature feature : tags) {
//                ImageView chipView = (ImageView) inflater.inflate(
//                        R.layout.item_features, mFeaturesLayout, false);
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
//                mFeaturesLayout.addView(chipView);
//            }
        }
    }


    private void displayInfo(String day,String date){
        mDay.setText(day);
        mDate.setText(date);
    }

    private void isMapAvailabe(LatLng coordinates) {
        if (coordinates== null) {
            return;
        }

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

//            getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
        mapFragment.getMapAsync(googleMap -> {
            LatLng latLng = new LatLng(coordinates.latitude, coordinates.longitude);
            CameraPosition selfLoc = CameraPosition.builder()
                    .target(latLng)
                    .zoom(13)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(selfLoc));

            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .snippet("SAMPLE LOCATION")
//                        .mMapIcon(BitmapDescriptorFactory
//                                .fromResource(R.drawable.ic_panorama))
                    .title("Event Location!"));
        });


        mUberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    private void displayText(TextView textView,String desc){
        textView.setText(desc);
    }


    private void initAdapter(){
//        mMediaAdapter = new EventMediaAdapter(R.layout.item_eventpage_media,memoryItems);
//        mMediaRcview.setLayoutManager(new LinearLayoutManager(this));
//        mMediaRcview.setAdapter(mMediaAdapter);

        List<TrendMemoryCell> memoryCells = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            memoryCells.add(new TrendMemoryCell(new MdMemoryItem()));
        }

        TrendMemoryCellHolder memoryCellHolder = new TrendMemoryCellHolder(new MdTrendMemory(memoryCells));
        mMediaRecyler.addCells(memoryCells);

    }

    private void openGoing(){

    }

    private void display_Interation(String name,String url){
        if (name==null || url==null)
            return;

        new Handler().postDelayed(() -> {
            mInteractionLayout.setVisibility(View.VISIBLE);

            Glide.with(WhiteEventPageActivity.this)
                    .load(currentEvent.getParseUser("from").getParseFile("avatar"))
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into(mInteractionAvatar);

            mInteractionName.setText(String.format("%s Invited you", currentEvent.getParseUser("from").getUsername()));
            mInteractionClose.setOnClickListener(view -> mInteractionLayout.setVisibility(View.GONE));
        },500);


    }

    private void showPopup(View view,String infoMsg) {
        popupMenu = new PopupMenu(WhiteEventPageActivity.this, view);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, infoMsg);
        popupMenu.show();
    }


    private void openRequest(){
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            DialogFragment requestDialog = new RequestFragment();
            requestDialog.show(getSupportFragmentManager(),"request");
        }
    }

    private void initload(){
        loaderManager.create(
                LoaderEventPage.loadEventGallery(currentEvent.getObjectId()),
                new RxLoaderObserver<List<MdMemoryItem>>() {
                    @Override
                    public void onNext(List<MdMemoryItem> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            if (value.size()>0){
                                mMediaAdapter.setNewData(value);
                            }
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

        loaderManager.create(
                LoaderEventPage.loadGoingFull(currentEvent.getObjectId(),currentEvent.getClassName(),1),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            if (value.size()>0){
                            }
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
                        Timber.d("completed");
                        super.onCompleted();
                    }
                }
        ).start();
    }





    private class EventMediaAdapter
            extends BaseQuickAdapter<MdMemoryItem> {

        public EventMediaAdapter(int layoutResId, List<MdMemoryItem> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final MdMemoryItem request) {

            if (holder.getAdapterPosition()>3){

                holder.itemView.setOnClickListener(view -> {

                    SingletonDataSource.getInstance().setCurrentEvent(currentEvent);
                    NavigateTo.goToHashtagGallery(mContext,currentEvent.getObjectId(),currentEvent.getClassName(),currentEvent.getString("hashTag"));

                });
            }else {
                holder.setVisible(R.id.overlay,true)
                        .setVisible(R.id.views,true)
                        .setText(R.id.views,"");
            }


                Glide.with(mContext)
                        .load("")
                        .crossFade()
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.placeholder_error_media)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .fallback(R.drawable.ic_default_avatar)
                        .thumbnail(0.4f)
                        .into(((RoundCornerImageView) holder.getView(R.id.image_view)));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionMediaCheckIslike action) {
        if (action.status) {
//            mFavBtn.setCompoundDrawables(mMapIcon, null, null, null);
        } else {
            Drawable Icon= ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
            mFavBtn.setCompoundDrawables(Icon, null, null, null);
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

}
