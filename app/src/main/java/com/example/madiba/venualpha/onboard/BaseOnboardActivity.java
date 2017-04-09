package com.example.madiba.venualpha.onboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.madiba.venualpha.MainActivity;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.post.MediaPost.PickerFragment;


public class BaseOnboardActivity extends AppCompatActivity implements
LoginFragment.OnFragmentInteractionListener,SignFragment.OnFragmentInteractionListener,
UserDetailFragment.OnFragmentInteractionListener,AddContactFragment.OnFragmentInteractionListener,
SelectCategoriesFragment.OnFragmentInteractionListener,PendingInvitesFragment.OnFragmentInteractionListener{
    android.support.v4.app.FragmentManager fragmentManager ;
    android.support.v4.app.FragmentTransaction ft ;

    private static final int login=0;
    private static final int signup=1;
    private static final int adddetail=2;
    private static final int contact=3;
    private static final int categories=4;
    private static final int pending=5;

    private Button mNext,mBack;

    private int currentPage=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_auth);
        init();
    }


    private void init(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new LoginFragment())
                .commit();
    }

    @Override
    public void onGotoPending() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PendingInvitesFragment())
                .addToBackStack("categories")
                .commit();
    }


    @Override
    public void onMain() {
        startActivity(new Intent(BaseOnboardActivity.this, MainActivity.class));
    }

    @Override
    public void onGoSignup() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SignFragment())
                .addToBackStack("singup")
                .commit();
    }

    @Override
    public void onGotoDetails() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new UserDetailFragment())
                .addToBackStack("details")
                .commit();
    }

    @Override
    public void onGoContact() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddContactFragment())
                .addToBackStack("contact")
                .commit();
    }

    @Override
    public void onGoCategories() {
//        startActivity(new Intent(BaseOnboardActivity.this,MainActivity.class));
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, new SelectCategoriesFragment())
//                .addToBackStack("categories")
//                .commit();
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
