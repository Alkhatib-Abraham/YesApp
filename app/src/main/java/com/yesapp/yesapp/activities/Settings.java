package com.yesapp.yesapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.yesapp.yesapp.fragments.ChangeName;
import com.yesapp.yesapp.fragments.ChangeStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Settings extends AppCompatActivity {

    static TextView nametxtview,statusTextView;
   // public static final int GALLERY_PICK = 1;
    static String userId = "";
   static CircleImageView circleImageView;
    FirebaseDatabase firebaseDatabase;
    ProgressDialog mProgressDialog;
     Toolbar mToolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mToolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        mToolbar.setTitle("User's Settings");
        mToolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        mToolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(mToolbar);


        //Declare the Views in the Settings Activity Screen
        nametxtview = findViewById(R.id.textView5);
        statusTextView = findViewById(R.id.textView6);
         circleImageView = findViewById(R.id.settings_image);


        //gets the crrent user and his ID so we can get his settings
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        nametxtview.setText(user.getDisplayName());
        userId = user.getUid();

        refreshSettings(0); //load everything







    }

    public static void refreshSettings(int level){

        if(level ==1 || level ==0) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference;
            storageReference = firebaseStorage.getReference();
            storageReference.child("profile_images").child(userId+".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        if(task.getResult().toString().equals("default"))
                        {
                            Picasso.get().load(R.drawable.no_avatar).placeholder(R.drawable.no_avatar).into(circleImageView);

                        }
                        else {
                            Picasso.get().load(task.getResult().toString()).placeholder(R.drawable.no_avatar).into(circleImageView);
                        }
                    }
                }
            });
        }
        DatabaseReference databaseReference;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(userId);

        if(level ==2 || level ==0) {
            databaseReference.child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!=null) {
                        statusTextView.setText(dataSnapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if(level ==3 || level ==0) {
            databaseReference = firebaseDatabase.getReference().child("users").child(userId);
            databaseReference.child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!=null) {
                        nametxtview.setText(dataSnapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
        }




    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }





    public void changeName(View view) {


        ChangeName changeName = new ChangeName();
        changeName.show(getSupportFragmentManager(),"Change Name");

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
       CropImage.activity().setAspectRatio(1,1).setGuidelines(CropImageView.Guidelines.ON).start(Settings.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = null;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
            File thumbFilePath =new File(resultUri.getPath());


            Bitmap thumb_bitmap = null;
            try {
                thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200).setMaxHeight(200).setQuality(75).compressToBitmap(thumbFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();



            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference file_path = mStorageRef.child("profile_images").child(userId+".jpg");
            final StorageReference thumb_file_path = mStorageRef.child("profile_images").child("thumbs").child(userId+".jpg");



            file_path.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){








                                    //when we are done with main pic upload we wanna uplode the Thumbnail
                            UploadTask uploadTask = thumb_file_path.putBytes(thumb_byte);

                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                                    if(task.isSuccessful()){

                                        Task<Uri> file_downloadLink =file_path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                if (task.isSuccessful()) {
                                                    final Uri imageUri = task.getResult();

                                                    Task<Uri> thumb_downloadLink = file_path.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {

                                                            if (task.isSuccessful()) {
                                                                Uri thumbUri = task.getResult();

                                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                                final DatabaseReference databaseReference = firebaseDatabase.getReference().child("users").child(userId);

                                                                Map<String, Object> linksMap = new HashMap<>();
                                                                linksMap.put("image", thumbUri.toString());
                                                                linksMap.put("thumb_image", imageUri.toString());
                                                                databaseReference.updateChildren(linksMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            mProgressDialog.dismiss();
                                                                            refreshSettings(1); //load the new pic
                                                                        } else {
                                                                            mProgressDialog.dismiss();
                                                                            Toast.makeText(Settings.this, "Error, please try Again!", Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                mProgressDialog.dismiss();
                                                                Toast.makeText(Settings.this, "Error, please try Again!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }

                                                else{
                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(Settings.this,"Error, please try Again!",Toast.LENGTH_SHORT).show();

                                                }
                                            }});






                                    }
                                    else{
                                        mProgressDialog.dismiss();
                                        Toast.makeText(Settings.this,"Error, please try Again!",Toast.LENGTH_SHORT).show();
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
        ChangeStatus changeStatus = new ChangeStatus();
        changeStatus.show(getSupportFragmentManager(),"Change Status");
    }
}


