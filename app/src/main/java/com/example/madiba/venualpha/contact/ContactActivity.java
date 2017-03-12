package com.example.madiba.venualpha.contact;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;

import com.example.madiba.venualpha.Actions.ActionContactSearch;
import com.example.madiba.venualpha.R;

import org.greenrobot.eventbus.EventBus;



public class ContactActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private SearchView mSearchView;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mSearchView = (SearchView) findViewById(R.id.search_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupSearchView();
    }

    private void setupSearchView(){
        ImageView magImage = (ImageView)mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        magImage.setVisibility(View.GONE);
        magImage.setImageDrawable(null);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setFocusable(true);
        mSearchView.requestFocusFromTouch();
        mSearchView.setIconified(false);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
//        mSearchView.setQueryHint(getString(R.string.search_hint));
        mSearchView.setImeOptions(mSearchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    searchFor(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        mSearchView.setOnCloseListener(() -> false);
    }

    private void searchFor(String query) {
        EventBus.getDefault().post(new ActionContactSearch(query));
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("position", "getItem: " + position);
            if (position == 0) {
                return FollowFragment.newInstance("", true);
            } else if (position == 1) {
                return FollowFragment.newInstance("", false);
            } else {
                return LocalContactFragment.newInstance();
//            }

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;

        }
    }
}
