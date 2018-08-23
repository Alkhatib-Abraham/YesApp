package com.yesapp.yesapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yesapp.yesapp.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    TextView nametxtview,statusTextView;
    Button changeNameBtn;
    EditText newNameText;
   // public static final int GALLERY_PICK = 1;
    String userId = "";
    CircleImageView circleImageView;
    FirebaseDatabase firebaseDatabase;

    ProgressDialog mProgressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //Declare the Views in the Settings Activity Screen
        nametxtview = findViewById(R.id.textView5);
        //TextView emailtxtview = (TextView) findViewById(R.id.textView6); unnecessary
        newNameText = findViewById(R.id.editText);
        changeNameBtn = findViewById(R.id.changeNameBtn);
        statusTextView = findViewById(R.id.textView6);
         circleImageView = findViewById(R.id.settings_image);


         //gets activated whenever the Change Name Button is pressed
        changeNameBtn.setVisibility(View.GONE);
        newNameText.setVisibility(View.GONE);

        //gets the crrent user and his ID so we can get his settings
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        nametxtview.setText(user.getDisplayName());
        userId = user.getUid();

        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("profile_images").child(userId+".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){

                    Picasso.get().load(task.getResult()).into(circleImageView);

                    //error Object not found
                }
            }
        });

        //ToDO: get The download Url properly!

        //load the settings
        firebaseDatabase =FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("status").getValue().toString();

                statusTextView.setText(status);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // get the status and the image
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(userId);
        databaseReference.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statusTextView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
        assert user != null;
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    nametxtview.setText(user.getDisplayName());
                    Toast.makeText(Settings.this, "Name Updated Successfully", Toast.LENGTH_SHORT).show();

                }
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

        CropImage.ActivityResult result = null;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            Uri imageUri = data.getData();
//            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
            result = CropImage.getActivityResult(data);

        }

        if (resultCode == RESULT_OK) {
            mProgressDialog = new ProgressDialog(Settings.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setTitle("Uploading Image...");
            mProgressDialog.setMessage("Your awesome Image is on it's way to the Yes world!");
            mProgressDialog.show();

            assert result != null;
            final Uri resultUri = result.getUri();
            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                            final StorageReference file_path = mStorageRef.child("profile_images").child(userId);
                file_path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){


                            final String download_url = file_path.getDownloadUrl().toString();


                            FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference1 = firebaseDatabase1.getReference().child(userId+"jpg");
                            databaseReference1.child("image").setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                       //circleImageView.setImageURI(Uri.parse(download_url));
                                        mProgressDialog.dismiss();
                                    }
                                }
                            });


                        }
                        else{
                            mProgressDialog.dismiss();
                            Toast.makeText(Settings.this,"Error, please try Again!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            assert result != null;
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
                statusTextView.setText(Objects.requireNonNull(dataSnapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}


