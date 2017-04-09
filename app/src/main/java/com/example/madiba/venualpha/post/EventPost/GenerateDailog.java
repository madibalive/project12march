package com.example.madiba.venualpha.post.EventPost;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.madiba.venualpha.Actions.ActionNewGossip;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.ui.CircleView;
import com.example.madiba.venualpha.ui.StateButton;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.ViewUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class GenerateDailog extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_ITEM = "item_count";
    private List<MdUserItem> datas ;
    private RecyclerView recyclerView;
    TextView mTitle;
    StateButton mPost;
    private View root;
    private int selectedColor;
    private LinearLayout mColorsLayout;
    private ViewGroup mColorsContainer;
    private ImageView mClose;
//    private Listener mListener;


    // TODO: Customize parameters
    public static GenerateDailog newInstance() {
        return new GenerateDailog();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_generate_color, container, false);
        mTitle = (TextView) v.findViewById(R.id.title);
        mPost = (StateButton) v.findViewById(R.id.submit);
        mColorsLayout = (LinearLayout) v.findViewById(R.id.colors_layout);
        root = (ViewGroup) v.findViewById(R.id.color_bg);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        displayColors();

    }
    private void displayColors() {


        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int[] mColors = getResources().getIntArray(R.array.default_rainbow);

        mColorsLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (mColors.length == 0) {
            mColorsLayout.setVisibility(View.GONE);
            return;
        }

        for (final int feature : mColors) {
            CircleView circleView = (CircleView) inflater.inflate(
                    R.layout.item_colorview, mColorsLayout, false);
            ViewUtils.setHeightAndWidth(circleView,(int)(size.x/7),(int)(size.x/7));
            ViewUtils.setMargins(circleView,0,10,5,5);
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

}
