package com.example.madiba.venualpha.ontap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.StateButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 11/5/2016.
 */



public class RequestFragment extends DialogFragment {

    private RecyclerView mRecyclerview;
    private PostRequestAdapter mAdapter;

    private ProgressDialog progress;
    private ParseQuery<ParseObject> categoryQuery;
    private ImageView back;
    private StateButton submit;
    private TextView mTitle;
    private ParseObject selectedCategory;
    private List<ParseObject> data= new ArrayList<>();
    public RequestFragment() {
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
            d.getWindow().setBackgroundDrawable(null);
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_request, container, false);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.recyclerView);
        submit = (StateButton) view.findViewById(R.id.submit);
        mTitle = (TextView) view.findViewById(R.id.title);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initListener();
        mAdapter = new PostRequestAdapter(R.layout.item_ontap,data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setOnRecyclerViewItemClickListener((view1, i) -> {

            CheckOutFragment.newInstance(data.get(i).getObjectId(),data.get(i).getClassName(),data.get(i).getString("name"));
        });
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL ,false));
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setHasFixedSize(true);

    }

    class PostRequestAdapter
            extends BaseQuickAdapter<ParseObject> {

        PostRequestAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final ParseObject category) {

//            holder.setText(R.id.ontap_dir_item_name,category.getString("name"));
//            Glide.with(mContext).load("http://uauage.org/upload/2014/10/avatar-round.png").crossFade().fitCenter().into((ImageView) holder.getView(R.id.notif_i_avatar));


        }
    }


}
