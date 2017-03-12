package com.example.madiba.venualpha.onboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.text.format.DateFormat;
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

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.Actions.ActionDate;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.map.TaskGetLocationByName;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.ImageUitls;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.SelectDateFragment;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserDetailFragment extends Fragment {
    public static final int index =2;

    public static final List<String> DATA = Arrays.asList("Male","Female");
    StateButton proceed;
    private ProgressDialog progress;
    private EditText mHighSchool;
    private Spinner genderSpinner;
    private Button mDate;
    private Date date;
    private RoundCornerImageView mAvatar;
    private int gender;
    private LatLng latLng;
    private PopupMenu popupMenu ;
    private ImageButton mSearchBtn;
    private FloatingActionButton fab;

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
        mDate = (Button) view.findViewById(R.id.date_of_birth);
        mAvatar = (RoundCornerImageView) view.findViewById(R.id.avatar);
        mSearchBtn = (ImageButton) view.findViewById(R.id.search_btn);
        genderSpinner = (Spinner) view.findViewById(R.id.gender_spinner);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
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

        mDate.setOnClickListener(view1 -> {
            DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getFragmentManager(), "DatePicker");
        });


        fab.setOnClickListener(view2 -> onSelectImageClick());

    }


    private void startCropImageActivity(Uri imageUri) {

    }


    private void addAvatar(Uri uri){

        progress = ProgressDialog.show(getActivity(), null,
                getResources().getString(R.string.progress_connecting), true);

        byte[] originalBytes;
        byte[] compressBytes;
        //get bitmap
        final Bitmap bitmap =BitmapFactory.decodeFile(uri.getPath());

        if (bitmap != null) {
            originalBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 100);
            compressBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 50);
            ParseFile origImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(), originalBytes);
            ParseFile cmprImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(), compressBytes);
            try {
                cmprImgs.save();
                origImgs.save();

                ParseUser.getCurrentUser().put("avatarSmall",cmprImgs);
                ParseUser.getCurrentUser().put("avatar",origImgs);
            }catch (Exception e){

            }finally {
                bitmap.recycle();
                progress.dismiss();
            }

        }
    }

    public void onSelectImageClick() {

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
            return false;
        });

        popupMenu.show();
    }

    private void proceed() {
        if (ParseUser.getCurrentUser().getParseFile("avatarMedium") != null){
            return;
        }
        if (date == null)
            return;

        if (NetUtils.hasInternetConnection(getApplicationContext())){
            progress = ProgressDialog.show(getActivity(), null,
                    getResources().getString(R.string.progress_connecting), true);
            updateUser();
        }
    }


    private void updateUser() {
        final ParseUser user =ParseUser.getCurrentUser();

        if (latLng != null)
            user.put("place",new ParseGeoPoint(latLng.latitude,latLng.longitude));

        user.put("gender",gender);
        user.put("age",mDate);
        user.saveInBackground(e -> progress.dismiss());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionDate action) {
        Timber.e("Date return %s",action.date);
        date = action.date;
        mDate.setText(DateFormat.getMediumDateFormat(getActivity().getApplicationContext()).format(date));

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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(index,false);
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
        void onFragmentInteraction(int index,Boolean aBoolean);
    }



}
