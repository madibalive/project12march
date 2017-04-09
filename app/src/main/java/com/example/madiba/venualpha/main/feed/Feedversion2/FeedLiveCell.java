package com.example.madiba.venualpha.main.feed.Feedversion2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.MdTrendEvents;
import com.example.madiba.venualpha.util.ViewUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

public class FeedLiveCell extends SimpleCell<MdMemoryItem, FeedLiveCell.ViewHolder>{

  public FeedLiveCell(MdMemoryItem item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_memory_withtag;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
    ViewUtils.setMargins(holder.itemView,2,2,2,2);


  }

  private void BindView(FeedEventAdsCellHolder.ViewHolder holder, MdTrendEvents item, Context context){
//    holder.mName.setText("");


//
//    holder.mTitle.setText("");
//    holder.mTitle.setText("");
//
//    if (item.getUrlLarge() !=null & item.getUrlSmall()!=null){
//      String url ;
//      if (item.getUrlSmall()!=null)
//        url=item.getUrlSmall();
//      else
//        url=item.getUrlLarge();
//
//      Glide.with(context)
//              .load(url)
//              .crossFade()
//              .placeholder(R.drawable.ic_default_avatar)
//              .error(R.drawable.placeholder_error_media)
//              .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//              .centerCrop()
//              .fallback(R.drawable.ic_default_avatar)
//              .thumbnail(0.4f)
//              .into(holder.mImageView);
//    }


  }


  @Override
  protected long getItemId() {
    return 0;
  }

  static class ViewHolder extends SimpleViewHolder {
    TextView mDate, mTitle, mAvatarRoll;
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
