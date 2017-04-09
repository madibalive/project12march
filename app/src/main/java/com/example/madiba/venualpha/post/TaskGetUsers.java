package com.example.madiba.venualpha.post;

import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/10/2016.
 */

public class TaskGetUsers implements Callable<List<MdUserItem>> {


    public TaskGetUsers() {
    }

    @Override
    public List<MdUserItem> call() throws Exception {
        List<MdUserItem> userItems = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending("createdAt");
        query.setLimit(20);

        for (ParseUser user:query.find()
             ) {
            userItems.add(ModelGenerator.generateUser(user));
        }

        return userItems;

    }
}
