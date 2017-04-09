package com.example.madiba.venualpha.profiles.mypage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.Actions.ActionCheck;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.profiles.TaskGetRelationship;
import com.example.madiba.venualpha.profiles.UseEvantsPageFragment;
import com.example.madiba.venualpha.profiles.UserGalleryPageFragment;
import com.example.madiba.venualpha.ui.SwitchTextView;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.jaychang.srv.SimpleRecyclerView;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.concurrent.Callable;

import timber.log.Timber;

public class MyUserActivity extends AppCompatActivity {

    private SimpleRecyclerView recyclerView;
    private View root;
    private ImageView avatar;
    private TextView mName, mStatus, mLocation, mTime, mCategory, mTag, mOverallCommenst, mDailyCommenst, mOverallStars, mDailyStars;
    private MdUserItem userItem;
    private Boolean userRelStatus;
    private String checkId;

    private SwitchTextView switchTextView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MyUserGalleryFragment.newInstance(""))
                .commit();

    }


    private void displayHeader(String url, String name, String status) {

        if (url == null || name == null) {
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
                .into(avatar);

        mName.setText("");
        if (status == null)
            mStatus.setText("");
    }

    private void displayRelationship() {

    }

    private void requestChat() {


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        progressDialog.show();
    }

    private void requestFollow() {


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                // TODO: 3/11/2017 add cancel chat request here  
            }
        });
        progressDialog.show();
    }

    private void requestunFollow() {

        final EditText txt = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setPositiveButton("Ok", (dialog, whichButton) -> {
                    unFollow();
                })
                .setNegativeButton("Cancel", (dialog, whichButton) -> dialog.cancel())
                .show();
    }

    private void unFollow() {
        ParseObject object = ParseObject.createWithoutData(GlobalConstants.CLASS_FOLLOW, checkId);
        object.deleteEventually(e -> {

        });

        toggle(false);

    }

    private void toggle(Boolean aBoolean) {
        userRelStatus = aBoolean;

    }

    private Callback<ActionCheck> loadCallBack = new Callback<ActionCheck>() {
        @Override
        public void onFinish(ActionCheck result, Callable callable, Throwable e) {
            if (e == null) {
                toggle(result.isFollow);
                checkId = result.id;

            } else {
                Timber.d("stated error %s", e.getMessage());
            }
        }

    };

    private void updateRelationship(String id) {
        Timber.d("stated");
        if (NetUtils.hasInternetConnection(getApplicationContext())) {
            TaskGetRelationship taskLoad = new TaskGetRelationship(id);
            Tasks.execute(taskLoad, loadCallBack);

        }
    }

    private void changeUsername(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_dailog_input, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        edt.setText(ParseUser.getCurrentUser().getString("status"));
        dialogBuilder.setTitle("Change Username");

        dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {
            String status = edt.getText().toString();
            if (status.length() > 0) {
                ParseUser.getCurrentUser().put("status",status);
                ParseUser.getCurrentUser().saveInBackground(e -> {
                    if (e == null){
                        Toast.makeText(this,"Username Updated",Toast.LENGTH_SHORT).show();
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
