package com.example.madiba.venualpha.discover.discover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.stateimageview.StateImageView;
import com.example.madiba.venualpha.util.ViewUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.example.madiba.venualpha.models.MdEventItem;

public class CategoryCell extends SimpleCell<MdEventItem, CategoryCell.ViewHolder>
  {

  private static final String KEY_TITLE = "KEY_TITLE";
  private boolean showHandle;

  public CategoryCell(MdEventItem item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_event_full;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
//    View a = holder.itemView;
//    ViewUtils.setWidth(a,(int) RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    ViewUtils.setMargins(holder.itemView,2,2,2,2);

  }
  
  private void BindView(ViewHolder holder,MdEventItem item){
    holder.mName.setText("");
    holder.mDate.setText("");
    holder.mLastMessage.setText("");
  }

  @Override
  protected long getItemId() {
    return 0;
  }




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
