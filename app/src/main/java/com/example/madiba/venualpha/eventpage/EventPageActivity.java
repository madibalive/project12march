package com.example.madiba.venualpha.eventpage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;

public class EventPageActivity extends AppCompatActivity {

    private Button mPlayBtn;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventpage_layout);
//
//       imageView = (ImageView) findViewById(R.id.session_photo);
//    load();
    }

    private void load(){
        String url;
//        if (getIntent().hasExtra("fromFeed") && getIntent().getBooleanExtra("fromFeed",false)) {
//            url = SingletonDataSource.getInstance().getCurrentEvent().getString("url");        <item name="android:textColor">#880E4F</item>

//            if (url !=null){

        Glide.with(this)
                .load("http://images.fastcompany.com/upload/gospel-choir.jpg")
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(imageView);

                imageView.setOnClickListener(view -> {
//                    startActivity(new Intent(EventPageActivity.this, BareImgViewerActivity.class).putExtra("url", url));
                });

//            }
//        }


    }











}
