package com.yesapp.yesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Create extends AppCompatActivity {

    EditText cityName;
    EditText action;





    DatabaseReference myRef;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
         cityName = (EditText) findViewById(R.id.editText);
        action = (EditText) findViewById(R.id.editText3);


    }


    public void post(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myName = database.getReference("posts");

        Posts post = new Posts();
        post.setCityName(cityName.getText().toString());
        post.setAction(action.getText().toString());
        myName.push().setValue(post);





    }

    private void writeNewUser(String userId, String message ) {
 
    }

}
