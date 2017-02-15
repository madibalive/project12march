package com.example.madiba.venualpha.models;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Madiba on 2/9/2017.
 */

public class ModelEventGoingContainer {
    private int friendsCount;
    private int globalCount;
    private List<ParseObject> friendsList;
    private List<ParseObject>  glbalList;

    public ModelEventGoingContainer() {
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public int getGlobalCount() {
        return globalCount;
    }

    public void setGlobalCount(int globalCount) {
        this.globalCount = globalCount;
    }

    public List<ParseObject> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<ParseObject> friendsList) {
        this.friendsList = friendsList;
    }

    public List<ParseObject> getGlbalList() {
        return glbalList;
    }

    public void setGlbalList(List<ParseObject> glbalList) {
        this.glbalList = glbalList;
    }
}
