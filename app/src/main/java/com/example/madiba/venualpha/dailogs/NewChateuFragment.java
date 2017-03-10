package com.example.madiba.venualpha.dailogs;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.ui.RotateLoading;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;


public class NewChateuFragment extends DialogFragment {

    private RecyclerView mRecyclerview;
    private CommentAdapter mAdapter;
    private View notLoadingView;
    private int delayMillis = 1000;
    private int mCurrentCounter = 0;
    private boolean hideStatus = false;
    private RotateLoading progressBar;
    private EditText mMesssage;
    private ImageButton mSend;
    private List<ParseObject> mDatas = new ArrayList<>();
    private ImageView close;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private ParseObject toObject;
    private ParseQuery<ParseObject> notifQuery;
    private ParseObject comment;
    private String toId, toClass;
    private RxLoaderManager loaderManager;

    public NewChateuFragment() {
    }

    public static NewChateuFragment newInstance() {
        return new NewChateuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            toId = getArguments().getString("toId");
            toClass = getArguments().getString("toClass");
            hideStatus = getArguments().getBoolean("hide");
            toObject = ParseObject.createWithoutData(toClass, toId);

        }
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

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
//
//        dialog.setOnShowListener(dialog1 -> {
//            BottomSheetDialog d = (BottomSheetDialog) dialog1;
//
//            LinearLayout bottomSheet = (LinearLayout) d.findViewById(R.id.linear_layout_bottom_sheet);
//            if (bottomSheet != null) {
//                Timber.e("setting to expamded");
//                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
//            }
//        });
//
//        // Do something with your dialog like setContentView() or whatever
//        return dialog;
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        mRecyclerview = (RecyclerView) view.findViewById(R.id.comment_dailog_recyclerview);

//        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//                else {
//                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//
//
//                LinearLayout bottomSheet = (LinearLayout) view.findViewById(R.id.linear_layout_bottom_sheet);
//                if (bottomSheet != null) {
//                    Timber.e("bottom sheet is not  null");
//                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

//                    bottomSheet.registerCallback(new BottomSheet.Callbacks() {
//                        @Override
//                        public void onSheetDismissed() {
//                       }
//                        @Override
//                        public void onSheetPositionChanged(int sheetTop, boolean interacted) {
//                            if (interacted && close.getVisibility() != View.VISIBLE) {
//                                close.setVisibility(View.VISIBLE);
//                                close.setAlpha(0f);
//                                close.animate()
//                                        .alpha(1f)
//                                        .setDuration(400L)
//                                        .setInterpolator(getLinearOutSlowInInterpolator(CommentActivityFragment.this))
//                                        .start();
//                            }
//                            if (sheetTop == 0) {
//                                showClose();
//                            } else {
//                                showDown();
//                            }
//                        }
//                    });


//                    if (hideStatus)
//                        bottomSheet.setBackground(null);
//                }else
//                    Timber.e("bottom sheet is  null");
//
//            }
//        });

        mSend = (ImageButton) view.findViewById(R.id.sendmsg);
        progressBar = (RotateLoading) view.findViewById(R.id.rotateloading);
        mMesssage = (EditText) view.findViewById(R.id.comment_dailog_message);
        close= (ImageView) view.findViewById(R.id.close);
        return view;
    }

    @SuppressLint("NewApi") @SuppressWarnings("deprecation")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loaderManager = RxLoaderManagerCompat.get(this);//
        mAdapter = new CommentAdapter(mDatas);
        LinearLayoutManager manager =new LinearLayoutManager(getActivity());
        manager.setStackFromEnd(true);
//        manager.setReverseLayout(true);
        mRecyclerview.setLayoutManager(manager);
        mRecyclerview.setAdapter(mAdapter);
        mSend.setOnClickListener(view1 -> attemptComment());
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        progressBar.start();
        initload();

    }

    private void initViewTreeObserver() {
//        ViewTreeObserver observer = getViewTreeObserver();
//        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                getGlobalVisibleRect(rawButtonRect);
//                rawButtonRectF.set(rawButtonRect.left, rawButtonRect.top, rawButtonRect.right, rawButtonRect.bottom);
//            }
//        });
    }

    private void showClose() {
//        if (dismissState == DISMISS_CLOSE) return;
//        dismissState = DISMISS_CLOSE;
//        close.setImageDrawable(downToClose);
//        downToClose.start();
    }


    private void showDown() {

//        if (dismissState == DISMISS_DOWN) return;
//        dismissState = DISMISS_DOWN;
//        close.setImageDrawable(closeToDown);
//        closeToDown.start();

    }



    private void attemptComment() {

        mMesssage.setError(null);
        Boolean send = true;
        String message = mMesssage.getText().toString();
        if (TextUtils.isEmpty(message)) {
            send = false;
            mMesssage.setError("Message is empty");
        }
        if (toObject == null)
            send = false;
        if (send)
            sendComment(message);
    }

