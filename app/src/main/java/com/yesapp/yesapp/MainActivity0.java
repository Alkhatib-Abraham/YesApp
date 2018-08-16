package com.yesapp.yesapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MainActivity0 extends AppCompatActivity {

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionPagerAdapter;
    TabLayout mTabLayout;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);

        //ToolBar
        mToolbar = (Toolbar) findViewById(R.id.main_bar_layout);
        mToolbar.setTitle("Yes App2 Baby!");
      //  mToolbar.setCollapsible(true);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);

        //view Pager
        mViewPager = (ViewPager) findViewById(R.id.tabPager);
        mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);







    }
}
