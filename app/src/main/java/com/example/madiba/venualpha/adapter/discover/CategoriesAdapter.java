package com.example.madiba.venualpha.adapter.discover;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.CategoriesModel;

import java.util.List;

import timber.log.Timber;


/**
 * Created by Madiba on 10/9/2016.
 */

public class CategoriesAdapter extends BaseQuickAdapter<CategoriesModel> {

    public CategoriesAdapter(int layoutResId, List<CategoriesModel> data) {
        super(layoutResId, data);
    }

    @Override
        protected void convert(BaseViewHolder holder, CategoriesModel category) {
        Timber.i( "convert: " + category.getmName());

        holder.setText(R.id.name1, category.getmName());
//        RelativeLayout m = holder.getView(R.id.ct_bgrnd);
//        m.setBackground(mContext.getResources(), R.drawable.category_background_default,R.color.venu_red,mContext));


    }
}