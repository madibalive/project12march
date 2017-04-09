package com.example.madiba.venualpha.Actions;

import com.parse.ParseObject;

/**
 * Created by Madiba on 10/31/2016.
 */

public class ActionUploudDone {

    public Boolean error ;
    public ParseObject parseObject;

    public ActionUploudDone(Boolean error, ParseObject parseObject) {
        this.error = error;
        this.parseObject = parseObject;
    }
}
