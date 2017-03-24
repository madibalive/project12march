package com.example.madiba.venualpha.chateu;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.example.madiba.venualpha.Actions.ActionChateuEvent;
import com.example.madiba.venualpha.Actions.ActionChateuMemory;
import com.example.madiba.venualpha.Actions.ActionNetwork;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.chateu.ChateuMemoriesMsgCell;
import com.example.madiba.venualpha.adapter.chateu.ChateuTextMsgCell;
import com.example.madiba.venualpha.dailogs.NewChateuFragment;
import com.example.madiba.venualpha.models.GlobalConstants;
import com.example.madiba.venualpha.util.NetUtils;
import com.example.madiba.venualpha.util.NotificationUtils;
import com.jaychang.srv.OnLoadMoreListener;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.tatarka.rxloader.RxLoader;
import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class ChateuGossipFragAct extends AppCompatActivity {

    private Boolean canLoadMore;
    private SimpleRecyclerView mRecyclerview;
    private View root;
    private Date lastDate;
    private int count;
    RxLoaderManager loaderManager;
    private RxLoader<List<SimpleCell>> listRxLoader;
    private EditText mMesssage;
    private ImageButton mSend;
    private ParseObject toObject;
    private TextView mTimeLeft,mGossipTitle;

    private RoundCornerImageView mavatar,mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chateu_gossip);
        root = getWindow().getDecorView().getRootView();
        mRecyclerview = (SimpleRecyclerView) findViewById(R.id.linearVerRecyclerView);
        mavatar = (RoundCornerImageView) findViewById(R.id.avatar);
        mImageView = (RoundCornerImageView) findViewById(R.id.gossip_image);
        mTimeLeft = (TextView) findViewById(R.id.title);
        mGossipTitle = (TextView) findViewById(R.id.gossip_title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());
        mRecyclerview.setLoadMoreToTop(true);
        mRecyclerview.setAutoLoadMoreThreshold(2);

        List<SimpleCell> cells = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ChateuTextMsgCell convoCell = new ChateuTextMsgCell(new ParseObject(""));

            cells.add(convoCell);
        }

        mRecyclerview.addCells(cells);
        mRecyclerview.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(SimpleRecyclerView simpleRecyclerView) {
                for (int i = 0; i < 4; i++) {
                    ChateuTextMsgCell convoCell = new ChateuTextMsgCell(new ParseObject(""));

                    mRecyclerview.addCell(convoCell);
                }
                mRecyclerview.setLoadMoreCompleted();
            }
        });
    }

    private void initAdapter(){
        mRecyclerview.setAutoLoadMoreThreshold(4);
        mRecyclerview.setLoadMoreToTop(true);

        mRecyclerview.setOnLoadMoreListener(simpleRecyclerView -> {
            if (canLoadMore)
                listRxLoader.restart();
        });
    }


    private void requestEvent() {
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            NewChateuFragment requestDialog = new NewChateuFragment();
            requestDialog.show(getSupportFragmentManager(),"request");
        }
    }

    private void requestMemories() {
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            NewChateuFragment requestDialog = new NewChateuFragment();
            requestDialog.show(getSupportFragmentManager(),"request");
        }
    }


    private void addMessage(String message){
        ParseObject comment = new ParseObject(GlobalConstants.CLASS_COMMENT);
        comment.put(GlobalConstants.FROM, ParseUser.getCurrentUser());
        comment.put(GlobalConstants.FROM_ID, ParseUser.getCurrentUser().getObjectId());
        comment.put("message", message);
        comment.put(GlobalConstants.TO, toObject);
        comment.put(GlobalConstants.TO_ID, toObject.getObjectId());
        comment.saveEventually(e -> {
            if (e ==null)
                Timber.e("done %s",comment.getObjectId());
            else
                Timber.e("error %s",e.getMessage());
        });

        ChateuTextMsgCell item = new ChateuTextMsgCell(comment);
        mRecyclerview.addCell( item);
        mMesssage.setText("");
        mSend.setEnabled(true);
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
            addMessage(message);
    }

    private void initload(){
        listRxLoader = loaderManager.create(
                LoaderChateu.load(),
                new RxLoaderObserver<List<SimpleCell>>() {
                    @Override
                    public void onNext(List<SimpleCell> value) {
                        new Handler().postDelayed(() -> {
//                            mSwipeRefreshLayout.setRefreshing(false);
                            if (value.size()>0){
                                mRecyclerview.addCells(value);
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
                        super.onError(e);
//                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCompleted() {
                        Timber.d("completed");
                        super.onCompleted();
                    }
                }

        );
    }


    public void updateData(List<SimpleCell> cells) {
        if (cells.size() > 0) {
            if (cells.size() <20) {
                canLoadMore =false;
            }
            mRecyclerview.addCells(0, cells);

        } else {
            canLoadMore =false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionChateuEvent action) {
        ParseObject message = new ParseObject("");
        message.put(GlobalConstants.FROM, ParseUser.getCurrentUser());
        message.put(GlobalConstants.FROM_ID, ParseUser.getCurrentUser().getObjectId());
        message.put("event", action.object);
        message.put(GlobalConstants.TO, toObject);
        message.put(GlobalConstants.TO_ID, toObject.getObjectId());
        message.saveEventually(e -> {
            if (e ==null){
                // TODO: 2/23/2017 update double tick here
            }
            else
                Timber.e("error %s",e.getMessage());
        });
        ChateuMemoriesMsgCell cell = new ChateuMemoriesMsgCell(message);
        mRecyclerview.addCell(cell);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionChateuMemory action) {
        ParseObject message = new ParseObject("");
        message.put(GlobalConstants.FROM, ParseUser.getCurrentUser());
        message.put(GlobalConstants.FROM_ID, ParseUser.getCurrentUser().getObjectId());
        message.put("event", action.object);
        message.put(GlobalConstants.TO, toObject);
        message.put(GlobalConstants.TO_ID, toObject.getObjectId());
        message.saveEventually(e -> {
            if (e ==null){
                // TODO: 2/23/2017 update double tick here
            }
            else
                Timber.e("error %s",e.getMessage());
        });
        ChateuMemoriesMsgCell cell = new ChateuMemoriesMsgCell(message);
        mRecyclerview.addCell(cell);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionNetwork action) {
//        String
        if (action.active)
            NotificationUtils.showSnackBar(root,"no network ",getApplicationContext(),action.active);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }



}
