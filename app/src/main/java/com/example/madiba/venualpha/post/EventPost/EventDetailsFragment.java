package com.example.madiba.venualpha.post.EventPost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.madiba.venualpha.Actions.ActionDate;
import com.example.madiba.venualpha.Actions.ActionInvitesList;
import com.example.madiba.venualpha.Actions.ActionTime;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.adapter.ThemeSelectAdapter;
import com.example.madiba.venualpha.map.MapEnterLocationActivity;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.MdPostEventItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.models.ModelEventFeature;
import com.example.madiba.venualpha.models.ModelThemeView;
import com.example.madiba.venualpha.post.AddUsersDialog;
import com.example.madiba.venualpha.services.ServiceUplouder;
import com.example.madiba.venualpha.ui.expandable.ExpandableLinearLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.ImageUitls;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.SelectDateFragment;
import com.example.madiba.venualpha.util.TimePickerFragment;
import com.example.madiba.venualpha.util.TimeUitls;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceRecyclerView;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import it.sephiroth.android.library.picasso.MemoryPolicy;
import it.sephiroth.android.library.picasso.Picasso;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.example.madiba.venualpha.util.ImageUitls.createImageFile;

public class EventDetailsFragment extends Fragment implements View.OnClickListener {

    public static final List<String> DATA = Arrays.asList("Category","Entertainment", "Party", "Fitness",
            "Gospel", "Social", "Promotion", "VenuChallange");

    private static final int CODE_MAP_LOCATION=100;
    private static final int TYPE_GENERAL=1;
    private static final int TYPE_PAID=2;
    private static final int TYPE_INVITE=3;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 300;
    private static final int GALLERY_PICKER = 400;
    private static final int RC_CAMERA_PERM = 123;
    static final int REQ_CODE_CAMERA = 1001;
    static final int REQ_CODE_VIDEO = 2001;

    private MultiChoiceRecyclerView mFeatureRcview;
    private ProgressDialog progressDialog;
    FeaturesMultipleAdapter featureAdapter;

    private RoundCornerImageView imageView;
    private LinearLayout tagHolder;
    private ProgressBar mHashTagProgress;
    private Spinner spinnerType;
    private Spinner spinnerCategory;
    private ExpandableLinearLayout extraOptionsExpandableLayout;
    private LinearLayout pricingLayout;
    private AppCompatCheckBox isFriendInvitable;
    private ExpandableLinearLayout moreExpandableLayout;
    private RecyclerView themeAdapter;
    private AppCompatCheckBox isverify;
    private StateButton btnInvite;
    private StateButton requestBtn;
    private StateButton submit;
    private EditText mTitle;
    private EditText mHashTag;
    private EditText mDesc;
    private EditText mContact;
    private EditText mPricing;
    private Button mDate;
    private Button mTime;
    private Button mAddress;



    public LatLng mCordinate;
    List<ModelThemeView> mData = new ArrayList<>();
    List<ModelEventFeature> mFeatureData;
    List<MdUserItem> mInviteList= new ArrayList<>();
    ThemeSelectAdapter mAdapter;
    private ParseObject event;
    private int mEventType=1;
    private String category;
    private String address;
    private String path;
    private File imageFile;
    private Uri imageUri;
    private MdPostEventItem postEventItem;
    private Date date,time;
    private Boolean flag=false;

    private Callback<Boolean> loadCallBack = (result, callable, e) -> {
        if (e == null) {
            if (result) {
                mHashTagProgress.setVisibility(View.GONE);
                mHashTag.setError("Similar Hashtag found");
            }else
                mHashTagProgress.setVisibility(View.GONE);
        } else {
            // On error
            Log.e("POST EVEENT", "error: "+e.getMessage() );
        }

        mHashTagProgress.setVisibility(View.GONE);
    };

    private OnFragmentInteractionListener mListener;


    public EventDetailsFragment() {
        // Required empty public constructor
    }


