package com.example.madiba.venualpha.adapter.discover.dis;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.stfalcon.multiimageview.MultiImageView;

public class CategoryCell extends SimpleCell<MdEventItem, CategoryCell.ViewHolder>
  implements Updatable<MdEventItem> {

  private static final String KEY_TITLE = "KEY_TITLE";
  private boolean showHandle;

  public CategoryCell(MdEventItem item) {
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
    holder.mName.setText("");
    holder.mDate.setText("");
    holder.mLastMessage.setText("");
  }

  @Override
  protected long getItemId() {
    return getItem().getObject().getObjectId().hashCode();
  }


  public void setShowHandle(boolean showHandle) {
    this.showHandle = showHandle;
  }

  /**
   * If the titles of books are same, no need to update the cell, onBindViewHolder() will not be called.
   */
  @Override
  public boolean areContentsTheSame(MdEventItem newItem) {
    return getItem().getObject().getObjectId().equals(newItem.getObject().getObjectId());
  }

  /**
   * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
   * and areContentsTheSame()  return false, then the cell need to be updated,
   * onBindViewHolder() will be called with this payload object.
   */
  @Override
  public Object getChangePayload(MdEventItem newItem) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_TITLE, newItem.getTitle());
    return bundle;
  }

  static class ViewHolder extends SimpleViewHolder {
    TextView mName,mDate,mLastMessage;
    MultiImageView mAvatar;

    ViewHolder(View itemView) {
      super(itemView);
      mAvatar = (MultiImageView) itemView.findViewById(R.id.avatar);
      mName = (TextView) itemView.findViewById(R.id.name);
      mLastMessage = (TextView) itemView.findViewById(R.id.last_message);
      mDate = (TextView) itemView.findViewById(R.id.date);
    }
  }

}
