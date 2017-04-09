package com.example.madiba.venualpha.viewer.gallery;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.profiles.adapter.ProfileGalleryCell;
import com.example.madiba.venualpha.models.MdEventItem;
import com.jaychang.srv.OnLoadMoreListener;
import com.jaychang.srv.SimpleRecyclerView;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class GalleryFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    private int mode;
    private int state;
    private MdEventItem eventItem;
    private RxLoader<List<ProfileGalleryCell>> listRxLoader;

    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private Date startDate,endDate;
    RxLoaderManager loaderManager;
    private SimpleRecyclerView recyclerView;
    private OnFragmentInteractionListener mListener;

    private String loadClassName,loadId;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public static GalleryFragment newInstance(int mode, int state, @Nullable MdEventItem eventItem) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, mode);
        args.putInt(ARG_PARAM2, state);
        args.putParcelable(ARG_PARAM2, Parcels.wrap(eventItem));
        fragment.setArguments(args);
        return fragment;
    }

    public static GalleryFragment newInstance(int mode) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, mode);
//        args.putInt(ARG_PARAM2, state);
//        args.putParcelable(ARG_PARAM2, Parcels.wrap(eventItem));
        fragment.setArguments(args);

        Log.e("PAGE","GALLARY ACTI frgamet pos : "+mode);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mode = getArguments().getInt(ARG_PARAM1);
//            state = getArguments().getInt(ARG_PARAM2);
//            eventItem = Parcels.unwrap(getArguments().getParcelable(ARG_PARAM3));
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }



    private void setupView(){

        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(SimpleRecyclerView simpleRecyclerView) {
                load();
            }
        });
    }



    private void load() {

        listRxLoader=  loaderManager.create(
                LoaderGallery.loadGallery(loadClassName,loadId,mCurrentCounter,mode,startDate,endDate),
                new RxLoaderObserver<List<ProfileGalleryCell>>() {
                    @Override
                    public void onNext(List<ProfileGalleryCell> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            if (value.size()>0) {
                                recyclerView.addCells(value);
                                mCurrentCounter = recyclerView.getItemCount();
                            }
                        },500);
                    }

                    @Override
                    public void onStarted() {
                        super.onStarted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                        recyclerView.setLoadMoreCompleted();

                    }
                }
        );

    }


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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
