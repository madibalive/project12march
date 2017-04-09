package com.example.madiba.venualpha.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceAdapter;

import java.util.ArrayList;

/**
 * Created by davidecirillo on 24/06/2016.
 */

public class SelectMultipleAdapter extends MultiChoiceAdapter<SelectMultipleAdapter.SampleCustomViewHolder> {

    ArrayList<MdUserItem> messageV0s;
    Context mContext;

    public SelectMultipleAdapter(ArrayList<MdUserItem> messageV0s, Context context) {
        this.messageV0s = messageV0s;
        this.mContext = context;
    }

    @Override
    public SampleCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SampleCustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_checkable, parent, false));
    }

    @Override
    public void onBindViewHolder(SampleCustomViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
//
//        MdUserItem currentItem = messageV0s.get(position);
//        holder.name.setText(currentItem.getName());


    }

    @Override
    public int getItemCount() {
        return messageV0s.size();
    }

    @Override
    protected void setActive(View view, boolean state) {


        ImageView imageView = (ImageView) view.findViewById(R.id.add);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.container);
        if(state){
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackgroundLight));
//            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            imageView.setVisibility(View.VISIBLE);
        }else{
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
//            imageView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
            imageView.setVisibility(View.GONE);

        }
    }

    public class SampleCustomViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView radio;
//        public RoundCornerImageView avatar;


        public SampleCustomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title);
            radio = (ImageView) itemView.findViewById(R.id.add);
//            avatar = (RoundCornerImageView) itemView.findViewById(R.id.avatar);
        }
    }
}