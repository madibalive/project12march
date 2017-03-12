package com.example.madiba.venualpha.discover;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMediaItem;
import com.example.madiba.venualpha.ui.customviewpager.EnchantedViewPager;

import org.lucasr.twowayview.widget.SpannableGridLayoutManager;
import org.lucasr.twowayview.widget.TwoWayView;

import java.util.Arrays;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;

public class DiscoverActivity extends Activity {

    public static final List<String> CATEGORY_DATA = Arrays.asList("Category","Entertainment", "Party", "Fitness",
            "Gospel", "Social", "Promotion", "VenuChallange");

    private TwoWayView mRecyclerview;
    private ExploreAdapter mAdapter;
    RxLoaderManager loaderManager;
    EnchantedViewPager mViewPager;
    private LinearLayout mCategoriesLayout;
    private ViewGroup mCategoriesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_main);
        mViewPager = (EnchantedViewPager) findViewById(R.id.viewPager);
    }

    private void setupEvents(List<MdEventItem> data) {

//        final ExploreEventsPagerAdapter adapter = new ExploreEventsPagerAdapter( data);
//        mViewPager.setAdapter(adapter);
        mViewPager.useAlpha();
        mViewPager.useScale();
    }

    private void setupCategories(){
        if (CATEGORY_DATA.size()<0) {
            mCategoriesContainer.setVisibility(View.GONE);
            return;
        }

        else {
            mCategoriesContainer.setVisibility(View.VISIBLE);
            mCategoriesLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            for (final String feature : CATEGORY_DATA) {
                ImageView chipView = (ImageView) inflater.inflate(
                        R.layout.item_channel, mCategoriesLayout, false);
//                chipView.setText(feature.getTitle());
//                chipView.setContentDescription(feature.getTitle());
//                chipView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
//                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
//                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        getActivity().startActivity(intent);
//                    }
//                });

                mCategoriesLayout.addView(chipView);
            }
        }
    }

    private void setupMediaList(List<MdMediaItem> datas){
        mAdapter = new ExploreAdapter(R.layout.item_channel, datas);
        mRecyclerview.setAdapter(mAdapter);
    }


    private class ExploreAdapter extends BaseQuickAdapter<MdMediaItem> {

        public ExploreAdapter(int layoutResId, List<MdMediaItem> data) {
            super(layoutResId, data);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        protected void convert(BaseViewHolder holder, MdMediaItem data) {

            final View itemView = holder.itemView;
            int pos = getData().indexOf(data);
            final SpannableGridLayoutManager.LayoutParams lp =
                    (SpannableGridLayoutManager.LayoutParams) itemView.getLayoutParams();

            final int span1 = (pos == 1 || pos == 3 ? 2 : 1);
            final int span2 = (pos == 1 ? 2 : (pos == 3 ? 3 : 1));
            final int rowSpan = span1;

            if (lp.rowSpan != rowSpan || lp.colSpan != span2) {
                lp.rowSpan = rowSpan;
                lp.colSpan = span2;
                itemView.setLayoutParams(lp);
            }


//            holder.setText(R.id.channel_name,data.getString("Name"))
//                    .setVisible(R.id.channel_indicator,false);
//            Glide.with(mContext).load(data.getParseFile("image").getUrl())
//                    .into((RoundCornerImageView) holder.getView(R.id.channel_imageView));

        }
    }



}
