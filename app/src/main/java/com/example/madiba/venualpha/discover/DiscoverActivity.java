package com.example.madiba.venualpha.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.trends.trendv2.TrendMemoryCell;
import com.example.madiba.venualpha.map.MapEventsActivity;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.ui.customviewpager.EnchantedViewPager;
import com.example.madiba.venualpha.viewer.player.BaseMemoryActivity;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;

public class DiscoverActivity extends AppCompatActivity {

    public static final List<String> CATEGORY_DATA = Arrays.asList("Category","Entertainment", "Party", "Fitness",
            "Gospel", "Social", "Promotion", "VenuChallange");

    private SimpleRecyclerView mRecyclerview;
    RxLoaderManager loaderManager;
    EnchantedViewPager mViewPager;
    private LinearLayout mCategoriesLayout;
    private ViewGroup mCategoriesContainer;
    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRecyclerview = (SimpleRecyclerView) findViewById(R.id.recyclerView);
        mViewPager = (EnchantedViewPager) findViewById(R.id.viewPager);

        mCategoriesLayout = (LinearLayout) findViewById(R.id.session_tags);
        mCategoriesContainer = (ViewGroup) findViewById(R.id.session_tags_container);

        List<MdEventItem> eventItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            eventItems.add(new MdEventItem());
        }
        setupEvents(eventItems);

        setupCategories(CATEGORY_DATA);
        setupMediaList();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discover, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            startActivity(new Intent(DiscoverActivity.this,SearchActivity.class));
        }else if (item.getItemId() == R.id.action_map){
            startActivity(new Intent(DiscoverActivity.this, MapEventsActivity.class));
        }else if (item.getItemId() == R.id.action_gossip){
            startActivity(new Intent(DiscoverActivity.this, GossipExploreActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }




    private void setupEvents(List<MdEventItem> data) {

        final ExploreEventsPagerAdapter adapter = new ExploreEventsPagerAdapter(this, data);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
        mViewPager.useAlpha();
        mViewPager.useScale();
    }

    private void setupCategories(List<String> CATEGORY_DATA){
        if (CATEGORY_DATA.size()<0) {
            mCategoriesContainer.setVisibility(View.GONE);
            return;
        }

        else {
            mCategoriesContainer.setVisibility(View.VISIBLE);
            mCategoriesLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            for (final String feature : CATEGORY_DATA) {
                View view = inflater.inflate(R.layout.item_category, mCategoriesLayout, false);

                TextView textView = (TextView) view.findViewById(R.id.name1);
                textView.setText(feature);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DiscoverActivity.this, CategoryExploreActivity.class)
                                .putExtra("title", feature)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                mCategoriesLayout.addView(view);
            }
        }
    }

    private void setupMediaList(){
        for (int i = 0; i < 12; i++) {
            TrendMemoryCell convoCell = new TrendMemoryCell(new MdMemoryItem());
            convoCell.setOnCellClickListener2((o, o2, o3) -> startActivity(new Intent(DiscoverActivity.this, BaseMemoryActivity.class)));
            mRecyclerview.addCell(convoCell);
        }
//        mAdapter = new ExploreAdapter(R.layout.item_channel, datas);
//        mRecyclerview.setAdapter(mAdapter);
    }





}
