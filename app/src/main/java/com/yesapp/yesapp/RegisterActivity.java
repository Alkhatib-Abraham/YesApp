package com.yesapp.yesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

// This is the register Activity
public class RegisterActivity extends AppCompatActivity {

    String register_usersname;
    private FirebaseAuth mAuth;
    TextView msg1,msg2,msg3,msg22;
     Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //these are the red messages
        msg1 = (TextView) findViewById(R.id.errorMsg1);
        msg2 = (TextView) findViewById(R.id.errorMsg2);
        msg3 = (TextView) findViewById(R.id.errorMsg3);
        msg22 = (TextView) findViewById(R.id.errorMsg22);

        msg1.setVisibility(View.GONE);
        msg2.setVisibility(View.GONE);
        msg3.setVisibility(View.GONE);
        msg22.setVisibility(View.GONE);


        //===================================ToolBar================================================



        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        mToolbar.setTitle("Create an Account");
//        mToolbar.setCollapsible(true);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);

    }



// this function will be triggerd when users presses the register button, and putting his details into the database

    public void register(View view) {
        msg1.setVisibility(View.GONE);
        msg2.setVisibility(View.GONE);
        msg3.setVisibility(View.GONE);
        msg22.setVisibility(View.GONE);


        EditText reg_email =    (EditText) findViewById(R.id.Register_Email);
        EditText reg_password =     (EditText) findViewById(R.id.Register_Password);
        EditText reg_password2 =         (EditText) findViewById(R.id.Register_Password2);
        EditText reg_name =         (EditText) findViewById(R.id.Register_User);


        String register_email = reg_email.getText().toString().trim();
        String register_password = reg_password.getText().toString().trim();
        String register_password2 = reg_password2.getText().toString().trim();
        register_usersname = reg_name.getText().toString().trim();

         // to check==================
        int error =0;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(register_email).matches()) {
            msg1.setVisibility(View.VISIBLE);
            error ++;
        }
        if (register_usersname.equals("")) {
            msg3.setVisibility(View.VISIBLE);
            error ++;
        }
        if (register_password.length() < 6) {
            msg2.setVisibility(View.VISIBLE);
            error ++;
        }
        if (!register_password.equals(register_password2)) {
            msg22.setVisibility(View.VISIBLE);
            error ++;
        }
        if(error !=0){
            return;
        }//----------------------------



        //========================the Register data get added to FireBase==================
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(register_email , register_password).addOnCompleteListener(new OnCompleteListener<com.google.firebase.auth.AuthResult>() {



            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(RegisterActivity.this, "Succesful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    //to add the register usersname
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(register_usersname).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.e("Test", "User display_name added");
                            }
                        }
                    });

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                }

                else {
                    // if email already registerd
                    if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                        Toast.makeText(RegisterActivity.this, "Already Registered", Toast.LENGTH_SHORT).show();

                    }//end of if

                    else {
                        Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }//end of else

            }//end of in Complete
        }); //end of the Auth. prossess

    } //end of the register Method
}// end of the class
