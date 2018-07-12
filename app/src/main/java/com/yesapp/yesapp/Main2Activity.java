package com.yesapp.yesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.yesapp.yesapp.login.sp;

// This is the register Activity
public class Main2Activity extends AppCompatActivity {



    static String s; // a limited solution to store the username-- need to be fixed soon


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }



// this function will be triggerd when users presses the register button, and putting his details into the database

    public void register(View view) {
        EditText email =    (EditText) findViewById(R.id.Register_Email);
        EditText password = (EditText) findViewById(R.id.Register_Password);
        EditText name = (EditText) findViewById(R.id.Register_User);

        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        String usersname = name.getText().toString().trim();

        sp.edit().putString("name", usersname).apply();


        //the User's Name gets saved in S
        //  UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
        //        .setDisplayName(name.getText().toString()).build();

        //  s =profileUpdates.getDisplayName();

        //Toast.makeText(Main2Activity.this,s, Toast.LENGTH_SHORT).show();


        //the Register data get added to Firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email1 , password1).addOnCompleteListener(new OnCompleteListener<com.google.firebase.auth.AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Main2Activity.this, "Succesful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Main2Activity.this, login.class));

                } else {
                    // if email already registerd
                    if (task.getException().getMessage().equals("The email address is already in use by another account.")) {
                        Toast.makeText(Main2Activity.this, "Already Registered", Toast.LENGTH_SHORT).show();

                    }//end of if

                    Toast.makeText(Main2Activity.this, "Failed", Toast.LENGTH_SHORT).show();

                }//end of else

            }//end of in Complete
        }); //end of the Auth. prossess

    } //end of the register Method
}// end of the class
