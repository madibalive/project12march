package com.example.madiba.venualpha.Actions;

/**
 * Created by Madiba on 10/31/2016.
 */

public class ActionCheck {

    public Boolean error = false;
    public Boolean isFollow = true;

    public ActionCheck(Boolean isFollow, Boolean error) {
        this.error = error;
        this.isFollow=isFollow;
    }

}