    public static EventDetailsFragment newInstance() {
        return new EventDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postEventItem = new MdPostEventItem();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_event_details, container, false);
        view.findViewById(R.id.closeBtn).setOnClickListener(this);
        view.findViewById(R.id.advanced).setOnClickListener(this);
        view.findViewById(R.id.openCamera).setOnClickListener(this);
        view.findViewById(R.id.openVideo).setOnClickListener(this);

        view.findViewById(R.id.btn_invite).setOnClickListener(this);
        view.findViewById(R.id.request_btn).setOnClickListener(this);
        view.findViewById(R.id.submit).setOnClickListener(this);

        mDate = (Button) view.findViewById(R.id.date);
        mDate.setOnClickListener(this);

        mTime = (Button) view.findViewById(R.id.time);
        mTime.setOnClickListener(this);

        mAddress = (Button) view.findViewById(R.id.location);
        mAddress.setOnClickListener(this);

        mFeatureRcview = (MultiChoiceRecyclerView) view.findViewById(R.id.recyclerView);
        imageView = (RoundCornerImageView) view.findViewById(R.id.image_view);
        tagHolder = (LinearLayout) view.findViewById(R.id.tag_holder);
        mHashTagProgress = (ProgressBar) view.findViewById(R.id.progressBar2);
        spinnerType = (Spinner) view.findViewById(R.id.spinner_type);
        spinnerCategory = (Spinner) view.findViewById(R.id.spinner_category);
        extraOptionsExpandableLayout = (ExpandableLinearLayout) view.findViewById(R.id.extra_options_expandableLayout);
        pricingLayout = (LinearLayout) view.findViewById(R.id.pricing_layout);
        isFriendInvitable = (AppCompatCheckBox) view.findViewById(R.id.isFriendInvitable);
        moreExpandableLayout = (ExpandableLinearLayout) view.findViewById(R.id.more_expandableLayout);
        themeAdapter = (RecyclerView) view.findViewById(R.id.bare_recyclerview);
        isverify = (AppCompatCheckBox) view.findViewById(R.id.isverify);
        mTitle=(EditText) view.findViewById(R.id.title);
        mHashTag=(EditText) view.findViewById(R.id.tag);
        mDesc=(EditText) view.findViewById(R.id.desc);
        mContact=(EditText) view.findViewById(R.id.contact);
        mPricing=(EditText) view.findViewById(R.id.pricing);
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub

