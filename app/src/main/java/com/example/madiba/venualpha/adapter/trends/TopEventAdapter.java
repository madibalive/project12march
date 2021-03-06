package com.example.madiba.venualpha.adapter.trends;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.util.TimeUitls;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Madiba on 9/25/2016.
 */


public class TopEventAdapter extends BaseQuickAdapter<ParseObject> {

    public TopEventAdapter(int layoutResId, List<ParseObject> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, ParseObject event) {
        holder.setText(R.id.ev_title, event.getString("title"))
                .setText(R.id.ev_tag, event.getString("hashTag"))
                .setText(R.id.ev_daily_stars, event.getString("category"))
                .setText(R.id.ev_status, TimeUitls.getLiveBadgeText(event.getDate("date")));


            Glide.with(mContext)
                    .load(event.getString("url"))
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into((ImageView) holder.getView(R.id.notif_i_avatar));
    }
}
