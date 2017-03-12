package com.example.madiba.venualpha.channels;

import com.example.madiba.venualpha.adapter.channel.ChVenuEventCell;
import com.example.madiba.venualpha.adapter.channel.ChVenuHighCell;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.jaychang.srv.SimpleCell;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
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

public class LoaderChannel {



    public static Observable<List<ChVenuEventCell>> loadEvent(String id, String className, int type){
        return Observable.create(new Observable.OnSubscribe<List<ChVenuEventCell>>() {
            @Override
            public void call(Subscriber<? super List<ChVenuEventCell>> subscriber) {
                List<ChVenuEventCell> cells = new ArrayList<>();

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
                    subscriber.onNext(cells);
                    // save to local store
                    subscriber.onCompleted();


                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ChVenuHighCell>> loadHighlights(String id, String className, int type){
        return Observable.create(new Observable.OnSubscribe<List<ChVenuHighCell>>() {
            @Override
            public void call(Subscriber<? super List<ChVenuHighCell>> subscriber) {
                List<ChVenuHighCell> cells = new ArrayList<>();

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
                    subscriber.onNext(cells);
                    // save to local store
                    subscriber.onCompleted();


                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<SimpleCell>> loadSports(SimpleCell.OnCellClickListener2 onCellClickListener2){
        return Observable.create(new Observable.OnSubscribe<List<SimpleCell>>() {
            @Override
            public void call(Subscriber<? super List<SimpleCell>> subscriber) {
                try {

                    List<SimpleCell> cells = new ArrayList<>();

                    // Init query
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_MEDIA);
//                    query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
                    query.include("from");
                    query.setLimit(4);
                    query.orderByAscending("createdAt");
                    List<ParseObject>  data = new ArrayList<>();
                    data = query.find();
//                    data.add(a);data.add(b);

                    //check status and return fetch event
                    ParseQuery queryR = ParseQuery.getQuery(GlobalConstants.CLASS_USER_RELATION);
                    queryR.whereEqualTo("user", ParseUser.getCurrentUser());
                    ParseObject relation = queryR.getFirst();
                    if (relation !=null){
                        ParseRelation<ParseObject> relationLikes = relation.getRelation("likes");
                        List<ParseObject> likes= relationLikes.getQuery().find();

//                        if (likes.contains(obj)){
//                            EventBus.getDefault().post(new ActionMediaCheckIslike(true));
//                        }else {
//                            EventBus.getDefault().post(new ActionMediaCheckIslike(false));
//                        }
                    }

                    Timber.d("connecting");
                    subscriber.onNext(cells);
                    subscriber.onCompleted();

                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
