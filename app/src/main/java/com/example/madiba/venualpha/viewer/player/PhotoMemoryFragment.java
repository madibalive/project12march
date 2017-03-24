package com.example.madiba.venualpha.viewer.player;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.dailogs.CommentDialog;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMediaItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.profiles.UserPageV2Activity;

import org.parceler.Parcels;

import uk.co.senab.photoview.PhotoView;


public class PhotoMemoryFragment extends Fragment {
    private MdMediaItem mediaItem;

    private TextView mCommets,mRxns,mHashtag,mName;
    private RoundCornerImageView mAvatar;

    private ProgressBar progressBar;
    private PhotoView photoView;


    public PhotoMemoryFragment() {
        // Required empty public constructor
    }
    public static PhotoMemoryFragment newInstance() {

        return new PhotoMemoryFragment();
    }
    public static PhotoMemoryFragment newInstance(MdMediaItem param1) {
        PhotoMemoryFragment fragment = new PhotoMemoryFragment();
        Bundle args = new Bundle();
        args.putParcelable("item", Parcels.wrap(param1));
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mediaItem = Parcels.unwrap(getArguments().getParcelable("item"));
        }else {
//            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_player, container, false);
        mHashtag = (TextView) view.findViewById(R.id.hashtag);
        mCommets = (TextView) view.findViewById(R.id.comment);
        mRxns = (TextView) view.findViewById(R.id.reaction);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        photoView = (PhotoView) view.findViewById(R.id.image_view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRxns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestChat();
            }
        });
    }

    private void displayPhoto(String url){

        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(photoView);
    }

    private void displayHeader(String url,String name){

        mName.setText("");

        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(mAvatar);
    }

    private void displayFooter(String tag,String comment,String rxn){
        mName.setText("");
        mCommets.setText("");
        mRxns.setText("");

    }


    private void requestChat(){
        Log.i("Memory", "Comment");

        CommentDialog.newInstance().show(getChildFragmentManager(), "dialog");

    }

    private void requestEvent(MdEventItem eventItem){

    }
    private void requestUser(MdUserItem event){

        mAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(new Intent(getActivity(), UserPageV2Activity.class)));
            }
        });
    }



//    BACKGROUND OPERATIONS ///////////////////////////////////////////////////////////////////////////
//    /////////////////////////////////////////////////////////////////////////////////////////////////
//    /////////////////////////////////////////////////////////////////////////////////////////////////




}
