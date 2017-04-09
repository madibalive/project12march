package com.example.madiba.venualpha.post.EventPost;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/10/2016.
 */

public class TaskCheckTag implements Callable<Boolean>{

    private final String term;

    TaskCheckTag(String term) {
        this.term = term;
    }


    @Override
    public Boolean call() throws Exception {

        final Map<String, Object> params = new HashMap<>();
        final List<String> userIds = new ArrayList<>();

//        params.put("otherUserIds", userIds);
//        params.put("groupName",  title.getText().toString());
//
//        ParseCloud.callFunctionInBackground("createGossip", params, new FunctionCallback<Object>() {
//            @Override
//            public void done(Object object, ParseException e) {
//                if (e==null){
//                    Log.e("Chat", "done: object:: "+object.toString() );
//
//                }else
//                    Log.e("Chat", "done: error ::"+e.toString() );
//
//                progressDialog.hide();
//            }
//
//        });

        ParseQuery<ParseObject> hashtagQuery = ParseQuery.getQuery("Eventest");
        hashtagQuery.whereStartsWith("hashTag",term);
        ParseObject data = hashtagQuery.getFirst();

        if (data !=null){
            return true;
        }else
            return false;
    }
}
