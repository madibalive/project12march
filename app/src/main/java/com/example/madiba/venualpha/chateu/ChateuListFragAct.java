package com.example.madiba.venualpha.chateu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.madiba.venualpha.Actions.ActionEvantPageBuy;
import com.example.madiba.venualpha.Actions.ActionNetwork;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.chateu.adapter.ChateuDefautConvoCell;
import com.example.madiba.venualpha.chateu.adapter.ChateuGossipConvoCell;
import com.example.madiba.venualpha.models.MConversationItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.util.NetUtils;
import com.jaychang.srv.SimpleCell;
import com.jaychang.srv.SimpleRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

public class ChateuListFragAct extends AppCompatActivity   {

    private SimpleRecyclerView mRecyclerview;
    private View root;
    private  FloatingActionButton fab;
    RxLoaderManager loaderManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chateu_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        root = getWindow().getDecorView().getRootView();
        mRecyclerview = (SimpleRecyclerView) findViewById(R.id.linearVerRecyclerView);
        fab.setOnClickListener(view -> requestCreateNewConversation());
        updateCell();
    }

    private void requestOpenConversation(@NonNull MConversationItem conversation) {
        Intent intent = new Intent(ChateuListFragAct.this,ChateuDefaultFragAct.class);
        intent.putExtra("conversationId",conversation.getObject().getObjectId());
        intent.putExtra("name",conversation.getName());
        intent.putExtra("avatar",conversation.getAvatar());
        startActivity(intent);
    }

    private void requestOpenGossip(@NonNull MConversationItem conversation) {
        Intent intent = new Intent(ChateuListFragAct.this,ChateuDefaultFragAct.class);
        intent.putExtra("conversationId",conversation.getObject().getObjectId());
        intent.putExtra("name",conversation.getName());
        intent.putExtra("avatar",conversation.getAvatar());
        startActivity(intent);
    }

    private void requestCreateNewConversation() {
        if (NetUtils.hasInternetConnection(getApplicationContext())){
//             UserListDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");

        }
    }

    private void updateCell(){
        List<SimpleCell> cells = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ChateuDefautConvoCell convoCell = new ChateuDefautConvoCell(new MConversationItem());
            convoCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
                @Override
                public void onCellClicked(Object o, Object o2, Object o3) {
                    startActivity(new Intent(ChateuListFragAct.this,ChateuDefaultFragAct.class));
                }
            });
            cells.add(convoCell);
        }

        for (int i = 0; i < 5; i++) {
            ChateuGossipConvoCell convoCell = new ChateuGossipConvoCell(new MConversationItem());

            convoCell.setOnCellClickListener2(new SimpleCell.OnCellClickListener2() {
                @Override
                public void onCellClicked(Object o, Object o2, Object o3) {
                    startActivity(new Intent(ChateuListFragAct.this,ChateuGossipFragAct.class));
                }
            });
            cells.add(convoCell);        }

        mRecyclerview.addCells(cells);
    }



    void initload(){
        loaderManager.create(
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

        ).start();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionEvantPageBuy action) {
        // TODO: 2/22/2017  recieve update from

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionNetwork action) {
        // TODO: 2/22/2017  recieve connetivitiy here

    }


//    @Override
//    public void onUserClicked(MdUserItem position) {
//
//    }

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
