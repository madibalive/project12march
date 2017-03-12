package com.example.madiba.venualpha.ontap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.madiba.venualpha.R;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;



public class OnTapActivity extends AppCompatActivity {
    private RecyclerView mRecyclerview;
    private View root;
//    private ChateuAdapter mAdapter;
    private List<ParseObject> mDatas=new ArrayList<>();

    RxLoaderManager loaderManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_tap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
