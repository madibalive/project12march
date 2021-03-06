package com.example.madiba.venualpha.discover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.discover.dis.CategoryCell;
import com.example.madiba.venualpha.chateu.ChateuFragAct;
import com.example.madiba.venualpha.models.MConversationItem;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.util.TimeUitls;
import com.jaychang.srv.SimpleRecyclerView;
import com.parse.ParseObject;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class CategoryExploreActivity extends AppCompatActivity {
    private SimpleRecyclerView mRecyclerview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View root;
    RxLoaderManager loaderManager;
    private RxLoader<List<ParseObject>> listRxLoader;

    private int mCurrentCounter = 0;
    private Boolean isLoading;
    private Date lastSince;
    private String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_explore);
        mRecyclerview = (SimpleRecyclerView) findViewById(R.id.core_recyclerview);
        lastSince = TimeUitls.getCurrentDate();


    }

    private void requestOpenConversation(@NonNull MdEventItem event) {
        Intent intent = new Intent(CategoryExploreActivity.this,ChateuFragAct.class);
        Bundle bundle = new Bundle();
        intent.putExtra( "example", Parcels.wrap(event));
        startActivity(intent);
    }

    private void initload(){
        listRxLoader=  loaderManager.create(
                LoaderGeneral.loadCategory(currentCategory,mCurrentCounter,lastSince,pos),
                new RxLoaderObserver<List<CategoryCell>>() {
                    @Override
                    public void onNext(List<CategoryCell> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            mSwipeRefreshLayout.setRefreshing(false);
                            if (value.size()>0) {
                                mRecyclerview.addCells(value);

                            }
                        },500);
                    }

                    @Override
                    public void onStarted() {
                        Timber.d("stated");
                        super.onStarted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("stated error %s",e.getMessage());
                        super.onError(e);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                    }
                }
        );
    }



}
