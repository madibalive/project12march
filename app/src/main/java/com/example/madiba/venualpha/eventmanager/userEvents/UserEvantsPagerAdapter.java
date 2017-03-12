package com.example.madiba.venualpha.eventmanager.userEvents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.madiba.venualpha.models.MdEventItem;

import java.util.List;

/**
 * Created by Madiba on 3/10/2017.
 */

public class UserEvantsPagerAdapter extends FragmentStatePagerAdapter {

    private List<MdEventItem> items;

    public UserEvantsPagerAdapter(FragmentManager fm, List<MdEventItem> items) {
        super(fm);
        this.items = items;
    }

    public UserEvantsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return null;
    }

    @Override
    public int getCount() {
        return items.size();
    }


}
