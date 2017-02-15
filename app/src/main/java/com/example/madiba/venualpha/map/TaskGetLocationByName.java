package com.example.madiba.venualpha.map;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/10/2016.
 */

public class TaskGetLocationByName implements Callable<List<Address>> {

    private final String location;
    private Context context;

    public TaskGetLocationByName(String location, Context context) {
        this.location = location;
        this.context = context;
    }

    @Override
    public List<Address> call() throws Exception {
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(context);
        addressList = geocoder.getFromLocationName(location, 5);
        return addressList;


    }
}
