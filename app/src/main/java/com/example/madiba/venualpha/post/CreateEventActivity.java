package com.example.madiba.venualpha.post;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.onboard.LoginFragment;
import com.example.madiba.venualpha.post.EventPost.AddDetailsFragment;
import com.example.madiba.venualpha.post.EventPost.AddImageFragment;

public class CreateEventActivity extends AppCompatActivity implements AddImageFragment.OnFragmentInteractionListener,AddDetailsFragment.OnFragmentInteractionListener {
    android.support.v4.app.FragmentManager fragmentManager ;
    android.support.v4.app.FragmentTransaction ft ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    private void init(){
        ft= fragmentManager.beginTransaction();
        Fragment frg = new LoginFragment();
        ft.add(R.id.container,frg);
        ft.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack("login");
        ft.commit();
    }


    @Override
    public void onFragmentInteraction(int page, String url) {
        Fragment frg = AddDetailsFragment.newInstance(url);
        swap(frg,page);
    }

    @Override
    public void onFragmentInteraction(int page) {

    }

    private void swap(Fragment frg,int index){
        ft.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.container, frg);
        ft.addToBackStack(String.valueOf(index));
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
