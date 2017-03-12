package com.example.madiba.venualpha.dailogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.parse.ParseUser;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     UserListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link NavMenuDailogFragment.Listener}.</p>
 */
public class NavMenuDailogFragment extends DialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_ITEM = "item_count";
    private Listener mListener;

    TextView mName;
    ImageButton mClose,mSettings;
    RoundCornerImageView mAvatar;

    // TODO: Customize parameters
    public static NavMenuDailogFragment newInstance() {
        return new NavMenuDailogFragment();
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
            d.setOnCancelListener(dialogInterface -> {
                if (mListener != null) {
                    mListener.onItemClicked(0);
                    dismiss();
                }
            });
            d.getWindow().setBackgroundDrawable(null);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navmenu_dialog, container, false);
        mAvatar = (RoundCornerImageView) v.findViewById(R.id.avatar);
        mName = (TextView) v.findViewById(R.id.name);
        mClose = (ImageButton) v.findViewById(R.id.close);
        mSettings = (ImageButton) v.findViewById(R.id.settings);
        return v;    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mClose.setOnClickListener(view12 -> {
            if (mListener != null) {
                mListener.onItemClicked(0);
                dismiss();
            }
        });

        mSettings.setOnClickListener(view1 -> {
            if (mListener != null) {
                mListener.onItemClicked(1);
                dismiss();
            }
        });

        mAvatar.setOnClickListener(view13 -> {
            if (mListener != null) {
                mListener.onItemClicked(1);
                dismiss();
            }
        });

//        display();

    }


    private void display(){
        if (ParseUser.getCurrentUser() ==null){
            return;
        }
        //avatar
        Glide.with(this)
                .load(ParseUser.getCurrentUser().getParseFile("avatarSmall"))
                .crossFade()
                .placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.placeholder_error_media)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .fallback(R.drawable.ic_default_avatar)
                .thumbnail(0.4f)
                .into(mAvatar);

        mName.setText("Name\nINCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY ");

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onItemClicked(int position);
    }

}
