package com.example.madiba.venualpha.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;
import com.jaychang.srv.Updatable;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class AddHashtagItemCell extends SimpleCell<MdMemoryItem, AddHashtagItemCell.ViewHolder>
        implements Updatable<MdMemoryItem> {

  private static final String KEY_TITLE = "KEY_TITLE";
  private boolean showHandle;

  public AddHashtagItemCell(MdMemoryItem item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_addhashtag_item;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
    if (payload != null) {
      // payload from updateCell()
      if (payload instanceof MdMemoryItem) {
//        BindView(holder,((MdMemoryItem) payload),context);
      }
      // payloads from updateCells()
      if (payload instanceof ArrayList) {
        List<MdMemoryItem> payloads = ((ArrayList<MdMemoryItem>) payload);
//        BindView(holder,payloads.get(position),context);

      }
      // payload from addOrUpdate()
      if (payload instanceof Bundle) {
        Bundle bundle = ((Bundle) payload);
        for (String key : bundle.keySet()) {
          if (KEY_TITLE.equals(key)) {
//            BindView(holder,bundle.getParcelable(key),context);
          }
        }
      }
      return;
    }

//    BindView(holder,getItem(),context);

  }

  private void BindView(ViewHolder holder, MdMemoryItem item, Context context){

//    if (item.getShared()){
//      holder.mShared.setText(item.getExtraInfo());
//    }
//
//    holder.mTag.setText("");
//
//
//    List<String> urls=new ArrayList<>();
//
//    String names="";
//    //set name onclick
//    if (item.getMemberCount()<1){
//      names = item.getMemberList().get(0).getName();
//      urls.add(item.getMemberList().get(0).getAvatarSmall());
//
//    }else {
//      for (MdUserItem userItem: item.getMemberList()
//              ) {
//        names.concat(userItem.getName() + ",");
//        urls.add(userItem.getAvatarSmall());
//      }
//    }
//
//    holder.mName.setText(names);
//
//
//    if (item.getExtraAvailable())
//      holder.mExtra.setText(item.getExtraInfo());
  }

  private void showPopup(View view, List<MdUserItem> list,Context context) {
    PopupMenu popupMenu = new PopupMenu(context, view);
    for (int i = 0; i < list.size(); i++) {
      popupMenu.getMenu().add(Menu.NONE, i, i, list.get(i).getName());
    }
    popupMenu.show();

    popupMenu.setOnMenuItemClickListener(item -> {
      int i = item.getItemId();

      return false;
    });

    popupMenu.show();
  }

  private void showOverflow(View view, MdMemoryItem item,Context context) {
    PopupMenu popupMenu = new PopupMenu(context, view);

    popupMenu.getMenu().add(Menu.NONE, 0, 0, "link");
    if(item.getMemberIdlIst().contains(ParseUser.getCurrentUser().getObjectId())&& item.getType()
            == GlobalConstants.MEMEMORY_LINK & item.getMemberList().size()>1)
      popupMenu.getMenu().add(Menu.NONE, 1, 1, "Unlink");

    if(item.getMemberIdlIst().contains(ParseUser.getCurrentUser().getObjectId())
            || item.getUserItem().getParseId().equals(ParseUser.getCurrentUser().getObjectId()))
      popupMenu.getMenu().add(Menu.NONE, 1, 1, "Link friend");

    if(item.getUserItem().getParseId().equals(ParseUser.getCurrentUser().getObjectId()) && item.getType()
            == GlobalConstants.MEMEMORY_DEFAULT & item.getMemberList().size()<1)
      popupMenu.getMenu().add(Menu.NONE, 1, 1, "Delete");

    popupMenu.show();

    popupMenu.setOnMenuItemClickListener(menuItem -> {
      int i = menuItem.getItemId();

      return false;
    });

    popupMenu.show();
  }

  @Override
  protected long getItemId() {
//    return getItem().getParseId().hashCode();
    return 0;
  }

  public void setShowHandle(boolean showHandle) {
    this.showHandle = showHandle;
  }

  /**
   * If the titles of books are same, no need to update the cell, onBindViewHolder() will not be called.
   */
  @Override
  public boolean areContentsTheSame(MdMemoryItem newItem) {
    return getItem().getParseId().equals(newItem.getParseId());
  }

  /**
   * If getItem() is the same as newItem (i.e. their return value of getItemId() are the same)
   * and areContentsTheSame()  return false, then the cell need to be updated,
   * onBindViewHolder() will be called with this payload object.
   */
  @Override
  public Object getChangePayload(MdMemoryItem newItem) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(KEY_TITLE, Parcels.wrap(newItem));
    return bundle;
  }
  static class ViewHolder extends SimpleViewHolder {
    TextView mName,mTag,mComment,mShared,mExtra;
//    SwitchTextView mViews;
//    StateImageView mImageView;
//    StateImageView avatar;
//    ImageButton mMore,mShare;


    ViewHolder(View itemView) {
      super(itemView);
//      mComment = (TextView) itemView.findViewById(R.id.comment);
//      mViews = (SwitchTextView) itemView.findViewById(R.id.views);
//      mExtra = (TextView) itemView.findViewById(R.id.extra);
//      mShared = (TextView) itemView.findViewById(R.id.status);
//      mName = (TextView) itemView.findViewById(R.id.date);
//      mTag = (TextView) itemView.findViewById(R.id.title);
//      mImageView = (StateImageView) itemView.findViewById(R.id.image_view);
//      avatar = (StateImageView) itemView.findViewById(R.id.avatar);
//      mMore  = (ImageButton) itemView.findViewById(R.id.overflow_btn);
//      mShare  = (ImageButton) itemView.findViewById(R.id.share);

    }
  }

}