        if (path !=null)
            outState.putString("url", path);
        if (mCordinate !=null) {
            outState.putDouble("lat", mCordinate.latitude);
            outState.putDouble("long", mCordinate.longitude);
        }
        outState.putInt("theme",mAdapter.getSelectedIndex());
        outState.putParcelable("inviteList", Parcels.wrap(mInviteList));
        if (date !=null)
            outState.putLong("date",date.getTime());
        if (time !=null)
            outState.putLong("time",time.getTime());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("url")) {
                path = savedInstanceState.getString("url");
                displayImage(path);
            }
            if (savedInstanceState.containsKey("lat") && savedInstanceState.containsKey("long")) {
                if (savedInstanceState.getDouble("lat")>0 && savedInstanceState.getDouble("long")>0)
                    mCordinate = new LatLng(savedInstanceState.getDouble("lat"), savedInstanceState.getDouble("long"));
            }
            if (savedInstanceState.containsKey("theme"))
                mAdapter.setSelectedIndex(savedInstanceState.getInt("theme"));

            if (savedInstanceState.containsKey("isInviteNull"))
                mInviteList = savedInstanceState.getParcelable("isInviteNull");

            if (savedInstanceState.containsKey("time") ) {
                time.setTime(savedInstanceState.getLong("time", -1));
                mTime.setText( DateFormat.getTimeFormat(getActivity().getApplicationContext()).format(time));
            }

            if (savedInstanceState.containsKey("date") ){
                date.setTime(savedInstanceState.getLong("date", -1));
                mDate.setText(DateFormat.getMediumDateFormat(getActivity().getApplicationContext()).format(date));

            }


        }

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSpinner();
        setUpGridView();
        displayFeatures();

        mHashTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 5) {
                    if (NetUtils.hasInternetConnection(getActivity().getApplicationContext()))
                        new Handler().removeCallbacksAndMessages(null);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHashTag.setError(null);
                                mHashTagProgress.setVisibility(View.VISIBLE);
                                checkHashtag(mHashTag.getText().toString());
                            }
                        },4000);

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closeBtn:
                //TODO implement
                break;
            case R.id.openCamera:
                chooser();
                break;
            case R.id.openVideo:
                reqeuestVideo();
                break;
            case R.id.date:
                showDate();
                break;
            case R.id.time:
                showTime();
                break;
            case R.id.location:
                openLocation();
                break;
            case R.id.advanced:
                moreExpandableLayout.toggle();
                break;
            case R.id.btn_invite:
                addUsers();
                break;
            case R.id.request_btn:
                //TODO implement
                break;
            case R.id.submit:
                //TODO implement
                attemptLogin();
                break;
        }
    }


    private void attemptLogin() {
        // Reset errors.
        mTitle.setError(null);
        mHashTag.setError(null);
        mDesc.setError(null);
        mContact.setError(null);

        // Store values at the time of the login attempt.
        String title = mTitle.getText().toString();
        String hashtag = mHashTag.getText().toString();
        String desc = mDesc.getText().toString();

        View focusView = null;
        // Check for a valid hashtag, if the user entered one.
        if (TextUtils.isEmpty(title)) {
            mTitle.setError("Username is empty");
            if (focusView==null) focusView = mTitle;
            focusView.requestFocus();
            return;
        } else if (!isUserNameValid(title)) {
            mTitle.setError("min lenght is 5");
            if (focusView==null) focusView = mTitle;
            focusView.requestFocus();
            return;
        }
        // Check for a valid title address.
        if (TextUtils.isEmpty(hashtag)) {
            mHashTag.setError("Email is empty");
            if (focusView==null) focusView = mHashTag;
            focusView.requestFocus();
            return;
        } else if (!isPasswordValid(hashtag)) {
            mHashTag.setError("Min lenght is 8 ");
            if (focusView==null) focusView = mHashTag;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(desc)) {
            mDesc.setError("Phone Contact is empty");
            if (focusView==null) focusView = mDesc;
            focusView.requestFocus();
            return;
        } else if (!isPasswordValid(desc)) {
            mDesc.setError("Must be more 10 number");
            mDesc.requestFocus();
            return;
        }

        if (date ==null || time ==null){
            Toast.makeText(getActivity(),"Date or Time Not Set",Toast.LENGTH_LONG).show();
            return;
        }

        if (mCordinate ==null ){
            Toast.makeText(getActivity(),"Location  Not Set",Toast.LENGTH_LONG).show();
            return;
        }


        if ( category ==null){
            Toast.makeText(getActivity(),"Event Category Not Set",Toast.LENGTH_LONG).show();
            return;
        }

        if ( this.path ==null){
            Toast.makeText(getActivity(),"Image Not Set",Toast.LENGTH_LONG).show();
            return;
        }


        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            Timber.e("staring parse login ");
//            progressDialog = ProgressDialog.show(getActivity(), null,
//                    getResources().getString(R.string.progress_connecting), true);
            createEvent(2);
        }




    }


    private void createEvent(int mode){
        postEventItem.setTitle(mTitle.getText().toString());
        postEventItem.setHashtag(mHashTag.getText().toString()+ TimeUitls.Format_dayOnly(date));
        postEventItem.setDesc(mDesc.getText().toString());
        postEventItem.setDate(date);
        postEventItem.setLocation(address);
        postEventItem.setCordinate(mCordinate);
        postEventItem.setTheme(mAdapter.getSelectedIndex());
        postEventItem.setTime(time);
        postEventItem.setCategory(category);
        postEventItem.setType(mEventType);
        postEventItem.setInvites(mInviteList);
        postEventItem.setPath(this.path);

        if (mFeatureRcview.getSelectedItemCount()>0){
            StringBuilder features =new StringBuilder();
            for (int i = 0; i < mFeatureRcview.getSelectedItemCount(); i++) {
                if (mFeatureRcview.getSelectedItemList().contains(1))
                    features.append(mFeatureData.get(i).getTitle()).append(',');
            }
            postEventItem.setFeatures(features.toString());
        }

        if (!TextUtils.isEmpty(mContact.getText()))
            postEventItem.setContact(mContact.getText().toString());

        ServiceUplouder.uploudEvent(getActivity().getApplicationContext(),postEventItem);
        getActivity().finish();
    }



    //    ////////////////////////////////////////////////////////////////
