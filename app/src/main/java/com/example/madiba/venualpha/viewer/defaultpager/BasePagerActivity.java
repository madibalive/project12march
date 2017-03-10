package com.example.madiba.venualpha.viewer.defaultpager;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.onboard.LoginFragment;
import com.example.madiba.venualpha.onboard.SignFragment;


public class BasePagerActivity extends AppCompatActivity implements SlideFragment.OnSlideFragmentInteractionListener,GridFragment.OnGridFragmentInteractionListener {
    android.support.v4.app.FragmentManager fragmentManager ;
    android.support.v4.app.FragmentTransaction ft ;

    private ProgressBar progressBar;
    private int type=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_auth);
        fragmentManager = getSupportFragmentManager();

    }


    private void init(){
        ft= fragmentManager.beginTransaction();
        Fragment frg = new SlideFragment();
        ft.add(R.id.container,frg);
        ft.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack("login");
        ft.commit();
    }


    private void update(int index){
        Fragment frg;
        switch (index){
            case 0:
                frg = new SignFragment();
                break;
            default:
                frg =new LoginFragment();
        }

        ft.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.container, frg);
        ft.addToBackStack(String.valueOf(index));
        ft.commit();
    }

    @Override
    public void onGridFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSlideFragmentInteraction(Uri uri) {

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
