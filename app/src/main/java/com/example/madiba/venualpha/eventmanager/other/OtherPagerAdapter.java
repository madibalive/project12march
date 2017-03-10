package com.example.madiba.venualpha.eventmanager.other;

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

import java.util.List;

/**
 * Created by tiago on 8/13/16.
 */
public class OtherPagerAdapter extends PagerAdapter{

    Context mContext;
    LayoutInflater inflater;
    final List<MdEventItem> mAlbumlist;
    int width ;
    int height ;
    Display display;

    public OtherPagerAdapter(Context context, List<MdEventItem> albumList, Display display) {
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
        View mCurrentView = (RelativeLayout) inflater.inflate(R.layout.item_event_full, container, false);
        RoundCornerImageView imageView = (RoundCornerImageView) mCurrentView.findViewById(R.id.image_view);
        TextView title = (TextView) mCurrentView.findViewById(R.id.title);

        MdEventItem album = this.mAlbumlist.get(position);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams((int)(height/3),LinearLayout.LayoutParams.WRAP_CONTENT);

        mCurrentView.setLayoutParams(lp);
        container.addView(mCurrentView);

        mCurrentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/8/2017 goto event
            }
        });
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
