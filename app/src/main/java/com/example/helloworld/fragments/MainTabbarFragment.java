package com.example.helloworld.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helloworld.R;

/**
 * Created by Administrator on 2016/12/6.
 */

public class MainTabbarFragment extends Fragment {

    View main_tab_feeds, main_tab_note, main_tab_search, main_tab_me, main_tab_btnnew;
    View[] tabbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tabbar, null);

        main_tab_feeds = view.findViewById(R.id.main_tab_feeds);
        main_tab_note = view.findViewById(R.id.main_tab_notes);
        main_tab_btnnew = view.findViewById(R.id.main_tab_btnnew);
        main_tab_search = view.findViewById(R.id.main_tab_search);
        main_tab_me = view.findViewById(R.id.main_tab_me);

        tabbar = new View[]{
                main_tab_feeds, main_tab_note, main_tab_search, main_tab_me
        };

        for (final View tab : tabbar) {
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTabbarClick(tab);
                }
            });
        }

        return view;
    }

    public static interface OnTabbarSelectedListener{
        void onTabbarSelected(int index);
    }

    public void setOnTabbarSelectedListener(OnTabbarSelectedListener onTabbarSelectedListener) {
        this.onTabbarSelectedListener = onTabbarSelectedListener;
    }

    OnTabbarSelectedListener onTabbarSelectedListener;



    private void onTabbarClick(View tab) {
        int selectedIndex = -1;

        for (int i=0;i<tabbar.length;i++) {
            View temp = tabbar[i];

            if (temp == tab) {
                temp.setSelected(true);
                selectedIndex = i;
            } else {
                temp.setSelected(false);
            }
        }

        if (onTabbarSelectedListener != null && selectedIndex>=0) {
            onTabbarSelectedListener.onTabbarSelected(selectedIndex);
        }


    }
}
