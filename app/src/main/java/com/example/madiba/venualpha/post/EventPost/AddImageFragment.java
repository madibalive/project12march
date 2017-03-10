package com.example.madiba.venualpha.post.EventPost;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.dailogs.CreateGossipSheet;
import com.example.madiba.venualpha.ui.CircleView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddImageFragment newInstance(String param1, String param2) {
        AddImageFragment fragment = new AddImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_image, container, false);
        return view;
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
    public class ColorAdapter extends BaseQuickAdapter<Integer> {
        public int color;
        private CreateGossipSheet.ColorListener colorListener;
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

        void setColorListener(CreateGossipSheet.ColorListener colorListener) {
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int  page, String url);
    }
}
