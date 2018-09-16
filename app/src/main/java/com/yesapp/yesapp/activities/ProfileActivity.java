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

import java.text.DateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {


    ImageView profileImage;
    TextView profileName, profileStatus ,profileFriendsCount;
    Button profileSendRequestBtn, profileDeclineRequestBtn;
    DatabaseReference mUserDatabaseReference;
    private String mCurrent_state;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private String profileUserName;

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
        profileSendRequestBtn = (Button) findViewById(R.id.friend_req_btn);
        profileDeclineRequestBtn = (Button) findViewById(R.id.friend_req_decline_btn);




        mCurrent_state ="not_friends";

        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (user_id.equals(mCurrentUser.getUid())){

            profileSendRequestBtn.setVisibility(View.GONE);
            profileDeclineRequestBtn.setVisibility(View.GONE);

        }

        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileUserName =dataSnapshot.child("name").getValue().toString();
                String status =dataSnapshot.child("status").getValue().toString();
                String image =dataSnapshot.child("image").getValue().toString();
                String thumb_image =dataSnapshot.child("thumb_image").getValue().toString();

                profileName.setText(profileUserName);
                profileStatus.setText(status);
                Picasso.get().load(image).into(profileImage);

            mFriendReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(user_id)){

                        String re_type = dataSnapshot.child(user_id).child("request_typ").getValue().toString();

                        if (re_type.equals("received")){

                            mCurrent_state ="req_received";
                            profileSendRequestBtn.setText("Accept Friend Request");
                        }
                        else if(re_type.equals("sent")){
                            mCurrent_state ="req_sent";
                            profileSendRequestBtn.setText("Cancel Friend Request");


                        }
                    }

                    else{

                        mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(user_id)){

                                    mCurrent_state ="friends";
                                    profileSendRequestBtn.setText("Unfriend "+ profileUserName);


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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

                                    }
                                });
                            }
                            else{
                                Toast.makeText(ProfileActivity.this,"Failed sending request, try again!",Toast.LENGTH_SHORT).show();

                            }

                            profileFriendsCount.setEnabled(true);

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

                                        }
                                        else{


                                        }

                                        profileSendRequestBtn.setEnabled(true);

                                    }
                                });
                            }

                        }
                    });


                }
                //================== Accept Friend Req+++++++++++++++++++
                if(mCurrent_state.equals("req_received")) {

                    final String current_date = DateFormat.getDateTimeInstance().format(new Date());
                    mFriendDatabase.child(mCurrentUser.getUid()).child(user_id).setValue(current_date).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {


                                mFriendDatabase.child(user_id).child(mCurrentUser.getUid()).setValue(current_date).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        if(task.isSuccessful()){


                                            //Delete the Request because they are Friends now
                                            mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                        mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if(task.isSuccessful()) {
                                                                    mCurrent_state = "friends";
                                                                    profileSendRequestBtn.setText("Unfriend " + profileUserName);

                                                                }
                                                                else{


                                                                }

                                                                profileSendRequestBtn.setEnabled(true);

                                                            }
                                                        });
                                                    }

                                                }
                                            });



                                        }
                                    }
                                });
                            }
                            else
                            {

                            }
                            profileSendRequestBtn.setEnabled(true);

                        }
                    });


                }


                }
        });


    }
}
