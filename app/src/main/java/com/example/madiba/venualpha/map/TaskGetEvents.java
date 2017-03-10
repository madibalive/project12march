package com.example.madiba.venualpha.map;

import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.util.Generators.ModelGenerator;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/10/2016.
 */

public class TaskGetEvents implements Callable<List<MdEventItem>> {

    private final LatLng location;

    public TaskGetEvents(LatLng location) {
        this.location = location;
    }

    @Override
    public List<MdEventItem> call() throws Exception {
        ParseQuery<ParseObject> tagQuery = ParseQuery.getQuery("EventsVersion3");
        tagQuery.orderByDescending("updateAt");
        tagQuery.setLimit(10);
        tagQuery.whereNear("cordinate",new ParseGeoPoint(location.latitude,location.longitude));

        List<MdEventItem> eventItems = new ArrayList<>();

        for (ParseObject object:tagQuery.find()
             ) {
            eventItems.add(ModelGenerator.generateEvent(object));
        }
        return eventItems;
    }
}
