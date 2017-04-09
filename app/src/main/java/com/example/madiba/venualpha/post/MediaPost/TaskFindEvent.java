package com.example.madiba.venualpha.post.MediaPost;

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

public class TaskFindEvent implements Callable<List<ParseObject>>{

    private final String term;

    TaskFindEvent(String term) {
        this.term = term;
    }


    @Override
    public List<ParseObject> call() throws Exception {

        ParseQuery<ParseObject> hashtagQuery = ParseQuery.getQuery("Eventest");
        hashtagQuery.whereStartsWith("hashTag",term);
        return hashtagQuery.find();


    }
}
