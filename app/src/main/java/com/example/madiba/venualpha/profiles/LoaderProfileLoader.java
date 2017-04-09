package com.example.madiba.venualpha.profiles;

import android.support.annotation.Nullable;

import com.example.madiba.venualpha.profiles.adapter.ProfileEventCell;
import com.example.madiba.venualpha.profiles.adapter.ProfileGalleryCell;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Madiba on 3/10/2017.
 */

public class LoaderProfileLoader {


    public static Observable<List<ProfileGalleryCell>> loadUserGallery(@Nullable String id, int skip, @Nullable Date date, @Nullable Date endDate){
        return Observable.create((Observable.OnSubscribe<List<ProfileGalleryCell>>) subscriber -> {

            List<ProfileGalleryCell> cells = new ArrayList<>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
            query.whereEqualTo("to", ParseUser.createWithoutData(ParseUser.class,id));

            if (date != null)
                query.whereLessThan("createdAt",date);

            query.orderByAscending("Created");
            query.setLimit(20);
            query.setSkip(skip);

            try {

                for (ParseObject object : query.find()) {
                    ProfileGalleryCell cell = new ProfileGalleryCell(ModelGenerator.generateMemmory(object));
                    cells.add(cell);
                }
                subscriber.onNext(cells);
                subscriber.onCompleted();
            }catch (Exception e){
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ProfileEventCell>> loadUserEvants(@Nullable String id, int skip, @Nullable Date date, @Nullable Date endDate){
        return Observable.create((Observable.OnSubscribe<List<ProfileEventCell>>) subscriber -> {

            List<ProfileEventCell> cells = new ArrayList<>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
            query.whereEqualTo("to", ParseUser.createWithoutData(ParseUser.class,id));

            if (date != null)
                query.whereLessThan("createdAt",date);

            query.orderByAscending("Created");
            query.setLimit(20);
            query.setSkip(skip);

            try {

                for (ParseObject object : query.find()) {
                    ProfileEventCell cell = new ProfileEventCell(ModelGenerator.generateEvent(object));
                    cells.add(cell);
                }
                subscriber.onNext(cells);
                subscriber.onCompleted();
            }catch (Exception e){
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

}
