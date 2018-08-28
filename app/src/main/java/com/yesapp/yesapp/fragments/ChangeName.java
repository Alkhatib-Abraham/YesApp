package com.yesapp.yesapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yesapp.yesapp.R;
import com.yesapp.yesapp.activities.Settings;


public class ChangeName extends AppCompatDialogFragment {

    private EditText newName;
    private ChangeName listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder DialogeBilder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view =layoutInflater.inflate(R.layout.change_name_status,null);
        DialogeBilder.setView(view)
                .setTitle("Change Name").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        }).setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (newName.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please enter a name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String userId = user.getUid();
                DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(userId).child("name");
                databaseReference.setValue(newName.getText().toString().trim());

                //update both user name and display name
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(newName.getText().toString().trim()).build();
                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.e("Test", "User display_name added");
                        }
                    }
                });


                Settings.refreshSettings(3);
            }
        });

        newName =(EditText)view.findViewById(R.id.changeNameStatusEditText);
        newName.setHint("Please Enter your Name");
        return DialogeBilder.create();

    }

}
