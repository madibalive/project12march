package com.example.madiba.venualpha.post.MediaPost;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.CheckBox;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.Actions.ActionHashtagUsers;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.MdEventItem;
import com.example.madiba.venualpha.models.MdMemoryItem;
import com.example.madiba.venualpha.models.MdPostMemoryItem;
import com.example.madiba.venualpha.models.MdUserItem;
import com.example.madiba.venualpha.models.VenuFile;
import com.example.madiba.venualpha.post.AddUsersDialog;
import com.example.madiba.venualpha.services.ServiceUplouder;
import com.example.madiba.venualpha.ui.SwitchTextView;
import com.example.madiba.venualpha.ui.expandable.ExpandableLinearLayout;

import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.madiba.venualpha.Generators.ModelGenerator;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.jaychang.srv.SimpleRecyclerView;
import com.parse.ParseObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import timber.log.Timber;

import static android.view.View.VISIBLE;


public class FragmentAddHashagFragment extends Fragment implements View.OnClickListener {

    private List<VenuFile> urls = new ArrayList<>();
    private VenuFile videoFile;
    private int fromType = 0;
    private MdPostMemoryItem postMemoryItem;
    private List<MdMemoryItem> mSingleCheckList = new ArrayList<>();
    private List<MdUserItem> mdUserItemList = new ArrayList<>();
    private SingleHashtagAdapter mAdapter;
    private SelectedUsersAdapter usersAdapter;
    private MdEventItem event;

    private LinearLayout mLinkLinearLayot;
    private LinearLayout mSarchLayout;
    private ProgressBar progressBar;
    private PopupMenu popupMenu;
    private TextView name;
    private ExpandableLinearLayout moreExpandableLayout;
    private SwitchTextView mAddUsers;
    private SwitchTextView mEventSelected;
    private EditText mAddEvent;
    private EditText mCaption;
    private RecyclerView usersRecyclerView;
    private SimpleRecyclerView recyclerView;
    private ImageButton mClose;
    private CheckBox isNew;
    private AppCompatCheckBox isLinked;
    private Handler handler =new Handler();

    public static FragmentAddHashagFragment newInstance(List<VenuFile> venuFiles) {
        FragmentAddHashagFragment fragment = new FragmentAddHashagFragment();
        Bundle args = new Bundle();
        args.putParcelable("files", Parcels.wrap(venuFiles));
        fragment.setArguments(args);
        return fragment;
    }


    public static FragmentAddHashagFragment newInstance(VenuFile venuFiles) {
        FragmentAddHashagFragment fragment = new FragmentAddHashagFragment();
        Bundle args = new Bundle();
        args.putParcelable("file", Parcels.wrap(venuFiles));
        args.putBoolean("type", true);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            if (getArguments().getBoolean("type")) {
                videoFile = Parcels.unwrap(getArguments().getParcelable("file"));
                fromType = 1;

            } else {
                urls = Parcels.unwrap(getArguments().getParcelable("files"));
            }
            postMemoryItem = new MdPostMemoryItem();

        } else {
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_hashag, null);
        view.findViewById(R.id.send).setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.name);
        moreExpandableLayout = (ExpandableLinearLayout) view.findViewById(R.id.more_expandableLayout);
        mAddUsers = (SwitchTextView) view.findViewById(R.id.add_user);
        mEventSelected = (SwitchTextView) view.findViewById(R.id.selectedEvent);
        mAddEvent = (EditText) view.findViewById(R.id.add_event);
        mCaption = (EditText) view.findViewById(R.id.caption);
        usersRecyclerView = (RecyclerView) view.findViewById(R.id.user_recyclerView);
        recyclerView = (SimpleRecyclerView) view.findViewById(R.id.recyclerView);
        isNew = (CheckBox) view.findViewById(R.id.radio);
        isLinked = (AppCompatCheckBox) view.findViewById(R.id.isLinked);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        mSarchLayout = (LinearLayout) view.findViewById(R.id.search_event);
        mLinkLinearLayot = (LinearLayout) view.findViewById(R.id.link_layout_container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isNew.setOnCheckedChangeListener((compoundButton, b) -> toggle(b));
        mAddUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUsers();
            }
        });

        mAddEvent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 4) {
                    if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())) {
                       handler.removeCallbacks(null);
                       handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(VISIBLE);
                                findEvent(mAddEvent.getText().toString());
                            }
                        }, 2000);
                    }
                }
            }
        });
        mEventSelected.setOnClickListener(view1 -> {
            evntToggle();

        });

        isLinked.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mLinkLinearLayot.setVisibility(View.VISIBLE);
            }else
                mLinkLinearLayot.setVisibility(View.GONE);
        });

        initUserAdapter();
        getMemoEvent();
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                //TODO implement
                createMemory();
                break;
        }
    }


    private void evntToggle(){
        if (mEventSelected.getVisibility() != VISIBLE){
            mAddEvent.setVisibility(View.GONE);
            mEventSelected.setVisibility(VISIBLE);

        }else {
            mEventSelected.setVisibility(View.GONE);
            mAddEvent.setVisibility(VISIBLE);
        }
    }


    private void toggle(Boolean b) {
        moreExpandableLayout.setExpanded(b);
        if (b)
            recyclerView.setVisibility(View.GONE);
        else
            recyclerView.setVisibility(VISIBLE);
    }

    private void bindData(List<MdEventItem> mdEventItems) {
        mAdapter = new SingleHashtagAdapter(getActivity(), mSingleCheckList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapterView, view, position, l) -> Toast.makeText(getActivity(), position + " - ", Toast.LENGTH_SHORT).show());
    }


    private void addUsers() {
        AddUsersDialog.newInstance().show(getChildFragmentManager(), "dialog");
    }


    private void initUserAdapter() {
        usersAdapter = new SelectedUsersAdapter(R.layout.item_addhash_user, mdUserItemList);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        usersRecyclerView.setAdapter(usersAdapter);

    }


    private void createMemory() {
        postMemoryItem.setCaption(mCaption.getText().toString());

        if (isNew.isChecked())
            postMemoryItem.setNew(true);
        else
            postMemoryItem.setNew(false);

        if (isLinked.isChecked()) {
            postMemoryItem.setLinked(true);
            postMemoryItem.setInvites(usersAdapter.getData());
        }else{
            postMemoryItem.setLinked(false);
        }

        if (videoFile !=null){
            postMemoryItem.setVideo(true);
            postMemoryItem.setVenuFile(videoFile);
        }
        else{
            postMemoryItem.setVideo(false);
            postMemoryItem.setUrls(urls);
        }

        postMemoryItem.setMdEventItem(event);
        postMemoryItem.setHashTag(event.getHashtag());
        postMemoryItem.setCaption(mCaption.getText().toString());

        if (postMemoryItem.getNew()){
            ServiceUplouder.uploudMemory(getActivity().getApplicationContext(), postMemoryItem);
        }else {
            ServiceUplouder.updateMemory(getActivity().getApplicationContext(), postMemoryItem);
        }
//        EventBus.getDefault().postSticky(new ActionStartProgress());

        getActivity().finish();
    }


