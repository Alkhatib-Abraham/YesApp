package com.yesapp.yesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PostView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
