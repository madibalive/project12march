package com.example.madiba.venualpha.dailogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.Actions.ActionNewGossip;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.CircleView;
import com.example.madiba.venualpha.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.example.madiba.venualpha.R.attr.selectedColor;


public class CreateGossipSheet extends BottomSheetDialogFragment {
    TextInputEditText mTitle;
    TextInputEditText mDesc;
    Button mPost;

    public CreateGossipSheet() {
    }

    static CreateGossipSheet newInstance(String string) {
        return new CreateGossipSheet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_gossip, container, false);
        mTitle = (TextInputEditText) v.findViewById(R.id.post_gossip_title);
//        mPost = (Button) v.findViewById(R.id.post_gossip_post);
        mPost.setOnClickListener(view -> attemptLogin());
        return v;
    }


    private void setupColorAdapter(View customView,int[] mainColors){
        RecyclerView recyclerView = (RecyclerView) customView.findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);


        List<Integer> colorViews = new ArrayList<>();
        for (int i = 0, length = mainColors.length; i < length; i++) {
            colorViews.add(colorView);
        }
        ColorAdapter colorAdapter = new ColorAdapter(R.layout.item_person,colorViews);
        recyclerView.setAdapter(colorAdapter);
        colorAdapter.setColorListener((position, color) -> {

        });
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    public class ColorAdapter extends BaseQuickAdapter<Integer> {
        public int color;
        private ColorListener colorListener;
        public boolean isSubColor = false;
        public boolean selected;


        public ColorAdapter(int layoutResId, List<Integer> data) {
            super(layoutResId, data);
        }

           @Override
        protected void convert(BaseViewHolder holder, Integer category) {

            holder.getConvertView().setOnClickListener(v -> {
                if (colorListener != null && holder.getAdapterPosition() != -1) {
                    colorListener.onColorSelected(holder.getAdapterPosition(), category);
                }
            });
            CircleView circleView = holder.getView(R.id.checked);
            circleView.setColor(color);
            if (holder.getAdapterPosition() == color)
              circleView.setActivated(selected);

        }

        void setColorListener(ColorListener colorListener) {
            this.colorListener = colorListener;
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

    public interface ColorListener {
        void onColorSelected(int position, int color);
    }
}
