package com.yesapp.yesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/// This is Sign in and MainActivity for this project
// If the user does not have an account he can register by going to register activity which is Main2Activity
// also.. already loged in users get automatically to the app
public class login extends AppCompatActivity {

   public static SharedPreferences sp;//to save if the user has been logged in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("logged", false)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(sp.getString("name", ""),
                    sp.getString("password","")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        startActivity(new Intent(login.this, MainActivity.class));
                    } else {
                        Toast.makeText(login.this, "password or email is not correct please dont try again", Toast.LENGTH_SHORT).show();
                        sp.edit().putBoolean("logged", false).apply();

                    }
                }
            });
            startActivity(new Intent(login.this, MainActivity.class));
        }

    }



// This function is triggered when the user presses a log in button


    //=======================================================================================================================================================
    // this function will be luanched when user press the register button
    public void re(View view) {
        startActivity(new Intent(login.this, Main2Activity.class));

    }
//=======================================================================================================================================================


    public void login(View view) {

//=======================================================================================================================================================
// These EditTexts are used when the user places his / her email and password and makes sure that his information is taken into the database
        EditText email2 = (EditText) findViewById(R.id.edit1);
        EditText password2 = (EditText) findViewById(R.id.edit2);
        String email3 = email2.getText().toString();
        String password3 = password2.getText().toString();

        //to save the logindata
        sp.edit().putString("name", email3).apply();
        sp.edit().putString("password", password3).apply();

// This function is triggered to let the user sign in with his details
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email3, password3).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sp.edit().putBoolean("logged", true).apply();

                    startActivity(new Intent(login.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(login.this, "password or email is not correct please dont try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
//=======================================================================================================================================================

    }
}
