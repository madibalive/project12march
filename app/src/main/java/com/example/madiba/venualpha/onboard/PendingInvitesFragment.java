package com.example.madiba.venualpha.onboard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceRecyclerView;
import com.parse.ParseObject;
import com.example.madiba.venualpha.post.MediaPost.SelectMultipleAdapter;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.ui.AnimateCheckBox;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.multichoicerecyclerview.MultiChoiceAdapter;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class PendingInvitesFragment extends Fragment {
    public static final int index =5;

    private MultiChoiceRecyclerView mRecyclerview;
    private MainAdapter mAdapter;
    private List<ParseObject> mDatas=new ArrayList<>();

    private Button mNext;
    private TextView mTitle;

    RxLoaderManager loaderManager;
    private OnFragmentInteractionListener mListener;

    public PendingInvitesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_onboard_pendinginvites, container, false);
        mNext = (Button) root.findViewById(R.id.go);
        mRecyclerview = (MultiChoiceRecyclerView) root.findViewById(R.id.recyclerView);
        mTitle = (TextView) root.findViewById(R.id.title);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        initAdapter();


    }
    private void initAdapter() {
        mAdapter=new MainAdapter(mDatas,getActivity());
        mRecyclerview.setRecyclerColumnNumber(1);

        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setSingleClickMode(true);

    }


    void initload(){
        loaderManager.create(
                LoaderGeneral.loadPendingInvites(),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
//                            if (value.size()>0)
//                                mAdapter.setNewData(value);
                        },500);
                    }

                    @Override
                    public void onStarted() {
                        Timber.d("stated");
                        super.onStarted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("stated error %s",e.getMessage());
                        super.onError(e);

                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                    }
                }

        ).start();
    }


    public class MainAdapter extends MultiChoiceAdapter<SampleCustomViewHolder> {

        List<ParseObject> messageV0s;
        Context mContext;

        public MainAdapter(List<ParseObject> messageV0s, Context context) {
            this.messageV0s = messageV0s;
            this.mContext = context;
        }

        @Override
        public SampleCustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SampleCustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_checkable, parent, false));
        }

        @Override
        public void onBindViewHolder(SampleCustomViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
//
//        ParseObject currentItem = messageV0s.get(position);
//        holder.name.setText(currentItem.getName());


        }

        @Override
        public int getItemCount() {
            return messageV0s.size();
        }

        @Override
        protected void setActive(View view, boolean state) {


            ImageView imageView = (ImageView) view.findViewById(R.id.add);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.container);
            if(state){
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBackgroundLight));
//            imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                imageView.setVisibility(View.VISIBLE);
            }else{
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
//            imageView.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
                imageView.setVisibility(View.GONE);

            }
        }

    }
    public class SampleCustomViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView radio;
//        public RoundCornerImageView avatar;


        public SampleCustomViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title);
            radio = (ImageView) itemView.findViewById(R.id.add);
//            avatar = (RoundCornerImageView) itemView.findViewById(R.id.avatar);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())){
            initload();
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onMain();
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
        // TODO: Update argument type and name
        void onMain();
    }

}
