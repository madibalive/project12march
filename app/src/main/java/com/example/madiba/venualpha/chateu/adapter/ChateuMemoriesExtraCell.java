package com.example.madiba.venualpha.chateu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.stateimageview.StateImageView;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.example.madiba.venualpha.models.MdMemoryItem;

public class ChateuMemoriesExtraCell extends SimpleCell<MdMemoryItem, ChateuMemoriesExtraCell.ViewHolder>
  {


  public ChateuMemoriesExtraCell(MdMemoryItem item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_conversation;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {

//    holder.textView.setText(getItem().getTitle());


  }
  
  private void BindView(ViewHolder holder,MdMemoryItem item){
    holder.mName.setText("");
    holder.mDate.setText("");
    holder.mLastMessage.setText("");
  }

  @Override
  protected long getItemId() {
//    return getItem().getObject().getObjectId().hashCode();
    return 0;  }


  static class ViewHolder extends SimpleViewHolder {
    TextView mName,mDate,mLastMessage;
    StateImageView mAvatar;

    ViewHolder(View itemView) {
      super(itemView);
      mAvatar = (StateImageView) itemView.findViewById(R.id.avatar);
      mName = (TextView) itemView.findViewById(R.id.name);
      mLastMessage = (TextView) itemView.findViewById(R.id.last_message);
      mDate = (TextView) itemView.findViewById(R.id.date);
    }
  }

}
