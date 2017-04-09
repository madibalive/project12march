package com.example.madiba.venualpha.post;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.post.EventPost.EventDetailsFragment;

public class BaseEventActivity extends AppCompatActivity implements
    EventDetailsFragment.OnFragmentInteractionListener
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
                .replace(R.id.container, new EventDetailsFragment())
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
