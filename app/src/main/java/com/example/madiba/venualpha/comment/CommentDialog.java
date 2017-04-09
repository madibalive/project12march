package com.example.madiba.venualpha.comment;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.jaychang.srv.SimpleViewHolder;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import timber.log.Timber;


public class CommentDialog extends DialogFragment {

    private static final String ARG_ITEM_COUNT = "item_count";
    private Listener mListener;
    private List<ParseObject> mDatas = new ArrayList<>();
    private RxLoaderManager loaderManager;

    private View notLoadingView;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;

    private ImageView mClose;
    private SimpleRecyclerView
            recyclerView;
    private EditText editText;
    private ImageButton mSendBtn;
    private boolean hideStatus = false;
    private ParseObject toObject;

    private String id,className;

    public static CommentDialog newInstance() {
        final CommentDialog fragment = new CommentDialog();
        return fragment;
    }

    public static CommentDialog newInstance(String toId, String toClass, boolean fromViewer) {
        final CommentDialog fragment = new CommentDialog();
        final Bundle args = new Bundle();
        args.putString("toId", toId);
        args.putString("toClass", toClass);
        args.putBoolean("fromViewer", fromViewer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
            d.getWindow().setBackgroundDrawable(null);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            id = getArguments().getString("toId");
            className = getArguments().getString("toClass");
            hideStatus = getArguments().getBoolean("hide");
            toObject = ParseObject.createWithoutData(className, id);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_comment, container, false);
        recyclerView = (SimpleRecyclerView) view.findViewById(R.id.recyclerView);
        mSendBtn = (ImageButton) view.findViewById(R.id.sendBtn);
        editText = (EditText) view.findViewById(R.id.message);
        mClose = (ImageView) view.findViewById(R.id.close);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        loaderManager = RxLoaderManagerCompat.get(this);//

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setTheme(){

    }


    private void attemptComment() {

        editText.setError(null);
        Boolean send = true;
        String message = editText.getText().toString();
        if (TextUtils.isEmpty(message)) {
            send = false;
            editText.setError("Message is empty");
            return;
        }

        sendComment(message);
    }

    private void sendComment(final String message) {

        ParseObject comment = new ParseObject(GlobalConstants.CLASS_COMMENT);
        comment.put(GlobalConstants.FROM, ParseUser.getCurrentUser());
        comment.put(GlobalConstants.FROM_ID, ParseUser.getCurrentUser().getObjectId());
        comment.put("message", message);
        comment.put(GlobalConstants.TO, id);
        comment.put(GlobalConstants.TO_ID, toObject.getObjectId());
        Timber.e("about to added ");
        comment.saveEventually(e -> {
            if (e ==null)
                Timber.e("done %s",comment.getObjectId());
            else
                Timber.e("error %s",e.getMessage());
        });
        Timber.e("about to added ");

//        comment.saveInBackground(e -> {
//            mSend.setEnabled(true);
//            if (e == null) {
//        mAdapter.setAnimationsLocked(false);
//        mAdapter.setDelayEnterAnimation(false);
//        mAdapter.addItem( comment);
//        mAdapter.notifyItemInserted(mAdapter.getData().size());

        editText.setText("");
        mSendBtn.setEnabled(true);

//
//            } else
//                Log.i(TAG, "done: err" + e.getMessage());
//        });
    }

//    WORKING AREA ////////////////////////////////////////////////////////////////////////////
//    /////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////////////////////////////////////////////


//    void initload(){
//        loaderManager.create(
//                LoaderGeneral.loadComment(toId,toClass),
//                new RxLoaderObserver<List<ParseObject>>() {
//                    @Override
//                    public void onNext(List<ParseObject> value) {
//                        new Handler().postDelayed(() -> {
//                            Timber.e(value.toString());
//                            if (value.size() > 0) {
//                                mAdapter.setNewData(value);
//
////                                if (mRecyclerview.getVisibility() != View.VISIBLE) {
//////                                        TransitionManager.beginDelayedTransition(container, auto);
////                                }
//                            } else {
////                                    TransitionManager.beginDelayedTransition(container, auto);
//                                //                                    setNoResultsVisibility(View.VISIBLE);
//                            }
//
//                        },500);
//
//                    }
//
//                    @Override
//                    public void onStarted() {
//                        Timber.d("stated");
//                        super.onStarted();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.d("stated error %s",e.getMessage());
//                        super.onError(e);
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("completed");
//                        super.onCompleted();
//                        progressBar.stop();
//                        progressBar.setVisibility(View.GONE);
//                        mRecyclerview.setVisibility(View.VISIBLE);
//
//                    }
//                }
//        ).start();
//    }


    public class CommentMsgOutCell extends SimpleCell<ParseObject, MsqOutViewHolder>
    {

        private static final String KEY_TITLE = "KEY_TITLE";
        private boolean showHandle;

        public CommentMsgOutCell(ParseObject item) {
            super(item);
        }

        @Override
        protected int getLayoutRes() {
            return R.layout.item_comment_in;
        }

        @NonNull
        @Override
        protected MsqOutViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
            return new MsqOutViewHolder(cellView);
        }

        @Override
        protected void onBindViewHolder(MsqOutViewHolder holder, int position, Context context, Object payload) {

//    holder.textView.setText(getItem().getTitle());


        }

        private void BindView(MsqOutViewHolder holder, ParseObject item){
            holder.mMsg.setText("");

        }

        @Override
        protected long getItemId() {
//    return getItem().getObjectId().hashCode();
            return 0;
        }

    }

    public class CommentMsgInCell extends SimpleCell<ParseObject, MsgInViewHolder>
    {

        private static final String KEY_TITLE = "KEY_TITLE";
        private boolean showHandle;

        public CommentMsgInCell(ParseObject item) {
            super(item);
        }

        @Override
        protected int getLayoutRes() {
            return R.layout.item_comment_in;
        }

        @NonNull
        @Override
        protected MsgInViewHolder onCreateViewHolder(ViewGroup parent, View cellView) {
            return new MsgInViewHolder(cellView);
        }

        @Override
        protected void onBindViewHolder(MsgInViewHolder holder, int position, Context context, Object payload) {

//    holder.textView.setText(getItem().getTitle());

        }

        private void BindView(MsgInViewHolder holder, ParseObject item){
            holder.mMsg.setText("");
            Glide.with(getContext())
                    .load(item.getParseUser("from").getParseFile(""))
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into(holder.mAvatar);
        }

        @Override
        protected long getItemId() {
//    return getItem().getObjectId().hashCode();
            return 0;
        }

    }



    static class MsgInViewHolder extends SimpleViewHolder {
        TextView mMsg;
        RoundCornerImageView mAvatar;

        MsgInViewHolder(View itemView) {
            super(itemView);
            mAvatar = (RoundCornerImageView) itemView.findViewById(R.id.avatar);
            mMsg = (TextView) itemView.findViewById(R.id.last_message);
        }
    }

    static class MsqOutViewHolder extends SimpleViewHolder {
        TextView mMsg;

        MsqOutViewHolder(View itemView) {
            super(itemView);
            mMsg = (TextView) itemView.findViewById(R.id.msg);
        }
    }


    public interface Listener {
        void onAdd(int position);
    }

    @Override
    public void onStop() {

        if (hideStatus) {
            if (Build.VERSION.SDK_INT > 10) {
                int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;
                getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            } else {
                getActivity().getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }

        super.onStop();
    }
    public void scrollToBottom(int pos){
        recyclerView.smoothScrollToPosition(pos);
    }



}
