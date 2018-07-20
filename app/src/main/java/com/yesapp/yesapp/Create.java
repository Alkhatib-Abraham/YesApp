package com.yesapp.yesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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
        cityName = (EditText) findViewById(R.id.cityname);
        action = (EditText) findViewById(R.id.action);



    }
    //to prevent going back here while trying to exit the app
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Create.this, MainActivity.class);
        startActivity(i);
        finish();
        }


    public void post(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myName = database.getReference("posts");

        Posts post = new Posts();
        post.setCityName(cityName.getText().toString());
        post.setAction(action.getText().toString());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        post.setName(user.getDisplayName());

        myName.push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Toast.makeText(Create.this, "Post Add Successfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Create.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(Create.this, "Error : post not add 🙁 ", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    public void sign_out(View view) {
        FirebaseAuth.getInstance().signOut();
        login.sp.edit().putBoolean("logged", false).apply();
        Intent b = new Intent(Create.this, login.class);
        startActivity(b);

    }
}