//    INITIALISING  PROCESSING
//    ////////////////////////////////////////////////////////////////

    private void initSpinner(){
        List<String> list = new ArrayList<String>();
        list.add("Type");
        list.add("General");
        list.add("Invite Only");
        list.add("Registration Required");
        list.add("Paid");

        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(eventTypeAdapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mEventType=i;
                if (mEventType == 2){
                    pricingLayout.setVisibility(View.GONE);
                    isFriendInvitable.setVisibility(View.VISIBLE);
                    extraOptionsExpandableLayout.expand();

                }else if (mEventType == 4){
                    pricingLayout.setVisibility(View.VISIBLE);
                    isFriendInvitable.setVisibility(View.GONE);
                    extraOptionsExpandableLayout.expand();
                }else {
                    extraOptionsExpandableLayout.collapse();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> eventCategoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, DATA);
        eventCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(eventCategoryAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category= adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpGridView() {

        ModelThemeView a = new ModelThemeView(R.color.venu_blue,1);
        ModelThemeView b = new ModelThemeView(R.color.venu_yellow,1);
        ModelThemeView c = new ModelThemeView(R.color.venu_red,1);

        mData.add(a);
        mData.add(b);
        mData.add(c);
        mAdapter = new ThemeSelectAdapter(R.layout.item_theme, mData);
        themeAdapter.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        themeAdapter.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            mAdapter.setSelectedIndex(i);
            Timber.i("CHANGE THEME %S", mAdapter.getSelectedIndex());

        });

    }

    private void addUsers(){
        AddUsersDialog.newInstance().show(getChildFragmentManager(), "dialog");
    }

    private void showTime(){
        new TimePickerFragment().show(getChildFragmentManager(), "dialog");
    }

    private void showDate(){
        new  SelectDateFragment().show(getChildFragmentManager(), "dialog");
    }

    private void displayToggle(Boolean aBoolean){
    }

    private void displayFeatures(){
        mFeatureData = new ArrayList<>();
        mFeatureData.add(new ModelEventFeature("+18 only"));
        mFeatureData.add(new ModelEventFeature("Free for Ladies"));
        mFeatureData.add(new ModelEventFeature("PhotoBooth"));
        mFeatureData.add(new ModelEventFeature("Food & drinks"));
        mFeatureData.add(new ModelEventFeature("Bus"));
        mFeatureData.add(new ModelEventFeature("Wifi"));


        featureAdapter=new FeaturesMultipleAdapter(mFeatureData,getActivity());
        mFeatureRcview.setRecyclerColumnNumber(3);

        mFeatureRcview.setHasFixedSize(true);
        mFeatureRcview.setAdapter(featureAdapter);
        mFeatureRcview.setSingleClickMode(true);
    }

    private void reqeuestVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),REQ_CODE_VIDEO);

    }

    private void requestGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), CAMERA_REQUEST_CODE);
    }
    private void requestCamera() {

        imageFile = createImageFile();
        imageUri = Uri.fromFile(imageFile);

        if (imageUri != null) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) == null) {
                Toast.makeText(getActivity(), "This Application do not have Camera Application", Toast.LENGTH_LONG).show();
                return;
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, REQ_CODE_CAMERA);
        }
    }

