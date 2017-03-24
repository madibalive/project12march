package com.example.madiba.venualpha.post.EventPost;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.ThemeSelectAdapter;
import com.example.madiba.venualpha.dailogs.GenerateDailogFragment;
import com.example.madiba.venualpha.dailogs.UserInviteDialog;
import com.example.madiba.venualpha.map.MapEnterLocationActivity;
import com.example.madiba.venualpha.models.ModelThemeView;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.ui.expandable.ExpandableLinearLayout;
import com.example.madiba.venualpha.util.ImageUitls;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;


public class AddDetailsFragment extends Fragment {
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


    private Button mDate, mTime,mLocationBtn,mMoreOptions;
    private EditText mDesc,mPrice, mTitle, mTag,mContact;
    private ImageView mImageBackground;
    private ImageButton mAddImage,addVideo;
    private CheckBox mIs18,mVerify,mBus,mWifi,mPhotoshot,mFnD;
    private AppCompatCheckBox mAllowFriendInvite;
    private StateButton onTapBtn,onInviteBtn,onSubmit;
    private Spinner mSpinnerEventType,mSpinnerEventCategory;
    private RecyclerView rcview;
    private ExpandableLinearLayout mMoreLayout,mExtraLayout;
    private LinearLayout mPricingLayout;
    private ProgressDialog progress;
    private ProgressBar mTagProgress;


    public ParseGeoPoint mCordinate;
    List<ModelThemeView> mData = new ArrayList<>();
    ThemeSelectAdapter mAdapter;
    private ParseObject event;
    private int mEventType=1;
    private String category;


    private Boolean flag=false;

    private Callback<Boolean> loadCallBack = (result, callable, e) -> {
        if (e == null) {
            if (result)
                mTag.setError("Similar Hashtag found");
        } else {
            // On error
        }

        mTagProgress.setVisibility(View.GONE);
    };

    private OnFragmentInteractionListener mListener;

    public AddDetailsFragment() {
        // Required empty public constructor
    }


    public static AddDetailsFragment newInstance(String param1, String param2) {
        return new AddDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_add_image, container, false);
        mSpinnerEventType = (Spinner) view.findViewById(R.id.spinner_type);
        mSpinnerEventCategory = (Spinner) view.findViewById(R.id.spinner_category);

        mTagProgress = (ProgressBar) view.findViewById(R.id.rotateloading);
        onTapBtn = (StateButton) view.findViewById(R.id.pe_request_btn);
        onInviteBtn = (StateButton) view.findViewById(R.id.pe_btn_invite);
        onSubmit = (StateButton) view.findViewById(R.id.submit);
        mLocationBtn = (Button) view.findViewById(R.id.pe_btn_location);
        mAllowFriendInvite=(AppCompatCheckBox) view.findViewById(R.id.isFriendInvitable);
        mIs18=(AppCompatCheckBox) view.findViewById(R.id.pe_check_is18);
        mVerify=(AppCompatCheckBox) view.findViewById(R.id.pe_isverify);
        mBus=(AppCompatCheckBox) view.findViewById(R.id.pe_bus);
        mPhotoshot=(AppCompatCheckBox) view.findViewById(R.id.pe_photoshop);
        mWifi=(AppCompatCheckBox) view.findViewById(R.id.pe_wifi);
        mFnD=(AppCompatCheckBox) view.findViewById(R.id.pe_fnd);

        mDate = (Button) view.findViewById(R.id.pe_date);
        mTime = (Button) view.findViewById(R.id.pe_time);
        mMoreOptions = (Button) view.findViewById(R.id.more_options);
        mAddImage = (ImageButton) view.findViewById(R.id.pe_add_image);

        mMoreLayout = (ExpandableLinearLayout) view.findViewById(R.id.more_expandableLayout);
        mExtraLayout = (ExpandableLinearLayout) view.findViewById(R.id.extra_options_expandableLayout);
        mPricingLayout = (LinearLayout) view.findViewById(R.id.pricing_layout);

