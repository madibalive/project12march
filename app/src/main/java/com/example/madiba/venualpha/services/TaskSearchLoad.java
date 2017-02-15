package com.example.madiba.venualpha.services;

import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.SearchModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import timber.log.Timber;

/**
 * Created by Madiba on 12/7/2016.
 */

public class TaskSearchLoad implements Callable<List<SearchModel>> {
    private  String searchTerm;
    public TaskSearchLoad(String searchTerm) {
        this.searchTerm=searchTerm;
    }

    @Override
    public List<SearchModel> call() throws Exception {
        List<SearchModel> mSearchDatas = new ArrayList<>();

        Timber.i("searchterm :%s",searchTerm);

        ParseQuery<ParseUser> userQuery= ParseUser.getQuery();
        userQuery.whereStartsWith("username", searchTerm);

        ParseQuery<ParseObject> GossipQuery= ParseQuery.getQuery("EventsTest");
        GossipQuery.whereStartsWith("title", searchTerm);
        GossipQuery.orderByAscending("shares");
        GossipQuery.addAscendingOrder("likes");

        ParseQuery<ParseObject> eventQuery= ParseQuery.getQuery(GlobalConstants.CLASS_EVENT);
        eventQuery.whereStartsWith("title", searchTerm);
        eventQuery.orderByAscending("shares");
        eventQuery.addAscendingOrder("likes");
//
        SearchModel peoples = new SearchModel("People", SearchModel.PEOPLE);
        peoples.setUsers(userQuery.find());
        Timber.i("peoples %s",peoples.getUsers().size());

        SearchModel gossips = new SearchModel("Gossip", SearchModel.GOSSIP);
        gossips.setmData(GossipQuery.find());
        Timber.i("gossips %s",gossips.getData().size());


        SearchModel events = new SearchModel("Event", SearchModel.EVENT);
        events.setmData(eventQuery.find());
        Timber.i("events %s",events.getData().size());

        mSearchDatas.add(gossips);
        mSearchDatas.add(peoples);
        mSearchDatas.add(events);

        Timber.i("%s",mSearchDatas.size());

        return mSearchDatas;
    }
}
