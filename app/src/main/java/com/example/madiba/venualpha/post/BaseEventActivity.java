package com.example.madiba.venualpha.post;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.VenuFile;
import com.example.madiba.venualpha.post.EventPost.AddDetailsFragment;
import com.example.madiba.venualpha.post.MediaPost.AddHashagFragment;
import com.example.madiba.venualpha.post.MediaPost.PickerFragment;

import java.util.List;

public class BaseEventActivity extends AppCompatActivity implements
    AddDetailsFragment.OnFragmentInteractionListener
{    android.support.v4.app.FragmentManager fragmentManager ;
    android.support.v4.app.FragmentTransaction ft ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main_post);
        init();
    }

    private void init(){

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddDetailsFragment())
                .commit();

    }



    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(int page) {
//        Fragment frg = .newInstance(venuFiles);
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, frg)
//                .addToBackStack("main")
//                .commit();
    }
}
