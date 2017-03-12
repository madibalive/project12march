package com.example.madiba.venualpha.profiles;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.EventItemCell;
import com.example.madiba.venualpha.adapter.GalleryItemCell;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.viewer.gallery.LoaderGallery;
import com.jaychang.srv.SimpleRecyclerView;

import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class UserPageActivity extends AppCompatActivity {

    private SimpleRecyclerView recyclerView;
    private View root;
    private ImageView avatar;
    private TextView mName,mStatus,mLocation,mTime,mCategory,mTag,mOverallCommenst,mDailyCommenst,mOverallStars,mDailyStars;

    private int mode;
    private RxLoader<List<GalleryItemCell>> galleryRxLoader;
    private RxLoader<List<EventItemCell>> eventxLoader;
    private RxLoaderManager loaderManager;


    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private Date startDate,endDate;
    private String loadClassName,loadId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Section 1",
                        "Section 2",
                        "Section 3",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toggleMode(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }



    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    private void toggleMode(int position) {
        mode = position;
        if (position == GlobalConstants.USER_PROF_MEMORY)
            resetToMemory();
        else
            resetToEvent();
    }

    private void resetToEvent() {

    }

    private void resetToMemory() {

    }


    private void displayHeader(String url,String name,String status){

        if (url ==null || name ==null){
            return;
        }
        //avatar
        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(avatar);
        mName.setText("");
        if (status ==null)
            mStatus.setText("");
    }

    private void displayRelationship(){

    }

    private void requestChat(){

    }

    private void requestFollow(){

    }

    private void requestunFollow(){

    }

    private void request(){

    }


    void initload(){
        galleryRxLoader=  loaderManager.create(
                LoaderGallery.loadGallery(loadClassName,loadId,mCurrentCounter,mode,startDate,endDate),
                new RxLoaderObserver<List<GalleryItemCell>>() {
                    @Override
                    public void onNext(List<GalleryItemCell> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            if (value.size()>0) {
                                recyclerView.addCells(value);
                                mCurrentCounter = recyclerView.getItemCount();
                            }
                        },500);
                    }

                    @Override
                    public void onStarted() {
                        super.onStarted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                        recyclerView.setLoadMoreCompleted();

                    }
                }
        );


//        eventxLoader=  loaderManager.create(
//                LoaderGallery.loadGallery(loadClassName,loadId,mCurrentCounter,mode,startDate,endDate),
//                new RxLoaderObserver<List<EventItemCell>>() {
//                    @Override
//                    public void onNext(List<EventItemCell> value) {
//                        Timber.d("onnext");
//                        new Handler().postDelayed(() -> {
//                            if (value.size()>0) {
//                                recyclerView.addCells(value);
//                                mCurrentCounter = recyclerView.getItemCount();
//                            }
//                        },500);
//                    }
//
//                    @Override
//                    public void onStarted() {
//                        super.onStarted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("completed");
//                        super.onCompleted();
//                        recyclerView.setLoadMoreCompleted();
//
//                    }
//                }
//        );
    }

}
