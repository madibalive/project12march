package com.example.madiba.venualpha.adapter.trends;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.TrendingModel;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class TrendingAdapter extends BaseQuickAdapter<TrendingModel> {

    public TrendingAdapter(int layoutResId, List<TrendingModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected int getDefItemViewType(int position) {

        
         if (getItem(position).getType() == TrendingModel.LIVE_NOW)
            return TrendingModel.LIVE_NOW;

        else if (getItem(position).getType() == TrendingModel.TRENDING)
            return TrendingModel.TRENDING;

        else
            return super.getDefItemViewType(position);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TrendingModel.TRENDING)
            return new TrendingViewHolder(getItemView(R.layout.container_box, parent));
        else if (viewType == TrendingModel.LIVE_NOW)
            return new LiveNowViewHolder(getItemView(R.layout.container_box_trend, parent));
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
            TrendingModel n = getItem(position);



        }catch (Exception e){
            Timber.e(e.getMessage());
        }

        if (holder instanceof LiveNowViewHolder) {
            RecyclerView rview = ((LiveNowViewHolder) holder).getView(R.id.box_recyclerview);
//            TextView title = ((LiveNowViewHolder) holder).getView(R.id.box_title);
//            title.setText("Trending Hashtags");
//            FrameLayout frameLayout = ((LiveNowViewHolder) holder).getView(R.id.bare_header);
//            frameLayout.setVisibility(View.GONE);
            LiveNowAdapter mAdapter = new LiveNowAdapter(R.layout.item_live_now, data);
            mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
//                SingletonDataSource.getInstance().setCurrentEvent(mAdapter.getItem(i));
//                NavigateTo.goToEventPage(mContext,mAdapter.getItem(i).getObjectId(),mAdapter.getItem(i).getClassName());
            });
            rview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            rview.setAdapter(mAdapter);

        }

        else if (holder instanceof TrendingViewHolder) {
            RecyclerView rview = ((TrendingViewHolder) holder).getView(R.id.box_recyclerview);
           
//            MemoryAdapter mAdapter = new MemoryAdapter(R.layout.item_gallery, data);
//            StaggeredGridLayoutManager layMng = new StaggeredGridLayoutManager(3,1); // TODO: 3/10/2017 staggered grid layout
//            rview.setLayoutManager(layMng);
//            rview.setAdapter(mAdapter);
//            mAdapter.setOnRecyclerViewItemClickListener((view, i) -> {
////                SingletonDataSource.getInstance().setCurrentEvent(mAdapter.getItem(i));
////                NavigateTo.goToEventPage(mContext,mAdapter.getItem(i).getObjectId(),mAdapter.getItem(i).getClassName());
//            });
        }

        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, TrendingModel discoverModel) {

    }

    private class LiveNowViewHolder extends BaseViewHolder {
        public LiveNowViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class TrendingViewHolder extends BaseViewHolder {
        public TrendingViewHolder(View itemView) {
            super(itemView);
        }
    }

   

}
