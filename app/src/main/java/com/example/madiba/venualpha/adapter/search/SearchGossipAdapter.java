package com.example.madiba.venualpha.adapter.search;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.parse.ParseObject;

import java.util.List;


/**
 * Created by Madiba on 10/12/2016.
 */
public class SearchGossipAdapter
        extends BaseQuickAdapter<ParseObject> {

    public SearchGossipAdapter(int layoutResId, List<ParseObject> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final ParseObject gossip) {

        holder.setText(R.id.gossip_title,gossip.getString("title") );

    }
}