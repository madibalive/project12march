package com.example.madiba.venualpha.Actions;

import com.example.madiba.venualpha.models.MdUserItem;

import java.util.List;

/**
 * Created by Madiba on 3/29/2017.
 */

public class ActionHashtagUsers {
    private List<MdUserItem> userItems;

    public ActionHashtagUsers(List<MdUserItem> userItems) {
        this.userItems = userItems;
    }

    public List<MdUserItem> getUserItems() {
        return userItems;
    }

    public void setUserItems(List<MdUserItem> userItems) {
        this.userItems = userItems;
    }
}
