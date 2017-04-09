package com.example.madiba.venualpha.channels;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.onboard.LoginFragment;


public class BaseChannelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_channel);


    }


    private void loader(){

    }

    private void sport(){

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment())
                .commit();
    }

    private void highlight(){

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment())
                .commit();
    }

}
