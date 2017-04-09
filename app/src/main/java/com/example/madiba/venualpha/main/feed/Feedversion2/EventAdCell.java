package com.example.madiba.venualpha.main.feed.Feedversion2;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.util.ViewUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleViewHolder;


public class EventAdCell extends SimpleCell<MdEventItem, EventAdCell.ViewHolder>
{

    public EventAdCell(MdEventItem item) {
        super(item);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.item_event_map_half;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
        return new ViewHolder(cellView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, Context context, Object payload) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
//        RelativeLayout.LayoutParams updatedOriginalParams =new RelativeLayout.LayoutParams((int)(size.x/2.5),(int)(size.x/2));
//
//        holder.layout.setLayoutParams(updatedOriginalParams);

        ViewUtils.setHeightAndWidth(holder.itemView,(int)(size.x/2),(int)(size.x/2.7));



    }

    private void BindView(ViewHolder holder,MdEventItem item,Context context){
//    holder.mName.setText("");

        if (item.getShared()){
            holder.mTitle.setText(item.getExtraInfo());
        }

        holder.mTitle.setText("");
        holder.mTitle.setText("");

        if (item.getUrlLarge() !=null & item.getUrlSmall()!=null){
            String url ;
            if (item.getUrlSmall()!=null)
                url=item.getUrlSmall();
            else
                url=item.getUrlLarge();

            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into(holder.mImageView);
        }

        if (item.getExtraAvailable())
            holder.mTitle.setText(item.getExtraInfo());
    }

    @Override
    protected long getItemId() {
//    return getItem().getParseId().hashCode();
        return 0;
    }


    static class ViewHolder extends SimpleViewHolder {
        TextView mDate,mTitle,mAvatarRoll;
        RelativeLayout layout;
        RoundCornerImageView mImageView;
        ViewHolder(View itemView) {
            super(itemView);
            mAvatarRoll = (TextView) itemView.findViewById(R.id.avatar_roll);
            layout = (RelativeLayout) itemView.findViewById(R.id.eventholder);
            mDate = (TextView) itemView.findViewById(R.id.date);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mImageView = (RoundCornerImageView) itemView.findViewById(R.id.image_view);

        }
    }

}
