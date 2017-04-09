package com.example.madiba.venualpha.eventpage;

import android.support.annotation.Nullable;

import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.ModelEventGoingContainer;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Madiba on 12/16/2016.
 */

public class LoaderEventPage {

    public static Observable<ModelEventGoingContainer> loadGoingSample(String id, String className){
        return Observable.create(new Observable.OnSubscribe<ModelEventGoingContainer>() {
            @Override
            public void call(Subscriber<? super ModelEventGoingContainer> subscriber) {
                ModelEventGoingContainer data = new ModelEventGoingContainer();
                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_COMMENT);
                query.whereEqualTo("event", ParseObject.createWithoutData(className,id));
                query.include("from");
                query.setLimit(8);
                query.orderByAscending("createdAt");
                Timber.d("connecting");


                ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                followersQuery.whereEqualTo("from", ParseUser.getCurrentUser());

                //non private query
                query.whereMatchesKeyInQuery(GlobalConstants.FROM, "to", followersQuery);

                try {
                    data.setGlbalList(query.find());
                    if (data.getGlbalList().size()>8){

                        ParseQuery<ParseObject> queryCountG = ParseQuery.getQuery(GlobalConstants.CLASS_COMMENT);
                        queryCountG.whereEqualTo("event", ParseObject.createWithoutData(className,id));
                        data.setGlobalCount(queryCountG.count());
                    }

                    query.whereMatchesKeyInQuery(GlobalConstants.FROM, "to", followersQuery);
                    query.setLimit(4);
                    data.setFriendsList(query.find());
                    if (data.getGlbalList().size()>4){


                        ParseQuery<ParseObject> queryCountG = ParseQuery.getQuery(GlobalConstants.CLASS_COMMENT);
                        queryCountG.whereEqualTo("event", ParseObject.createWithoutData(className,id));
                        queryCountG.whereMatchesKeyInQuery(GlobalConstants.FROM, "to", followersQuery);
                        data.setFriendsCount(queryCountG.count());
                    }

                    subscriber.onNext(data);
                    // save to local store
                    subscriber.onCompleted();



                } catch (ParseException e) {
                    subscriber.onError(e);
                }



            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseObject>> loadGoingFull(String id, String className,int type){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_EVENT);
                query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
                query.include("from");
                query.orderByAscending("createdAt");


                if (type==2) {

                    ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                    followersQuery.whereEqualTo("from", ParseUser.getCurrentUser());
                    query.whereMatchesKeyInQuery(GlobalConstants.FROM, "to", followersQuery);
               }

                try {
                    subscriber.onNext(query.find());
                    // save to local store
                    subscriber.onCompleted();


                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<List<MdMemoryItem>> loadEventGallery(@Nullable String id){
        return Observable.create((Observable.OnSubscribe<List<MdMemoryItem>>) subscriber -> {

            List<MdMemoryItem> cells = new ArrayList<>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
            query.whereEqualTo("to", ParseObject.createWithoutData("Events",id));
            query.orderByAscending("views");
            query.setLimit(60);

            try {

                for (ParseObject object : query.find()) {
                    MdMemoryItem cell = ModelGenerator.generateMemmory(object);
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
