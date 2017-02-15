package com.example.madiba.venualpha.adapter.search;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.parse.ParseUser;

import java.util.List;



/**
 * Created by Madiba on 10/12/2016.
 */
public class SearchPeopleAdapter
        extends BaseQuickAdapter<ParseUser> {

    public SearchPeopleAdapter(int layoutResId, List<ParseUser> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ParseUser user) {

        holder.setText(R.id.name, user.getUsername());
        if (user.getParseFile("avatarSmall")!=null ){
            Glide.with(mContext)
                    .load(user.getParseFile("avatarSmall").getUrl())
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into(((RoundCornerImageView) holder.getView(R.id.avatar)));
        }

    }
}