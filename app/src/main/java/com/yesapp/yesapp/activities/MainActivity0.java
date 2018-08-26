package com.yesapp.yesapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yesapp.yesapp.R;
import com.yesapp.yesapp.viewInflatorsAdapters.SectionsPagerAdapter;

public class MainActivity0 extends AppCompatActivity {

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionPagerAdapter;
    TabLayout mTabLayout;
    Toolbar mToolbar;
    static SharedPreferences sp;
    private int onBackPressed = 0;         //to track how many times back was pressed


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.menu_settings_btn1){
            //================================Go to the Settings Activity===================================
            Intent intent = new Intent(MainActivity0.this,Settings.class);
            startActivity(intent);

        }
        else if(item.getItemId()==R.id.menu_settings_btn2){
            //=============================go to the Create Activity========================================
            Intent i = new Intent(MainActivity0.this, Create.class);
            startActivity(i);

        }

        return true;
    }
    @Override
    protected void onResume() {
        checkIfLogged();
        super.onResume();
    }

    //-------------------------End of the SwipeRefresh Layout Listener------------------------------

    void checkIfLogged(){
        //to check for the Login++++++++++++++++++++++++++++++++++
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if(currentUser ==null){

            if (sp.getBoolean("logged", false)) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(sp.getString("name", ""),
                        sp.getString("password","")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        } else {

                            sp.edit().putBoolean("logged", false).apply();
                            startActivity(new Intent(MainActivity0.this, LoginActivity.class));

                            finish();
                        }
                    }
                });
            }
            else{
                sp.edit().putBoolean("logged", false).apply();
                startActivity(new Intent(MainActivity0.this, StartActivity.class));

                finish();


            }
        }
    }

    //================================to prevent unwanted exiting of the app========================
    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Press Back again to Exit",Toast.LENGTH_SHORT).show();
        if(onBackPressed==1){
            onBackPressed =0;
            super.onBackPressed();
        }
        onBackPressed ++;
    }
    //---
}
