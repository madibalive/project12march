package com.example.madiba.venualpha;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.madiba.venualpha.chateu.ChateuListFragAct;
import com.example.madiba.venualpha.contact.ContactActivity;
import com.example.madiba.venualpha.dailogs.NavMenuDailogFragment;
import com.example.madiba.venualpha.dailogs.NewGossipDailogFragment;
import com.example.madiba.venualpha.discover.DiscoverActivity;
import com.example.madiba.venualpha.eventmanager.userEvents.UserEventActivity;
import com.example.madiba.venualpha.main.ChallangeFragment;
import com.example.madiba.venualpha.main.MainFragment;
import com.example.madiba.venualpha.main.TrendFragment;
import com.example.madiba.venualpha.ontap.OnTapActivity;
import com.example.madiba.venualpha.post.BaseEventActivity;
import com.example.madiba.venualpha.post.BaseMediaPostActivity;
import com.example.madiba.venualpha.settings.SettingsActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        NavMenuDailogFragment.Listener,
        MainFragment.OnFragmentInteractionListener{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_add);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(view -> NavMenuDailogFragment.newInstance().show(getSupportFragmentManager(), "dialog"));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabmedia);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BaseMediaPostActivity.class));
            }
        });
        FloatingActionButton fabevent = (FloatingActionButton) findViewById(R.id.fab);
        fabevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BaseEventActivity.class));
            }
        });

        FloatingActionButton fabgossip = (FloatingActionButton) findViewById(R.id.fabgossip);
        fabgossip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          NewGossipDailogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            startActivity(new Intent(MainActivity.this, DiscoverActivity.class));
            return true;
        } else if (id == R.id.action_message) {
            startActivity(new Intent(MainActivity.this, ChateuListFragAct.class));
            return true;
        }

        return super.onOptionsItemSelected(item);    }



    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return MainFragment.newInstance("","");
            }else if(position ==1){
                return TrendFragment.newInstance();

            }else
                return ChallangeFragment.newInstance();

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "1";
                case 1:
                    return "2";
                case 2:
                    return "3";
            }
            return null;
        }
    }

    @Override
    public void onItemClicked(int position) {
        Timber.i("selected item %d",position);
        if (position == 1) {
            startActivity(new Intent(MainActivity.this, UserEventActivity.class));

        } else if (position == 4) {

//            startActivity(new Intent(MainActivity.this, UserEventActivity.class));

        } else if (position == 2) {

            startActivity(new Intent(MainActivity.this, ContactActivity.class));

        } else if (position == 3) {
            startActivity(new Intent(MainActivity.this, OnTapActivity.class));

        } else if (position == 10) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));

        } else if (position == R.id.nav_profile) {
//            startActivity(new Intent(MainActivity.this, MyProfileActivity.class));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
