package com.example.madiba.venualpha.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.Callable;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import timber.log.Timber;

public class MapEnterLocationActivity extends FragmentActivity implements
        OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        EasyPermissions.PermissionCallbacks {

    private static final String TAG = "POST_MAP";
    private GoogleMap mMap;
    protected GoogleApiClient mGoogleApiClient;
    private UiSettings mUiSettings;
    protected Location mLastLocation;
    private PopupMenu popupMenu ;
    private Geocoder  geocoder;
    private LatLng latLng;
    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int RC_SETTINGS_SCREEN = 125;
    private boolean mLocationPermissionDenied = false;
    private LocationManager locationManager;
    private Marker mCurrentLocation;
    private MarkerOptions selfMarker;
    private ImageButton searchBtn;
    private Button cancel,proceed;
    private ImageButton mClose;
    private EditText locationName,locationSearch;
    private static final String MAP_VIEW_SAVE_STATE = "mapViewSaveState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_enter_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationSearch = (EditText) findViewById(R.id.enter_search);
        locationName = (EditText) findViewById(R.id.enter_Location);
        searchBtn = (ImageButton) findViewById(R.id.search);
        cancel = (Button) findViewById(R.id.buttonEnd);
        proceed = (Button) findViewById(R.id.buttonStart);
        mClose = (ImageButton) findViewById(R.id.close);
        setListener();
   }

    private void setListener(){

        searchBtn.setOnClickListener(view -> onMapSearch());

        cancel.setOnClickListener(view -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        });

        proceed.setOnClickListener(view -> {
          returnBack();
        });
        mClose.setOnClickListener(view -> {
            if (locationSearch.getText() != null){
                if (locationSearch.getText().length()>0)
                    locationSearch.setText("");
                else {
                    onBackPressed();
                }
            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setPadding(10,30,10,10);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }



        mMap.setMyLocationEnabled(true);

        updateLastKnownLocation();




        if (mLastLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);
            mLastLocation = locationManager.getLastKnownLocation(provider);

        }

        if (mLastLocation != null) {

            latLng =  new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

            CameraPosition selfLoc = CameraPosition.builder()
                    .target(latLng)
                    .zoom(13)
                    .bearing(0)
                    .tilt(45)
                    .build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(selfLoc));
            selfMarker = new MarkerOptions()
                    .position(latLng)
                    .title("Here you are!");

            mCurrentLocation= mMap.addMarker(selfMarker);

            CircleOptions selfCircle = new CircleOptions()
                    .center(latLng)
                    .radius(1000)
                    .strokeColor(getResources().getColor(R.color.venu_red));


            mMap.addCircle(selfCircle);



        }else {



        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(MapEnterLocationActivity.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        //call location request
    }
    @Override
    public void onMapClick(LatLng latLng2) {
        if (mCurrentLocation != null) {
            mCurrentLocation.remove();
        }

        latLng=latLng2;
//        ParseGeoPoint location = new ParseGeoPoint(latLng.latitude,latLng.longitude);
        selfMarker = new MarkerOptions().position(latLng).title("set Location here");
        mCurrentLocation = mMap.addMarker(selfMarker);
    }

    private boolean isLocationServicesEnabled() {

        // We are creating a local locationManager here, as it's not sure we already have one
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        }
        boolean gpsEnabled;
        boolean netEnabled;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            gpsEnabled = false;
        }

        try {
            netEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            netEnabled = false;
        }
        return netEnabled || gpsEnabled;
    }

    @SuppressWarnings("ResourceType")
    @SuppressLint("MissingPermission")
    private void updateLastKnownLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            mLastLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        } else if (mGoogleApiClient != null ) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        if (mLastLocation != null) {
        }
    }

    public void onMapSearch() {
        String location = locationSearch.getText().toString();
        Timber.d("stated");
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            TaskGetLocationByName taskLoad = new TaskGetLocationByName(location,this);
            Tasks.execute(taskLoad,locationNameCallack);
        }
    }

    private Callback<List<Address>> locationNameCallack = new Callback<List<Address>>() {
        @Override
        public void onFinish(List<Address> result, Callable callable, Throwable e) {
            if (e == null) {
                Timber.d("got data");
                if (result.size()>0){
                    showPopup(locationSearch,result);

                }
                // On success
            } else {
                // On error
                Timber.d("stated error %s",e.getMessage());
            }
        }

    };


    private void showPopup(View view,List<Address> list) {
        popupMenu = new PopupMenu(MapEnterLocationActivity.this, view);
        for (int i = 0; i < list.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, list.get(i).getAddressLine(0)+", "+list.get(i).getAddressLine(1));
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {

            int i = item.getItemId();
            Address address = list.get(i);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,13.5f));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));

            locationName.setText(address.getAddressLine(0));
            return false;

        });

        popupMenu.show();
    }


    @AfterPermissionGranted(MY_LOCATION_PERMISSION_REQUEST_CODE)
    private void loadLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (EasyPermissions.hasPermissions(MapEnterLocationActivity.this, perms)) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            buildGoogleApiClient();

        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.requestLocaion),
                    MY_LOCATION_PERMISSION_REQUEST_CODE, perms);
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: " +bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.rationale_ask_again))
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.setting))
                    .setNegativeButton(getString(R.string.cancel), null /* click listener */)
                    .setRequestCode(RC_SETTINGS_SCREEN)
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SETTINGS_SCREEN) {
        }
    }

    private void returnBack() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(GlobalConstants.LATLONG,latLng);
        returnIntent.putExtra(GlobalConstants.LOCATION_NAME,locationName.getText().toString());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null)
             mGoogleApiClient.disconnect();
    }

    protected void onResume() {
        super.onResume();
    }

}
