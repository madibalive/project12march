package com.example.madiba.venualpha.eventmanager;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.ui.customviewpager.EnchantedViewPager;
import com.example.madiba.venualpha.util.ViewUtils;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiago on 8/13/16.
 */
public class MyPagerAdapter extends PagerAdapter{

    Context mContext;
    LayoutInflater inflater;
    final List<MdEventItem> mAlbumlist;
    int width ;
    int height ;
    Display display;

    public MyPagerAdapter(Context context, List<MdEventItem> albumList, Display display) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mAlbumlist = albumList;

        this.display=display;
        Point size = new Point();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                this.display.getRealSize(size);
            }
        } catch (NoSuchMethodError err) {
            this.display.getSize(size);
        }
        width = size.x;
        height = size.y;

        Log.e("OTHER PAGER",""+albumList.size());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mCurrentView =  inflater.inflate(R.layout.item_event_myevent, container, false);
        RoundCornerImageView imageView = (RoundCornerImageView) mCurrentView.findViewById(R.id.image_view);
        TextView title = (TextView) mCurrentView.findViewById(R.id.title);

        List<ParseUser> items = new ArrayList<>();
        displayAttendees(items,mCurrentView);
        MdEventItem album = this.mAlbumlist.get(position);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)(height/3),LinearLayout.LayoutParams.WRAP_CONTENT);

        mCurrentView.setLayoutParams(lp);

        mCurrentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/8/2017 goto event
            }
        });

        mCurrentView.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(mCurrentView);

        return mCurrentView;
    }

    private void displayAttendees(List<ParseUser> attendeeDatas,View view){

        LinearLayout mAttendeesLayout = (LinearLayout) view.findViewById(R.id.attendees_layout);
        ViewGroup mAttendeesContainers = (ViewGroup) view.findViewById(R.id.attendees_container);

//
//        if (attendeeDatas.size()<0) {
//            mAttendeesContainers.setVisibility(View.GONE);
//            return;
//        }
//
//        else {
        mAttendeesContainers.setVisibility(View.VISIBLE);
        mAttendeesLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        for (int i = 0; i < 8; i++) {
            RoundCornerImageView chipView = (RoundCornerImageView) inflater.inflate(
                    R.layout.item_attende, mAttendeesLayout, false);
            ViewUtils.setMargins(chipView,0,8,8,8);
////                chipView.setText(feature.getTitle());
////                chipView.setContentDescription(feature.getTitle());
////                chipView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
////                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
////                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        getActivity().startActivity(intent);
////                    }
////                });

            mAttendeesLayout.addView(chipView);
        }
//        }

    }


    @Override
    public int getCount() {
        return mAlbumlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void removePosition(int position) {
        mAlbumlist.remove(mAlbumlist.get(position));
        notifyDataSetChanged();
    }
}
