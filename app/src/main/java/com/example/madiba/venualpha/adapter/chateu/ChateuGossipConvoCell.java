package com.example.madiba.venualpha.adapter.chateu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MConversationItem;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;

public class ChateuGossipConvoCell extends SimpleCell<MConversationItem, ChateuGossipConvoCell.ViewHolder>
  implements Updatable<MConversationItem> {

  private static final String KEY_TITLE = "KEY_TITLE";
  private boolean showHandle;

  public ChateuGossipConvoCell(MConversationItem item) {
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
//      if (payload instanceof MConversationItem) {
//        holder.textView.setText(((MConversationItem) payload).getTitle());
//      }
//      // payloads from updateCells()
//      if (payload instanceof ArrayList) {
//        List<MConversationItem> payloads = ((ArrayList<MConversationItem>) payload);
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
  
  private void BindView(ViewHolder holder,MConversationItem item){
    holder.mName.setText("");
    holder.mTitle.setText("");
  }

  @Override
  protected long getItemId() {
//    return getItem().getObject().getObjectId().hashCode();
      return 0;
  }

  public void setShowHandle(boolean showHandle) {
    this.showHandle = showHandle;
  }

  /**
   * If the titles of books are same, no need to update the cell, onBindViewHolder() will not be called.
   */
  @Override
  public boolean areContentsTheSame(MConversationItem newItem) {
    return true;
  }

  /**
   * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
   * and areContentsTheSame()  return false, then the cell need to be updated,
   * onBindViewHolder() will be called with this payload object.
   */
  @Override
  public Object getChangePayload(MConversationItem newItem) {
    Bundle bundle = new Bundle();
    bundle.putString(KEY_TITLE, newItem.getTitle());
    return bundle;
  }

  static class ViewHolder extends SimpleViewHolder {
    TextView mName,mTitle;
    ViewHolder(View itemView) {
      super(itemView);
      mName = (TextView) itemView.findViewById(R.id.name);
      mTitle = (TextView) itemView.findViewById(R.id.last_message);
    }
  }

}
