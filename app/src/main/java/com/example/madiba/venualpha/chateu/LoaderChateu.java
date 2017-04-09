package com.example.madiba.venualpha.chateu;

import com.example.madiba.venualpha.chateu.adapter.ChateuEventExtraCell;
import com.example.madiba.venualpha.chateu.adapter.ChateuMemoriesExtraCell;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.example.madiba.venualpha.viewusers.ViewUserCell;
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

public class LoaderChateu {

    public static Observable<List<ViewUserCell>> loadContacts(){
        return Observable.create(new Observable.OnSubscribe<List<ViewUserCell>>() {
            @Override
            public void call(Subscriber<? super List<ViewUserCell>> subscriber) {
                List<ViewUserCell> data = new ArrayList<ViewUserCell>();
                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_COMMENT);
                query.whereEqualTo("event", ParseUser.getCurrentUser());
                query.include("from");
                query.setLimit(8);
                query.orderByAscending("createdAt");
                Timber.d("connecting");


                ParseQuery<ParseObject> followersQuery = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                followersQuery.whereNotEqualTo("from", ParseUser.getCurrentUser());

                //non private query
                query.whereMatchesKeyInQuery(GlobalConstants.FROM, "to", followersQuery);

                try {

                    subscriber.onNext(data);
                    // save to local store
                    subscriber.onCompleted();



                } catch (Exception e) {
                    subscriber.onError(e);
                }



            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<SimpleCell>> loadMessage(String id, String className,int type){
        return Observable.create(new Observable.OnSubscribe<List<SimpleCell>>() {
            @Override
            public void call(Subscriber<? super List<SimpleCell>> subscriber) {
                List<SimpleCell> cells = new ArrayList<>();

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_EVENT);
                query.whereEqualTo("to", ParseUser.getCurrentUser());
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

    public static Observable<List<SimpleCell>> load(){
        return Observable.create(new Observable.OnSubscribe<List<SimpleCell>>() {
            @Override
            public void call(Subscriber<? super List<SimpleCell>> subscriber) {
                try {

                    List<SimpleCell> cells = new ArrayList<>();

                    // Init query
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_MEDIA);
//                    query.whereEqualTo("to", ParseUser.getCurrentUser());
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

    public static Observable<List<ChateuMemoriesExtraCell>> loadUserGallery(){
        return Observable.create((Observable.OnSubscribe<List<ChateuMemoriesExtraCell>>) subscriber -> {

            List<ChateuMemoriesExtraCell> cells = new ArrayList<>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
            query.whereEqualTo("to", ParseUser.getCurrentUser());
            query.orderByAscending("Created");
            query.setLimit(50);
            try {

                for (ParseObject object : query.find()) {
                    ChateuMemoriesExtraCell cell = new ChateuMemoriesExtraCell(ModelGenerator.generateMemmory(object));
                    cells.add(cell);
                }
                subscriber.onNext(cells);
                subscriber.onCompleted();
            }catch (Exception e){
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ChateuEventExtraCell>> loadUserEvants(){
        return Observable.create((Observable.OnSubscribe<List<ChateuEventExtraCell>>) subscriber -> {

            List<ChateuEventExtraCell> cells = new ArrayList<>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
            query.whereEqualTo("to", ParseUser.getCurrentUser());
            query.orderByAscending("Created");
            query.setLimit(50);

            try {

                for (ParseObject object : query.find()) {
                    ChateuEventExtraCell cell = new ChateuEventExtraCell(ModelGenerator.generateEvent(object));
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
