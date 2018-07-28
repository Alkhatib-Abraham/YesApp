package com.yesapp.yesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PostView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        TextView textView = findViewById(R.id.textView11);
        TextView textView1 = findViewById(R.id.textView12);
        TextView textView2 = findViewById(R.id.textView13);
        TextView textView3 = findViewById(R.id.textView15);


        Intent intent =getIntent();
        textView.setText(intent.getStringExtra("city"));
        textView1.setText(intent.getStringExtra("action"));
        textView2.setText(intent.getStringExtra("user"));
        textView3.setText(intent.getStringExtra("discreption"));



    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
