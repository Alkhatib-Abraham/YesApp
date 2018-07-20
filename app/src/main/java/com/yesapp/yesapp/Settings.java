package com.yesapp.yesapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Settings extends AppCompatActivity {

    TextView nametxtview;
    Button changeNameBtn;
    EditText newNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        nametxtview = (TextView)findViewById(R.id.textView5);
        TextView emailtxtview = (TextView)findViewById(R.id.textView6);
        newNameText =(EditText) findViewById(R.id.editText);
        changeNameBtn = (Button) findViewById(R.id.changeNameBtn);

        changeNameBtn.setVisibility(View.GONE);
        newNameText.setVisibility(View.GONE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nametxtview.setText(user.getDisplayName());
        emailtxtview.setText(user.getEmail());



    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
        }


    public void changeName(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newNameText.getText().toString()).build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    nametxtview.setText(user.getDisplayName());
                    Toast.makeText(Settings.this,"Name Updated Successfully",Toast.LENGTH_SHORT).show();

                }
            }
        });
        changeNameBtn.setVisibility(View.GONE);
        newNameText.setVisibility(View.GONE);
    }


    public void openEdit(View view) {
        if(changeNameBtn.getVisibility()==View.GONE) {
            changeNameBtn.setVisibility(View.VISIBLE);
            newNameText.setVisibility(View.VISIBLE);
        }
        else{
            changeNameBtn.setVisibility(View.GONE);
            newNameText.setVisibility(View.GONE);
        }
    }
}