//    ////////////////////////////////////////////////////////////////
//    BACKGROUND PROCESSING
//    ////////////////////////////////////////////////////////////////

    private void chooser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera","Generate Banner"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            requestGallery();
                            break;
                        case 1:
                            requestCamera();
                            break;

                        case 2:
                            generate();
                            break;
                        default:
                            break;
                    }
                }).show();

    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void openCamera(){
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA)) {
            chooser();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera),
                    RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    public void startInviteFrg() {
//        DialogFragment newFragment = new UserInviteDialog();
//        newFragment.show(getChildFragmentManager(), "post");
    }

    private void openLocation(){
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            startActivityForResult(new Intent(getActivity(), MapEnterLocationActivity.class),CODE_MAP_LOCATION);
        }

    }

    private void generate(){
        DialogFragment newFragment = new GenerateDailog();
        newFragment.show(getChildFragmentManager(), "generate");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    try {
                        Timber.d("image %s", Uri.parse(data.getData().toString()));
                        displayImage(processImage(data.getData()));

                    }catch (Exception e){
                        Timber.e("erorr getting path for image %s",e.getMessage());
                    }
                } else {

                }
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    displayImage(processImage(data.getData()));
                }
            }
        }

        if (requestCode == CODE_MAP_LOCATION) {
            if (resultCode == RESULT_OK) {
                try {
                    mCordinate= data.getParcelableExtra(GlobalConstants.LATLONG);
                    address = data.getStringExtra(GlobalConstants.LOCATION_NAME);
                    mAddress.setText(address);
                }catch (Exception e){
                    Timber.e("erorr %s",e.getMessage());
                }
            }
        }



    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(int  page);
    }


    private String processImage(Uri imageUri){
        try {
            String realPath;
            if (Build.VERSION.SDK_INT < 11)
                realPath = ImageUitls.getRealPathFromURI_BelowAPI11(getActivity().getApplicationContext(),imageUri);

            else if (Build.VERSION.SDK_INT < 19)
                realPath = ImageUitls.getRealPathFromURI_API11to18(getActivity().getApplicationContext(),imageUri);
            else
                realPath = ImageUitls.getRealPathFromURI_API19(getActivity().getApplicationContext(),imageUri);

            return realPath;

        }catch (Exception e){
            Timber.e("erorr getting path for image %s",e.getMessage());
            return null;
        }
    }

    private void displayImage(String path){
        this.path = path;

        if (this.path != null) {
            Picasso.with(getContext())
                    .load(this.path)
                    .resize(400, 400)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.placeholder_media)
                    .error(R.drawable.placeholder_error_media)
                    .noFade()
                    .centerCrop()
                    .into(imageView);
        }

    }


    private void checkHashtag(String term) {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            Timber.i("task started");
            TaskCheckTag taskLoad = new TaskCheckTag(term);
            Tasks.execute(taskLoad,loadCallBack);
        }

    }

    private boolean isEmailValid(String title) {
        return title.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    private boolean isPhoneValid(String number) {
        return number.length() > 9;
    }

    private boolean isUserNameValid(String username) {
        return username.length() > 4;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionInvitesList action) {
        Log.e("EVENT", "InviteList return "+action.userItems.size());
        mInviteList = action.userItems;


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionDate action) {
        Timber.e("Date return %s",action.date);
        date = action.date;
        mDate.setText(DateFormat.getMediumDateFormat(getActivity().getApplicationContext()).format(date));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionTime action) {
        Timber.e("Time return %s",action.date);
        time = action.date;
        mTime.setText( DateFormat.getTimeFormat(getActivity().getApplicationContext()).format(time));

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

}
