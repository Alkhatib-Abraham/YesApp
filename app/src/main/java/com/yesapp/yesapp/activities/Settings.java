package com.yesapp.yesapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yesapp.yesapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    TextView nametxtview,statusTextView;
    Button changeNameBtn;
    EditText newNameText;
    public static final int GALLERY_PICK = 1;
    String userId = "";
    CircleImageView circleImageView;

    //StorageReference for saving the Image
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        nametxtview = (TextView) findViewById(R.id.textView5);
        //TextView emailtxtview = (TextView) findViewById(R.id.textView6); unnecessary
        newNameText = (EditText) findViewById(R.id.editText);
        changeNameBtn = (Button) findViewById(R.id.changeNameBtn);
        statusTextView = (TextView) findViewById(R.id.textView6);

         circleImageView = (CircleImageView) findViewById(R.id.settings_image);

        changeNameBtn.setVisibility(View.GONE);
        newNameText.setVisibility(View.GONE);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nametxtview.setText(user.getDisplayName());
       // emailtxtview.setText(user.getEmail()); unnecessary
        userId = user.getUid();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference file_path = mStorageRef.child("profile_images").child("profile_image.jpg");
        /*
        Get the Image Uri from the users's tree brunch Uri
        -- maybe get the thumbnail for a faster onCreate
         */

      //  circleImageView.setImageURI(file_path.getFile(ImageUri));
            // TODO: Get The Image proparly and save it somewhere for fast loading





    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    public void changeName(View view) {


        if (newNameText.getText().toString().equals("")) {
            Toast.makeText(Settings.this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newNameText.getText().toString()).build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    nametxtview.setText(user.getDisplayName());
                    Toast.makeText(Settings.this, "Name Updated Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
        // get the status and the image
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(userId).child("status");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusTextView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        changeNameBtn.setVisibility(View.GONE);
        newNameText.setVisibility(View.GONE);
    }


    public void openEdit(View view) {
        if (changeNameBtn.getVisibility() == View.GONE) {
            changeNameBtn.setVisibility(View.VISIBLE);
            newNameText.setVisibility(View.VISIBLE);
        } else {
            changeNameBtn.setVisibility(View.GONE);
            newNameText.setVisibility(View.GONE);
        }
    }


    public void sign_out(View view) {
        FirebaseAuth.getInstance().signOut();
        MainActivity0.sp.edit().putBoolean("logged", false).apply(); //should be fixed in the future to not depend on another activity
        Intent b = new Intent(Settings.this, StartActivity.class);
        b.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//solves the problem of going back
        startActivity(b);
        finish();

    }

    public void changeImage(View view) {

//        Intent gallary_intent = new Intent();
//        gallary_intent.setType("image/*");
//        gallary_intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(gallary_intent,"SELECT IMAGE"),GALLERY_PICK);
        //start picker to get image for cropping and then use the image in cropping activity]7p
       CropImage.activity().setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(Settings.this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
//            Uri imageUri = data.getData();
//            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            Uri imageUri = data.getData();
//            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);

        }
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            final Uri resultUri = result.getUri();
            Toast.makeText(this, resultUri.toString(), Toast.LENGTH_LONG).show();
            mStorageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference file_path = mStorageRef.child("profile_images").child("profile_image.jpg");
                file_path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(Settings.this,"Image Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            circleImageView.setImageURI(resultUri);

                        }
                        else{
                            Toast.makeText(Settings.this,"Error, please try Again!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    public void changeStatus(View view) {

        if (changeNameBtn.getVisibility() == View.GONE) {
            changeNameBtn.setVisibility(View.VISIBLE);
            newNameText.setVisibility(View.VISIBLE);

        } else {
            changeNameBtn.setVisibility(View.GONE);
            newNameText.setVisibility(View.GONE);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(userId).child("status");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusTextView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}


