package com.example.madiba.venualpha.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.madiba.venualpha.Actions.ActionUploudDone;
import com.example.madiba.venualpha.Actions.ActionUploudStart;
import com.example.madiba.venualpha.models.MdPostEventItem;
import com.example.madiba.venualpha.models.MdPostMemoryItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.models.VenuFile;
import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.example.madiba.venualpha.util.ImageUitls;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ServiceUplouder extends IntentService {

    private static final String ACTION_MEMORY_UPDATE = "com.example.madiba.venualpha.services.action.MEMORY_UPDATE";
    private static final String ACTION_MEMORY = "com.example.madiba.venualpha.services.action.MEMORY";
    private static final String ACTION_EVENT = "com.example.madiba.venualpha.services.action.EVENT";

    private static final String EXTRA_PARAM1 = "com.example.madiba.venualpha.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.madiba.venualpha.services.extra.PARAM2";

    public ServiceUplouder() {
        super("ServiceUplouder");
    }


    public static void uploudMemory(Context context, MdPostMemoryItem param1) {
        Intent intent = new Intent(context, ServiceUplouder.class);
        intent.setAction(ACTION_MEMORY);
        intent.putExtra(EXTRA_PARAM1, Parcels.wrap(param1));
        context.startService(intent);
    }

    public static void updateMemory(Context context, MdPostMemoryItem param1) {
        Intent intent = new Intent(context, ServiceUplouder.class);
        intent.setAction(ACTION_MEMORY_UPDATE);
        intent.putExtra(EXTRA_PARAM1, Parcels.wrap(param1));
        context.startService(intent);
    }


    public static void uploudEvent(Context context, MdPostEventItem md) {
        Intent intent = new Intent(context, ServiceUplouder.class);
        intent.setAction(ACTION_EVENT);
        intent.putExtra(EXTRA_PARAM1, Parcels.wrap(md));
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MEMORY.equals(action)) {
                final MdPostMemoryItem param1 =Parcels.unwrap(intent.getParcelableExtra(EXTRA_PARAM1));

                handleActionMemory(param1);
            } else if (ACTION_EVENT.equals(action)) {
                final MdPostEventItem param1 =Parcels.unwrap(intent.getParcelableExtra(EXTRA_PARAM1));
                handleActionEvent(param1);
            }
            else if (ACTION_MEMORY_UPDATE.equals(action)) {
                final MdPostMemoryItem param1 =Parcels.unwrap(intent.getParcelableExtra(EXTRA_PARAM1));
                handleActionUpdateMemory(param1);
            }
        }
    }


    private void handleActionUpdateMemory(MdPostMemoryItem item) {
        ParseObject peep = ParseObject.createWithoutData("","");
        try {

            peep.put("lastUpdateMessage", ParseUser.getCurrentUser().getUsername() + " Updated");

            List<ParseObject> media;

            if (item.getVideo()){
                media = uploudManager(item.getVenuFile(),item);
            }else {
                media = uploudManager(item.getUrls(),item);

            }

            peep.put("mediaCount",peep.getInt("mediaCount")+media.size());

            ParseRelation<ParseObject> relation = peep.getRelation("media");
            for (ParseObject object : media) {
                relation.add(object);
            }

            peep.save();
        }catch (Exception e){

        }


    }


    private void handleActionMemory(MdPostMemoryItem item) {
        EventBus.getDefault().postSticky(new ActionUploudStart(true));

        try {


            item.getInvites().add(ModelGenerator.generateUser(ParseUser.getCurrentUser()));

            ParseObject peep = new ParseObject("Peep");
            peep.put("event", ParseObject.createWithoutData("Eventest", item.getMdEventItem().getParseId()));
            peep.put("hashTag", item.getHashTag());
            peep.put("searchOnTag", item.getHashTag().toLowerCase().trim());
            peep.put("lastUpdateMessage", ParseUser.getCurrentUser().getUsername() + " Created");
            peep.put("owner", ParseUser.getCurrentUser());

            if (item.getLinked()){
                peep.put("type",1);
            }else {
                peep.put("type",0);
            }

            ParseRelation<ParseObject> relationMembers = peep.getRelation("members");
            for (MdUserItem userItem : item.getInvites()) {
                relationMembers.add(ParseUser.createWithoutData(ParseUser.class, userItem.getParseId()));
            }
            List<String> memberIds =new ArrayList<>();
            for (MdUserItem userItem : item.getInvites()) {
                memberIds.add(userItem.getParseId());
            }
            peep.put("memberIds", memberIds);
            peep.put("memberCount", item.getInvites().size());


            List<ParseObject> media;


            if (item.getVideo()){
                media = uploudManager(item.getVenuFile(),item);
            }else {
                media = uploudManager(item.getUrls(),item);
            }
            peep.put("mediaCount",media.size());
            ParseRelation<ParseObject> relationMedia = peep.getRelation("media");
            for (ParseObject object : media) {
                relationMedia.add(object);
            }

            peep.save();
            EventBus.getDefault().post(new ActionUploudDone(false,peep));
            Log.e("SERVICEUPLOUD", "handleActionEvent: successs");

        }catch (Exception e){
            Log.e("SERVICEUPLOUD", "handleActionEvent: ERRO " +e);
            EventBus.getDefault().post(new ActionUploudDone(true,null));

            e.printStackTrace();
        }
    }

    private void handleActionEvent(MdPostEventItem item) {

        ParseObject event = new ParseObject("Eventest");

        try {
            EventBus.getDefault().postSticky(new ActionUploudStart(true));

            event.put("title",item.getTitle());
            event.put("hashTag",item.getHashtag());
            event.put("searchOnTag",item.getHashtag().toLowerCase().trim());
            event.put("time",item.getTime());
            event.put("date",item.getDate());
            event.put("address",item.getLocation());
            event.put("coordinate",new ParseGeoPoint(item.getCordinate().latitude,item.getCordinate().longitude));
            event.put("desc",item.getDesc());
            event.put("contact",item.getContact());
            event.put("features",item.getFeatures());
            event.put("theme",item.getTheme());
            event.put("isVideo",false);
//            if (item.getType()==4){
//                event.put("price",item.getPrice());
//            }

            event.put("url100", uploudImage(item.getPath(),100));
            event.put("url50", uploudImage(item.getPath(),50));
            event.save();

            if (item.getInvites() !=null || item.getInvites().size()>1)
                afterEventSequence1(event.getObjectId(),item.getInvites());

            EventBus.getDefault().post(new ActionUploudDone(false,event));

            Log.e("SERVICEUPLOUD", "handleActionEvent: successs");

        } catch (Exception e) {
            Log.e("SERVICEUPLOUD", "handleActionEvent: ERRO " +e);
            EventBus.getDefault().post(new ActionUploudDone(true,null));

            e.printStackTrace();
        }

    }

    private void afterEventSequence1(String eventId,List<MdUserItem> userItems){
        final List<String> userIds = new ArrayList<>();
        for (MdUserItem item:userItems
                ) {
            userIds.add(item.getParseId());
        }

        final Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("otherUserIds", userIds);
        expectedParams.put("eventId", eventId);

        ParseCloud.callFunctionInBackground("createInvite", expectedParams, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e==null){
                    Log.e("Chat", "done: object:: "+object.toString() );

                }else
                    Log.e("Chat", "done: error ::"+e.getMessage() );

            }
        });
    }


    private List<ParseObject> uploudManager(List<VenuFile> venuFiles,MdPostMemoryItem item){
        List<ParseObject> returnData = new ArrayList<>();

        for (VenuFile file:venuFiles
                ) {
            ParseObject media =new ParseObject("MediaV2");
            media.put("from", ParseUser.getCurrentUser());
            media.put("fromId", ParseUser.getCurrentUser().getObjectId());
            media.put("type",0);
            media.put("url100",uploudImage(file.getUrl(),100));
            media.put("url50",uploudImage(file.getUrl(),50));

            media.put("hashTag", item.getHashTag());
            media.put("searchOnTag", item.getHashTag().toLowerCase().trim());
            try {
                media.save();
                returnData.add(media);
                Log.e("SERVICEUPLOUD", "uploudManager: successs");

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e("SERVICEUPLOUD", "uploudManager: ERRO " +e.getMessage());

            }
        }
        Log.e("SERVICEUPLOUD", "uploudManager: size " +returnData.size());

        return returnData;
    }


    private List<ParseObject> uploudManager(VenuFile venuFile,MdPostMemoryItem item){
        List<ParseObject> returnData = new ArrayList<>();

        ParseObject media =new ParseObject("MediaV2");
        media.put("from", ParseUser.getCurrentUser());
        media.put("fromId", ParseUser.getCurrentUser().getObjectId());
        media.put("type",1);
        media.put("url100",uploudThumbnail(venuFile.getUrl(),100));
        media.put("url50",uploudThumbnail(venuFile.getUrl(),50));
        media.put("urlVideo",uploudVideo(venuFile.getUrl()));


        media.put("hashTag", item.getHashTag());
        media.put("searchOnTag", item.getHashTag().toLowerCase().trim());

        try {
            media.save();
            returnData.add(media);
            Log.e("SERVICEUPLOUD", "uploudManager: successs");

        } catch (ParseException e) {
            Log.e("SERVICEUPLOUD", "uploudManager: ERRO " +e.getMessage());

            e.printStackTrace();
        }
        Log.e("SERVICEUPLOUD", "uploudManager: size " +returnData.size());
        return returnData;

    }

    private ParseFile uploudImage(String file,int compress){
        byte[] byteData;
        ParseFile returnData;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(file);
            bitmap = ImageUitls.rotateImageIfRequired(bitmap,Uri.parse(file));
            byteData = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, compress);
            returnData = new ParseFile(ParseUser.getCurrentUser().getUsername(),byteData);
            returnData.save();
            bitmap.recycle();
            Log.e("SERVICEUPLOUD", "uploudImage: successs");

            return returnData;
        } catch (Exception  e) {
            Log.e("SERVICEUPLOUD", "uploudImage: ERRO " +e.getMessage());

            e.printStackTrace();
            return  null;
        }finally {

        }
    }

    private ParseFile uploudThumbnail(String file,int compress){
        byte[] byteData;
        ParseFile returnData;
        try {
            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(file, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            bitmap = ImageUitls.rotateImageIfRequired(bitmap,Uri.parse(file));
            byteData = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, compress);
            returnData = new ParseFile(ParseUser.getCurrentUser().getUsername(),byteData);
            returnData.save();
            bitmap.recycle();
            Log.e("SERVICEUPLOUD", "uploudImage: successs");

            return returnData;
        } catch (Exception  e) {
            Log.e("SERVICEUPLOUD", "uploudImage: ERRO " +e.getMessage());

            e.printStackTrace();
            return  null;
        }finally {

        }
    }


    private ParseFile uploudVideo(String file){
        ParseFile returnData;
        FileInputStream fileInputStream = null;
        File fileObj = new File(file);
        byte[] byteData = new byte[(int) fileObj.length()];

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(fileObj);
            fileInputStream.read(byteData);
            returnData = new ParseFile(ParseUser.getCurrentUser().getUsername(),byteData);
            returnData.save();
            Log.e("SERVICEUPLOUD", "uploudVideo: successs");

            fileInputStream.close();
            return returnData;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SERVICEUPLOUD", "uploudVideo: ERRO " +e.getMessage());

            return null;
        }

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(fileObj);
//        } catch (FileNotFoundException e) {
//
//
//        });
//
//        byte[] buf = new byte[1024];
//        int n;
//        while (-1 != (n = fis.read(buf)))
//            baos.write(buf, 0, n);
//
//        byte[] videoBytes = baos.toByteArray(); //
    }




}
