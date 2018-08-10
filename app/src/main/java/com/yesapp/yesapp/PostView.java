package com.yesapp.yesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostView extends AppCompatActivity {

    String postId ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        TextView textView = findViewById(R.id.textView11);
        TextView textView1 = findViewById(R.id.textView12);
        TextView textView2 = findViewById(R.id.textView13);
        TextView textView3 = findViewById(R.id.textView15);
        TextView textView4 = findViewById(R.id.textView2);






        Intent intent =getIntent();
        textView.setText(intent.getStringExtra("city"));
        textView1.setText(intent.getStringExtra("action"));
        textView2.setText(intent.getStringExtra("user"));
        textView3.setText(intent.getStringExtra("description"));
        postId = intent.getStringExtra("postId");
        textView4.setText(postId);



    }



    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void Yes(View view) {
     // save the post Id by the user to be able to load it again with it's unice Id
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(postId);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();









    }

}