        mTitle = (EditText) view.findViewById(R.id.pe_title);
        mTag = (EditText) view.findViewById(R.id.pe_tag);
        mContact = (EditText) view.findViewById(R.id.pe_contact);
        mDesc = (EditText) view.findViewById(R.id.pe_desc);
        mPrice = (EditText) view.findViewById(R.id.pe_pricing);
        mImageBackground = (ImageView) view.findViewById(R.id.image_view);
        rcview = (RecyclerView) view.findViewById(R.id.bare_recyclerview);
        return view;
    }


    private void displayToggle(Boolean aBoolean){

    }


    private void displayFeatures(){

    }

    private void displayAdvance(){

    }

    private void displayImage(){

    }

    private void initSpinner(){
        List<String> list = new ArrayList<String>();
        list.add("Type");
        list.add("General");
        list.add("Invite Only");
        list.add("Registration Required");
        list.add("Paid");

        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEventType.setAdapter(eventTypeAdapter);
        mSpinnerEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mEventType=i;
                if (mEventType == 2){
                    mPricingLayout.setVisibility(View.GONE);
                    mAllowFriendInvite.setVisibility(View.VISIBLE);
                    mExtraLayout.expand();

                }else if (mEventType == 4){
                    mPricingLayout.setVisibility(View.VISIBLE);
                    mAllowFriendInvite.setVisibility(View.GONE);
                    mExtraLayout.expand();
                }else {
                    mExtraLayout.collapse();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> eventCategoryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, DATA);
        eventCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEventCategory.setAdapter(eventCategoryAdapter);
        mSpinnerEventCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        rcview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rcview.setAdapter(mAdapter);

        mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
            mAdapter.setSelectedIndex(i);
            Timber.i("CHANGE THEME %S", mAdapter.getSelectedIndex());

        });

    }



    private void getHashTag(String term) {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            Timber.i("task started");
            TaskCheckTag taskLoad = new TaskCheckTag(term);
            Tasks.execute(taskLoad,loadCallBack);
        }

    }

    private void openLocation(){
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            startActivityForResult(new Intent(getActivity(), MapEnterLocationActivity.class),CODE_MAP_LOCATION);
        }

    }

    public void startInviteFrg() {
        DialogFragment newFragment = new UserInviteDialog();
        newFragment.show(getChildFragmentManager(), "post");
    }


    private void chooser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera","Generate Banner"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent galleryIntent = new Intent();
                            galleryIntent.setType("image/*");
                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                            break;
                        case 1:
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                            break;

                        case 2:
                           generate();
                            break;
                        default:
                            break;
                    }
                }).show();

    }



    private void generate(){
            DialogFragment newFragment = new GenerateDailogFragment();
        newFragment.show(getChildFragmentManager(), "generate");
    }












//   BACKGROUND PROCESS //////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void openCamera(){
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA)) {
            chooser();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_camera),
                    RC_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    try {
                        Timber.d("image %s",Uri.parse(data.getData().toString()));

//                        String url = ImageUitls.getPath(Uri.parse(data.getData().toString()),getActivity().getApplicationContext());
//                        Timber.d("path for image %s",url);
//                        updateImage(url);
                    }catch (Exception e){
                        Timber.e("erorr getting path for image %s",e.getMessage());
                    }
                } else {

                }
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    String realPath;
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = ImageUitls.getRealPathFromURI_BelowAPI11(getActivity().getApplicationContext(), data.getData());

                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = ImageUitls.getRealPathFromURI_API11to18(getActivity().getApplicationContext(), data.getData());
                    else
                        realPath = ImageUitls.getRealPathFromURI_API19(getActivity().getApplicationContext(), data.getData());

//                    updateImage(realPath);
                }catch (Exception e){
                    Timber.e("erorr getting path for image %s",e.getMessage());

                }
            }
        }

        if (requestCode == CODE_MAP_LOCATION) {
            if (resultCode == RESULT_OK) {
                try {
//                    LatLng latLng= data.getParcelableExtra(GlobalConstants.LATLONG);
//                    mCordinate = new ParseGeoPoint(latLng.latitude,latLng.longitude);
//                    mLocationName = data.getStringExtra(GlobalConstants.LOCATION_NAME);
//                    Timber.e("location name : %s , latalong: %s",mLocationName,mCordinate);
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
}
