package com.example.madiba.venualpha.discover;

import android.app.SearchManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.search.SearchAdapter;
import com.example.madiba.venualpha.models.SearchModel;
import com.example.madiba.venualpha.services.TaskSearchLoad;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import timber.log.Timber;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private RecyclerView mRecyclerview;
    public SearchAdapter mSearchAdapter;
    List<SearchModel> mSearchDatas ;
    private ProgressBar rotateLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mSearchView = (SearchView) findViewById(R.id.search_view);
        mRecyclerview = (RecyclerView) findViewById(R.id.search_results);
        rotateLoading = (ProgressBar) findViewById(R.id.rotateloading);
        ImageView magImage = (ImageView)mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        magImage.setVisibility(View.GONE);
        magImage.setImageDrawable(null);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mSearchDatas =new ArrayList<>();
//        mSearchAdapter = new SearchAdapter(R.layout.container_bare,mSearchDatas);
        mRecyclerview.setAdapter(mSearchAdapter);

        setupSearchView();
    }

    private void setupSearchView(){
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
                    clearResults();
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

    private void searchFor(String query){
        if (rotateLoading.getVisibility() == View.GONE) {
            rotateLoading.setVisibility(View.VISIBLE);
        }

        doSearch(query);

    }

    private Callback<List<SearchModel>> loadCallBack = new Callback<List<SearchModel>>() {
        @Override
        public void onFinish(List<SearchModel> value, Callable callable, Throwable e) {
            if (e == null) {
                Timber.d("got data");
                new Handler().postDelayed(() -> {
                    if (rotateLoading.getVisibility() == View.VISIBLE) {
                        rotateLoading.setVisibility(View.GONE);
                    }

                    if (value.size()>0)
                        mSearchAdapter.setNewData(value);

                    if (mRecyclerview.getVisibility() == View.GONE) {
                        mRecyclerview.setVisibility(View.VISIBLE);
                    }
                },500);
            } else {
                // On error
                Timber.d("stated error %s",e.getMessage());

            }
        }

    };

    private void doSearch(String searchTerm){
        TaskSearchLoad taskLoad = new TaskSearchLoad(searchTerm);
        Tasks.execute(taskLoad,loadCallBack);
        rotateLoading.setVisibility(View.VISIBLE);
        mSearchView.clearFocus();
    }

    private void clearResults() {
        mSearchAdapter.clear();
//        TransitionManager.beginDelayedTransition(container, auto);
        mRecyclerview.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        if (mSearchView.isInEditMode()){
        }
        super.onBackPressed();
    }
}
