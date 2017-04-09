package com.example.madiba.venualpha.Actions;

import com.example.madiba.venualpha.models.MdUserItem;

import java.util.List;

/**
 * Created by Madiba on 10/31/2016.
 */

public class ActionInvitesList {
    public int size;
    public List<MdUserItem> userItems;

    public ActionInvitesList(int size, List<MdUserItem> userItems) {
        this.size = size;
        this.userItems = userItems;
    }
}
