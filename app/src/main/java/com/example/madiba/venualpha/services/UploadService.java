package com.example.madiba.venualpha.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;

import com.applozic.mobicomkit.api.people.ChannelInfo;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.channel.ChannelMetadata;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.madiba.venualpha.Actions.ActionCompleteGossip;
import com.example.madiba.venualpha.BuildConfig;
import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.util.ImageUitls;
import com.example.madiba.venualpha.util.NotificationUtils;
import com.example.madiba.venualpha.util.TimeUitls;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;


public class UploadService extends IntentService {

    //init facebook posting here
    private static final String ACTION_EVENT = "com.example.madiba.venu_v3.servicesv2.action.EVENT";
    private static final String ACTION_VIDEO = "com.example.madiba.venu_v3.servicesv2.action.VIDEO";
    private static final String ACTION_IMAGE = "com.example.madiba.venu_v3.servicesv2.action.IMAGE";
    private static final String ACTION_GOSSIP = "com.example.madiba.venu_v3.servicesv2.action.GOSSIP";
    private static final String EXTRA_ID = "com.example.madiba.venu_v3.servicesv2.extra.ID";
    private static final String EXTRA_CLASSNAME = "com.example.madiba.venu_v3.servicesv2.extra.CLASSNAME";
    private static final String EXTRA_URL = "com.example.madiba.venu_v3.servicesv2.extra.URL";
    private static final String EXTRA_TITLE = "com.example.madiba.venu_v3.servicesv2.extra.TITLE";
    private static final String EXTRA_ISVIDEO = "com.example.madiba.venu_v3.servicesv2.extra.ISVIDEO";

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Timber.d("Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Timber.d("Error: %s", error.toString());
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Timber.d("Success!");
        }
    };

    public UploadService() {
        super("UploadService");
    }

    public static void startActionEvent(Context context, String id, String className,String url,Boolean isVideo) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_EVENT);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_CLASSNAME, className);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_ISVIDEO, isVideo);
        context.startService(intent);
    }


    public static void startActionVideo(Context context, String id, String className,String url) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_VIDEO);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_CLASSNAME, className);
        intent.putExtra(EXTRA_URL, url);
        context.startService(intent);
    }
    public static void startActionImage(Context context, String id, String className,String url) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_VIDEO);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_CLASSNAME, className);
        intent.putExtra(EXTRA_URL, url);
        context.startService(intent);
    }

    public static void startActionGossip(Context context, String title) {
        Intent intent = new Intent(context, UploadService.class);
        intent.setAction(ACTION_VIDEO);
        intent.putExtra(EXTRA_TITLE, title);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_EVENT.equals(action)) {
                final String id = intent.getStringExtra(EXTRA_ID);
                final String className = intent.getStringExtra(EXTRA_CLASSNAME);
                final String url = intent.getStringExtra(EXTRA_URL);
                final Boolean isVideo = intent.getBooleanExtra(EXTRA_ISVIDEO,false);
                handleActionEvent(id, className,url,isVideo);
            } else if (ACTION_VIDEO.equals(action)) {
                final String id = intent.getStringExtra(EXTRA_ID);
                final String className = intent.getStringExtra(EXTRA_CLASSNAME);
                final String url = intent.getStringExtra(EXTRA_URL);
                handleActionVideo(id, className,url);
            }else if (ACTION_IMAGE.equals(action)) {
                final String id = intent.getStringExtra(EXTRA_ID);
                final String className = intent.getStringExtra(EXTRA_CLASSNAME);
                final String url = intent.getStringExtra(EXTRA_URL);
                handleActionImage(id, className,url);
            }else if (ACTION_GOSSIP.equals(action)) {
                final String title = intent.getStringExtra(EXTRA_TITLE);
                handleActionGossip(title);
            }

        }
    }


    private void handleActionEvent(String id, String className, String uri,Boolean isVideo) {
        Timber.e("handleActionEventUploud: started  id: %s image  :  %s classname : %s : is video :%s", id, uri, className,isVideo);

        int vibrantColor = 0;
        int mutedtColor = 0;
        int textcolor = 0;
        if (isVideo) {
            Timber.d("EVENT:UPLOUD:: VIDEO MODE ");

            Map config = new HashMap();

            config.put("cloud_name", BuildConfig.CLOUDINARY_NAME);
            config.put("api_key", BuildConfig.CLOUDINARY_KEY);
            config.put("api_secret", BuildConfig.CLOUDINARY_SECRET);

            Cloudinary cloudinary = new Cloudinary(config);
            String url;

            File file = new File(uri);

            @SuppressWarnings("rawtypes")
            Map cloudinaryResult;
            if (file.exists()) {
                try {
                    cloudinaryResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("resource_type", "video"));
                    url = cloudinaryResult.get("url").toString();

                    // create thumbnail
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(uri, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    thumb.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                    byte[] scaledData = bos.toByteArray();

                    // generate pallete
                    Palette palette = Palette.from(thumb)
                            .clearFilters()
                            .generate();

                    // try the named swatches in preference order
                    if (palette.getVibrantSwatch() != null) {
//                            rippleColor =
//                                    ColorUtils.modifyAlpha(palette.getVibrantSwatch().getRgb(), darkAlpha);

                    } else if (palette.getLightVibrantSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getLightVibrantSwatch().getRgb(),
//                                    lightAlpha);
                    } else if (palette.getDarkVibrantSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getDarkVibrantSwatch().getRgb(),
//                                    darkAlpha);
                    } else if (palette.getMutedSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getMutedSwatch().getRgb(), darkAlpha);
                    } else if (palette.getLightMutedSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getLightMutedSwatch().getRgb(),
//                                    lightAlpha);
                    } else if (palette.getDarkMutedSwatch() != null) {
//                        rippleColor =
//                                ColorUtils.modifyAlpha(palette.getDarkMutedSwatch().getRgb(), darkAlpha);
                    }

                    //save parsefile
                    ParseFile photoFile = new ParseFile(ParseUser.getCurrentUser().getUsername(), scaledData);
                    photoFile.save();

                    //save parse object
                    ParseObject event = ParseObject.createWithoutData(className, id);
                    event.fetchFromLocalDatastore();
                    event.put(GlobalConstants.MEDIA_PALETTE_VIBRANT, vibrantColor);
                    event.put(GlobalConstants.MEDIA_PALETTE_MUTED, mutedtColor);
                    event.put(GlobalConstants.MEDIA_PALETTE_TEXT_COLOR, textcolor);
                    event.put(GlobalConstants.MEDIA_URL, url);
                    event.put(GlobalConstants.MEDIA_IMAGE__THUMB, photoFile);
                    event.put("isVideo", 0);
                    event.save();

                    ParseObject shareObject = new ParseObject(GlobalConstants.CLASS_SHARE);
                    shareObject.put("from", ParseUser.getCurrentUser());
                    shareObject.put("fromID", ParseUser.getCurrentUser().getObjectId());
                    shareObject.put(GlobalConstants.MEDIA_OBJECT, event);
                    shareObject.put("toId", event.getObjectId());
                    shareObject.put(GlobalConstants.MEDIA_TYPE ,3);
                    shareObject.save();

                    ParseObject feed =new ParseObject(GlobalConstants.CLASS_FEED);
                    feed.put("from", ParseUser.getCurrentUser());
                    feed.put("fromId", ParseUser.getCurrentUser().getObjectId());
                    feed.put(GlobalConstants.FEED_TYPE, GlobalConstants.FEED_TYPE_SHARE);
                    feed.put(GlobalConstants.FEED_OBJECT,shareObject);



                    thumb.recycle();
                } catch (RuntimeException | ParseException | IOException e) {
                    Timber.e("upload Error uploading file");
                    ParseObject media = ParseObject.createWithoutData(className, id);
                    media.deleteEventually();

                } finally {
                }

            }

        } else{
            Timber.d("EVENT:UPLOUD:: IMAGE MODE ");

            byte[] originalBytes;
            byte[] compressBytes;
                //get bitmap
                Timber.d("EVENT:UPLOUD:: COMPRESSING IMAGE ");
                final Bitmap bitmap =compressImage(uri);
                if (bitmap != null) {
                    // compress bytes
                    originalBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 100);
                    compressBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 60);
                    ParseFile origImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(), originalBytes);
                    ParseFile cmprImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(), compressBytes);
                    Timber.d("EVENT:UPLOUD:: SAVING IMAGE TO PARSE");

                    try {
                        cmprImgs.save();
                        origImgs.save();

//                        Palette palette = Palette.from(bitmap)
//                                .clearFilters()
//                                .generate();
//
//                        // try the named swatches in preference order
//                        if (palette.getVibrantSwatch() != null) {
////                            rippleColor =
////                                    ColorUtils.modifyAlpha(palette.getVibrantSwatch().getRgb(), darkAlpha);
//
//                        } else if (palette.getLightVibrantSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getLightVibrantSwatch().getRgb(),
////                                    lightAlpha);
//                        } else if (palette.getDarkVibrantSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getDarkVibrantSwatch().getRgb(),
////                                    darkAlpha);
//                        } else if (palette.getMutedSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getMutedSwatch().getRgb(), darkAlpha);
//                        } else if (palette.getLightMutedSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getLightMutedSwatch().getRgb(),
////                                    lightAlpha);
//                        } else if (palette.getDarkMutedSwatch() != null) {
////                        rippleColor =
////                                ColorUtils.modifyAlpha(palette.getDarkMutedSwatch().getRgb(), darkAlpha);
//                        }

                        Timber.d("EVENT:UPLOUD:: GETTING PARSE OBJECT ");


                        ParseObject event = ParseObject.createWithoutData(className, id);
                        event.fetchFromLocalDatastore();
//                        event.put(GlobalConstants.MEDIA_PALETTE_VIBRANT, vibrantColor);
//                        event.put(GlobalConstants.MEDIA_PALETTE_MUTED, mutedtColor);
//                        event.put(GlobalConstants.MEDIA_PALETTE_TEXT_COLOR, textcolor);
                        event.put(GlobalConstants.IMAGE_100, origImgs); // //
                        event.put(GlobalConstants.IMAGE_60, cmprImgs);
                        event.put("isVideo", 1);
                        event.save();

                        Timber.d("EVENT:UPLOUD:: SAVEING PARSE OBJECT ");

                        Timber.d("EVENT:UPLOUD:: CREATING SHARE PARSE OBJECT ");


                        ParseObject shareObject = new ParseObject(GlobalConstants.CLASS_SHARE);
                        shareObject.put(GlobalConstants.FROM, ParseUser.getCurrentUser());
                        shareObject.put(GlobalConstants.FROM_ID, ParseUser.getCurrentUser().getObjectId());
                        shareObject.put(GlobalConstants.OBJECT, event);
                        shareObject.put("toId", event.getObjectId());
                        shareObject.put(GlobalConstants.TYPE, GlobalConstants.SHARE_TYPE_EVENT);
                        shareObject.save();

                        Timber.d("EVENT:UPLOUD:: SAVING PARSE OBJECT ");

                        ParseObject feed =new ParseObject(GlobalConstants.CLASS_FEED);
                        feed.put(GlobalConstants.FROM, ParseUser.getCurrentUser());
                        feed.put(GlobalConstants.FROM_ID, ParseUser.getCurrentUser().getObjectId());
                        feed.put(GlobalConstants.TYPE, GlobalConstants.FEED_TYPE_SHARE);
                        feed.put(GlobalConstants.OBJECT,shareObject);

                        feed.save();

                        Timber.d("EVENT:UPLOUD::FEED SAVE PARSE OBJECT ");


                    } catch (ParseException e) {
                        e.printStackTrace();
                        Timber.d("EVENT:UPLOUD:: ERROR UPLOUDING IMAGE ");
                        ParseObject media = ParseObject.createWithoutData(className, id);
                        media.deleteEventually();

                    }finally {
                        bitmap.recycle();
                        originalBytes = null;
                        compressBytes = null;
                    }
                } else {
                    Timber.d("EVENT:UPLOUD:: FILE DOES NOT EXIST  ");
                }
            }

    }

    /**
     * Handle action Video in the provided background thread with the provided
     * parameters.
     */
    private void handleActionVideo2gif( String id, String classname,String filepath) {
        Map config = new HashMap();

        config.put("cloud_name", BuildConfig.CLOUDINARY_NAME);
        config.put("api_key", BuildConfig.CLOUDINARY_KEY);
        config.put("api_secret", BuildConfig.CLOUDINARY_SECRET);

        Cloudinary cloudinary = new Cloudinary(config);
        String url;

//        File file = new File(filepath);
//        Map cloudinaryResult;
//        if (file.exists()) {
//            try {
//                cloudinaryResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("resource_type", "video")
//                )  ;
//                cloudinary.uploader().upload(file, Arrays.asList(
//                        new Transformation().aspectRatio(2.5),
//                        new Transformation().angle()
//                ))
//                url = cloudinaryResult.get("url").toString();
//
//            } catch (Exception e) {
//
//            } finally {
//
//            }
//        }
    }


    /**
     * Handle action Video in the provided background thread with the provided
     * parameters.
     */
    private void handleActionVideo( String id, String classname,String filepath) {
        Timber.i("handleActionVidUploud2: " + filepath + " " + id + " " + classname);
        Map config = new HashMap();

        config.put("cloud_name", BuildConfig.CLOUDINARY_NAME);
        config.put("api_key", BuildConfig.CLOUDINARY_KEY);
        config.put("api_secret", BuildConfig.CLOUDINARY_SECRET);

        Cloudinary cloudinary = new Cloudinary(config);
        String url;
        int vibrantColor = 0;
        int mutedtColor = 0;
        int textcolor =0;
        File file = new File(filepath);




        @SuppressWarnings("rawtypes")
        Map cloudinaryResult;
        if (file.exists()) {
            try {
                cloudinaryResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("resource_type", "video"));
                url = cloudinaryResult.get("url").toString();

                // create thumbnail
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                thumb.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] scaledData = bos.toByteArray();

                // generate pallete
                Palette palette = Palette.from(thumb)
                        .clearFilters()
                        .generate();

                // try the named swatches in preference order
                if (palette.getVibrantSwatch() != null) {
//                            rippleColor =
//                                    ColorUtils.modifyAlpha(palette.getVibrantSwatch().getRgb(), darkAlpha);

                } else if (palette.getLightVibrantSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getLightVibrantSwatch().getRgb(),
//                                    lightAlpha);
                } else if (palette.getDarkVibrantSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getDarkVibrantSwatch().getRgb(),
//                                    darkAlpha);
                } else if (palette.getMutedSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getMutedSwatch().getRgb(), darkAlpha);
                } else if (palette.getLightMutedSwatch() != null) {
//                            rippleColor = ColorUtils.modifyAlpha(palette.getLightMutedSwatch().getRgb(),
//                                    lightAlpha);
                } else if (palette.getDarkMutedSwatch() != null) {
//                        rippleColor =
//                                ColorUtils.modifyAlpha(palette.getDarkMutedSwatch().getRgb(), darkAlpha);
                }

                //save parsefile
                ParseFile photoFile = new ParseFile(ParseUser.getCurrentUser().getUsername(), scaledData);
                photoFile.save();

                //save parse object
                ParseObject media = ParseObject.createWithoutData(classname, id);

                media.fetchFromLocalDatastore();
                media.put(GlobalConstants.MEDIA_URL, url);
                media.put(GlobalConstants.MEDIA_PALETTE_VIBRANT,vibrantColor);
                media.put(GlobalConstants.MEDIA_PALETTE_MUTED,mutedtColor);
                media.put(GlobalConstants.MEDIA_PALETTE_TEXT_COLOR,textcolor);
                media.put(GlobalConstants.MEDIA_IMAGE_ORIGINAL,photoFile);
                media.put(GlobalConstants.MEDIA_IMAGE__THUMB,photoFile);
                media.put(GlobalConstants.MEDIA_TYPE,1);
                media.save();

                ParseObject feed =new ParseObject(GlobalConstants.CLASS_FEED);
                feed.put("from", ParseUser.getCurrentUser());
                feed.put("fromId", ParseUser.getCurrentUser().getObjectId());
                feed.put(GlobalConstants.FEED_TYPE, GlobalConstants.FEED_TYPE_MEDIA);
                feed.put(GlobalConstants.FEED_OBJECT,media);
                if (media.getInt("isPrivate")==1){
                    feed.put(GlobalConstants.FEED_ISPRIVATE,false);
                    feed.put(GlobalConstants.FEED_PRIVATE_LIST, SingletonDataSource.getInstance().getPrivateUserList());
                }
                feed.save();

                thumb.recycle();
            } catch (RuntimeException |ParseException | IOException e) {
                Timber.e("Error uploading file");
                ParseObject media = ParseObject.createWithoutData(classname, id);
                media.deleteEventually();

            } finally {

            }

        }
    }



    /**
     * Handle action Video in the provided background thread with the provided
     * parameters.
     */

    private void handleActionImage(String id, String className,String url) {

        // intial variable
        byte[] originalBytes;
        byte[] compressBytes;
        float darkAlpha = 0.25f;
        float lightAlpha =0.5f;
        int vibrantColor = 0;
        int mutedtColor = 0;
        int textcolor =0;

        if (ImageUitls.exists(getApplicationContext(),url)){
            //get bitmap
            final Bitmap bitmap = BitmapFactory.decodeFile(url);
            if (bitmap !=null){
                // compress bytes
                originalBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG,100);
                compressBytes = ImageUitls.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG,20);
                ParseFile origImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(),originalBytes);
                ParseFile cmprImgs = new ParseFile(ParseUser.getCurrentUser().getUsername(),compressBytes);

                try {
                    cmprImgs.save();
                    origImgs.save();

//                    Palette palette = Palette.from(bitmap)
//                            .clearFilters()
//                            .generate();
//
//                    // try the named swatches in preference order
//                    if (palette.getVibrantSwatch() != null) {
////                            rippleColor =
////                                    ColorUtils.modifyAlpha(palette.getVibrantSwatch().getRgb(), darkAlpha);
//
//                    } else if (palette.getLightVibrantSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getLightVibrantSwatch().getRgb(),
////                                    lightAlpha);
//                    } else if (palette.getDarkVibrantSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getDarkVibrantSwatch().getRgb(),
////                                    darkAlpha);
//                    } else if (palette.getMutedSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getMutedSwatch().getRgb(), darkAlpha);
//                    } else if (palette.getLightMutedSwatch() != null) {
////                            rippleColor = ColorUtils.modifyAlpha(palette.getLightMutedSwatch().getRgb(),
////                                    lightAlpha);
//                    } else if (palette.getDarkMutedSwatch() != null) {
////                        rippleColor =
////                                ColorUtils.modifyAlpha(palette.getDarkMutedSwatch().getRgb(), darkAlpha);
//                    }

                    ParseObject media = ParseObject.createWithoutData(className, id);
                    media.fetchFromLocalDatastore();
//                    media.put(GlobalConstants.MEDIA_PALETTE_VIBRANT,vibrantColor);
//                    media.put(GlobalConstants.MEDIA_PALETTE_MUTED,mutedtColor);
//                    media.put(GlobalConstants.MEDIA_PALETTE_TEXT_COLOR,textcolor);
                    media.put(GlobalConstants.MEDIA_IMAGE_ORIGINAL,origImgs);
                    media.put(GlobalConstants.MEDIA_IMAGE__THUMB,cmprImgs);
                    media.put(GlobalConstants.MEDIA_TYPE,0);
                    media.save();

                    ParseObject feed =new ParseObject(GlobalConstants.CLASS_FEED);
                    feed.put("from", ParseUser.getCurrentUser());
                    feed.put("fromId", ParseUser.getCurrentUser().getObjectId());
                    feed.put(GlobalConstants.FEED_TYPE, GlobalConstants.FEED_TYPE_MEDIA);
                    feed.put(GlobalConstants.FEED_OBJECT,media);
                    if (media.getInt("isPrivate")==1){

                        feed.put(GlobalConstants.FEED_ISPRIVATE,false);
                        feed.put(GlobalConstants.FEED_PRIVATE_LIST, SingletonDataSource.getInstance().getPrivateUserList());

                    }
                    feed.save();

                    NotificationUtils.sendLocatNotification(getApplicationContext(),"Saved success","success",null);


                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    bitmap.recycle();

                }
            }

        }

    }


    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGossip(String title) {

        ChannelMetadata channelMetadata = new ChannelMetadata();
        channelMetadata.setCreateGroupMessage(ChannelMetadata.ADMIN_NAME + " created " + ChannelMetadata.GROUP_NAME);
        channelMetadata.setAddMemberMessage(ChannelMetadata.ADMIN_NAME + " added " + ChannelMetadata.USER_NAME);
        channelMetadata.setRemoveMemberMessage(ChannelMetadata.ADMIN_NAME + " removed " + ChannelMetadata.USER_NAME);
        channelMetadata.setGroupNameChangeMessage(ChannelMetadata.USER_NAME + " changed group name " + ChannelMetadata.GROUP_NAME);
        channelMetadata.setJoinMemberMessage(ChannelMetadata.USER_NAME + " joined");
        channelMetadata.setGroupLeftMessage(ChannelMetadata.USER_NAME + " left group " + ChannelMetadata.GROUP_NAME);
        channelMetadata.setGroupIconChangeMessage(ChannelMetadata.USER_NAME + " changed icon");
        channelMetadata.setDeletedGroupMessage(ChannelMetadata.ADMIN_NAME + " deleted group " + ChannelMetadata.GROUP_NAME);
        List<String> channelMembersList =  new ArrayList<String>();

        ChannelInfo channelInfo  = new ChannelInfo(title,channelMembersList);
        channelInfo.setType(Channel.GroupType.OPEN.getValue().intValue()); //group type
        channelInfo.setChannelMetadata(channelMetadata); //Optional option for setting group meta data

        Channel channel = ChannelService.getInstance(getApplicationContext()).createChannel(channelInfo); //Thread or Async task
        if(channel != null){
            Timber.i(new StringBuilder().append("channelKey").append(channel.getKey()).toString());
            ParseObject gossip =new ParseObject(GlobalConstants.CLASS_GOSSIP);
            gossip.put("title", title);
            gossip.put("shares",0);
            gossip.put("likes",0);
            gossip.put("from", ParseUser.getCurrentUser());
            gossip.put("fromId", ParseUser.getCurrentUser().getObjectId());
            gossip.put("chatId",channel.getKey());
            gossip.put("expiryDate", TimeUitls.addExpiryDate());
            try {
                gossip.save();

                ParseObject feed =new ParseObject(GlobalConstants.CLASS_FEED);
                feed.put("from", ParseUser.getCurrentUser());
                feed.put("fromId", ParseUser.getCurrentUser().getObjectId());
                feed.put(GlobalConstants.FEED_TYPE, GlobalConstants.FEED_TYPE_GOSSIP);
                feed.put(GlobalConstants.FEED_GOSSIPID,channel.getKey());
                feed.put(GlobalConstants.FEED_EXPIRY_DATE, TimeUitls.addExpiryDate());
                feed.put(GlobalConstants.FEED_OBJECT,gossip);
                feed.save();

                EventBus.getDefault().post(new ActionCompleteGossip(false, channel.getKey()));

            } catch (ParseException e) {
                EventBus.getDefault().post(new ActionCompleteGossip(false, 0));
                e.printStackTrace();
            }

        }else {
        }
    }

    private boolean hasPublishPermission () {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private void postToFb(Bitmap image) {

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);


    }

    private void postToFb(Uri videoUrl) {
        ShareVideo video = new ShareVideo.Builder()
                .setLocalUrl(videoUrl)
                .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(video)
                .build();

        ShareApi.share(content, null);

    }
    
    
    private void sendInviteNotification(){
        // TODO: 2/4/2017  
        
        
    }

    private void onCreateFeed(){
        ParseObject feed =new ParseObject(GlobalConstants.CLASS_FEED);
        feed.put("from", ParseUser.getCurrentUser());
        feed.put("fromId", ParseUser.getCurrentUser().getObjectId());
        feed.put(GlobalConstants.FEED_TYPE,1);
        feed.put(GlobalConstants.FEED_GOSSIPID,"");
        feed.put(GlobalConstants.FEED_ISPRIVATE,false);
        feed.put(GlobalConstants.FEED_PRIVATE_LIST,"");
        feed.put("expiryDate","");
        feed.put(GlobalConstants.FEED_OBJECT,"");
        try {
            feed.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Bitmap compressImage(String imageUri) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imageUri, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(imageUri, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Timber.d( "Exif: %s " , orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Timber.d( "Exif: %s " , orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Timber.d( "Exif: %s " , orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Timber.d( "Exif: %s " , orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timber.i("IMAGE SIZE RETURNED , %S",ImageUitls.humanReadableByteCount(scaledBitmap.getByteCount(),true));


//        FileOutputStream out = null;
//        String filename = getFilename();
//        try {
//            out = new FileOutputStream(filename);
//
////          write the compressed bitmap at the destination specified by filename.
//            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        return scaledBitmap;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}
