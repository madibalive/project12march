package com.example.madiba.venualpha.onboard;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.internal.headless.utils.MegaPixels;
import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.Actions.ActionDate;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.map.TaskGetLocationByName;
import com.example.madiba.venualpha.models.VenuFile;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.FlipUtil;
import com.example.madiba.venualpha.util.ImageUitls;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.event.LoggingEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import it.sephiroth.android.library.picasso.MemoryPolicy;
import it.sephiroth.android.library.picasso.Picasso;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.example.madiba.venualpha.util.ImageUitls.createImageFile;
import static com.facebook.FacebookSdk.getApplicationContext;

public class UserDetailFragment extends Fragment {
    public static final int index =2;

    public static final List<String> DATA = Arrays.asList("Male","Female");
    StateButton proceed;
    private ProgressDialog progress;
    private EditText mHighSchool;
    private Spinner genderSpinner;
    private Button submit;
    private Date date;
    private RoundCornerImageView mAvatar;
    private int gender;
    private LatLng latLng;
    private PopupMenu popupMenu ;
    private ImageButton mSearchBtn;
    private FloatingActionButton fab;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 300;
    static final int REQ_CODE_CSDK_IMAGE_EDITOR = 3001;

    private File imageFile;
    private Uri imageUri;
    private OnFragmentInteractionListener mListener;

    public UserDetailFragment() {
    }

    public static UserDetailFragment newInstance() {
        return new UserDetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_onboard_add_details, container, false);

        mHighSchool = (EditText) view.findViewById(R.id.university);
        mAvatar = (RoundCornerImageView) view.findViewById(R.id.avatar);
        mSearchBtn = (ImageButton) view.findViewById(R.id.search_btn);
        genderSpinner = (Spinner) view.findViewById(R.id.gender_spinner);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        submit = (Button) view.findViewById(R.id.go);
        return view;
    }




    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mHighSchool.getText())){
                    onMapSearch();
                }
            }
        });

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, DATA);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gender =i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });


        fab.setOnClickListener(view2 -> chooser());

    }


    private void proceed() {
        if (ParseUser.getCurrentUser().getParseFile("avatarMedium") != null){
            return;
        }
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            progress = ProgressDialog.show(getActivity(), null,
                    getResources().getString(R.string.progress_connecting), true);
            updateUser();
        }


    }

    private void updateUser() {
        ParseUser user =ParseUser.getCurrentUser();

        if (latLng != null)
            user.put("place",new ParseGeoPoint(latLng.latitude,latLng.longitude));

        if (latLng != null)
            user.put("gender",gender);

        try {
            user.save();
            progress.dismiss();
            onButtonPressed(2);
        } catch (ParseException e) {
            progress.dismiss();
            e.printStackTrace();
        }



    }

    private void chooser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Gallery", "Camera"},
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            requestGallery();
                            break;
                        case 1:
                            requestCamera();
                            break;

                        default:
                            break;
                    }
                }).show();

    }


    private void requestGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), GALLERY_REQUEST_CODE);
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
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }



    private Callback<List<Address>> locationNameCallack = new Callback<List<Address>>() {
        @Override
        public void onFinish(List<Address> result, Callable callable, Throwable e) {
            if (e == null) {
                Timber.d("got data");
                if (result.size()>0){
                    showPopup(mHighSchool,result);
                }
                // On success
            } else {
            }
        }

    };

    public void onMapSearch() {
        String location = mHighSchool.getText().toString();

        if (NetUtils.hasInternetConnection(getApplicationContext())){
            TaskGetLocationByName taskLoad = new TaskGetLocationByName(location,getApplicationContext());
            Tasks.execute(taskLoad,locationNameCallack);
        }
    }

    private void showPopup(View view,List<Address> list) {
        popupMenu = new PopupMenu(getActivity(), view);
        for (int i = 0; i < list.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, list.get(i).getAddressLine(0)+", "+list.get(i).getAddressLine(1));
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int i = item.getItemId();
            Address address = list.get(i);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Log.e("ONboard", "showPopup: "+address.toString() );
            mHighSchool.setText(list.get(i).getAddressLine(0)+", "+list.get(i).getAddressLine(1));
            return false;
        });

        popupMenu.show();
    }



    private boolean isPhoneValid(String number) {
        return number.length() > 9;
    }

    private boolean isUserNameValid(String username) {
        return username.length() > 4;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_CSDK_IMAGE_EDITOR) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    addAvatar(imageUri);
                }catch (Exception e){
                    Log.e("erorr" ,"getting path for images"+e.getMessage());
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    try {
                        Timber.d("image %s", Uri.parse(data.getData().toString()));
                        requestEditor(FlipUtil.processImageUri(getActivity().getApplicationContext(),data.getData()));
//                        displayImage(FlipUtil.processImageUri(getActivity().getApplicationContext(),imageUri));

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

                    requestEditor(FlipUtil.processImageUri(getActivity().getApplicationContext(),data.getData()));
                }
            }
        }

    }

    private void requestEditor(String venuFile) {
        try {


            imageFile = createImageFile();
            imageUri = Uri.fromFile(imageFile);

            if (imageUri != null) {

            }
            AdobeImageIntent.ForceCrop forceCropSettings = new AdobeImageIntent.ForceCrop.Builder()
                    .addCropRatio("Square", "1:1")
                    .addCropRatio("3:2", "3:2")
                    .withSelectedIndex(0)
                    .canInvert(true)
                    .build();

            Intent imageEditorIntent = new AdobeImageIntent.Builder(getActivity())
                    .setData(Uri.parse(venuFile))
                    .withOutputFormat(Bitmap.CompressFormat.JPEG) // output format
                    .withOutputSize(MegaPixels.Mp5) // output size
                    .withVibrationEnabled(true)
                    .withOutput(imageUri)
                    .withOutputQuality(90) // output quality
                    .forceCrop(forceCropSettings)
                    .build();

            startActivityForResult(imageEditorIntent, REQ_CODE_CSDK_IMAGE_EDITOR);
        } catch (Exception e) {
        }
    }

    private void displayImage(String path){

        if (path != null) {
            Picasso.with(getContext())
                    .load(path)
                    .resize(400, 400)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .placeholder(R.drawable.placeholder_media)
                    .error(R.drawable.placeholder_error_media)
                    .noFade()
                    .centerCrop()
                    .into(mAvatar);
        }

    }

    private void addAvatar(Uri uri){

        progress = ProgressDialog.show(getActivity(), null,
                getResources().getString(R.string.progress_connecting), true);
        Bitmap bitmap;
        try {

            byte[] originalBytes;
            byte[] compressBytes;
            //get bitmap
              bitmap =BitmapFactory.decodeFile(uri.getPath());

            if (bitmap != null) {
                originalBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 100);
                compressBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 50);
                ParseFile origImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(), originalBytes);
                ParseFile cmprImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(), compressBytes);
                cmprImgs.save();
                origImgs.save();

                ParseUser.getCurrentUser().put("avatar50",cmprImgs);
                ParseUser.getCurrentUser().put("avatar100",origImgs);
                ParseUser.getCurrentUser().saveInBackground(e -> {
                    if (e==null){
                        progress.dismiss();
                    }else
                        Log.e("ONBOARD", "addAvatar: erro"+e.getMessage() );
                });

                displayImage(origImgs.getUrl());
                bitmap.recycle();
            }
        }catch (Exception e){
            progress.dismiss();
            Log.e("ONBOARD", "addAvatar: error"+e.getMessage() );
        }

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int index) {
        if (mListener != null) {
            mListener.onGoContact();
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
        void onGoContact();
    }



}
