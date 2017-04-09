package com.example.madiba.venualpha.discover.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

public class SearchEventCell extends SimpleCell<MdEventItem, SearchEventCell.ViewHolder>{

  public SearchEventCell(MdEventItem item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_search_event;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {

    holder.mName.setText("");
    holder.mDate.setText("");
    Glide.with(context)
            .load("")
            .crossFade()
            .placeholder(R.drawable.ic_default_avatar)
            .error(R.drawable.placeholder_error_media)
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .centerCrop()
            .fallback(R.drawable.ic_default_avatar)
            .thumbnail(0.4f)
            .into(holder.mAvatar);

  }
  
  @Override
  protected long getItemId() {
    return 0;
  }


  static class ViewHolder extends SimpleViewHolder {
    TextView mName,mDate;
    RoundCornerImageView mAvatar;

    ViewHolder(View itemView) {
      super(itemView);
      mAvatar = (RoundCornerImageView) itemView.findViewById(R.id.image);
      mName = (TextView) itemView.findViewById(R.id.title);
      mDate = (TextView) itemView.findViewById(R.id.date);
    }
  }

}
