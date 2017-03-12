package com.example.madiba.venualpha.onboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.example.madiba.venualpha.MainActivity;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment {

    public static final int index =0;

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};
    private static final String TAG = "LOGIN";
    StateButton signUp,loginBtn;
    private ProgressDialog progress;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_onboard_login, container, false);

        mEmailView = (EditText) view.findViewById(R.id.login_username);
        signUp = (StateButton) view.findViewById(R.id.login_to_signup);
        mLoginFormView = view.findViewById(R.id.login_login_form);
        mPasswordView = (EditText) view.findViewById(R.id.login_password);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        signUp.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), SignFragment.class)));

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        loginBtn.setOnClickListener(view2 ->{
            attemptLogin();
        });

    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email is empty");
            if (focusView==null)focusView = mEmailView;
            cancel = true;
        }
//         if (!isEmailValid(email)) {
//            mEmailView.setError("Valid Email Required");
//            if (focusView==null)focusView = mEmailView;
//            cancel = true;
//        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Password is empty");
            if (focusView==null)focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("min Lenght of 8 char");
            if (focusView==null)focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            Timber.d("error loggin - missing parameters");
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (NetUtils.hasInternetConnection(getApplicationContext())){
                progress = ProgressDialog.show(getActivity(), null,
                        getResources().getString(R.string.progress_connecting), true);
                userLogin(email, password);
            }
        }
    }

    private void userLogin(String email, String password) {
        ParseUser.logInInBackground(email, password, (user, e) -> {
            if (e != null) {
                // Show the error message
                progress.dismiss();
                Timber.d("error loging user : %s" ,e.getMessage());
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Timber.i("success signing up");
                setupInitialSettings(user);



            }
        });
    }

    private void setupInitialSettings(ParseUser user){
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.put("user_id", ParseUser.getCurrentUser().getObjectId());
        installation.saveInBackground();
        startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();

    }

    private boolean isPasswordValid(String password) {
        Timber.e("password too short");
        return password.length() > 7;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(index,true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int index,Boolean aBoolean);
    }

}
