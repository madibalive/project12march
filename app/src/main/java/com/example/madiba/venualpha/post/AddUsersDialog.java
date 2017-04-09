package com.example.madiba.venualpha.post;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.madiba.venualpha.Actions.ActionHashtagUsers;
import com.example.madiba.venualpha.Actions.ActionInvitesList;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.map.TaskGetEvents;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.post.MediaPost.SelectMultipleAdapter;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceRecyclerView;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import timber.log.Timber;


public class AddUsersDialog extends DialogFragment {

    private SelectMultipleAdapter mAdapter;
    private ArrayList<String> userIds;
    private ProgressBar progressBar;
    private ImageButton close,done;
    private RxLoaderManager loaderManager;
    private MultiChoiceRecyclerView mRecyclerview;
    private List<MdUserItem> mDatas=new ArrayList<>();
    private List<MdUserItem> returnData=new ArrayList<>();
    private StateButton stateButton;
    private int mode,number;
//    private Listener mListener;

    public AddUsersDialog() {
    }

    public static AddUsersDialog newInstance(){
        return new AddUsersDialog();
    }

    public static AddUsersDialog newInstance(List<MdUserItem> users) {
        AddUsersDialog fragment = new AddUsersDialog();
        Bundle args = new Bundle();
        args.putParcelable("users", Parcels.wrap(users));
        fragment.setArguments(args);
        return new AddUsersDialog();
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
        done.setOnClickListener(view12 -> returnData());

        getEvents();
    }


    private void returnData(){
        List<MdUserItem> data = new ArrayList<MdUserItem>();
        for (int i = 0; i < mDatas.size(); i++) {
            if (mRecyclerview.getSelectedItemList().contains(i))
                data.add(mDatas.get(i));
        }
        EventBus.getDefault().post(new ActionInvitesList(mRecyclerview.getSelectedItemCount(),data));
        EventBus.getDefault().post(new ActionHashtagUsers(data));

        Log.e("USERDATA", "USER ::"+data.size() );
        dismiss();
    }



    private void setupAdapter(){

        mAdapter=new SelectMultipleAdapter(mDatas,getActivity());
        mRecyclerview.setRecyclerColumnNumber(1);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setSingleClickMode(true);
    }

    private void getEvents() {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            TaskGetUsers taskLoad = new TaskGetUsers();
            Tasks.execute(taskLoad,loadCallBack);
        }
    }


    private Callback<List<MdUserItem>> loadCallBack = (result, callable, e) -> {
        if (e == null) {
            mDatas = result;
            setupAdapter();
        } else {
            // On error
            Log.e("POST EVEENT", "error: "+e.getMessage() );
        }

    };





}