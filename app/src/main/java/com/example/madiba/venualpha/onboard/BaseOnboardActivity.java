package com.example.madiba.venualpha.onboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.madiba.venualpha.R;


public class BaseOnboardActivity extends AppCompatActivity {
    android.support.v4.app.FragmentManager fragmentManager ;
    android.support.v4.app.FragmentTransaction ft ;

    private static final int login=1;
    private static final int signup=2;
    private static final int contact=3;
    private static final int categories=4;
    private static final int pending=5;

    private Button mNext,mBack;

    private int currentPage=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_auth);
        fragmentManager = getSupportFragmentManager();

    }


    private void init(){
        ft= fragmentManager.beginTransaction();
        Fragment frg = new LoginFragment();
        ft.add(R.id.container,frg);
        ft.setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack("login");
        ft.commit();
    }


    private void update(int index){
        Fragment frg;
        switch (index){
            case signup:
                frg = new SignFragment();
                break;
            case contact:
                frg = new SignFragment();

                break;
            case categories:
                frg = new SignFragment();

                break;
            case pending:
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
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }


}
