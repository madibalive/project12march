package com.example.madiba.venualpha.settings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseUser;

import timber.log.Timber;

public class SettingsActivityFragment extends Fragment {


    ProgressDialog progressDialog;
    private Button password, username, logout, feedback;
    private ParseUser mCurrentUser;


    public SettingsActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCurrentUser = ParseUser.getCurrentUser();
//        FeedbackManager.register(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClick();

    }

    private void initClick() {


        password.setOnClickListener(view -> {
            if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
                changePassword();
            }
        });
        username.setOnClickListener(view -> {
            if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
                changeUsername();
            }
        });
        logout.setOnClickListener(view -> {
            if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
                deleteAccount();
            }
        });

//        feedback.setOnClickListener(view -> {
////            FeedbackManager.showFeedbackActivity(getActivity());
//
//        });
    }



    private void initView(View view) {


        username = (Button) view.findViewById(R.id.account_change_username);
        password = (Button) view.findViewById(R.id.account_change_password);
        logout = (Button) view.findViewById(R.id.account_logout);
    }


    private void deleteAccount(){
        ParseUser.logOut();
//        startActivity(new Intent(getActivity(), LoginFragment.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private void changePassword(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_dailog_input, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        dialogBuilder.setTitle("Change Password");
        dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
            String newUsername = edt.getText().toString();
            if (newUsername.length() > 0) {
                ParseUser.getCurrentUser().setPassword(newUsername);
                ParseUser.getCurrentUser().saveInBackground(e -> {
                    if (e == null){
                        Toast.makeText(getActivity(),"Username Updated",Toast.LENGTH_SHORT).show();
                    }else {
                        Timber.e("error updateing user %s",e.getMessage());
                    }
                });
            }
        });
        dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void changeUsername(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_dailog_input, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        edt.setHint(ParseUser.getCurrentUser().getUsername());
        dialogBuilder.setTitle("Change Username");

        dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
            String newUsername = edt.getText().toString();
            if (newUsername.length() > 0) {
                ParseUser.getCurrentUser().setUsername(newUsername);
                ParseUser.getCurrentUser().saveInBackground(e -> {
                    if (e == null){

                        Toast.makeText(getActivity(),"Username Updated",Toast.LENGTH_SHORT).show();

                    }else {
                        Timber.e("error updateing user %s",e.getMessage());
                    }
                });
            }
        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {

        });

        AlertDialog b = dialogBuilder.create();
        b.show();

    }




}
