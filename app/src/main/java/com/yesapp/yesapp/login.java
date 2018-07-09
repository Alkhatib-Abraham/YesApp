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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText email2 =    (EditText) findViewById(R.id.edit1);
        EditText password2 = (EditText) findViewById(R.id.edit2);
        String email3 = email2.getText().toString();
        String password3 = password2.getText().toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email3, password3).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){

                  startActivity(new Intent(login.this, MainActivity.class));
              }else{
                  Toast.makeText(login.this , "password or email is not correct please dont try again", Toast.LENGTH_SHORT ).show();
              }
            }
        });



    }

    public void re(View view) {
        startActivity(new Intent(login.this, Main2Activity.class));

    }
}
