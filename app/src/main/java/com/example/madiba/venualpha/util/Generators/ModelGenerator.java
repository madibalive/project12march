package com.example.madiba.venualpha.util.Generators;

import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdGossip;
import com.example.madiba.venualpha.models.MdMediaItem;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.util.TimeUitls;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 3/9/2017.
 */

public class ModelGenerator {

    public ModelGenerator() {
    }


    public static MdUserItem generateUser(ParseUser user){
        MdUserItem item = new MdUserItem();
        item.setParseId(user.getObjectId());
        item.setName(user.getUsername());
        item.setPhone(user.getString("phone"));
        item.setDate(user.getCreatedAt());
        item.setType(0);

        if (user.getParseFile("avatarSmall")!=null)
            item.setAvatarSmall(user.getParseFile("avatarSmall").getUrl());
        if (user.getParseFile("avatarLarge")!=null)
            item.setAvatarLarge(user.getParseFile("avatarLarge").getUrl());

        item.setDateToString(TimeUitls.getRelativeTime(user.getCreatedAt()));
        return item;
    }

    public static MdEventItem generateEvent(ParseObject object){
        MdEventItem item = new MdEventItem();

        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setComment(object.getInt("comment"));
        item.setHashtag(object.getString("tag"));
        item.setTrackers(object.getInt("rxn"));
        item.setUserItem(generateUser(object.getParseUser("from")));

        if (object.getParseFile("avatarSmall")!=null)
            item.setUrlLarge(object.getParseFile("avatarSmall").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlMedium(object.getParseFile("avatarLarge").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlSmall(object.getParseFile("avatarLarge").getUrl());

        item.setEvDate(object.getCreatedAt());
        item.setEvDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setEvTime(object.getCreatedAt());
        item.setEvTimeToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setEvDesc(object.getString("desc"));
        item.setEvTitle(object.getString("title"));
        item.setEvLocation(object.getString("address"));

//        if (object.getInt("type") == pay)
//            item.setEvPrice(object.getInt("price"));
        return item;
    }

    public static MdEventItem generateEvent(ParseObject object,List<ParseObject> matchList){
        MdEventItem item = new MdEventItem();

        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setComment(object.getInt("comment"));
        item.setHashtag(object.getString("tag"));
        item.setTrackers(object.getInt("rxn"));
        item.setUserItem(generateUser(object.getParseUser("from")));

        if (matchList.contains(object))
            item.setStatus(true);
        else
            item.setStatus(true);

        if (object.getParseFile("avatarSmall")!=null)
            item.setUrlLarge(object.getParseFile("avatarSmall").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlMedium(object.getParseFile("avatarLarge").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlSmall(object.getParseFile("avatarLarge").getUrl());

        item.setEvDate(object.getCreatedAt());
        item.setEvDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setEvTime(object.getCreatedAt());
        item.setEvTimeToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setEvDesc(object.getString("desc"));
        item.setEvTitle(object.getString("title"));
        item.setEvLocation(object.getString("address"));

//        if (object.getInt("type") == pay)
//            item.setEvPrice(object.getInt("price"));
        return item;
    }

    public static MdMediaItem generateMedia(ParseObject object){
        MdMediaItem item = new MdMediaItem();
        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setCommentCount(object.getInt("comment"));
        item.setHashtag(object.getString("tag"));
        item.setRxnCount(object.getInt("rxn"));
        item.setUserItem(generateUser(object.getParseUser("from")));
        item.setEventItem(generateEvent(object.getParseObject("event")));

        if (object.getParseFile("avatarSmall")!=null)
            item.setUrlLarge(object.getParseFile("avatarSmall").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlMedium(object.getParseFile("avatarLarge").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlSmall(object.getParseFile("avatarLarge").getUrl());

        return item;
    }
    public static MdMediaItem generateMedia(ParseObject object,List<ParseObject> matchList){
        MdMediaItem item = new MdMediaItem();
        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));

        item.setCommentCount(object.getInt("comment"));
        item.setHashtag(object.getString("tag"));
        item.setRxnCount(object.getInt("rxn"));
        item.setUserItem(generateUser(object.getParseUser("from")));
        item.setEventItem(generateEvent(object.getParseObject("event")));

        if (matchList.contains(object))
            item.setStatus(true);
        else
            item.setStatus(true);

        if (object.getParseFile("avatarSmall")!=null)
            item.setUrlLarge(object.getParseFile("avatarSmall").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlMedium(object.getParseFile("avatarLarge").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlSmall(object.getParseFile("avatarLarge").getUrl());

        return item;
    }

    public static MdMemoryItem generateMemmory(ParseObject object){
        List<MdMediaItem> mdMediaItems = new ArrayList<>();
        List<MdUserItem> mdUserItems = new ArrayList<>();

        MdMemoryItem item = new MdMemoryItem();

        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));
        item.setViews(object.getInt("views"));
        item.setHashTag(object.getString("tag"));
        item.setMediaCount(object.getInt("mediaCount"));
        item.setMemberCount(object.getInt("memberCount"));

        item.setUserItem(generateUser(object.getParseUser("from")));
        item.setEventItem(generateEvent(object.getParseObject("event")));

        return item;
    }


    public static MdMemoryItem generateMemmory(ParseObject object,List<ParseObject> matchList){
        List<MdMediaItem> mdMediaItems = new ArrayList<>();
        List<MdUserItem> mdUserItems = new ArrayList<>();

        MdMemoryItem item = new MdMemoryItem();

        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));
        item.setViews(object.getInt("views"));
        item.setHashTag(object.getString("tag"));
        item.setMediaCount(object.getInt("mediaCount"));
        item.setMemberCount(object.getInt("memberCount"));


        ParseRelation<ParseUser> memberQuery = object.getRelation("members");
        ParseRelation<ParseObject> mediaQuery = object.getRelation("medias");
        try {
            for (ParseUser user:memberQuery.getQuery().find()
                    ) {
                mdUserItems.add(generateUser(user));
            }

            for (ParseObject obj:mediaQuery.getQuery().find()
                    ) {
                mdMediaItems.add(generateMedia(obj));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        item.setUserItem(generateUser(object.getParseUser("from")));
        item.setEventItem(generateEvent(object.getParseObject("event")));

        return item;
    }

    public static MdGossip generateGossip(ParseObject object){
        MdGossip item = new MdGossip();

        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));
        item.setViews(object.getInt("views"));
        item.setTitle(object.getString("title"));
        item.setColor(object.getString("color"));

        item.setUserItem(generateUser(object.getParseUser("from")));

        if (object.getParseFile("avatarSmall")!=null)
            item.setUrlSmall(object.getParseFile("avatarSmall").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlLarge(object.getParseFile("avatarLarge").getUrl());
        return item;
    }

    public static MdGossip generateGossip(ParseObject object,List<ParseObject> matchList){
        MdGossip item = new MdGossip();

        item.setParseId(object.getObjectId());
        item.setDate(object.getCreatedAt());
        item.setDateToString(TimeUitls.getRelativeTime(object.getCreatedAt()));
        item.setViews(object.getInt("views"));
        item.setTitle(object.getString("title"));
        item.setColor(object.getString("color"));

        if (matchList.contains(object))
            item.setStatus(true);
        else
            item.setStatus(true);


        item.setUserItem(generateUser(object.getParseUser("from")));

        if (object.getParseFile("avatarSmall")!=null)
            item.setUrlSmall(object.getParseFile("avatarSmall").getUrl());
        if (object.getParseFile("avatarLarge")!=null)
            item.setUrlLarge(object.getParseFile("avatarLarge").getUrl());
        return item;
    }


}
