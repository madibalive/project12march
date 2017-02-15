package com.example.madiba.venualpha.adapter.discover;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.parse.ParseUser;

import java.util.List;


/**
 * Created by Madiba on 10/9/2016.
 */

public class TopPeopleAdapter extends BaseQuickAdapter<ParseUser> {

    public TopPeopleAdapter(int layoutResId, List<ParseUser> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, ParseUser user) {

        holder.setText(R.id.cc_i_name, user.getUsername())
                .setOnClickListener(R.id.cc_i_unfollow,new OnItemChildClickListener());
        Glide.with(mContext)
                .load(user.getString("avatar"))
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(((com.android.liuzhuang.rcimageview.CircleImageView) holder.getView(R.id.cc_i_avatar)));

    }
}