package com.example.madiba.venualpha.adapter;

import com.example.madiba.venualpha.models.ModelFeedItem;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Madiba on 12/8/2016.
 */
public class SingletonDataSource {

    private List<ParseObject> galleryPagerData;
    private ParseObject currentComment;
    private ParseObject currentEvent;
    private ParseObject currentPhoto;
    private ParseObject eventPageObject;
    private ParseObject currentVideo;
    private ParseUser currentUser;
    private ArrayList<String> privateUserList;
    private ModelFeedItem currentFeedItem;

    //    INVITE DATA FILE
    private List<ParseUser> inviteUserList = new ArrayList<>();
    private Set<ParseUser> onboardUserList = new HashSet<>();
    private Set<ParseObject> onboardCategories = new HashSet<>();
    private List<String> inviteIdList = new ArrayList<>();

    public int getRotateX() {
        return RotateX;
    }

    public void setRotateX(int rotateX) {
        RotateX = rotateX;
    }

    private int RotateX;


    private static SingletonDataSource ourInstance = new SingletonDataSource();

    public static SingletonDataSource getInstance() {
        return ourInstance;
    }

    private SingletonDataSource() {
    }

    public List<ParseObject> getgalleryPagerData() {
        return galleryPagerData;
    }

    public void setgalleryPagerData(List<ParseObject> pagerdata) {
        galleryPagerData = pagerdata;
    }

    public ParseObject getCurrentComment() {
        return currentComment;
    }

    public void setCurrentComment(ParseObject currentComment) {
        this.currentComment = currentComment;
    }

    public ParseObject getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(ParseObject currentEvent) {
        this.currentEvent = currentEvent;
    }

    public ParseObject getCurrentPhoto() {
        return currentPhoto;
    }

    public void setCurrentPhoto(ParseObject currentPhoto) {
        this.currentPhoto = currentPhoto;
    }

    public ParseObject getCurrentVideo() {
        return currentVideo;
    }

    public void setCurrentVideo(ParseObject currentVideo) {
        this.currentVideo = currentVideo;
    }

    public ParseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(ParseUser currentUser) {
        this.currentUser = currentUser;
    }

    public ModelFeedItem getCurrentFeedItem() {
        return currentFeedItem;
    }

    public void setCurrentFeedItem(ModelFeedItem currentFeedItem) {
        this.currentFeedItem = currentFeedItem;
    }

    public ArrayList<String> getPrivateUserList() {
        return privateUserList;
    }

    public void setPrivateUserList(ArrayList<String> privateUserList) {
        this.privateUserList = privateUserList;
    }

    public List<ParseUser> getInviteUserList() {
        return inviteUserList;
    }

    public void setInviteUserList(List<ParseUser> inviteUserList) {
        this.inviteUserList = inviteUserList;
    }

    public List<String> getInviteIdList() {
        return inviteIdList;
    }

    public void setInviteIdList(List<String> inviteIdList) {
        this.inviteIdList = inviteIdList;
    }

    public ParseObject getEventPageEvent() {
        return eventPageObject;
    }

    public void setEventPageEvent(ParseObject eventPageObject) {
        this.eventPageObject = eventPageObject;
    }

    public Set<ParseUser> getOnboardUserList() {
        return onboardUserList;
    }

    public void setOnboardUserList(Set<ParseUser> onboardUserList) {
        this.onboardUserList = onboardUserList;
    }

    public Set<ParseObject> getOnboardCategories() {
        return onboardCategories;
    }

    public void setOnboardCategories(Set<ParseObject> onboardCategories) {
        this.onboardCategories = onboardCategories;
    }
}
