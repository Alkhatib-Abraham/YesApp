package com.yesapp.yesapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yesapp.yesapp.R;
import com.yesapp.yesapp.fragments.ResetPassword;
import com.yesapp.yesapp.viewInflatorsAdapters.ResetPasswordListener;

import static com.yesapp.yesapp.activities.MainActivity.sp;
import static com.yesapp.yesapp.R.layout.activity_login;

/// This is Sign in and MainActivity for this project
// If the user does not have an account he can register by going to register activity which is Main2Activity
// also.. already loged in users get automatically to the app
public class LoginActivity extends AppCompatActivity implements ResetPasswordListener {

    TextView msg1,msg2;
    Button loginBtn;
    Toolbar mToolbar;
    ProgressDialog progressDialogLoginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        //preparing for the login====
        msg1 = (TextView) findViewById(R.id.errorMsg4);
        msg2 = (TextView) findViewById(R.id.errorMsg5);
        loginBtn = (Button) findViewById(R.id.loginBtnn);
        msg1.setVisibility(View.GONE);
        msg2.setVisibility(View.GONE);

        progressDialogLoginProgress = new ProgressDialog(LoginActivity.this);


        //==================The Login Process==========================
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

                        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class) ;
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();
                    } else {

                        Toast.makeText(LoginActivity.this, "password or email is not correct please try again", Toast.LENGTH_SHORT).show();
                        sp.edit().putBoolean("logged", false).apply();

                    }
                }
            });
        }

        //===================================ToolBar================================================



        mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        mToolbar.setTitle("Login");
//        mToolbar.setCollapsible(true);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);


    }




    //==============================================================================================
    // this function will be lunched when the user presses on the the register button
    public void goToRegisterActivity(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

//===============================The Login Method===================================================

    public void login(View view) {
        progressDialogLoginProgress.setTitle("Logging in");
        progressDialogLoginProgress.setMessage("You are a few seconds away from the Yes World! please wait...");
        progressDialogLoginProgress.setCanceledOnTouchOutside(false);
        progressDialogLoginProgress.show();
        msg1.setVisibility(View.GONE);
        msg2.setVisibility(View.GONE);


// These EditTexts are used when the user places his / her email
// and password and makes sure that his information is taken into the database

        EditText log_email = (EditText) findViewById(R.id.loginEmailEdiText);
        EditText log_password = (EditText) findViewById(R.id.loginPasswordEdiText);

        String login_email    = log_email.getText().toString().trim();
        String login_password = log_password.getText().toString().trim();

        //If a false Email was entered
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(login_email).matches()) {
            msg1.setVisibility(View.VISIBLE);

            if(login_password.length() < 6){
                msg2.setVisibility(View.VISIBLE);
                progressDialogLoginProgress.dismiss();
                return;
            }

            progressDialogLoginProgress.dismiss();
            return;
        }
        else if(login_password.length() < 6){
            msg2.setVisibility(View.VISIBLE);
            progressDialogLoginProgress.dismiss();

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
                    progressDialogLoginProgress.dismiss();

                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity0.class) ;
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "password or email is not correct please try again", Toast.LENGTH_SHORT).show();
                    progressDialogLoginProgress.dismiss();

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
    @Override
    public void onBackPressed() {

        Intent i = new Intent(LoginActivity.this,StartActivity.class);
        startActivity(i);
        finish();
    }


}//end of the Class
