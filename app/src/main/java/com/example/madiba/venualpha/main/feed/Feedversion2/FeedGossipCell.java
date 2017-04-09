package com.example.madiba.venualpha.main.feed.Feedversion2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdGossip;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

public class FeedGossipCell extends SimpleCell<MdGossip, FeedGossipCell.ViewHolder>
   {

  private static final String KEY_TITLE = "KEY_TITLE";

  public FeedGossipCell(MdGossip item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_feed_gossip;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {

  }
  
  private void BindView(ViewHolder holder,MdGossip item,Context context){
//    holder.mName.setText("");

    if (item.getShared()){
      holder.mTitle.setText(item.getExtraInfo());
    }

    holder.mTitle.setText("");
    holder.mTitle.setText("");

    if (item.getUrlLarge() !=null & item.getUrlSmall()!=null){
      String url ;
      if (item.getUrlSmall()!=null)
        url=item.getUrlSmall();
      else
        url=item.getUrlLarge();

      Glide.with(context)
              .load(url)
              .crossFade()
              .placeholder(R.drawable.ic_default_avatar)
              .error(R.drawable.placeholder_error_media)
              .diskCacheStrategy(DiskCacheStrategy.SOURCE)
              .centerCrop()
              .fallback(R.drawable.ic_default_avatar)
              .thumbnail(0.4f)
              .into(holder.mImageView);
    }

    if (item.getExtraAvailable())
        holder.mTitle.setText(item.getExtraInfo());
  }

  @Override
  protected long getItemId() {
    //    return getItem().getParseId().hashCode();
    return 0;

  }


  /**
   * If the titles of books are same, no need to update the cell, onBindViewHolder() will not be called.
   */
//  @Override
//  public boolean areContentsTheSame(MdGossip newItem) {
//    if (newItem.getShared()){
//      return getItem().getSharedBy().getParseId().equals(newItem.getSharedBy().getParseId());
//    }else
//      return getItem().getParseId().equals(newItem.getParseId());
//  }


  static class ViewHolder extends SimpleViewHolder {
    TextView mDate,mTitle,mAvatarRoll;
    RoundCornerImageView mImageView;
    ViewHolder(View itemView) {
      super(itemView);
      mAvatarRoll = (TextView) itemView.findViewById(R.id.avatar_roll);
      mDate = (TextView) itemView.findViewById(R.id.date);
      mTitle = (TextView) itemView.findViewById(R.id.title);
      mImageView = (RoundCornerImageView) itemView.findViewById(R.id.image_view);

    }
  }

}
