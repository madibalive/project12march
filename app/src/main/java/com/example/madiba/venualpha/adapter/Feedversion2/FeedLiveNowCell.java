package com.example.madiba.venualpha.adapter.Feedversion2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdLiveNowItem;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.util.ViewUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;

import java.util.List;

import static com.example.madiba.venualpha.discover.DiscoverActivity.CATEGORY_DATA;

public class FeedLiveNowCell extends SimpleCell<MdLiveNowItem, FeedLiveNowCell.ViewHolder>{

  public FeedLiveNowCell(MdLiveNowItem item) {
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
  

  private void bindViews(ViewHolder holder, MdLiveNowItem item, List<MdMemoryItem> mDatas, Context context){
    if (CATEGORY_DATA.size()<0) {
      holder.mMemoriesContainer.setVisibility(View.GONE);
      return;
    }

    else {
      holder.mMemoriesContainer.setVisibility(View.VISIBLE);
      holder.mMemoriesLayout.removeAllViews();
      ViewUtils.fadeInView(holder.mMemoriesLayout);
      LayoutInflater inflater = LayoutInflater.from(context);

      for (final MdMemoryItem channel : mDatas) {
        View view = inflater.inflate(R.layout.item_channel, holder.mMemoriesLayout, false);
        ViewUtils.setWidth(view,100);
        ViewUtils.setMargins(view,10,10,10,10);

        RoundCornerImageView imageView = (RoundCornerImageView) view.findViewById(R.id.image_view);
        ImageButton update = (ImageButton) view.findViewById(R.id.updated);
        TextView title = (TextView) view.findViewById(R.id.title);

        //set Size

        if (true)
          update.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(channel.getMediaItems().get(0).getUrlMedium())
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(imageView);

        title.setText(channel.getViews());

        view.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            // TODO: 3/11/2017 goto memeory
          }
        });

        holder.mMemoriesLayout.addView(view);
      }

    }
  }


  @Override
  protected long getItemId() {
    return getItem().getEventItem().getParseId().hashCode();
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
