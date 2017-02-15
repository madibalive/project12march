package com.example.madiba.venualpha.post;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.venu.venualpha.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainPostFragment extends Fragment {

    public MainPostFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_post, container, false);
    }
}
