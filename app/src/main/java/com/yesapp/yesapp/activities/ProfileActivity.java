package com.yesapp.yesapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yesapp.yesapp.R;

public class ProfileActivity extends AppCompatActivity {


    ImageView profileImage;
    TextView profileName, profileStatus ,profileFriendsCount;
    Button profileSendRequestBtn;
    DatabaseReference mUserDatabaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String user_id = getIntent().getStringExtra("uid");

        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        profileName = (TextView) findViewById(R.id.profile_display_name);
        profileStatus = (TextView) findViewById(R.id.profile_status);
        profileFriendsCount =(TextView) findViewById(R.id.profile_friend_count);
        profileSendRequestBtn = (Button) findViewById(R.id.profile_cancel_request_btn);


        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name =dataSnapshot.child("name").getValue().toString();
                String status =dataSnapshot.child("status").getValue().toString();
                String image =dataSnapshot.child("image").getValue().toString();
                String thumb_image =dataSnapshot.child("thumb_image").getValue().toString();

                profileName.setText(name);
                profileStatus.setText(status);
                Picasso.get().load(image).into(profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
