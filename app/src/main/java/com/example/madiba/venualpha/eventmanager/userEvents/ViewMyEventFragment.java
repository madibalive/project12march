package com.example.madiba.venualpha.eventmanager.userEvents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.util.ViewUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ViewMyEventFragment extends Fragment {
    PopupMenu popupMenu;
    private List<MdUserItem> attendeeDatas=new ArrayList<>();

    private LinearLayout mStatLayout,mAttendeesLayout;
    private ViewGroup mStatContainer,mAttendeesContainers;
    private View mProgressView;
    private RoundCornerImageView imageView;
    private ProgressDialog progressDialog;

    private Button mInviteMore,mPromote,mVerifiedBar;
    private ImageButton mEditBtn,mAddImage,mMoreBtn;
    private TextView mEventInfo,mEventDate;


    public ViewMyEventFragment() {
    }

    public static ViewMyEventFragment newInstance() {
        return new ViewMyEventFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_event, container, false);
        mStatLayout = (LinearLayout) view.findViewById(R.id.stats_layout);
        mStatContainer = (ViewGroup) view.findViewById(R.id.stats_container);
        mAttendeesLayout = (LinearLayout) view.findViewById(R.id.attendees_layout);
        mAttendeesContainers = (ViewGroup) view.findViewById(R.id.attendees_container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<ParseObject> eventItems = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            eventItems.add(new ParseObject(""));
        }
        displayStat(eventItems);


        List<ParseUser> items = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            items.add(new ParseUser());
//        }


        displayAttendees(items);
    }

    private void initView(){

    }


    private void setListeners(View view){

//        mPromote = (Button) view.findViewById(R.id.promote_btn);
//        mInviteMore = (Button) view.findViewById(R.id.attendees_more);
//
//        mEditBtn = (ImageButton) view.findViewById(R.id.edit_btn);
//        mAddImage = (ImageButton) view.findViewById(R.id.add_memory_btn);
//        mMoreBtn = (ImageButton) view.findViewById(R.id.overflow_btn);

        mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mInviteMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private void displayEvent(MdEventItem eventItem){

        if (eventItem ==null){
            return;
        }

        if (eventItem ==null) {

            //avatar
            Glide.with(this)
                    .load(eventItem.getUrlMedium())
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into(imageView);
        }
        mEventDate.setText("");
        mEventInfo.setText("");

        // TODO: 3/5/2017 goto profile
    }


    private void displayAttendees(List<ParseUser> attendeeDatas){
//
//        if (attendeeDatas.size()<0) {
//            mAttendeesContainers.setVisibility(View.GONE);
//            return;
//        }
//
//        else {
            mAttendeesContainers.setVisibility(View.VISIBLE);
            mAttendeesLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getActivity());

        for (int i = 0; i < 8; i++) {
                RoundCornerImageView chipView = (RoundCornerImageView) inflater.inflate(
                        R.layout.item_attende, mAttendeesLayout, false);
                ViewUtils.setMargins(chipView,0,4,4,4);
////                chipView.setText(feature.getTitle());
////                chipView.setContentDescription(feature.getTitle());
////                chipView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
////                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
////                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        getActivity().startActivity(intent);
////                    }
////                });

                mAttendeesLayout.addView(chipView);
            }
//        }

    }


    private void displayStats(List<ParseUser> attendeeDatas){

        if (attendeeDatas.size()<0) {
            mStatContainer.setVisibility(View.GONE);
            return;
        }

        else {
            mStatContainer.setVisibility(View.VISIBLE);
            mStatLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getActivity());

//            for (final ParseObject feature : attendeeDatas) {
//                ImageView chipView = (ImageView) inflater.inflate(
//                        R.layout.draw_layout, mTags, false);
////                chipView.setText(feature.getTitle());
////                chipView.setContentDescription(feature.getTitle());
////                chipView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        Intent intent = new Intent(getContext(), ExploreSessionsActivity.class)
////                                .putExtra(ExploreSessionsActivity.EXTRA_FILTER_TAG, tag.getId())
////                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                        getActivity().startActivity(intent);
////                    }
////                });
//
//                mTags.addView(chipView);
//            }
        }

    }


    private void displayStat(List<ParseObject> stats){

        if (stats.size()<0) {
            mStatContainer.setVisibility(View.GONE);
            return;
        }

        else {
            mStatContainer.setVisibility(View.VISIBLE);
            mStatLayout.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            for (final ParseObject feature : stats) {
                LinearLayout chipView = (LinearLayout) inflater.inflate(
                        R.layout.item_stat, mStatLayout, false);

                mStatLayout.addView(chipView);
            }
        }

    }


    private void requestEvent(){

    }


    private void requestEditEvent(){

    }


    private void requestDelete(){

    }


    private void requestShare(){

    }

    private void requestInviteList(){

    }

    private void requestGallery(){

    }




    private void showPopup(View view) {
        popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.menu_discover);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() ==R.id.action_editt){

                // what to do


            }else if (item.getItemId() == R.id.action_delete){

            }
            return false;
        });
        popupMenu.show();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mStatContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            mStatContainer.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mStatContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mStatContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}
