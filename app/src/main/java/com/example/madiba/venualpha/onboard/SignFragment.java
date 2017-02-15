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
import android.widget.AutoCompleteTextView;
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
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.NetUtils;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SignFragment extends Fragment {

    public static final List<String> mPermissions = new ArrayList<String>() {{
        add("public_profile");
        add("email");
        add("user_friends");
        add(" publish_actions");
    }};

    StateButton mBtnFb;
    StateButton gotoLogin,mEmailSignInButton;
    private ProgressDialog progress;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mPhoneView;
    private EditText mUsername;
    private View mProgressView;
    private View mLoginFormView;
    private MobiComUserPreference mobiComUserPreference;
    private UserLoginTask mAuthTask = null;


    public SignFragment() {
    }

    public static SignFragment newInstance() {
        return new SignFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_signup, container, false);

        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.signup_email);
        mPhoneView = (EditText) view.findViewById(R.id.signup_phone);
        mUsername = (EditText) view.findViewById(R.id.signup_username);
        gotoLogin = (StateButton) view.findViewById(R.id.signup_goto_login);
        mBtnFb = (StateButton) view.findViewById(R.id.fb_signup);
        mEmailSignInButton = (StateButton) view.findViewById(R.id.signup_signup_btn);
        mLoginFormView = view.findViewById(R.id.signup_sign_up_form);
        mPasswordView = (EditText) view.findViewById(R.id.signup_password);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gotoLogin.setOnClickListener(view1 -> {

        });
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.sign_up || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mEmailSignInButton.setOnClickListener(view2 -> attemptLogin());


        mBtnFb.setOnClickListener(v -> {
            if (NetUtils.hasInternetConnection(getApplicationContext()))
                onFbSignup();

        });

    }


    private void updateUser() {
        // request phonenumber
        //
    }


    private void onFbSignup() {

        ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(), mPermissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Timber.i("MyApp Uh oh. The user cancelled the Facebook login.");

                    Toast.makeText(getApplicationContext(), "Log-out from Facebook and try again please!", Toast.LENGTH_SHORT).show();

                    ParseUser.logOut();

                } else if (user.isNew()) {
                    Timber.i("MyApp User signed up and logged in through Facebook!");

                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, getActivity(), mPermissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Timber.i("MyApp Woohoo, user logged in with Facebook!");
                                    progress = ProgressDialog.show(getActivity(), null,
                                            getResources().getString(R.string.progress_connecting), true);
                                    getUserDetailsFromFB();
                                }
                            }
                        });
                    } else {
                        progress = ProgressDialog.show(getActivity(), null,
                                getResources().getString(R.string.progress_connecting), true);
                        getUserDetailsFromFB();
                    }
                } else {
                    Timber.i("MyApp User logged in through Facebook!");

                    if (!ParseFacebookUtils.isLinked(user)) {
                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, getActivity(), mPermissions, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseFacebookUtils.isLinked(user)) {
                                    Timber.i("MyApp Woohoo, user logged in with Facebook!");
                                    ParseUser.logOut();

                                }
                            }
                        });
                    } else {
                        ParseUser.logOut();
                    }
                }
            }
        });
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPhoneView.setError(null);
        mUsername.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String username = mUsername.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(username)) {
            mUsername.setError("Username is empty");
            if (focusView==null) focusView = mUsername;
            cancel = true;
        } else if (!isUserNameValid(username)) {
            mUsername.setError("min lenght is 5");
            if (focusView==null) focusView = mUsername;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Email is empty");
            if (focusView==null) focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            if (focusView==null) focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Password is empty");
            if (focusView==null) focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError("Min lenght is 8 ");
            if (focusView==null) focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError("Phone Contact is empty");
            if (focusView==null) focusView = mPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mPhoneView.setError("Must be more 10 number");
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            Timber.e("failed parse login ");

        } else {

            if (NetUtils.hasInternetConnection(getApplicationContext())){
                Timber.e("staring parse login ");
                progress = ProgressDialog.show(getActivity(), null,
                        getResources().getString(R.string.progress_connecting), true);
                userSignUp(username, email, password, phone);
            }
        }


    }


    private void userSignUp(String username, String email, String password, String phone) {

        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.put("phone", phone);
        user.put("followers",0);
        user.put("followering",0);

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    progress.dismiss();
                    // Show the error message
                    Timber.d("error signing user : %s" ,e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Timber.i("success signing up");
                    initSettings();


                }
            }
        });
    }

    private void initSettings(){

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.put("user_id", ParseUser.getCurrentUser().getObjectId());
        installation.saveInBackground();

        ParseObject userRelations = new ParseObject(GlobalConstants.CLASS_USER_RELATION);
        userRelations.put("user", ParseUser.getCurrentUser());
        userRelations.saveInBackground();

        mobiComUserPreference = MobiComUserPreference.getInstance(getApplicationContext());
        UserLoginTask.TaskListener listener = new UserLoginTask.TaskListener() {
            @Override
            public void onSuccess(RegistrationResponse registrationResponse, Context context) {
                ApplozicSetting.getInstance(context).setSentMessageBackgroundColor(R.color.venu_flat_color); // accepts the R.color.name
                ApplozicSetting.getInstance(context).setReceivedMessageBackgroundColor(R.color.venu_orange); // accepts the R.color.name
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
                startActivity(new Intent(getActivity(), OnboardUsersActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                finish();
                // TODO: 2/9/2017 implement next page
            }

            @Override
            public void onFailure(RegistrationResponse registrationResponse, Exception exception) {

                mAuthTask = null;
                progress.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("");
                alertDialog.setMessage(exception.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(android.R.string.ok),
                        (dialog, which) -> {
                            dialog.dismiss();
                        });
//                if (!isFinishing()) {
//                    alertDialog.show();
//                }
            }
        };

        User applozicUser = new User();
        applozicUser.setUserId(ParseUser.getCurrentUser().getObjectId()); //applozicUserId it can be any unique applozicUser identifier
        applozicUser.setDisplayName(ParseUser.getCurrentUser().getUsername()); //displayName is the name of the applozicUser which will be shown in chat messages
        applozicUser.setEmail(ParseUser.getCurrentUser().getEmail()); //optional
        applozicUser.setImageLink("");//optional,pass your image link


        mAuthTask = new UserLoginTask(applozicUser, listener, getApplicationContext());
        mAuthTask.execute((Void) null);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

    }

    private void getFbUserPhoneNumber(){
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.dailog_settings, null);
//        dialogBuilder.setView(dialogView);
//        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
//        edt.setHint("+233XXXXXXXXX");
//        dialogBuilder.setTitle("Required Phone Number");
//        dialogBuilder.setMessage("This allows us to find friend in your contact ");
//        dialogBuilder.setPositiveButton("Continue", (dialog, whichButton) -> {
//
//            String phoneNumber = edt.getText().toString();
//            if (phoneNumber.length() > 0) {
//                ParseUser.getCurrentUser().put("phone",phoneNumber);
//                ParseUser.getCurrentUser().saveInBackground(e -> {
//                    if (e == null){
//                        progress = ProgressDialog.show(getActivity(), null,
//                                getResources().getString(R.string.progress_connecting), true);
//                        initSettings();
//
//                    }else {
//                        Timber.e("error updateing user %s",e.getMessage());
//                    }
//                });
//            }
//        });
//        dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> getUserDetailsFromFB());
//        AlertDialog b = dialogBuilder.create();
//        b.show();
    }

    private void getUserDetailsFromFB() {

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,name");

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                (object, response) -> {
                    try {
//                        mEmailView.setText(email);
//                        mUsername.setText(name);

                        String name = response.getJSONObject().getString("name");
                        String email = response.getJSONObject().getString("email");

                        // TODO: 2/15/2017 set to all users

//                        ParseUser user = ParseUser.getCurrentUser();
//                        user.setUsername(name);
//                        user.setEmail(email);
//                        user.put("followers",0);
//                        user.put("followering",0);
//                        user.save();

//                        JSONObject picture = response.getJSONObject().getJSONObject("picture");
//                        JSONObject data = picture.getJSONObject("data");
                        getFbUserPhoneNumber();
                    } catch (JSONException  e) {
                        e.printStackTrace();
                    }finally {
                        progress.dismiss();

                    }
                });

        request.setParameters(parameters);
        request.executeAsync();

    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    private boolean isPhoneValid(String number) {
        return number.length() > 9;
    }

    private boolean isUserNameValid(String username) {
        return username.length() > 4;
    }


}
