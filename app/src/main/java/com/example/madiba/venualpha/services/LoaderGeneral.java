package com.example.madiba.venualpha.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.madiba.venualpha.models.CategoriesModel;
import com.example.madiba.venualpha.models.DiscoverModel;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.ModelLoadLocal;
import com.example.madiba.venualpha.models.ModelNotification;
import com.example.madiba.venualpha.models.ModelOtherContact;
import com.example.madiba.venualpha.models.PhoneContact;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class LoaderGeneral {

    public static Observable<List<ParseObject>> PagerLoader(String id, String className, int skip, int type, Date date){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);


                query.orderByAscending("Created");
                query.setLimit(15);
                query.whereLessThan("createdAt",date);
                query.setSkip(skip);

                switch (type){
                    case 1:
                        // user
                        query.whereEqualTo("to", ParseUser.createWithoutData(ParseUser.class,id));
                        break;
                    case 2:
                        // event
                        query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
                        break;
                    default:
                        break;
                }

                Timber.d("connecting");
                try {
                    subscriber.onNext(query.find());
                    // save to local store
                    subscriber.onCompleted();


                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseObject>> loadCategory(String category, int skip, Date date,int pos){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_CATEGORY);
                query.orderByAscending("Created");
                if (pos==1){
                    query.orderByDescending("date");
                }else if (pos ==2){
                    query.orderByDescending("going");
                    query.addDescendingOrder("comments");
                }else {
                    query.whereStartsWith("category",category);

                }
                query.setLimit(15);
                query.whereLessThan("createdAt",date);
                query.setSkip(skip);

                try {
                    subscriber.onNext(query.find());
                    // save to local store
                    subscriber.onCompleted();


                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseObject>> loadOnTap(){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Ontap");
                query.whereEqualTo("from", ParseUser.getCurrentUser());
                query.orderByAscending("createdAt");
                try {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<List<ParseObject>> loadPendingInvites(){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_PENDING);
                query.whereStartsWith("phone", ParseUser.getCurrentUser().getString("phone"));
                query.orderByAscending("createdAt");
                try {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }



    public static Observable<List<ParseObject>> loadUsersContacts(String id, int type){

        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                String term;

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                query.whereExists("from");
                query.include("from");

                if (type == 0)
                    term = "to";
                else
                    term = "from";

                if (TextUtils.equals(id, ParseUser.getCurrentUser().getObjectId())) {
                    query.whereEqualTo(term, ParseUser.getCurrentUser());

                } else {
                    query.whereEqualTo(term, ParseUser.createWithoutData(ParseUser.class, id));
                }

                query.orderByAscending("createdAt");
                Timber.d("connecting");
                try {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseUser>> loadUsersContacts(){

        return Observable.create(new Observable.OnSubscribe<List<ParseUser>>() {
            @Override
            public void call(Subscriber<? super List<ParseUser>> subscriber) {
                List<ParseUser> users = new ArrayList<ParseUser>();
                ParseQuery<ParseUser> userQuery= ParseUser.getQuery();

//                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
//                query.whereExists("from");
//                query.include("from");
//                query.whereEqualTo("from", ParseUser.getCurrentUser());
//                query.orderByAscending("createdAt");
//                Timber.d("connecting");
                try {
//                    for (ParseObject parseObject: query.find()
//                         ) {
//                        users.add(parseObject.getParseUser("to"));
//
//                    }
                    subscriber.onNext(userQuery.find());
                    subscriber.onCompleted();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseUser>> editInviteLoad(String id, String className){

        return Observable.create(new Observable.OnSubscribe<List<ParseUser>>() {
            @Override
            public void call(Subscriber<? super List<ParseUser>> subscriber) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                query.whereExists("from");
                query.whereEqualTo("from", ParseUser.getCurrentUser());

                // FIND  CONTACT NOT IN ARRAY

                ParseQuery<ParseObject> syncQuery = ParseQuery.getQuery("");
                syncQuery.whereEqualTo("event", ParseObject.createWithoutData(className,id));
                syncQuery.whereDoesNotMatchKeyInQuery(GlobalConstants.FROM, "to", query);
                syncQuery.orderByAscending("createdAt");



                List<ParseUser> users = new ArrayList<ParseUser>();

                try {
                    for (ParseObject parseObject: syncQuery.find()
                            ) {
                        users.add(parseObject.getParseUser("to"));

                    }
                    subscriber.onNext(users);
                    subscriber.onCompleted();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }



    public static Observable<List<ModelNotification>> loadNotifications(){
        return Observable.create(new Observable.OnSubscribe<List<ModelNotification>>() {
            @Override
            public void call(Subscriber<? super List<ModelNotification>> subscriber) {

                List<ModelNotification> list = new ArrayList<ModelNotification>();

                ParseQuery<ParseObject> defualtQuery = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
                defualtQuery.whereEqualTo("to", ParseUser.getCurrentUser());
                defualtQuery.include("from");
                defualtQuery.orderByAscending("Created");
                defualtQuery.setLimit(20);
                defualtQuery.whereEqualTo("type",2);



                ParseQuery<ParseObject> extendedQuery = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
                extendedQuery.whereEqualTo("to", ParseUser.getCurrentUser());
                extendedQuery.include("from");
                extendedQuery.orderByAscending("Created");
                extendedQuery.setLimit(20);
                extendedQuery.whereEqualTo("type",2);




                Timber.d("connecting");
                try {

                    ModelNotification notificationDedault= new ModelNotification(ModelNotification.TYPE_DEFAULT,defualtQuery.find());
                    ModelNotification notificationExtended= new ModelNotification(ModelNotification.TYPE_EXTENDED,defualtQuery.find());
                    list.add(notificationDedault);
                    list.add(notificationExtended);

                    subscriber.onNext(list);
                    // save to local store
                    subscriber.onCompleted();


                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<DiscoverModel>> loadDiscover(){
        return Observable.create(new Observable.OnSubscribe<List<DiscoverModel>>() {
            @Override
            public void call(Subscriber<? super List<DiscoverModel>> subscriber) {
                List<DiscoverModel> mDatas= new ArrayList<>();

                // add categories

                List<CategoriesModel> categoriesList = new ArrayList<>();

                CategoriesModel today = new CategoriesModel("Live \n Today");
                CategoriesModel weekend = new CategoriesModel("Weekend \n Picks");
                CategoriesModel top = new CategoriesModel("Top \nEvent");
                CategoriesModel entertain = new CategoriesModel("Entertain \nEvents");
                CategoriesModel gospel = new CategoriesModel("Gospol \nEvents");
                CategoriesModel social = new CategoriesModel("Social \nEvents");
                CategoriesModel fitness = new CategoriesModel("Fitness \nEvents");

                categoriesList.add(today);
                categoriesList.add(weekend);
                categoriesList.add(top);
                categoriesList.add(entertain);
                categoriesList.add(gospel);
                categoriesList.add(social);
                categoriesList.add(fitness);

                DiscoverModel a = new DiscoverModel();
                a.setMtitle(null);
                a.setType(DiscoverModel.CATEGORIES);
                a.setListCategories(categoriesList);
                mDatas.add(a);


                // creat all queries heres
                ParseQuery<ParseObject> queryEvents= ParseQuery.getQuery(GlobalConstants.CLASS_EVENT);
                queryEvents.orderByAscending("createdAt");
                queryEvents.setLimit(20);

                ParseQuery<ParseUser> queryPeople = ParseUser.getQuery();
                queryPeople.orderByAscending("createdAt");
                queryPeople.setLimit(20);

                ParseQuery<ParseObject> queryMedia= ParseQuery.getQuery(GlobalConstants.CLASS_EVENT);
                queryMedia.orderByAscending("createdAt");
                queryMedia.setLimit(20);

                ParseQuery<ParseObject> queryGossip= ParseQuery.getQuery(GlobalConstants.CLASS_GOSSIP);
                queryGossip.orderByAscending("createdAt");
                queryGossip.setLimit(20);


                Timber.d("connecting");
                try {
                    // add return datas

                    DiscoverModel gossip = new DiscoverModel();
                    gossip.setType(DiscoverModel.TOP_GOSSIP);
                    gossip.setListObject(queryGossip.find());
                    gossip.setMtitle("Top Users");
                    mDatas.add(gossip);

                    DiscoverModel newPeople = new DiscoverModel();
                    newPeople.setType(DiscoverModel.New_PEOPLE);
                    newPeople.setListUsers(queryPeople.find());
                    newPeople.setMtitle("Top new  Users");
                    mDatas.add(newPeople);

                    DiscoverModel media = new DiscoverModel();
                    media.setType(DiscoverModel.DIS_EXPLORE);
                    media.setListObject(queryMedia.find());
                    media.setMtitle("New Peeps");

                    DiscoverModel newEvents = new DiscoverModel();
                    newEvents.setType(DiscoverModel.NEW_EVENT);
                    newEvents.setListObject(queryEvents.find());
                    newEvents.setMtitle("New Peeps");

                    mDatas.add(newPeople);
                    mDatas.add(media);
                    mDatas.add(newEvents);
                    mDatas.add(gossip);
                    subscriber.onNext(mDatas);
                    subscriber.onCompleted();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseObject>> loadComment(String id, String className){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_COMMENT);
                query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
                query.include("from");
                query.orderByAscending("createdAt");
                Timber.d("connecting");
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

    public static Observable<List<ParseObject>> loadGoing(String id, String className){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_COMMENT);
                query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
                query.include("from");
                query.orderByAscending("createdAt");
                Timber.d("connecting");
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

    public static Observable<Map<String,String>> loadInvites(){
        return Observable.create(new Observable.OnSubscribe<Map<String,String>>() {
            @Override
            public void call(Subscriber<? super Map<String,String>> subscriber) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                query.whereExists("from");
                query.include("from");
                query.whereEqualTo("to", ParseUser.getCurrentUser());
                query.orderByAscending("createdAt");

                try {
                    Map<String,String> hashMap = new HashMap<String, String>();
                    List<ParseObject> data = query.find();
                    for (ParseObject a:data ) {
                        hashMap.put(a.getParseUser("from").getUsername(),a.getParseUser("from").getObjectId());
                    }

                    subscriber.onNext(hashMap);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseUser>> loadRecentUsers(){
        return Observable.create(new Observable.OnSubscribe<List<ParseUser>>() {
            @Override
            public void call(Subscriber<? super List<ParseUser>> subscriber) {

                // Init query
                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.orderByAscending("createdAt");
                query.setLimit(20);

                try {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseObject>> loadRecentGossip(){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_GOSSIP);
                query.whereEqualTo("from", ParseUser.getCurrentUser());
                query.orderByAscending("createdAt");
                query.setLimit(20);

                try {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ParseObject>> loadFbContact() {
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {
                Bundle parameters = new Bundle();
                parameters.putString("fields", "friends");

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        (object, response) -> {
                            try {
                                JSONArray friends = response.getJSONObject().getJSONArray("data");
                                String name = response.getJSONObject().getString("name");
                                JSONObject picture = response.getJSONObject().getJSONObject("picture");
                                JSONObject data = picture.getJSONObject("data");
                                String pictureUrl = data.getString("url");

                                subscriber.onCompleted();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                subscriber.onError(e);
                            }
                        });

                request.setParameters(parameters);
                request.executeAsync();

            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<List<ParseObject>> loadGallery(String id, String className,int skip, int type, @Nullable Date startDatae,@Nullable Date endDate){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_MEDIA);
                query.whereLessThan("createdAt",startDatae);
                query.orderByAscending("createdAt");
                query.setLimit(20);
                query.setSkip(skip);


                //DEFULAT Case normal with loading and
                // @params  startDate
                query.whereEqualTo("to", ParseObject.createWithoutData(className,id));




                //CHALLNGE  Case normal with loading and
                // @params startDate and enddate and type
                query.whereEqualTo("to", ParseObject.createWithoutData(className,id));
                query.whereGreaterThanOrEqualTo("createdAt",endDate);


                // Discover Recent Images
                // @params start date , type
                query.orderByDescending("reactions");
                query.addDescendingOrder("comments");


                // Init query

                try {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }


    public static Observable<List<ParseObject>> loadRecentMedia(){
        return Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
            @Override
            public void call(Subscriber<? super List<ParseObject>> subscriber) {

                // Init query
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_MEDIA);
                query.whereEqualTo("from", ParseUser.getCurrentUser());
                query.orderByAscending("createdAt");
                query.setLimit(20);

                try {
                    subscriber.onNext(query.find());
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    public static Observable<List<ModelLoadLocal>> ContactLoad(Context context){
        List<PhoneContact> alContacts=new ArrayList<>();
        ArrayList<String> phoneNumbers = new ArrayList<>();
        return Observable.create(new Observable.OnSubscribe<List<ModelLoadLocal>>() {
            @Override
            public void call(Subscriber<? super List<ModelLoadLocal>> subscriber) {
                getAll(context,alContacts,phoneNumbers);

                ParseQuery<ParseUser> syncQuery = ParseUser.getQuery();
                syncQuery.whereContainedIn("phone", phoneNumbers);

                try {
                    List<ParseUser> foundUsers = syncQuery.find();
                    //remove from following
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                    query.whereExists("from");
                    query.include("to");
                    query.whereEqualTo("from", ParseUser.getCurrentUser());
                    List<ParseObject> followingData = query.find();

                    for (int i = 0; i < phoneNumbers.size(); i++) {
                        for (int j = 0; j < foundUsers.size() ; j++) {
                            if (alContacts.get(i).getPhoneNumber().equals(foundUsers.get(j))){
                                alContacts.remove(i);
                            }
                        }
                    }



                    for (int i = 0; i <followingData.size() ; i++) {

                        for (int j = 0; j < foundUsers.size(); j++) {
                            if (followingData.get(i).getParseUser("to").getObjectId().equals(foundUsers.get(j).getObjectId())){
                                foundUsers.remove(j);
                            }
                        }

                    }

                    List<ModelLoadLocal> returnData= new ArrayList<ModelLoadLocal>();

                    for (ParseUser m : foundUsers) {
                        ModelLoadLocal n = new ModelLoadLocal();
                        n.setType(ModelLoadLocal.TYPE_PARSE);
                        n.setParseUser(m);
                    }
                    for (PhoneContact m : alContacts) {
                        ModelLoadLocal n = new ModelLoadLocal();
                        n.setType(ModelLoadLocal.TYPE_LOCAL);
                        n.setPhoneContact(m);
                    }


                    subscriber.onNext(returnData);
                    subscriber.onCompleted();

                } catch (ParseException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io());

    }


    public static Observable<List<ParseUser>> onBoardContactLoad(Context context){
        List<PhoneContact> alContacts=new ArrayList<>();
        ArrayList<String> phoneNumbers = new ArrayList<>();



        return Observable.create((Observable.OnSubscribe<List<ParseUser>>) subscriber -> {
            try {
                getAll(context, alContacts, phoneNumbers);

                ParseQuery<ParseUser> syncQuery = ParseUser.getQuery();
                syncQuery.whereContainedIn("phone", phoneNumbers);

                subscriber.onNext(syncQuery.find());
                subscriber.onCompleted();

            }catch (Exception e){
                subscriber.onError(e);
            }

        }).subscribeOn(Schedulers.io());

    }

    public static Observable<List<ParseUser>> onboardFindUser(Context context,Boolean isFbEnable){
        List<PhoneContact> alContacts=new ArrayList<>();
        ArrayList<String> phoneNumbers = new ArrayList<>();
        ArrayList<String> facebookIds = new ArrayList<>();


        return Observable.create((Observable.OnSubscribe<List<ParseUser>>) subscriber -> {
            try {
                getAll(context,alContacts,phoneNumbers);

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                query.whereExists("from");
                query.whereEqualTo("from", ParseUser.getCurrentUser());

                // FIND  CONTACT NOT IN ARRAY

                ParseQuery<ParseUser> syncQuery = ParseUser.getQuery();
                syncQuery.whereContainedIn("phone", phoneNumbers);
                syncQuery.whereDoesNotMatchKeyInQuery(GlobalConstants.FROM, "to", query);

                Bundle parameters = new Bundle();
                parameters.putString("fields", "friends");

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        (object, response) -> {
                            try {
                                JSONArray dataArrya = response.getJSONObject().getJSONArray("data");
                                for (int i = 0; i < dataArrya.length(); i++) {
                                    facebookIds.add(dataArrya.getJSONObject(i).getString("id"));
                                }
                                ParseQuery<ParseUser> syncQuery2 = ParseUser.getQuery();
                                syncQuery2.whereExists("fbId");
                                syncQuery2.whereContainedIn("fbId", facebookIds);
                                syncQuery2.whereDoesNotMatchKeyInQuery(GlobalConstants.FROM, "to", query);

                                List<ParseQuery<ParseUser>> queries = new ArrayList<>();
                                queries.add(syncQuery);
                                queries.add(syncQuery2);

                                ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
                                mainQuery.orderByDescending("createdAt");
                                mainQuery.include("from");
                                mainQuery.include("object");
                                mainQuery.whereExists("from");

                                subscriber.onNext(mainQuery.find());
                                subscriber.onCompleted();

                            } catch (JSONException |ParseException e) {

                            }
                        });


                if (isFbEnable){
                    request.setParameters(parameters);
                    request.executeAndWait();
                }else {
                    subscriber.onNext(syncQuery.find());
                    subscriber.onCompleted();
                }
            } catch (Exception  e) {
                subscriber.onError(e);
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io());

    }

    public static Observable<List<ModelOtherContact>> ContactLoadOnce(Context context){
        List<PhoneContact> alContacts=new ArrayList<>();
        ArrayList<String> phoneNumbers = new ArrayList<>();
        List<ModelOtherContact> returnData= new ArrayList<>();
        ArrayList<String> facebookIds = new ArrayList<>();


        return Observable.create((Observable.OnSubscribe<List<ModelOtherContact>>) subscriber -> {
            try {
                getAll(context,alContacts,phoneNumbers);

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
                query.whereExists("from");
                query.whereEqualTo("from", ParseUser.getCurrentUser());

                // FIND  CONTACT NOT IN ARRAY

                ParseQuery<ParseUser> syncQuery = ParseUser.getQuery();
                syncQuery.whereContainedIn("phone", phoneNumbers);
                syncQuery.whereDoesNotMatchKeyInQuery(GlobalConstants.FROM, "to", query);

                Bundle parameters = new Bundle();
                parameters.putString("fields", "friends");

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        (object, response) -> {
                            try {
                                JSONArray dataArrya = response.getJSONObject().getJSONArray("data");
                                for (int i = 0; i < dataArrya.length(); i++) {
                                    facebookIds.add(dataArrya.getJSONObject(i).getString("id"));
                                }



                                ParseQuery<ParseUser> syncQuery2 = ParseUser.getQuery();
                                syncQuery2.whereContainedIn("fbId", facebookIds);
                                syncQuery2.whereDoesNotMatchKeyInQuery(GlobalConstants.FROM, "to", query);


                                List<ParseQuery<ParseUser>> queries = new ArrayList<>();
                                queries.add(syncQuery);
                                queries.add(syncQuery2);

                                ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
                                mainQuery.orderByDescending("createdAt");
                                mainQuery.include("from");
                                mainQuery.whereExists("from");

                                ModelOtherContact model = new ModelOtherContact();
                                model.setType(ModelOtherContact.TYPE_PARSE);
                                List<ParseUser>  users = mainQuery.find();
                                model.setParseContacts(users);
                                returnData.add(model);


                                for (int i = 0; i < phoneNumbers.size(); i++) {
                                    for (int j = 0; j < users.size() ; j++) {
                                        if (alContacts.get(i).getPhoneNumber().equals(users.get(j).getString("phone"))){
                                            alContacts.remove(i);
                                        }
                                    }
                                }

                                ModelOtherContact model2 = new ModelOtherContact();
                                model2.setType(ModelOtherContact.TYPE_LOCAL);
                                model2.setLocalContact(alContacts);
                                returnData.add(model2);

                                subscriber.onNext(returnData);
                                subscriber.onCompleted();

                            } catch (JSONException |ParseException e) {

                            }
                        });

                if (!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                    request.setParameters(parameters);
                    request.executeAndWait();
                }else {
                    ModelOtherContact model = new ModelOtherContact();
                    model.setType(ModelOtherContact.TYPE_PARSE);
                    final  List<ParseUser>  users = syncQuery.find();
                    model.setParseContacts(users);
                    returnData.add(model);

                    for (int i = 0; i < phoneNumbers.size(); i++) {
                        for (int j = 0; j < users.size() ; j++) {
                            if (alContacts.get(i).getPhoneNumber().equals(users.get(j).getString("phone"))){
                                alContacts.remove(i);
                            }
                        }
                    }

                    ModelOtherContact model2 = new ModelOtherContact();
                    model2.setType(ModelOtherContact.TYPE_LOCAL);
                    model2.setLocalContact(alContacts);
                    returnData.add(model2);

                    subscriber.onNext(returnData);
                    subscriber.onCompleted();
                }
            } catch (Exception  e) {
                subscriber.onError(e);
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io());

    }

    private static void getAll(Context context, List<PhoneContact> alContacts,ArrayList<String> phoneNumbers) {


        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                PhoneContact person = new PhoneContact();

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                person.setId(id);
                person.setUsername(name);


                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactNumber=contactNumber.replaceAll("\\s+", "");
                        person.setPhoneNumber(contactNumber);
                        phoneNumbers.add(contactNumber);
                        break;
                    }
                    pCur.close();
                }

                alContacts.add(person);

            } while (cursor.moveToNext());
        } else {
        }


    }

}
