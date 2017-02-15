package com.example.madiba.venualpha.adapter.notif;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.ModelNotification;
import com.example.madiba.venualpha.util.ViewUtils;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;


public class NotificationAdapter extends BaseQuickAdapter<ModelNotification> {

    public NotificationAdapter(int layoutResId, List<ModelNotification> data) {
        super(layoutResId, data);
    }

    @Override
    protected int getDefItemViewType(int position) {

        if (getItem(position).getType() == ModelNotification.TYPE_DEFAULT)
            return ModelNotification.TYPE_DEFAULT;

        else if (getItem(position).getType() == ModelNotification.TYPE_EXTENDED)
            return ModelNotification.TYPE_EXTENDED;

        else if (getItem(position).getType() == ModelNotification.TYPE_SUGGESTTED)
            return ModelNotification.TYPE_SUGGESTTED;


        else
            return super.getDefItemViewType(position);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ModelNotification.TYPE_DEFAULT)
            return new DefaultNotifViewHolder(getItemView(R.layout.container_box_header, parent));
        else if (viewType == ModelNotification.TYPE_EXTENDED)
            return new ExtendedNotification(getItemView(R.layout.container_box_header, parent));
        else if (viewType == ModelNotification.TYPE_SUGGESTTED)
            return new SuggestedViewHolder(getItemView(R.layout.container_box_header, parent));
        else
            return super.onCreateDefViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        List<ParseObject> data = new ArrayList<>();
        try {
            for (int i = 0; i < 5; i++) {
                ParseObject a=new ParseObject("");
                data.add(a);
            }
            ModelNotification n = getItem(position);
        }catch (Exception e){
            Timber.e(e.getMessage());
        }

        if (holder instanceof DefaultNotifViewHolder) {
            LinearLayout background = ((DefaultNotifViewHolder) holder).getView(R.id.box_background);
            background.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ViewUtils.setMargins(background,10,20,10,10);
            RecyclerView rview = ((DefaultNotifViewHolder) holder).getView(R.id.box_recyclerview);
            TextView title = ((DefaultNotifViewHolder) holder).getView(R.id.box_title);
            title.setText("Trending Hashtags");
            DefaultNotifdapter mAdapter = new DefaultNotifdapter(R.layout.item_notif, data);
            mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
//                SingletonDataSource.getInstance().setCurrentEvent(mAdapter.getItem(i));
//                NavigateTo.goToEventPage(mContext,mAdapter.getItem(i).getObjectId(),mAdapter.getItem(i).getClassName());
            });
            rview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            rview.setAdapter(mAdapter);

        }

        else if (holder instanceof SuggestedViewHolder) {
            LinearLayout background = ((SuggestedViewHolder) holder).getView(R.id.box_background);
            background.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ViewUtils.setMargins(background,10,20,10,10);
            RecyclerView rview = ((SuggestedViewHolder) holder).getView(R.id.box_recyclerview);
            TextView title = ((SuggestedViewHolder) holder).getView(R.id.box_title);
            title.setText("Trending Gossips");
            SuggestedAdapter mAdapter = new SuggestedAdapter(R.layout.item_person, data);
            rview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            rview.setAdapter(mAdapter);
            mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
//                NavigateTo.goToGossip(mContext,mAdapter.getItem(i).getInt("chat_id"));
            });
        }

        else if (holder instanceof ExtendedNotification) {
            LinearLayout background = ((ExtendedNotification) holder).getView(R.id.box_background);
            background.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            ViewUtils.setMargins(background,10,20,10,10);
            RecyclerView rview = ((ExtendedNotification) holder).getView(R.id.box_recyclerview);
            TextView title = ((ExtendedNotification) holder).getView(R.id.box_title);
            title.setText("Trending Gossips");
            ExtendedNotifAdapter mAdapter = new ExtendedNotifAdapter(R.layout.item_notif_extended, data);
            rview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            rview.setAdapter(mAdapter);
            mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
//                SingletonDataSource.getInstance().setCurrentEvent(mAdapter.getItem(i));
//                NavigateTo.goToEventPage(mContext,mAdapter.getItem(i).getObjectId(),mAdapter.getItem(i).getClassName());
            });
        }

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ModelNotification discoverModel) {

    }

    private class DefaultNotifViewHolder extends BaseViewHolder {
        public DefaultNotifViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ExtendedNotification extends BaseViewHolder {
        public ExtendedNotification(View itemView) {
            super(itemView);
        }
    }

    private class SuggestedViewHolder extends BaseViewHolder {
        public SuggestedViewHolder(View itemView) {
            super(itemView);
        }
    }


}
