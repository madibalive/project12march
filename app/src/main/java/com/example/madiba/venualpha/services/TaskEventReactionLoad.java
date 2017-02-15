package com.example.madiba.venualpha.services;

import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/7/2016.
 */

public class TaskEventReactionLoad implements Callable<Boolean> {
    private  String searchTerm;
    public TaskEventReactionLoad() {  }

    @Override
    public Boolean call() throws Exception {

        // Init query
        ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_GOING);
        query.whereEqualTo("to", SingletonDataSource.getInstance().getEventPageEvent());
        query.whereEqualTo("from", ParseUser.getCurrentUser());


        if (SingletonDataSource.getInstance().getEventPageEvent().getInt("type")
                ==GlobalConstants.EVENT_TYPE_INVITED){
            ParseQuery<ParseObject> inviterQuery = ParseQuery.getQuery(GlobalConstants.CLASS_NOTIFICATION);
            inviterQuery.whereEqualTo("object", SingletonDataSource.getInstance().getEventPageEvent());
            inviterQuery.whereEqualTo("to", ParseUser.getCurrentUser());
            inviterQuery.whereEqualTo("type",1);

            inviterQuery.getFirst();
        }


//        ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_GOING);
//        query.whereEqualTo("to", SingletonDataSource.getInstance().getEventPageEvent());
//        query.whereEqualTo("from", ParseUser.getCurrentUser());

//                EventBus.query.countInBackground();

        if (query.getFirst() == null){
            return false;
        }else
            return true;


    }
}
