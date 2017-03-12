package com.example.madiba.venualpha.adapter.Feedversion2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdEventRowItem;
import com.example.madiba.venualpha.util.ViewUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

import java.util.List;

import static com.example.madiba.venualpha.discover.DiscoverActivity.CATEGORY_DATA;

public class FeedEventsAdCell extends SimpleCell<MdEventRowItem, FeedEventsAdCell.ViewHolder>{

  public FeedEventsAdCell(MdEventRowItem item) {
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

  }


  private void bindViews(ViewHolder holder, MdEventRowItem item, List<MdEventItem> mDatas, Context context){
    if (CATEGORY_DATA.size()<0) {
      holder.mMemoriesContainer.setVisibility(View.GONE);
      return;
    }

    else {
      holder.mMemoriesContainer.setVisibility(View.VISIBLE);
      holder.mMemoriesLayout.removeAllViews();
      ViewUtils.fadeInView(holder.mMemoriesLayout);
      LayoutInflater inflater = LayoutInflater.from(context);

      for (final MdEventItem channel : mDatas) {
        View view = inflater.inflate(R.layout.item_event_map_half, holder.mMemoriesLayout, false);
        ViewUtils.setHeightAndWidth(view,170,140);
        ViewUtils.setMargins(view,10,10,10,10);

        RoundCornerImageView imageView = (RoundCornerImageView) view.findViewById(R.id.image_view);
//        ImageButton update = (ImageButton) view.findViewById(R.id.updated);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView date = (TextView) view.findViewById(R.id.date);

        //set Size
//
//        if (true)
//          update.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(channel.getUrlMedium())
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(imageView);

        date.setText(channel.getEvDateToString());
        title.setText(channel.getEvTitle());

        view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // TODO: 3/11/2017 goto event
          }
        });

        holder.mMemoriesLayout.addView(view);
      }

    }
  }


  @Override
  protected long getItemId() {
    return 0;
  }

  static class ViewHolder extends SimpleViewHolder {
    LinearLayout mMemoriesLayout;
    ViewGroup mMemoriesContainer;

    TextView mTitle,mMore;

    ViewHolder(View itemView) {
      super(itemView);

      mTitle = (TextView) itemView.findViewById(R.id.title);
      mMore = (TextView) itemView.findViewById(R.id.more);

      mMemoriesContainer = (ViewGroup) itemView.findViewById(R.id.livenow_container);
      mMemoriesLayout = (LinearLayout) itemView.findViewById(R.id.livenow_layout);

    }
  }

}
