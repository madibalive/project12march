package com.example.madiba.venualpha.profiles;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.Actions.ActionCheck;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.jaychang.srv.SimpleRecyclerView;
import com.parse.ParseObject;

import java.util.concurrent.Callable;

import timber.log.Timber;

public class UserPageActivity extends AppCompatActivity {

    private SimpleRecyclerView recyclerView;
    private View root;
    private ImageView avatar;
    private TextView mName,mStatus,mLocation,mTime,mCategory,mTag,mOverallCommenst,mDailyCommenst,mOverallStars,mDailyStars;
    private MdUserItem userItem;
    private Boolean userRelStatus;
    private String checkId;

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

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Section 1",
                        "Section 2",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.


                Fragment frg;
                if (position ==0){
                    frg = UserGalleryPageFragment.newInstance("");
                }else {
                    frg = UseEvantsPageFragment.newInstance("");
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,frg)
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }



    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }


    private void displayHeader(String url,String name,String status){

        if (url ==null || name ==null){
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
        if (status ==null)
            mStatus.setText("");
    }

    private void displayRelationship(){

    }

    private void requestChat(){
        
        
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

    private void requestFollow(){
        
        
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

    private void requestunFollow(){

        final EditText txt = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setPositiveButton("Ok", (dialog, whichButton) -> {
                    unFollow();
                })
                .setNegativeButton("Cancel", (dialog, whichButton) -> dialog.cancel())
                .show();
    }

    private void unFollow(){
        ParseObject object = ParseObject.createWithoutData(GlobalConstants.CLASS_FOLLOW,checkId);
        object.deleteEventually(e -> {

        });

        toggle(false);

    }

    private void toggle(Boolean aBoolean){
        userRelStatus = aBoolean;

    }

    private Callback<ActionCheck> loadCallBack = new Callback<ActionCheck>() {
        @Override
        public void onFinish(ActionCheck result, Callable callable, Throwable e) {
            if (e == null) {
                toggle(result.isFollow);
                checkId = result.id;

            } else {
                Timber.d("stated error %s",e.getMessage());
            }
        }

    };

    private void updateRelationship(String id) {
        Timber.d("stated");
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            TaskGetRelationship taskLoad = new TaskGetRelationship(id);
            Tasks.execute(taskLoad,loadCallBack);
            
        }
    }



    private void unfollow(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.view_dailog_input, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Change Username");

        dialogBuilder.setPositiveButton("Done", (dialog, whichButton) -> {

        });

        dialogBuilder.setNegativeButton("Cancel", (dialog, whichButton) -> {

        });

        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private void follow(){

    }

}
