package com.example.madiba.venualpha.post.MediaPost;

import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.example.madiba.venualpha.models.MdEventItem;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/10/2016.
 */

public class TaskGetRecentMemories implements Callable<List<MdEventItem>>{


    public TaskGetRecentMemories() {
    }

    @Override
    public List<MdEventItem> call() throws Exception {
        List<MdEventItem> eventItems= new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Peep");

        for (ParseObject a:query.find()
                ) {
            eventItems.add(ModelGenerator.generateSimpleEvent(a));
        }

        return eventItems;
    }
}
