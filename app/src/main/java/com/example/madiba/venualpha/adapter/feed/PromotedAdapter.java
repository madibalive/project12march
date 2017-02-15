package com.example.madiba.venualpha.adapter.feed;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.util.ViewUtils;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Madiba on 9/25/2016.
 */

public class PromotedAdapter extends BaseQuickAdapter<ParseObject> {
    public PromotedAdapter(int layoutResId, List<ParseObject> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ParseObject event) {

        ViewUtils.setMargins(holder.getConvertView(),10,20,10,40);
//        holder.setText(R.id.live_now_title, event.getString("title"))
//                .setText(R.id.live_now_desc,event.getString("desc").substring(0,80) + "...");
//
//        Glide.with(mContext)
//                .load(event.getString("url"))
//                .crossFade()
//                .placeholder(R.drawable.ic_default_avatar)
//                .error(R.drawable.placeholder_error_media)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .centerCrop()
//                .fallback(R.drawable.ic_default_avatar)
//                .thumbnail(0.4f)
//                .into((ImageView)holder.getView(R.id.live_now_image));
//                .into(new TintTarget((ImageView) holder.getView(R.id.live_now_image)));

    }
}
