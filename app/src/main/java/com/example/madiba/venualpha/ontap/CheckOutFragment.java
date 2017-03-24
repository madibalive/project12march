package com.example.madiba.venualpha.ontap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.ui.PlusMinusView;
import com.example.madiba.venualpha.ui.StateButton;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import timber.log.Timber;

/**
 * Created by Madiba on 11/5/2016.
 */



public class CheckOutFragment extends DialogFragment {


    private ProgressDialog progress;
    private ParseQuery<ParseObject> categoryQuery;
    private ImageView back;
    private StateButton submit;
    private TextView itemName,orderNumber;
    private ParseObject selectedCategory;
    private Listener mListener;
    private PlusMinusView plusMinusView;

    public CheckOutFragment() {


    }

    public static CheckOutFragment newInstance(String id,String className,String title) {
        final CheckOutFragment fragment = new CheckOutFragment();
        final Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("class", className);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_checkout, container, false);
        back = (ImageView) view.findViewById(R.id.close);
        itemName = (TextView) view.findViewById(R.id.title);
        orderNumber = (TextView) view.findViewById(R.id.order_number);
        plusMinusView = (PlusMinusView) view.findViewById(R.id.plusminusview);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        plusMinusView.setPlusMinusListener(new PlusMinusView.OnPlusMinusClickListener() {
            @Override
            public void onPlusClick(int currentNum) {
                orderNumber.setText(String.format("%d",currentNum));
            }

            @Override
            public void onMinusClick(int currentNum) {
                orderNumber.setText(String.format(" %d", currentNum));
            }
        });

    }

    private void initListener(){
        back.setOnClickListener(view -> dismiss());
        submit.setOnClickListener(view -> {
            progress = ProgressDialog.show(getActivity(), null,
                    "Sending Request", true);
            ParseObject request = new ParseObject("OnTapRequest");
            request.put("from", ParseUser.getCurrentUser());
            request.put("category",selectedCategory);
            request.put("categoryName",selectedCategory.getString("name"));
            request.saveInBackground(e -> {
                if (e == null){
                    Toast.makeText(getActivity(),"Request sent, go to ontap to view",Toast.LENGTH_LONG).show();
                }else {
                    Timber.e("Error posting request $s",e.getMessage());
                    Toast.makeText(getActivity(),"Request failed",Toast.LENGTH_LONG).show();
                }
                progress.dismiss();
                dismiss();
            });
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onCheckProced() {
        if (mListener != null) {
            mListener.onCheckOut();
        }
    }


    public interface Listener {
        void onCheckOut();
    }

}
