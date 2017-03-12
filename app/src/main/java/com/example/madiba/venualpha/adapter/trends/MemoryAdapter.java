package com.example.madiba.venualpha.adapter.trends;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.ui.SwitchTextView;
import com.example.madiba.venualpha.ui.stateimageview.StateImageView;
import com.jaychang.srv.SimpleViewHolder;
import com.stfalcon.multiimageview.MultiImageView;

import java.util.List;

/**
 * Created by Madiba on 9/25/2016.
 */


public class MemoryAdapter extends BaseQuickAdapter<MdMemoryItem> {

    public MemoryAdapter(int layoutResId, List<MdMemoryItem> data) {
        super(layoutResId, data);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void convert(BaseViewHolder holder, MdMemoryItem event) {




//            Glide.with(mContext)
//                    .load(event.getString("url"))
//                    .crossFade()
//                    .placeholder(R.drawable.ic_default_avatar)
//                    .error(R.drawable.placeholder_error_media)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .centerCrop()
//                    .fallback(R.drawable.ic_default_avatar)
//                    .thumbnail(0.4f)
//                    .into((ImageView) holder.getView(R.id.notif_i_avatar));
    }

//    private void BindView(BaseViewHolder holder, MdMemoryItem item, Context context){
//
//        if (item.getShared()){
//            holder.mShared.setText(item.getExtraInfo());
//        }
//
//        holder.mTag.setText("");
//        holder.mMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        List<String> urls=new ArrayList<>();
//
//        String names="";
//        //set name onclick
//        if (item.getMemberCount()<1){
//            names = item.getMemberList().get(0).getName();
//            urls.add(item.getMemberList().get(0).getAvatarSmall());
//
//        }else {
//            for (MdUserItem userItem: item.getMemberList()
//                    ) {
//                names.concat(userItem.getName() + ",");
//                urls.add(userItem.getAvatarSmall());
//            }
//        }
//
//        holder.mName.setText(names);
//
//
//        holder.avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (item.getMemberList().size()>1){
//                    showPopup(view,item.getMemberList(),context);
//                }else {
//
//                }
//            }
//        });
//
//        if (item.getEventItem() !=null){
//            TextDecorator
//                    .decorate(holder.mTag, item.getHashTag())
//                    .setTextColor(R.color.colorAccent, 0, 5)
//                    .setTextStyle(Typeface.BOLD , 27, 40)
//                    .makeTextClickable(new com.example.madiba.venualpha.util.textdecorator.callback.OnTextClickListener() {
//                        @Override public void onClick(View view, String text) {
//
//                        }
//                    }, 250, 270, false)
//                    .setTextColor(android.R.color.holo_green_light, 250, 270)
//                    .build();
//        }else
//            holder.mTag.setText(item.getHashTag());
//
//
//        //setimages
//
//        List<String> imageUrls = new ArrayList<>();
//
//        for (MdMediaItem mediaItem: item.getMediaItems()
//                ) {
//            if (mediaItem.getUrlLarge() !=null& mediaItem.getUrlMedium()!=null) {
//                String url;
//                if (mediaItem.getUrlMedium() != null)
//                    url = mediaItem.getUrlSmall();
//                else
//                    url = mediaItem.getUrlLarge();
//                imageUrls.add(url);
//            }
//        }
//
//        holder.mImageView.addImages(imageUrls);
//
//
//        // set comment, & others
//
//        holder.mViews.setEnabled(item.getStatus());
//        holder.mViews.setText(item.getViews());
//
//        holder.mComment.setText(item.getComments());
//
//
//        if (item.getExtraAvailable())
//            holder.mExtra.setText(item.getExtraInfo());
//    }

    static class ViewHolder extends SimpleViewHolder {
        TextView mName,mTag,mComment,mShared,mExtra;
        SwitchTextView mViews;
        StateImageView mImageView;
        MultiImageView avatar;
        ImageButton mMore,mShare;


        ViewHolder(View itemView) {
            super(itemView);
            mComment = (TextView) itemView.findViewById(R.id.comment);
            mViews = (SwitchTextView) itemView.findViewById(R.id.views);
            mExtra = (TextView) itemView.findViewById(R.id.extra);
            mShared = (TextView) itemView.findViewById(R.id.status);
            mName = (TextView) itemView.findViewById(R.id.date);
            mTag = (TextView) itemView.findViewById(R.id.title);
            mImageView = (StateImageView) itemView.findViewById(R.id.image_view);
            avatar = (MultiImageView) itemView.findViewById(R.id.avatar);
            mMore  = (ImageButton) itemView.findViewById(R.id.overflow_btn);
            mShare  = (ImageButton) itemView.findViewById(R.id.share);

        }
    }

}
