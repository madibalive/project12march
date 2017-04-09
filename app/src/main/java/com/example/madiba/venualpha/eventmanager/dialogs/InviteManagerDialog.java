package com.example.madiba.venualpha.eventmanager.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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


public class InviteManagerDialog extends DialogFragment {

    private SelectMultipleAdapter mAdapter;
    private ArrayList<String> userIds;
    private ProgressBar progressBar;
    private ImageButton close,done;
    private RxLoaderManager loaderManager;
    private MultiChoiceRecyclerView mRecyclerview;
    private List<MdUserItem> mDatas=new ArrayList<>();
    private List<MdUserItem> returnData=new ArrayList<>();
    private StateButton stateButton;
    private int mode=0;
    public InviteManagerDialog() {
    }

    public static InviteManagerDialog newInstance(){
        return new InviteManagerDialog();
    }

    public static InviteManagerDialog newInstance(int mode) {
        InviteManagerDialog fragment = new InviteManagerDialog();
        Bundle args = new Bundle();
        args.putInt("mode",mode);
        fragment.setArguments(args);
        return new InviteManagerDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt("mode");
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
            d.getWindow().setBackgroundDrawable(null);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiple_choice, container, false);
        close = (ImageButton) view.findViewById(R.id.close);
        done = (ImageButton) view.findViewById(R.id.done);
        mRecyclerview = (MultiChoiceRecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loaderManager = RxLoaderManagerCompat.get(this);
        close.setOnClickListener(view1 -> dismiss());
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processData();
            }
        });
        setupAdapter();

    }


    private void processData(){
        if (mode==1){
            processInvite();
        }else {
            processUnInvite();
        }
    }

    private void processUnInvite() {
    }

    private void processInvite() {
    }


    private void setupAdapter(){

        for (int i = 0; i < 15; i++) {
            mDatas.add(new MdUserItem());
        }

        mAdapter=new SelectMultipleAdapter(mDatas,getActivity(),mode);
        mRecyclerview.setRecyclerColumnNumber(1);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setSingleClickMode(true);
    }




}