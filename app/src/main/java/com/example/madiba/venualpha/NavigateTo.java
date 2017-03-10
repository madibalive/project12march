package com.example.madiba.venualpha;

import android.content.Context;
import android.support.v4.app.FragmentManager;


/**
 * Created by Madiba on 12/8/2016.
 */

public class NavigateTo {

    public static void goToUserPage(Context context,String id){
//        Intent intent = new Intent(context, PhotoHolderActivity.class);
//        intent.putExtra(GlobalConstants.PASS_ID, id);
//        context.startActivity(intent);
    }

    public static void gotoPhotoViewer(Context context,String id, String className){
//        Intent intent = new Intent(context, PhotoHolderActivity.class);
//        intent.putExtra(GlobalConstants.PASS_ID, id);
//        intent.putExtra(GlobalConstants.PASS_CLASSNAME, className);
//        context.startActivity(intent);


    }

    public static void goToVideoViewer(Context context,String id, String className){
//        Intent intent = new Intent(context, PhotoHolderActivity.class);
//        intent.putExtra(GlobalConstants.PASS_ID, id);
//        intent.putExtra(GlobalConstants.PASS_CLASSNAME, className);
//        context.startActivity(intent);

    }

    public static void goToEventPage(Context context, String id, String className,int type){
//        Intent intent;
//        switch (type){
//            case 1:
//                intent = new Intent(context, WhiteEventPageActivity.class);
//                break;
//            case 2:
//                intent = new Intent(context, TransparentEventPageActivity.class);
//                break;
//            case 3:
//                intent = new Intent(context, DarkEventPageActivity.class);
//                break;
//            default:
//                intent = new Intent(context, DarkEventPageActivity.class);
//
//        }
//        intent.putExtra(GlobalConstants.PASS_ID, id);
//        intent.putExtra(GlobalConstants.PASS_CLASSNAME, className);
//        context.startActivity(intent);

    }

    public static void goToHashtagGallery(Context context, String id, String className,String hashTag){
//        Intent intent = new Intent(context, PhotoHolderActivity.class);
//        intent.putExtra(GlobalConstants.PASS_ID, id);
//        intent.putExtra(GlobalConstants.PASS_CLASSNAME, className);
//        intent.putExtra("hashtag",hashTag);
//        context.startActivity(intent);
    }

    public static void goToCategory(Context context, String category,int pos){
//        Intent intent = new Intent(context, CategoryActivity.class);
//        intent.putExtra("category",category);
//        intent.putExtra("pos",pos);
//        context.startActivity(intent);
    }

    public static void goToGalleryPager(Context context, int pos,int type,  String id, String className){
//        Intent intent = new Intent(context, PhotoHolderActivity.class);
//        intent.putExtra(GlobalConstants.PASS_ID, id);
//        intent.putExtra("type", id);
//        intent.putExtra("position", id);
//        intent.putExtra(GlobalConstants.PASS_CLASSNAME, className);
//
//        context.startActivity(intent);
    }

    public static void goToGossip(Context context, int chat_id){
//        Intent intent = new Intent(context, ConversationActivity.class);
//        intent.putExtra(ConversationUIService.GROUP_ID,chat_id);
//        context.startActivity(intent);
    }

    public static void gotoComment(Context context, String id, String className, FragmentManager manager){
//        DialogFragment frg = CommentActivityFragment.newInstance(id,className,false);
//        frg.show(manager,"id");
    }
    public static void gotoGoingList(Context context, String id, String className, FragmentManager manager){
//        DialogFragment frg = UserListFragment.newInstance(id,className);
//        frg.show(manager,"id");
    }



    public static void gotoPostMedia(Context context,String url, Boolean type){
//        Intent intent = new Intent(context, PostMediaActivity.class);
//        if (type)
//               intent.putExtra("type",1);
//        else
//            intent.putExtra("type",0);
//        intent.putExtra("url",url);
//        context.startActivity(intent);
    }

    public static void gotoEditEvent(Context context,int flag,String thumb){
//        Intent intent = new Intent(context, PostMediaActivity.class);
//        intent.putExtra("flag",flag);
//        context.startActivity(intent);
    }


}
