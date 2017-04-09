package com.example.madiba.venualpha.eventpage;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.PlusMinusView;

public class EventReactionFragment extends DialogFragment {


    private View mProgressView;
    private View mModeView;

    Spinner mTicketType ;
    TextView mTicketamountXprice,mTicketprice, note ;

    String selectedType,tickeformat;
    double price;
    int currentItem;

    EditText regMname,regMnumber;
    public EventReactionFragment() {
    }

    public static EventReactionFragment newInstance() {
        EventReactionFragment fragment = new EventReactionFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            d.getWindow().setLayout(width, height);
//            d.getWindow().setBackgroundDrawable(null);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_reaction, container, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    private void modeInvite(View view,String name,String url){
        RoundCornerImageView avatar = (RoundCornerImageView) view.findViewById(R.id.invite_avatar);
        TextView title = (TextView) view.findViewById(R.id.invite_text);

        Glide.with(this)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(avatar);
        title.setText(String.format("%s \nInvited you", name));


    }



    private void modeRegistration(View view){
         regMname = (EditText) view.findViewById(R.id.reg_name);
         regMnumber = (EditText) view.findViewById(R.id.reg_number);
        note = (TextView) view.findViewById(R.id.reg_info);


    }



    private void modeBuy(View view){

         mTicketType  = (Spinner) view.findViewById(R.id.ticket_selector);
         mTicketamountXprice = (TextView) view.findViewById(R.id.ticket_x_amount);
         mTicketprice = (TextView) view.findViewById(R.id.ticke_price);
         note = (TextView) view.findViewById(R.id.ticket_infor);

        PlusMinusView minusView = (PlusMinusView) view.findViewById(R.id.ticket_action);

        mTicketType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        minusView.setPlusMinusListener(new PlusMinusView.OnPlusMinusClickListener() {
            @Override
            public void onPlusClick(int currentNum) {
                currentItem=currentNum;
                mTicketamountXprice.setText(String.format("%s x %s", price,currentNum));
                mTicketamountXprice.setText(String.format("%s x", price*currentNum));
            }

            @Override
            public void onMinusClick(int currentNum) {
                currentItem=currentNum;
                mTicketamountXprice.setText(String.format("%s x %s", price,currentNum));
                mTicketamountXprice.setText(String.format("%s x", price*currentNum));
            }
        });


    }

    private String ticketFormat(){


        tickeformat = String.format("%s x %s \n %s \n %s", price,"","event title ",mTicketprice.getText().toString());
        return tickeformat;
    }







    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mModeView.setVisibility(show ? View.GONE : View.VISIBLE);
            mModeView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mModeView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mModeView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }






}
