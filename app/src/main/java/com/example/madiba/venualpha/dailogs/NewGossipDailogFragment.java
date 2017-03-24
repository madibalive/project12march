package com.example.madiba.venualpha.dailogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.madiba.venualpha.Actions.ActionNewGossip;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.ui.CircleView;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class NewGossipDailogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_ITEM = "item_count";
    private List<MdUserItem> datas ;
    private RecyclerView recyclerView;
    EditText mTitle,mLink;
    TextInputEditText mDesc;
    StateButton mPost;
    private View root;
    private int selectedColor;
    private LinearLayout mColorsLayout;
    private ViewGroup mColorsContainer;
    private ImageView mClose;
//    private Listener mListener;


    // TODO: Customize parameters
    public static NewGossipDailogFragment newInstance() {
        return new NewGossipDailogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_gossip, container, false);
        mTitle = (EditText) v.findViewById(R.id.title);
        mLink = (EditText) v.findViewById(R.id.link);
        mPost = (StateButton) v.findViewById(R.id.submit);
        mClose = (ImageView) v.findViewById(R.id.close);
        mColorsLayout = (LinearLayout) v.findViewById(R.id.colors_layout);
//        mColorsContainer = (ViewGroup) v.findViewById(R.id.colors_container);

        root=v;
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        displayColors();

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


    private void displayColors() {
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);

        mColorsContainer.setVisibility(View.VISIBLE);
        mColorsLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (mColors.length == 0) {
            mColorsLayout.setVisibility(View.GONE);
            return;
        }

        for (final int feature : mColors) {
            CircleView circleView = (CircleView) inflater.inflate(
                    R.layout.item_colorview, mColorsLayout, false);
            circleView.setColor(feature);
            circleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    if (null != mListener) {
//                        mListener.onColorSelected(feature);
//                    }
                        selectedColor=feature;
                        updateColor(selectedColor);
                }
            });

            mColorsLayout.addView(circleView);
        }

    }

    private void updateColor(int color){
        root.setBackgroundColor(color);
    }




//    public class ColorAdapter extends BaseQuickAdapter<Integer> {
//        public int color;
//        public boolean isSubColor = false;
//        public boolean selected;
//
//        public ColorAdapter(int layoutResId, List<Integer> data) {
//            super(layoutResId, data);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder holder, Integer category) {
//
//            holder.getConvertView().setOnClickListener(v -> {
//                if (mListener != null  && holder.getAdapterPosition() != -1) {
//                    mListener.onColorSelected(holder.getAdapterPosition(), category);
//
//                }
//            });
//            CircleView circleView = holder.getView(R.id.checked);
//            circleView.setColor(color);
//            if (holder.getAdapterPosition() == color)
//                circleView.setActivated(selected);
//        }
//
//        void setSelectedPosition(int position,CircleView circleView) {
//            if (color !=position){
//                circleView.setActivated(false);
//                notifyItemChanged(color);
//            }
//            color =position;
//            notifyItemChanged(position);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        final Fragment parent = getParentFragment();
//        if (parent != null) {
//            mListener = (Listener) parent;
//        } else {
//            mListener = (Listener) context;
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        mListener = null;
//        super.onDetach();
//    }
//
//    public interface Listener {
//        void onColorSelected( int color);
//    }



}
