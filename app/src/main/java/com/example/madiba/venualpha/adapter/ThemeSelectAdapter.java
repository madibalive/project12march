package com.example.madiba.venualpha.adapter;

import com.android.liuzhuang.rcimageview.RoundCornerImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.models.ModelThemeView;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Madiba on 9/25/2016.
 */

public class ThemeSelectAdapter extends BaseQuickAdapter<ModelThemeView> {
    private int selectedIndex;

    public ThemeSelectAdapter(int layoutResId, List<ModelThemeView> data) {
        super(layoutResId, data);
        this.selectedIndex=0;
    }

    @Override
    protected void convert(BaseViewHolder holder, ModelThemeView event) {
        RoundCornerImageView roundCornerImageView = (holder.getView(R.id.avatar));
        if (selectedIndex==getData().indexOf(event)){
            event.setSelected(true);
           roundCornerImageView.setAlpha(0.8f);
        } else  {
            event.setSelected(false);
            roundCornerImageView.setAlpha(1f);
        }

    }

    public void setSelectedIndex(int index){
        this.selectedIndex=index;
        Timber.i( "selectedIndex: " +index);

        notifyDataSetChanged();
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }
}
