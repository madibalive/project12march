package com.example.madiba.venualpha.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceRecyclerView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;


public class HashtagSelectUsersDialog extends DialogFragment {

    private SelectMultipleAdapter mAdapter;
    private ArrayList<String> userIds;
    private ProgressBar progressBar;
    private ImageView close;
    private RxLoaderManager loaderManager;
    private MultiChoiceRecyclerView mRecyclerview;
    private List<MdUserItem> mDatas=new ArrayList<>();
    private List<MdUserItem> returnData=new ArrayList<>();
    private StateButton stateButton;
    private int mode,number;
    private Listener mListener;

    public HashtagSelectUsersDialog() {
    }

    public static HashtagSelectUsersDialog newInstance(){
        return new HashtagSelectUsersDialog();
    }

    public static HashtagSelectUsersDialog newInstance(int mode, List<MdUserItem> users) {
        HashtagSelectUsersDialog fragment = new HashtagSelectUsersDialog();
        Bundle args = new Bundle();
        args.putParcelable("users", Parcels.wrap(users));
        fragment.setArguments(args);
        return new HashtagSelectUsersDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            returnData = Parcels.unwrap(getArguments().getParcelable("user"));
        }

    }



    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_invite, container, false);
        close = (ImageView) view.findViewById(R.id.close);
        progressBar = (ProgressBar) view.findViewById(R.id.rotateloading);
        mRecyclerview = (MultiChoiceRecyclerView) view.findViewById(R.id.recyclerView);
        stateButton = (StateButton) view.findViewById(R.id.submit);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loaderManager = RxLoaderManagerCompat.get(this);
        close.setOnClickListener(view1 -> dismiss());
        setupAdapter();

    }



    private void setupAdapter(){

        for (int i = 0; i < 15; i++) {
            mDatas.add(new MdUserItem());
        }
        mRecyclerview.setRecyclerColumnNumber(1);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setSingleClickMode(true);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onUserClicked(MdUserItem position);
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {

        super.onStop();
    }
}