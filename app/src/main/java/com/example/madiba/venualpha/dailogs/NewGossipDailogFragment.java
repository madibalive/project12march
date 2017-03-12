package com.example.madiba.venualpha.dailogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.Actions.ActionNewGossip;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.ui.CircleView;
import com.example.madiba.venualpha.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     UserListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link NewGossipDailogFragment.Listener}.</p>
 */
public class NewGossipDailogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_ITEM = "item_count";
    private Listener mListener;
    private List<MdUserItem> datas ;
    private RecyclerView recyclerView;
    TextInputEditText mTitle;
    TextInputEditText mDesc;
    Button mPost;


    // TODO: Customize parameters
    public static NewGossipDailogFragment newInstance() {
        return new NewGossipDailogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_gossip, container, false);
        mTitle = (TextInputEditText) v.findViewById(R.id.post_gossip_title);
//        mPost = (Button) v.findViewById(R.id.post_gossip_post);
        mPost.setOnClickListener(view -> attemptLogin());
        return v;    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void setupColorAdapter(View customView,int[] mainColors){
        RecyclerView recyclerView = (RecyclerView) customView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);


        List<Integer> colorViews = new ArrayList<>();
        for (int i = 0, length = mainColors.length; i < length; i++) {
//            colorViews.add(colorView);
        }
        ColorAdapter colorAdapter = new ColorAdapter(R.layout.item_person,colorViews);
        recyclerView.setAdapter(colorAdapter);

    }

    private void attemptLogin() {
        mTitle.setError(null);
        String title = mTitle.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(title)) {
            mTitle.setError("title cannot be empty");
            focusView = mTitle;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
                EventBus.getDefault().post(new ActionNewGossip(title));
            }
            dismiss();
        }
    }




    public class ColorAdapter extends BaseQuickAdapter<Integer> {
        public int color;
        public boolean isSubColor = false;
        public boolean selected;

        public ColorAdapter(int layoutResId, List<Integer> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, Integer category) {

            holder.getConvertView().setOnClickListener(v -> {
                if (mListener != null  && holder.getAdapterPosition() != -1) {
                    mListener.onColorSelected(holder.getAdapterPosition(), category);

                }
            });
            CircleView circleView = holder.getView(R.id.checked);
            circleView.setColor(color);
            if (holder.getAdapterPosition() == color)
                circleView.setActivated(selected);
        }

        void setSelectedPosition(int position,CircleView circleView) {
            if (color !=position){
                circleView.setActivated(false);
                notifyItemChanged(color);
            }
            color =position;
            notifyItemChanged(position);
        }
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
        void onColorSelected(int position, int color);
    }



}
