package com.example.madiba.venualpha.viewusers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

/**
 * Created by Madiba on 3/27/2017.
 */



public class ViewUserCell extends SimpleCell<MdUserItem, ViewUserCell.ViewHolder>
{



    public ViewUserCell(MdUserItem item) {
        super(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_comment_in;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
        return new ViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {


//    holder.textView.setText(getItem().getTitle());

        holder.mName.setText("");

    }



    @Override
    protected long getItemId() {
//    return getItem().getObjectId().hashCode();
        return 0;
    }
    static class ViewHolder extends SimpleViewHolder {
        TextView mName;
        RoundCornerImageView mAvatar;

        ViewHolder(View itemView) {
            super(itemView);
            mAvatar = (RoundCornerImageView) itemView.findViewById(R.id.avatar);
            mName = (TextView) itemView.findViewById(R.id.name);

        }
    }

}