//    background proccessing
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    private Callback<List<ParseObject>> findCallBack = new Callback<List<ParseObject>>() {
        @Override
        public void onFinish(List<ParseObject> result, Callable callable, Throwable e) {
            if (e == null) {
                Log.e("stated ",result.size()+"");
                if (result.size() > 0) {
                    showPopup(mAddEvent, result);
                }
                // On success
            } else {
                // On error
                Timber.d("stated error %s", e.getMessage());

            }
            progressBar.setVisibility(View.GONE);
        }

    };


    private void showPopup(View view, List<ParseObject> list) {
        popupMenu = new PopupMenu(getActivity(), view);
        for (int i = 0; i < list.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i,"#" + list.get(i).getString("hashTag") + "\n"+ list.get(i).getString("title"));
        }

        popupMenu.setOnMenuItemClickListener(item -> {

            int i = item.getItemId();
            event = ModelGenerator.generateSimpleEvent(list.get(i));
            mEventSelected.setText(event.getEvTitle() + "\n" + event.getHashtag());
            evntToggle();
            return false;

        });

        popupMenu.show();
    }


    private void findEvent(String term) {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())) {
            TaskFindEvent taskLoad = new TaskFindEvent(term);
            Tasks.execute(taskLoad, findCallBack);
        }
    }


    private void getMemoEvent() {
        if (NetUtils.hasInternetConnection(getActivity().getApplicationContext())) {
            TaskGetRecentMemories taskLoad = new TaskGetRecentMemories();
            Tasks.execute(taskLoad, RecentMemoCallback);
        }
    }

    private Callback<List<MdEventItem>> RecentMemoCallback = (result, callable, e) -> {
        if (e == null) {
            if (result.isEmpty()) {
                bindData(result);
            }
        } else {

        }
    };


//    background proccessing
//    ////////////////////////////////////////////////////////////////////////////////////////////////////////
//    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    private class SelectedUsersAdapter extends BaseQuickAdapter<MdUserItem> {
        public SelectedUsersAdapter(int layoutResId, List<MdUserItem> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MdUserItem event) {

            Glide.with(mContext)
                    .load(event.getAvatarSmall())
                    .crossFade()
                    .placeholder(R.drawable.ic_default_avatar)
                    .error(R.drawable.placeholder_error_media)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .fallback(R.drawable.ic_default_avatar)
                    .thumbnail(0.4f)
                    .into(((ImageView) holder.getView(R.id.avatar)));

            holder.getView(R.id.close).setOnClickListener(view -> {
                remove(mData.indexOf(event));
                notifyDataSetChanged();
            });
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActionHashtagUsers action) {
        Log.e("POSTER", "USER ::" + action.getUserItems().size());
        usersAdapter.addData(action.getUserItems());
        mAddUsers.setText(usersAdapter.getItemCount()+"/4");
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
