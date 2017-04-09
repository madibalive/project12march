package com.example.madiba.venualpha.discover;

import android.util.Log;

import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.example.madiba.venualpha.discover.search.SearchHashtagCell;
import com.example.madiba.venualpha.discover.search.SearchUserCell;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.models.SearchModel;
import com.jaychang.srv.SimpleCell;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/7/2016.
 */

public class TaskSearchLoad implements Callable<List<SimpleCell>> {
    private  String searchTerm;
    public TaskSearchLoad(String searchTerm) {
        this.searchTerm=searchTerm;
    }

    @Override
    public List<SimpleCell> call() throws Exception {
        List<SimpleCell> mSearchDatas = new ArrayList<>();

        ParseQuery<ParseUser> userQuery= ParseUser.getQuery();
        userQuery.whereStartsWith("username", searchTerm);

        ParseQuery<ParseObject> tagQuery= ParseQuery.getQuery("Eventest");
        tagQuery.whereStartsWith("title", searchTerm);
        tagQuery.orderByAscending("shares");
        tagQuery.addAscendingOrder("likes");

        ParseQuery<ParseObject> eventQuery= ParseQuery.getQuery(GlobalConstants.CLASS_EVENT);
        eventQuery.whereStartsWith("title", searchTerm);
        eventQuery.orderByAscending("shares");
        eventQuery.addAscendingOrder("likes");

        // generate final query
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(tagQuery);
        queries.add(eventQuery);


        // extra condition on mainquery
        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByDescending("createdAt");


        for (ParseUser user:userQuery.find()
             ) {
            mSearchDatas.add(new SearchUserCell(ModelGenerator.generateUser(user)));
        }

        for (ParseObject event:mainQuery.find()
                ) {
            mSearchDatas.add(new SearchHashtagCell(ModelGenerator.generateEvent(event)));
        }
//
        return mSearchDatas;
    }
}
