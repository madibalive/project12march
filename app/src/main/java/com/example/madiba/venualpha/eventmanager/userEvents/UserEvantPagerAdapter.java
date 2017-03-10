package com.example.madiba.venualpha.eventmanager.userEvents;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
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
import com.example.madiba.venualpha.ui.expandable.ExpandableLinearLayout;

import java.util.List;

/**
 * Created by tiago on 8/13/16.
 */
public class UserEvantPagerAdapter extends PagerAdapter{

    Context mContext;
    LayoutInflater inflater;
    final List<MdEventItem> mAlbumlist;
    int width ;
    int height ;
    Display display;

    public UserEvantPagerAdapter(Context context, List<MdEventItem> albumList, Display display) {
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
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View mCurrentView = (RelativeLayout) inflater.inflate(R.layout.item_my_event, container, false);
        RoundCornerImageView imageView = (RoundCornerImageView) mCurrentView.findViewById(R.id.image_view);
        TextView title = (TextView) mCurrentView.findViewById(R.id.title);
        ExpandableLinearLayout mExlayout = (ExpandableLinearLayout) mCurrentView.findViewById(R.id.expanded_controller_layout);

        LinearLayout mAttendeesLayout = (LinearLayout) mCurrentView.findViewById(R.id.attendees_layout);
        ViewGroup mAttendeesContainers = (ViewGroup) mCurrentView.findViewById(R.id.attendees_container);

        MdEventItem album = this.mAlbumlist.get(position);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)(height/3),LinearLayout.LayoutParams.WRAP_CONTENT);

        mCurrentView.setLayoutParams(lp);
        container.addView(mCurrentView);
        return mCurrentView;
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
