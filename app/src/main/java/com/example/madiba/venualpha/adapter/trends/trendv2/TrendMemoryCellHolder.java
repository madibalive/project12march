package com.example.madiba.venualpha.adapter.trends.trendv2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdTrendMemory;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.SimpleViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TrendMemoryCellHolder extends SimpleCell<MdTrendMemory, TrendMemoryCellHolder.ViewHolder>{

  public TrendMemoryCellHolder(MdTrendMemory item) {
    super(item);
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.container_simple_grid_header;
  }

  @NonNull
  @Override
  protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
    return new ViewHolder(cellView);
  }

  @Override
  protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {

    holder.mTitle.setVisibility(View.GONE);
    holder.recyclerView.removeAllCells();
    holder.recyclerView.addCells(getItem().getTrendEventCells());

  }



  @Override
  protected long getItemId() {
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
