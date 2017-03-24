package com.example.madiba.venualpha.adapter.channel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdTrendEvents;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.SimpleViewHolder;

public class ChSpGossipHolder extends SimpleCell<MdTrendEvents, ChSpGossipHolder.ViewHolder>
{

  public ChSpGossipHolder(MdTrendEvents item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.container_simple_horizontal_header;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
    holder.recyclerView.addCells(getItem().getTrendEventCells());
  }


  @Override
  protected long getItemId() {
//    return getItem().getParseId().hashCode();
    return 0;
  }


  static class ViewHolder extends SimpleViewHolder {
    TextView mTitle;
    SimpleRecyclerView recyclerView;

    ViewHolder(View itemView) {
      super(itemView);

      mTitle = (TextView) itemView.findViewById(R.id.title);
      recyclerView = (SimpleRecyclerView) itemView.findViewById(R.id.recyclerView);

    }
  }

}
