package com.example.madiba.venualpha.post.MediaPost;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/10/2016.
 */

public class TaskGetShots implements Callable<List<ParseObject>> {


    public TaskGetShots() {
    }

    @Override
    public List<ParseObject> call() throws Exception {
        ParseQuery<ParseObject> tagQuery = ParseQuery.getQuery("EventsVersion3");
        tagQuery.orderByDescending("updateAt");
        tagQuery.setLimit(10);



        return tagQuery.find();
    }
}
