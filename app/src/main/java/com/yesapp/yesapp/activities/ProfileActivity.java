package com.yesapp.yesapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private String mCurrent_state;
    private DatabaseReference mFriendReqDatabase;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String user_id = getIntent().getStringExtra("uid");

        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        profileName = (TextView) findViewById(R.id.profile_display_name);
        profileStatus = (TextView) findViewById(R.id.profile_status);
        profileFriendsCount =(TextView) findViewById(R.id.profile_friend_count);
        profileSendRequestBtn = (Button) findViewById(R.id.profile_cancel_request_btn);

        mCurrent_state ="not_friends";

        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();


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


        profileSendRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profileFriendsCount.setEnabled(false);


                //============NOT FRIENDS++++++++++++++++++++++++++++++++++++++++
                if(mCurrent_state.equals("not_friends")){
                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).child("request_typ")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mFriendReqDatabase.child(user_id).child("").child(mCurrentUser.getUid())
                                        .child("request_typ").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileActivity.this,"Request Sent!",Toast.LENGTH_SHORT).show();
                                        mCurrent_state ="req_sent";
                                        profileSendRequestBtn.setText("Cancel Friend Request");
                                        profileFriendsCount.setEnabled(true);

                                    }
                                });
                            }
                            else{
                                Toast.makeText(ProfileActivity.this,"Failed sending request, try again!",Toast.LENGTH_SHORT).show();
                                profileFriendsCount.setEnabled(true);

                            }
                        }
                    });


                }

                //++++++++++++++++++++++++ CANCEL REQUEST STATE========================================
                if(mCurrent_state.equals("req_sent")){

                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()) {
                                            mCurrent_state = "not_friends";
                                            profileSendRequestBtn.setText("Send Friend Request");
                                            profileSendRequestBtn.setEnabled(true);

                                        }
                                    }
                                });
                            }
                        }
                    });


                }
                }
        });


    }
}
