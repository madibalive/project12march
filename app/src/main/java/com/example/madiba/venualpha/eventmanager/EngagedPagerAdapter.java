package com.example.madiba.venualpha.eventmanager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GIGAMOLE on 7/27/16.
 */
public class EngagedPagerAdapter extends PagerAdapter {



    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<MdEventItem> datas = new ArrayList<>();

    public EngagedPagerAdapter(final Context context, final List<MdEventItem> datas) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.datas =datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view;
        view = mLayoutInflater.inflate(R.layout.item, container, false);
        setupItem(view, datas.get(position));
        container.addView(view);
        return view;
    }

    public static void setupItem(final View view, final MdEventItem item) {
//        final TextView txt = (TextView) view.findViewById(R.id.txt_item);
//        txt.setText(item.toString());
//
//        final ImageView img = (ImageView) view.findViewById(R.id.img_item);
//        img.setImageResource(item.getRes());
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
}