//    mSend.setEnabled(false);

    private void sendComment(final String message) {

        final ParseObject comment = new ParseObject(GlobalConstants.CLASS_COMMENT);
        comment.put(GlobalConstants.FROM, ParseUser.getCurrentUser());
        comment.put(GlobalConstants.FROM_ID, ParseUser.getCurrentUser().getObjectId());
        comment.put("message", message);
        comment.put(GlobalConstants.TO, toObject);
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
        mAdapter.setAnimationsLocked(false);
        mAdapter.setDelayEnterAnimation(false);
        mAdapter.addItem( comment);
//        mAdapter.notifyItemInserted(mAdapter.getData().size());

        mMesssage.setText("");
        mSend.setEnabled(true);

//
//            } else
//                Log.i(TAG, "done: err" + e.getMessage());
//        });
    }

    private boolean validateComment() {
//        if (TextUtils.isEmpty(mMesssage.getText())) {
//            mSend.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
//            return false;
//        }

        return true;
    }

    void initload(){
        loaderManager.create(
                LoaderGeneral.loadComment(toId,toClass),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        new Handler().postDelayed(() -> {
                            Timber.e(value.toString());
                            if (value.size() > 0) {
                                mAdapter.setNewData(value);

//                                if (mRecyclerview.getVisibility() != View.VISIBLE) {
////                                        TransitionManager.beginDelayedTransition(container, auto);
//                                }
                            } else {
//                                    TransitionManager.beginDelayedTransition(container, auto);
                               //                                    setNoResultsVisibility(View.VISIBLE);
                            }

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
                        progressBar.stop();
                        progressBar.setVisibility(View.GONE);
                        mRecyclerview.setVisibility(View.VISIBLE);

                    }
                }
        ).start();
    }

    public void scrollToBottom(int pos){
        mRecyclerview.smoothScrollToPosition(pos);
    }



    private class CommentAdapter
            extends BaseQuickAdapter<ParseObject> {
        private static final int TEXT_IN = 1;
        private static final int TEXT_OUT = 2;

        private int lastAnimatedPosition = -1;
        private boolean animationsLocked = false;
        private boolean delayEnterAnimation = true;

        public void addItem(ParseObject item) {
            if (item != null) {
                mData.add(item);
                notifyDataSetChanged();
                scrollToBottom(mData.size());
            }
        }

        public void removeItem(ParseObject item) {
            if (item != null) {
                int position = mData.indexOf(item);
                mData.remove(item);
                notifyItemRemoved(position);
            }
        }

        public CommentAdapter(List<ParseObject> data) {
            super(R.layout.item_comment_out, data);
        }

        @Override
        protected int getDefItemViewType(int position) {
            Timber.e("getdefviewtype FROM_ID ::%s",getItem(position).getString(GlobalConstants.FROM_ID));
            if (getItem(position).getString(GlobalConstants.FROM_ID).equals(ParseUser.getCurrentUser().getObjectId()))
                return TEXT_OUT;
            else
                return TEXT_IN;

        }
        @Override
        protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
            Timber.e("Oncreate  ::%s",viewType);
            if (viewType == TEXT_IN)
                return new MessageInViewHolder(getItemView(R.layout.item_comment_in, parent));
            else if (viewType==TEXT_OUT)
                return new MessageOutViewHolder(getItemView(R.layout.item_comment_in, parent));
            return super.onCreateDefViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
            ParseObject data = getItem(holder.getAdapterPosition());
            runEnterAnimation(holder.itemView, position);

            if (holder instanceof MessageInViewHolder) {


                ((MessageInViewHolder) holder).setText(R.id.comment_msg,data.getString("message"));
                Glide.with(mContext).load(data.getParseUser("from").getParseFile("avatarSmall").getUrl())
                        .priority(Priority.LOW)
                        .placeholder(R.drawable.ic_default_avatar)
                        .skipMemoryCache(true)
                        .error(R.drawable.placeholder_error_media)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .crossFade()
                        .into((RoundCornerImageView)  ((MessageInViewHolder) holder).getView(R.id.comment_avatar));



            }else if (holder instanceof MessageOutViewHolder){

                ((MessageOutViewHolder) holder).setText(R.id.comment_msg,data.getString("message"));
                Glide.with(mContext).load(data.getParseUser("from").getParseFile("avatarSmall").getUrl())
                        .priority(Priority.LOW)
                        .placeholder(R.drawable.ic_default_avatar)
                        .skipMemoryCache(true)
                        .error(R.drawable.placeholder_error_media)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .crossFade()
                        .into((RoundCornerImageView)  ((MessageOutViewHolder) holder).getView(R.id.comment_avatar));



            }

            super.onBindViewHolder(holder, position, payloads);



        }

        private void runEnterAnimation(View view, int position) {
            if (animationsLocked) return;

            if (position > lastAnimatedPosition) {
                lastAnimatedPosition = position;
                view.setTranslationY(100);
                view.setAlpha(0.f);
                view.animate()
                        .translationY(0).alpha(1.f)
                        .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)
                        .setInterpolator(new DecelerateInterpolator(2.f))
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                animationsLocked = true;
                            }
                        })
                        .start();
            }
        }

        public void setAnimationsLocked(boolean animationsLocked) {
            this.animationsLocked = animationsLocked;
        }

        public void setDelayEnterAnimation(boolean delayEnterAnimation) {
            this.delayEnterAnimation = delayEnterAnimation;
        }


        @Override
        protected void convert(BaseViewHolder holder, final ParseObject comment) {

////            holder.setText(R.id.comment_msg, comment.getString("message"));
//            if (holder instanceof MessageInViewHolder) {
////                Glide.with(mContext).load(comment.getParseUser("from").getString("avatarUrl")).crossFade().fitCenter().into((RoundCornerImageView) holder.getView(R.id.comment_avatar));
//            }

        }

        public class MessageInViewHolder extends BaseViewHolder {
            public MessageInViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class MessageOutViewHolder extends BaseViewHolder {
            public MessageOutViewHolder(View itemView) {
                super(itemView);
            }
        }
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
}
