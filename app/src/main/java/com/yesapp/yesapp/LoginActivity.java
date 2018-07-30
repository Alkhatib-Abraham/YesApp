package com.yesapp.yesapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.yesapp.yesapp.R.layout.activity_login;

/// This is Sign in and MainActivity for this project
// If the user does not have an account he can register by going to register activity which is Main2Activity
// also.. already loged in users get automatically to the app
public class LoginActivity extends AppCompatActivity implements ResetPasswordListener {

    TextView msg1,msg2;
    public static SharedPreferences sp;//to save if the user has been logged in
    ProgressBar progressBar;
    Button loginBtn, registerBtn;
    private int onBackPressed = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        //preparing for the login====
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        msg1 = (TextView) findViewById(R.id.errorMsg4);
        msg2 = (TextView) findViewById(R.id.errorMsg5);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        msg1.setVisibility(View.GONE);
        msg2.setVisibility(View.GONE);


        //==================The Login Process==========================
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("logged", false)) {

             progressBar.setVisibility(View.VISIBLE);
             loginBtn.setEnabled(false);
             registerBtn.setEnabled(false);


             FirebaseAuth.getInstance().signInWithEmailAndPassword(sp.getString("name", ""),
                    sp.getString("password","")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(LoginActivity.this, "password or email is not correct please try again", Toast.LENGTH_SHORT).show();
                        sp.edit().putBoolean("logged", false).apply();

                    }
                }
            });
        }

    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press Back again to Exit",Toast.LENGTH_SHORT).show();
        if(onBackPressed==1){
            onBackPressed =0;
            finish();
        }
        onBackPressed ++;
    }

    //==============================================================================================
    // this function will be lunched when the user presses on the the register button
    public void goToRegisterActivity(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

//===============================The Login Method===================================================

    public void login(View view) {
        msg1.setVisibility(View.GONE);
        msg2.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


// These EditTexts are used when the user places his / her email
// and password and makes sure that his information is taken into the database

        loginBtn.setEnabled(false);
        registerBtn.setEnabled(false);
        EditText log_email = (EditText) findViewById(R.id.loginEmailEdiText);
        EditText log_password = (EditText) findViewById(R.id.loginPasswordEdiText);

        String login_email    = log_email.getText().toString().trim();
        String login_password = log_password.getText().toString().trim();

        //If a false Email was entered
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(login_email).matches()) {
            msg1.setVisibility(View.VISIBLE);

            if(login_password.length() < 6){
                msg2.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                loginBtn.setEnabled(true);
                registerBtn.setEnabled(true);
                return;
            }

            progressBar.setVisibility(View.INVISIBLE);
            loginBtn.setEnabled(true);
            registerBtn.setEnabled(true);
            return;
        }
        else if(login_password.length() < 6){
            msg2.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            loginBtn.setEnabled(true);
            registerBtn.setEnabled(true);
            return;
        }



        //to save the loginData for future auto-login
        sp.edit().putString("name", login_email).apply();
        sp.edit().putString("password", login_password).apply();



        // This function is triggered to let the user sign in with his details
        FirebaseAuth.getInstance().signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sp.edit().putBoolean("logged", true).apply();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "password or email is not correct please try again", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    loginBtn.setEnabled(true);
                    registerBtn.setEnabled(true);
                }
            }
        }); //end of the FireBaseAuth

    }//end of Login Method

    public void resetPassword(View view) {
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.show(getSupportFragmentManager(),"Reset Password");



    }

    //This Methode recieves the Email from the Dialoge and resets it
    @Override
    public void passTheResetEmail(String Email) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            Toast.makeText(this, "Failed, Please try again later!", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //not Shown -- must be fixed later
                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Failed, Please try again later!", Toast.LENGTH_SHORT).show();


                        }
                    }
                });


    }



}//end of the Class
