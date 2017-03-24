package com.example.madiba.venualpha.channels;

import android.app.Activity;
import android.os.Bundle;

import com.example.madiba.venualpha.R;


public class BaseChannelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_channel);
    }

}
