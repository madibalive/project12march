package com.example.madiba.venualpha.onboard;

import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.adapter.SingletonDataSource;
import com.example.madiba.venualpha.map.TaskGetLocationByName;
import com.example.madiba.venualpha.services.GeneralService;
import com.example.madiba.venualpha.services.LoaderGeneral;
import com.example.madiba.venualpha.ui.AnimateCheckBox;
import com.example.madiba.venualpha.util.NetUtils;
import com.github.rongi.async.Callback;
import com.github.rongi.async.Tasks;
import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import me.tatarka.rxloader.RxLoaderManager;
import me.tatarka.rxloader.RxLoaderManagerCompat;
import me.tatarka.rxloader.RxLoaderObserver;
import timber.log.Timber;

import static com.facebook.FacebookSdk.getApplicationContext;


public class SelectCategoriesFragment extends Fragment {

    private RecyclerView mRecyclerview;
    private CategoryAdapter mAdapter;
    private List<ParseObject> mDatas=new ArrayList<>();
    private RxLoaderManager loaderManager;
    private PopupMenu popupMenu ;
    private EditText mLocation;
    private LatLng latLng;

    public SelectCategoriesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.container_core, container, false);
        mRecyclerview = (RecyclerView) root.findViewById(R.id.core_recyclerview);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loaderManager = RxLoaderManagerCompat.get(this);
        initAdapter();


    }
    private void initAdapter() {
        mAdapter = new CategoryAdapter(R.layout.item_ontap, mDatas);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setAdapter(mAdapter);


    }

    private void addCategories(){
        if (latLng !=null) {
            ParseUser.getCurrentUser().put("location", new ParseGeoPoint(latLng.latitude,latLng.longitude));
            if (mAdapter.returnData().size() > 0) {
                SingletonDataSource.getInstance().setOnboardCategories(mAdapter.returnData());
                GeneralService.startActionOnboardAddCategories(getApplicationContext());
            }
            // TODO: 3/1/2017 goto pendingInvited
        }
    }


    private Callback<List<Address>> locationNameCallack = new Callback<List<Address>>() {
        @Override
        public void onFinish(List<Address> result, Callable callable, Throwable e) {
            if (e == null) {
                if (result.size()>0){
                    showPopup(mLocation,result);
                }
            } else {

            }
        }

    };

    public void onMapSearch() {
        String location = mLocation.getText().toString();
        if (NetUtils.hasInternetConnection(getApplicationContext())){
            TaskGetLocationByName taskLoad = new TaskGetLocationByName(location,getApplicationContext());
            Tasks.execute(taskLoad,locationNameCallack);
        }
    }

    private void showPopup(View view,List<Address> list) {
        popupMenu = new PopupMenu(getActivity(), view);
        for (int i = 0; i < list.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, list.get(i).getAddressLine(0)+", "+list.get(i).getAddressLine(1));
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {

            int i = item.getItemId();
            Address address = list.get(i);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            return false;

        });

        popupMenu.show();
    }

    private void initload(){
        loaderManager.create(
                LoaderGeneral.loadPendingInvites(),
                new RxLoaderObserver<List<ParseObject>>() {
                    @Override
                    public void onNext(List<ParseObject> value) {
                        Timber.d("onnext");
                        new Handler().postDelayed(() -> {
                            if (value.size()>0)
                                mAdapter.setNewData(value);
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


    private class CategoryAdapter
            extends BaseQuickAdapter<ParseObject> {


        private Set<ParseObject> checkedSet = new HashSet<>();

        public CategoryAdapter(int layoutResId, List<ParseObject> data) {
            super(layoutResId, data);
        }
        @Override
        protected void convert(BaseViewHolder holder, final ParseObject category) {
            holder.setText(R.id.ot_i_location, category.getString("categoryName"))
                    .setText(R.id.ot_i_order_item,category.getString("number"))
                    .setText(R.id.ot_i_location,category.getObjectId());

            final AnimateCheckBox checkBox = ((AnimateCheckBox) holder.getView(R.id.checkbox));

            if(checkedSet.contains(category)){
                checkBox.setChecked(true);
            }else {
                //checkBox.setChecked(false); //has animation
                checkBox.setUncheckStatus();
            }
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    checkedSet.add(category);
                } else {
                    checkedSet.remove(category);
                }
            });
        }

        public Set<ParseObject> returnData(){
            return checkedSet;
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

}
