package com.yesapp.yesapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Main2Activity extends AppCompatActivity {


    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void register(View view) {
        EditText email =    (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.h);
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email1 , password1).addOnCompleteListener(new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Main2Activity.this, "Succesful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = task.getResult().getUser();
                    UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name.getText().toString())
                            .build();
                    startActivity(new Intent(Main2Activity.this, login.class));

                } else {
                    // if email already registerd
                    if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                        Toast.makeText(Main2Activity.this, "Already Registered", Toast.LENGTH_SHORT).show();

                    }

                    Toast.makeText(Main2Activity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
