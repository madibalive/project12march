package com.example.madiba.venualpha.profiles;

import com.example.madiba.venualpha.Actions.ActionCheck;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.concurrent.Callable;

/**
 * Created by Madiba on 12/10/2016.
 */

public class TaskGetRelationship implements Callable<ActionCheck> {

    private final String  id;

    public TaskGetRelationship(String  id) {
        this.id = id;
    }

    @Override
    public ActionCheck call() throws Exception {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalConstants.CLASS_FOLLOW);
        query.whereEqualTo("to", ParseUser.createWithoutData(ParseUser.class,id));
        query.whereEqualTo("From", ParseUser.getCurrentUser());
        ActionCheck check;
        if (query.getFirst() !=null){
             check = new ActionCheck(query.getFirst().getObjectId(),true);
        }else
             check = new ActionCheck(query.getFirst().getObjectId(),true);
        return check;
    }
}
