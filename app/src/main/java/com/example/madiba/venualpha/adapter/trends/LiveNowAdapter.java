package com.example.madiba.venualpha.adapter.trends;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Madiba on 9/25/2016.
 */

public class LiveNowAdapter extends BaseQuickAdapter<ParseObject> {
    public LiveNowAdapter(int layoutResId, List<ParseObject> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ParseObject event) {
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
