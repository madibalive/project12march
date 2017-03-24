package com.example.madiba.venualpha.post.MediaPost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.VenuFile;
import com.example.madiba.venualpha.ui.expandable.ExpandableLinearLayout;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.jaychang.srv.SimpleRecyclerView;
import com.parse.ParseObject;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;


public class AddHashagFragment extends Fragment {

    List<VenuFile> urls = new ArrayList<>();
    private SimpleRecyclerView recyclerView;
    private ExpandableLinearLayout expandableLinearLayout;
    private TextView mCount,mNew;
    private ImageButton mClose,mSend;
    private ImageView mAddUsers;
    private CheckBox checkBox;

    public AddHashagFragment() {
        // Required empty public constructor
    }


    public static AddHashagFragment newInstance(List<VenuFile> venuFiles) {
        AddHashagFragment fragment = new AddHashagFragment();
        Bundle args = new Bundle();
        args.putParcelable("files", Parcels.wrap(venuFiles));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            urls = Parcels.unwrap(getArguments().getParcelable("files"));
            Log.e("HASHTAG",urls.size()+"");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_engaged_event, container, false);
        mClose= (ImageButton) view.findViewById(R.id.close);
        mSend= (ImageButton) view.findViewById(R.id.send);
        mAddUsers= (ImageView) view.findViewById(R.id.next);
        expandableLinearLayout= (ExpandableLinearLayout) view.findViewById(R.id.more_expandableLayout);
        expandableLinearLayout.setExpanded(false);
        mCount = (TextView) view.findViewById(R.id.count);
        recyclerView = (SimpleRecyclerView) view.findViewById(R.id.recyclerView);
        checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> expandableLinearLayout.setExpanded(b));
    }

    private Callback<List<ParseObject>> locationNameCallack = (result, callable, e) -> {
        if (e == null) {
            Timber.d("got data");
            if (result.size()>0){

            }
            // On success
        } else {
            // On error
            Timber.d("stated error %s",e.getMessage());

        }
    };

    public void getShots() {

        if (NetUtils.hasInternetConnection(getApplicationContext())){
            TaskGetShots taskLoad = new TaskGetShots();
            Tasks.execute(taskLoad,locationNameCallack);
        }
    }

    public class ShotsAdapter extends BaseQuickAdapter<String> {
        int selected;
        public ShotsAdapter(int layoutResId, List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, String event) {
            holder.setText(R.id.msg, event);
            if (selected!=500) {
                if (selected == holder.getAdapterPosition()) {
                    holder.setVisible(R.id.check_imageview, true);
                } else {
                    holder.setVisible(R.id.check_imageview, false);

                }
            }

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected=holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }

        private void reset(){
            selected=500;
            notifyDataSetChanged();
        }


    }

}
