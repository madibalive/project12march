    package com.example.madiba.venualpha.eventmanager;

    import android.os.Bundle;
    import android.support.annotation.Nullable;
    import android.support.v4.app.Fragment;
    import android.support.v7.widget.PopupMenu;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;

    import com.android.liuzhuang.rcimageview.RoundCornerImageView;
    import com.example.madiba.venualpha.R;

    import timber.log.Timber;

    public class UserEventsFragment extends Fragment {
        PopupMenu popupMenu;

        private TextView mtitle,mdate,mLocation,mTime,mCategory,mTag,mOverallCommenst,mDailyCommenst,mOverallStars,mDailyStars;
        private Button btnAction;
        private RoundCornerImageView imageView;



        public UserEventsFragment() {
        }

        public static UserEventsFragment newInstance() {
            return new UserEventsFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            Timber.i("JUST STARTED HERE ");
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view= inflater.inflate(R.layout.fragment_my_event, container, false);
//            btnAction = (Button) view.findViewById(R.id.ev_action_btn);
//            mCategory = (TextView) view.findViewById(R.id.ev_category);
            mdate = (TextView) view.findViewById(R.id.ev_date);
            mtitle = (TextView) view.findViewById(R.id.ev_title);
//            mLocation = (TextView) view.findViewById(R.id.ev_location);
//            mtitle = (TextView) view.findViewById(R.id.ev_time);
            mTag = (TextView) view.findViewById(R.id.ev_tag);
            Timber.i("JUST VIEW  HERE ");

            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }


        private void initView(){

        }

        private void showPopup(View view,int pos) {
            popupMenu = new PopupMenu(getActivity(), view);
            popupMenu.inflate(R.menu.menu_discover);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() ==R.id.action_editt){

                    // what to do


                }else if (item.getItemId() == R.id.action_delete){

                }
                return false;
            });
            popupMenu.show();
        }


    }
