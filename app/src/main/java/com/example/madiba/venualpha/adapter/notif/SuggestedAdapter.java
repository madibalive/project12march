package com.example.madiba.venualpha.adapter.notif;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Madiba on 9/25/2016.
 */


public class SuggestedAdapter extends BaseQuickAdapter<ParseObject> {

    public SuggestedAdapter(int layoutResId, List<ParseObject> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, ParseObject event) {
//        holder.setText(R.id.ev_title, event.getString("title"))
//                .setText(R.id.ev_tag, event.getString("hashTag"))
//                .setText(R.id.ev_feature_tag, event.getString("category"))
//                .setText(R.id.ev_status, TimeUitls.getLiveBadgeText(event.getDate("date")));
//
//
//            Glide.with(mContext)
//                    .load(event.getString("url"))
//                    .crossFade()
//                    .placeholder(R.drawable.ic_default_avatar)
//                    .error(R.drawable.placeholder_error_media)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .fallback(R.drawable.ic_default_avatar)
//                    .thumbnail(0.4f)
//                    .into((ImageView) holder.getView(R.id.notif_i_avatar));
    }
}
