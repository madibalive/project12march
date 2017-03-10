package com.example.madiba.venualpha.viewer.defaultpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.madiba.venualpha.viewer.ViewerImageFragment;
import com.example.madiba.venualpha.models.MdMediaItem;

import java.util.List;


/**
 * Created by oracleen on 2016/7/4.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<MdMediaItem> mDatas;
    private Context mContext;

    public PagerAdapter(FragmentManager fm, List<MdMediaItem> data, Context context) {
        super(fm);
        mDatas = data;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        MdMediaItem data = mDatas.get(position);

//        if (data.getInt("typeV2") == 0) {
//            return PhotoViewerFragment.newInstance(data.getParseUser("from").getUsername(), data.getParseUser("from").getParseFile("avatar").getUrl()
//                    , data.getString("tag"), data.getString("url"), data.getObjectId(), data.getClassName());
//
//        } else if(data.getInt("typev2")==1) {
//            return VideoPlayerPagerFragment.newInstance(data.getParseUser("from").getUsername(), data.getParseUser("from").getParseFile("avatar").getUrl()
//                    , data.getString("tag"), data.getString("url"), data.getObjectId(), data.getClassName());
//        }else {
//            return null;
//        }

        return ViewerImageFragment.newInstance();

    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    public void update(List<MdMediaItem> data) {
        this.mDatas.addAll(data);
        notifyDataSetChanged();
    }

}

