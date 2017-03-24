package com.example.madiba.venualpha.onboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.NetUtils;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SignFragment extends Fragment {
    public static final int index =1;
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
    private Spinner spinner;

    private OnFragmentInteractionListener mListener;


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
        View view=inflater.inflate(R.layout.fragment_onboard_signup, container, false);

        mEmailView = (AutoCompleteTextView) view.findViewById(R.id.signup_email);
        mPhoneView = (EditText) view.findViewById(R.id.signup_phone);
        mUsername = (EditText) view.findViewById(R.id.signup_username);
        spinner = (Spinner) view.findViewById(R.id.country);
//        gotoLogin = (StateButton) view.findViewById(R.id.signup_goto_login);
        mEmailSignInButton = (StateButton) view.findViewById(R.id.sign_up_btn);
        mLoginFormView = view.findViewById(R.id.signup_sign_up_form);
        mPasswordView = (EditText) view.findViewById(R.id.signup_password);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.sign_up || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        });

        mEmailSignInButton.setOnClickListener(view2 ->
                onButtonPressed(3));
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




    private void initPhoneNumber(){
        /*********** read indicative sim *************/
        TelephonyManager telMgr =  (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);

        String simContryiso = telMgr.getSimCountryIso();
        int indicative = Iso2Phone.getPosition(simContryiso);
        LinkedHashMap<String, String> data= Iso2Phone.country_to_indicative;
        MyListAdapter adapter = new MyListAdapter(getActivity(), android.R.layout.simple_spinner_item,data);
        spinner.setAdapter(adapter);
        spinner.setSelection(indicative);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    data.get(i)
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void onButtonPressed(int num) {
        if (mListener != null) {
            mListener.onGotoDetails();
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
        void onGotoDetails();
    }


    public class MyListAdapter extends ArrayAdapter{

        private int resource;
        private LayoutInflater inflater;
        private Context context;
        private LinkedHashMap<String,String> data = new LinkedHashMap<>();
        public MyListAdapter ( Context ctx, int resourceId, LinkedHashMap<String,String> data) {

            super( ctx, resourceId );
            resource = resourceId;
            inflater = LayoutInflater.from( ctx );
            context=ctx;
            this.data = data;
        }

        @Override
        public View getView ( int position, View convertView, ViewGroup parent ) {

            convertView = (RelativeLayout) inflater.inflate( resource, null );

            String code =  data.get(position);

            TextView txtName = (TextView) convertView.findViewById(R.id.text1);
            txtName.setText(code);


            return convertView;
        }
    }

}
