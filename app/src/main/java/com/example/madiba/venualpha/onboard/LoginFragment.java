package com.example.madiba.venualpha.onboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.account.register.RegistrationResponse;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.PushNotificationTask;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.account.user.UserLoginTask;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment {
    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
    }};
    private static final String TAG = "LOGIN";
    StateButton signUp,mBtnFb,loginBtn;
    private ProgressDialog progress;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private MobiComUserPreference mobiComUserPreference;
    private UserLoginTask mAuthTask = null;


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
        mBtnFb = (StateButton) view.findViewById(R.id.login_facebook);
        loginBtn = (StateButton) view.findViewById(R.id.login_button);
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


        mBtnFb.setOnClickListener(v ->{
//            if (NetUtils.hasInternetConnection(getApplicationContext()))
//                onFbLogin();
        });
    }


    private void onFbLogin() {

        ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(), mPermissions, (user, err) -> {

            if (err == null) {
                if (user == null) {
                    Timber.i("MyApp Uh oh. The user cancelled the Facebook login.");
                    Toast.makeText(getApplicationContext(), "Facebook Login cancelled!", Toast.LENGTH_SHORT).show();
                    ParseUser.logOut();
                }else if (user.isNew()) {
                    Timber.i("MyApp User signed up and logged in through Facebook!");
                    try {
                        user.delete();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    Timber.i("MyApp User logged in through Facebook!");

                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, getActivity(), mPermissions, ex -> {
                            if (ParseFacebookUtils.isLinked(user)) {
                                Timber.i("MyApp Woohoo, user logged in with Facebook!");
                                progress = ProgressDialog.show(getActivity(), null,
                                        getResources().getString(R.string.progress_connecting), true);
                                setupInitialSettings(ParseUser.getCurrentUser());
                            }
                        });
                    } else {
                        progress = ProgressDialog.show(getActivity(), null,
                                getResources().getString(R.string.progress_connecting), true);
                        setupInitialSettings(user);
                    }
                }
            } else {
                Timber.e(err.getMessage());
                Toast.makeText(getApplicationContext(), "Facebook Login Failed!", Toast.LENGTH_SHORT).show();

            }
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


        mobiComUserPreference = MobiComUserPreference.getInstance(getApplicationContext());
        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                ApplozicSetting.getInstance(context).setSentMessageBackgroundColor(R.color.venu_green); // accepts the R.color.name
                ApplozicSetting.getInstance(context).setReceivedMessageBackgroundColor(R.color.venu_flat_color); // accepts the R.color.name
                ApplozicSetting.getInstance(context).setSentMessageBorderColor(R.color.venu_flat_color); // accepts the R.color.name
                ApplozicSetting.getInstance(context).setReceivedMessageBorderColor(R.color.venu_flat_color); // accepts the R.color.name
                ApplozicSetting.getInstance(context).disableLocationSharingViaMap();
                PushNotificationTask.TaskListener pushNotificationTaskListener = new PushNotificationTask.TaskListener() {
                    @Override
                    public void onSuccess(RegistrationResponse registrationResponse) {

                    }

                    @Override
                    public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                    }
                };
                PushNotificationTask pushNotificationTask = new PushNotificationTask(Applozic.getInstance(context).getDeviceRegistrationId(), pushNotificationTaskListener, context);
                pushNotificationTask.execute((Void) null);
                progress.dismiss();
//                startActivity(new Intent(getActivity(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));


            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {
                //If any failure in registration the callback  will come here
                progress.dismiss();

                mAuthTask = null;

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("");
                alertDialog.setMessage(exception.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                        (dialog, which) -> {
                            dialog.dismiss();
                            startActivity(new Intent(getActivity(), OnboardUsersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                            finish();
                        });
//                if (!isFinishing()) {
//
//                    alertDialog.show();
//                }


            }
        };

        User applozicUser = new User();
        applozicUser.setUserId(user.getObjectId()); //applozicUserId it can be any unique applozicUser identifier
        applozicUser.setDisplayName(user.getUsername()); //displayName is the name of the applozicUser which will be shown in chat messages
        applozicUser.setEmail(user.getEmail()); //optional
        applozicUser.setImageLink("");//optional,pass your image link


        mAuthTask = new UserLoginTask(applozicUser, listener, getApplicationContext());
        mAuthTask.execute((Void) null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

    }

    private boolean isPasswordValid(String password) {
        Timber.e("password too short");
        return password.length() > 7;
    }

}
