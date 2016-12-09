package com.example.helloworld;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.example.helloworld.bar_page.FeedListFragment;
import com.example.helloworld.bar_page.MyProfileFragment;
import com.example.helloworld.bar_page.NoteListFragment;
import com.example.helloworld.bar_page.SearchPageFragment;
import com.example.helloworld.fragments.MainTabbarFragment;

public class HelloWorldActivity extends Activity {

    FeedListFragment feedListFragment = new FeedListFragment();
    NoteListFragment noteListFragment = new NoteListFragment();
    SearchPageFragment searchPageFragment = new SearchPageFragment();
    MyProfileFragment myProfileFragment = new MyProfileFragment();

    MainTabbarFragment mainTabbarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hello);

        mainTabbarFragment = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.main_tabber);

        mainTabbarFragment.setOnTabbarSelectedListener(new MainTabbarFragment.OnTabbarSelectedListener() {
            @Override
            public void onTabbarSelected(int index) {
                changeContentFragment(index);
            }
        });

        mainTabbarFragment.setOnNewClickListener(new MainTabbarFragment.OnNewClickListener() {
            @Override
            public void onNewClicked() {
                bringUpeditor();
            }
        });

    }

    private void bringUpeditor() {
        Intent intent = new Intent(this, NewContentActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom,R.anim.none);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mainTabbarFragment.setSelectedItem(0);
    }

    private void changeContentFragment(int index) {
        Fragment newFragment = null;

        switch (index) {
            case 0:
                newFragment = feedListFragment;
                break;
            case 1:
                newFragment = noteListFragment;
                break;
            case 2:
                newFragment = searchPageFragment;
                break;
            case 3:
                newFragment= myProfileFragment;
                break;
            default:break;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content,newFragment).commit();
    }
}
