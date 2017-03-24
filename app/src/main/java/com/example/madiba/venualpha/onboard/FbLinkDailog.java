package com.example.madiba.venualpha.onboard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madiba.venualpha.R;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 11/5/2016.
 */



public class FbLinkDailog extends DialogFragment {


    private ImageView back;
    private Button submit;
    private TextView mTitle;
    private OnFragmentInteractionListener mListener;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
        add(" publish_actions");
    }};

    public static FbLinkDailog newInstance( ) {

        return new FbLinkDailog();
    }

    public FbLinkDailog() {
    }


    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
            d.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    onButtonPressed(false);
                }
            });
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_link_facebook, container, false);
        submit = (Button) view.findViewById(R.id.submit);
        mTitle = (TextView) view.findViewById(R.id.title);
        back= (ImageView) view.findViewById(R.id.close);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
//        submit.setOnClickListener(view1 -> onButtonPressed(true));

    }

    private void linkFacebook(){
        if (!ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseFacebookUtils.linkWithReadPermissionsInBackground(ParseUser.getCurrentUser(), this, mPermissions, new SaveCallback() {
                @Override
                public void done(ParseException ex) {
                    if (ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                        onButtonPressed(true);
                    }
                }
            });
        }
    }

      // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Boolean uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Boolean uri);
    }
}
