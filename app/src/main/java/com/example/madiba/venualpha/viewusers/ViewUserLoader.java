package com.example.madiba.venualpha.viewusers;

import android.text.TextUtils;

import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.viewusers.ViewUserCell;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Madiba on 3/27/2017.
 */

public class ViewUserLoader {

    public static Observable<List<ViewUserCell>> loadUsersContacts(String id, int type){

        return Observable.create(new Observable.OnSubscribe<List<ViewUserCell>>() {
            @Override
            public void call(Subscriber<? super List<ViewUserCell>> subscriber) {

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
//                    subscriber.onNext(query.find());
                    subscriber.onCompleted();

                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

}
