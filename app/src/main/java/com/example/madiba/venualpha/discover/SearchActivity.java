package com.example.madiba.venualpha.discover;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.SearchManager;
import android.os.Build;
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
import com.example.madiba.venualpha.models.SearchModel;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import timber.log.Timber;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private SimpleRecyclerView mRecyclerview;
    List<SearchModel> mSearchDatas ;
    private ProgressBar mProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mRecyclerview = (SimpleRecyclerView) findViewById(R.id.search_results);
        mProgressView = (ProgressBar) findViewById(R.id.rotateloading);
        ImageView magImage = (ImageView)mSearchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        magImage.setVisibility(View.GONE);
        magImage.setImageDrawable(null);

        mSearchDatas =new ArrayList<>();

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
//        if (mProgressView.getVisibility() == View.GONE) {
//            mProgressView.setVisibility(View.VISIBLE);
//        }
        showProgress(true);

        doSearch(query);

    }

    private Callback<List<SearchModel>> loadCallBack = new Callback<List<SearchModel>>() {
        @Override
        public void onFinish(List<SearchModel> value, Callable callable, Throwable e) {
            if (e == null) {
                Timber.d("got data");
                new Handler().postDelayed(() -> {
//                    if (mProgressView.getVisibility() == View.VISIBLE) {
//                        mProgressView.setVisibility(View.GONE);
//                    }

                    if (value.size()>0)

                    showProgress(false);

//                    if (mRecyclerview.getVisibility() == View.GONE) {
//                        mRecyclerview.setVisibility(View.VISIBLE);
//                    }
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

//        mProgressView.setVisibility(View.VISIBLE);
        mSearchView.clearFocus();
    }

    private void clearResults() {
//        TransitionManager.beginDelayedTransition(container, auto);
        mRecyclerview.setVisibility(View.GONE);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRecyclerview.setVisibility(show ? View.GONE : View.VISIBLE);
            mRecyclerview.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRecyclerview.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRecyclerview.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    @Override
    public void onBackPressed() {
        if (mSearchView.isInEditMode()){
        }
        super.onBackPressed();
    }
}
