package com.example.madiba.venualpha.discover.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

public class SearchHashtagCell extends SimpleCell<MdEventItem, SearchHashtagCell.ViewHolder>{

  public SearchHashtagCell(MdEventItem item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.item_search_gossip;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
    holder.mHashtag.setText("");


  }

  @Override
  protected long getItemId() {
    return 0;
  }


  static class ViewHolder extends SimpleViewHolder {
    TextView mHashtag;

    ViewHolder(View itemView) {
      super(itemView);
      mHashtag = (TextView) itemView.findViewById(R.id.title);
    }
  }

}
