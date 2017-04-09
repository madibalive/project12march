package com.example.madiba.venualpha.viewer.gallery;

import android.support.annotation.Nullable;

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

public class LoaderGallery {

    public static Observable<List<ProfileGalleryCell>> loadGallery(@Nullable String id, @Nullable String className, int skip, int type, @Nullable Date startDate, @Nullable Date endDate){
        return Observable.create((Observable.OnSubscribe<List<ProfileGalleryCell>>) subscriber -> {

            List<ProfileGalleryCell> cells = new ArrayList<>();

            ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_MEDIA);
            query.whereLessThan("updateAt",startDate);
            query.orderByAscending("updateAt");
            query.setLimit(20);
            query.setSkip(skip);

            if (type == GlobalConstants.GALLERY_TYPE_CHALLANGE){
                //CHALLNGE  Case normal with loading and
                // @params startDate and enddate and type
                query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
                query.whereGreaterThanOrEqualTo("updateAt",endDate);

            }else if (type == GlobalConstants.GALLERY_TYPE_DISCOVER){

                // Discover Recent Images
                // @params start date , type
                query.orderByDescending("reactions");
                query.addDescendingOrder("comments");
            }else {
                //DEFULAT Case normal with loading and
                // @params  startDate
                query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
            }

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

}
