package com.example.madiba.venualpha.dailogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     UserListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link UserListDialogFragment.Listener}.</p>
 */
public class UserListDialogFragment extends BottomSheetDialogFragment {
    private RxLoaderManager loaderManager;
    private View notLoadingView;

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_ITEM = "item_count";
    private Listener mListener;
    private List<MdUserItem> datas ;
    private int mode;
    private RecyclerView recyclerView;
    private MdUserItem userItem;

    // TODO: Customize parameters
    public static UserListDialogFragment newInstance(int mode, MdUserItem userItem) {
        final UserListDialogFragment fragment = new UserListDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, mode);
        args.putParcelable(ARG_ITEM, Parcel.);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    private void setAdapter(List<MdUserItem> items){
        recyclerView.setAdapter(new UserAdapter(getArguments().getInt(ARG_ITEM_COUNT),items));

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
        void onUserClicked(MdUserItem position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final RoundCornerImageView avatar;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.item_person, parent, false));
            text = (TextView) itemView.findViewById(R.id.cc_i_name);
            avatar = (RoundCornerImageView) itemView.findViewById(R.id.cc_i_avatar);
//            avatar.setOnClickListener(v -> {
//                if (mListener != null) {
//                    mListener.onUserClicked(getAdapterPosition());
//                    dismiss();
//                }
//            });
        }

    }

    private class UserAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<MdUserItem> data ;
        private final int mItemCount;

        UserAdapter(int itemCount,List<MdUserItem> data) {
            mItemCount = itemCount;
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MdUserItem item = this.data.get(position);
            holder.text.setText(String.valueOf(position));
            holder.avatar.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onUserClicked(item);
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }


    void initload(){
        loaderManager.create(
                LoaderGeneral.loadGoing(mSoureEvent.getObjectId(),mSoureEvent.getClassName()),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setAdapter(value);
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
                        super.onCompleted();
                    }
                }
        ).start();
    }


}
