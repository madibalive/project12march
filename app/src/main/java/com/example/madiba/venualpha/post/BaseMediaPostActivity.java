package com.example.madiba.venualpha.post;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.VenuFile;
import com.example.madiba.venualpha.post.MediaPost.FragmentAddHashagFragment;
import com.example.madiba.venualpha.post.MediaPost.PickerFragment;

import java.util.List;

public class BaseMediaPostActivity extends AppCompatActivity implements
    PickerFragment.OnFragmentInteractionListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_post);
        init();
    }

    private void init(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new PickerFragment())
                .commit();
    }


    @Override
    public void multImage(List<VenuFile> venuFiles) {

        Fragment frg = FragmentAddHashagFragment.newInstance(venuFiles);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, frg)
                .addToBackStack("main")
                .commit();
    }

    @Override
    public void singleImage(VenuFile venuFile) {

    }

    @Override
    public void singleVideo(VenuFile venuFile) {
        Fragment frg = FragmentAddHashagFragment.newInstance(venuFile);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, frg)
                .addToBackStack("main")
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
}
