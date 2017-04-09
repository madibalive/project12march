package com.example.madiba.venualpha.channels.channel;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;

public class ChannelEventCell extends SimpleCell<MdEventItem, ChannelEventCell.ViewHolder>
  implements Updatable<MdEventItem> {

  private static final String KEY_TITLE = "KEY_TITLE";
  private boolean showHandle;

  public ChannelEventCell(MdEventItem item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_gossip_small;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
//    if (payload != null) {
//      // payload from updateCell()
//      if (payload instanceof MdEventItem) {
//        holder.textView.setText(((MdEventItem) payload).getTitle());
//      }
//      // payloads from updateCells()
//      if (payload instanceof ArrayList) {
//        List<MdEventItem> payloads = ((ArrayList<MdEventItem>) payload);
//        holder.textView.setText(payloads.get(position).getTitle());
//      }
//      // payload from addOrUpdate()
//      if (payload instanceof Bundle) {
//        Bundle bundle = ((Bundle) payload);
//        for (String key : bundle.keySet()) {
//          if (KEY_TITLE.equals(key)) {
//            holder.textView.setText(bundle.getString(key));
//          }
//        }
//      }
//      return;
//    }

//    holder.textView.setText(getItem().getTitle());

//    if (showHandle) {
//      holder.dragHandle.setVisibility(View.VISIBLE);
//    } else {
//      holder.dragHandle.setVisibility(View.GONE);
//    }
  }
  
  private void BindView(ViewHolder holder,MdEventItem item){
//    holder.mName.setText("");
    holder.mTitle.setText("");
  }

  @Override
  protected long getItemId() {
    return getItem().getParseId().hashCode();
  }

  public void setShowHandle(boolean showHandle) {
    this.showHandle = showHandle;
  }

  /**
   * If the titles of books are same, no need to update the cell, onBindViewHolder() will not be called.
   */
  @Override
  public boolean areContentsTheSame(MdEventItem newItem) {
    return getItem().getParseId().equals(newItem.getParseId());
  }

  /**
   * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
   * and areContentsTheSame()  return false, then the cell need to be updated,
   * onBindViewHolder() will be called with this payload object.
   */
  @Override
  public Object getChangePayload(MdEventItem newItem) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_TITLE, newItem.getEvTitle());
    return bundle;
  }
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
