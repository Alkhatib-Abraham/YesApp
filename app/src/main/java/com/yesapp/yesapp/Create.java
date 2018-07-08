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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Create extends AppCompatActivity {

    EditText messageText;
    EditText phoneText;
    EditText adressText;

    DatabaseReference myRef;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
         messageText = (EditText) findViewById(R.id.editText);
        phoneText = (EditText) findViewById(R.id.editText3);
        adressText = (EditText) findViewById(R.id.editText5);


    }


    public void post(View view) {



    }

    private void writeNewUser(String userId, String message ) {
 
    }

}
