package com.example.madiba.venualpha.post.EventPost;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.models.ModelEventFeature;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceAdapter;

import java.util.List;

/**
 * Created by davidecirillo on 24/06/2016.
 */

public class FeaturesMultipleAdapter extends MultiChoiceAdapter<FeaturesMultipleAdapter.SampleCustomViewHolder> {

    List<ModelEventFeature> messageV0s;
    Context mContext;

    public FeaturesMultipleAdapter(List<ModelEventFeature> messageV0s, Context context) {
        this.messageV0s = messageV0s;
        this.mContext = context;
    }

    @Override
    public SampleCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SampleCustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_features_select, parent, false));
    }

    @Override
    public void onBindViewHolder(SampleCustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
//
        ModelEventFeature currentItem = messageV0s.get(position);
        holder.name.setText(currentItem.getTitle());


    }

    @Override
    public int getItemCount() {
        return messageV0s.size();
    }

    @Override
    protected void setActive(View view, boolean state) {


        ImageView radio = (ImageView) view.findViewById(R.id.image_view);
        if(state){
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackgroundLight));
            radio.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_frame));
        }else{
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            radio.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));

        }
    }

    public class SampleCustomViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView radio;
//        public RoundCornerImageView avatar;


        public SampleCustomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title);
//            avatar = (RoundCornerImageView) itemView.findViewById(R.id.avatar);
        }
    }
}
