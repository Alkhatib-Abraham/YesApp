package com.yesapp.yesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.yesapp.yesapp.login.sp;

// This is the register Activity
public class Main2Activity extends AppCompatActivity {

    String usersname;
    private FirebaseAuth mAuth;

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
        usersname = name.getText().toString().trim();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email.setText("Not an Email!");
            return;
        }

        if (usersname.equals("")) {
            name.setText("Please Enter a name!");
            return;
        }

        if (password1.length() < 6) {
          password.setText("Password should be at least 6 digits long! ");
            return;
        }



        //the Register data get added to Firebase
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email1 , password1).addOnCompleteListener(new OnCompleteListener<com.google.firebase.auth.AuthResult>() {



            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(Main2Activity.this, "Succesful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(usersname).build();
                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Log.e("Test", "User display_name added");
                            }
                        }
                    });

                    Log.e("Test",user.getDisplayName()+"");




                        startActivity(new Intent(Main2Activity.this, login.class));

                }
                else {
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
