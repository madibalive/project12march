package com.example.madiba.venualpha.eventmanager.userEvents;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.expandable.ExpandableLinearLayout;
import com.parse.ParseUser;

import java.util.List;

import timber.log.Timber;

public class UserEventsFragment extends Fragment {
    PopupMenu popupMenu;

    private TextView mtitle,mdate,mLocation,mTime,mCategory,mTag,mOverallCommenst,mDailyCommenst,mOverallStars,mDailyStars;
    private Button btnAction;
    private RoundCornerImageView imageView;
    private LinearLayout mAttendeesLayout;
    private ViewGroup mAttendeesContainers;
    private ExpandableLinearLayout mMoreLayout;
    private View mProgressView;
    private View mView;
    private int mode;

    public UserEventsFragment() {
    }




    public static UserEventsFragment newInstance(int number) {
        UserEventsFragment fragment = new UserEventsFragment();
        Bundle args = new Bundle();
        args.putInt("mode", number);
        fragment.setArguments(args);
        return fragment;    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt("mode");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_event, container, false);
//            btnAction = (Button) view.findViewById(R.id.ev_action_btn);
//            mCategory = (TextView) view.findViewById(R.id.ev_category);
        mdate = (TextView) view.findViewById(R.id.ev_date);
        mtitle = (TextView) view.findViewById(R.id.ev_title);
//            mLocation = (TextView) view.findViewById(R.id.ev_location);
//            mtitle = (TextView) view.findViewById(R.id.ev_time);
        mTag = (TextView) view.findViewById(R.id.ev_tag);
        Timber.i("JUST VIEW  HERE ");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initView(){

        mMoreLayout.setOnClickListener(view -> {
            Timber.e("clicked more button ");
            mMoreLayout.toggle();
        });

    }





    private void displayHeader(String url,String day,String name){

        if (url ==null ||day ==null || name ==null){
            return;
        }
        //avatar
        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(imageView);
        mtitle.setText("");
        mdate.setText("");
    }

    private void displayButton(View view){

    }

    private void displayStat(){

    }



    private void displayAttendees(List<ParseUser> attendeeDatas){


        if (attendeeDatas.size()<0) {
            mAttendeesContainers.setVisibility(View.GONE);
            return;
        }

        else {
            mAttendeesContainers.setVisibility(View.VISIBLE);
            mAttendeesLayout.removeAllViews();
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


    private void requestEdit(){

    }


    private void requestSendUpdate(){

    }

    private void requestAddImages(){

    }

    private void requestUpdateGoal(){

    }


    private void requestManageInvite(){

    }



    private void showPopup(View view) {
        popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "share");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "delete");

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() ==R.id.action_editt){

            }else if (item.getItemId() == R.id.action_delete){

            }
            return false;
        });

        popupMenu.show();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mView.setVisibility(show ? View.GONE : View.VISIBLE);
            mView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




}
