package com.example.hp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ButtonActivity extends AppCompatActivity {

    private int lastIndex;
    private String id=null;
    private String token;
    List<Fragment> mFragments;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.todo) {
                    setFragmentPosition(0);
                    return true;
                } else if (id == R.id.todo_set) {
                    setFragmentPosition(1);
                    return true;
                } else if (id == R.id.time) {
                    setFragmentPosition(2);
                    return true;
                }else if (id == R.id.per) {
                    setFragmentPosition(3);
                    return true;
                }
            return false;
        }
    };
    public void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(Todofragment.newInstance(id,token));
        mFragments.add(new Todosfragment());
        mFragments.add(Timefragment.newInstance(id,token));
        mFragments.add(new Personfragment());
        setFragmentPosition(0);
    }
    private void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastIndex);
        lastIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.frame, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        initData();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
