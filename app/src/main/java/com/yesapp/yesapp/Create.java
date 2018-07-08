package com.yesapp.yesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class Create extends AppCompatActivity {

    EditText messageText;
    DatabaseReference myRef;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
         messageText = (EditText) findViewById(R.id.editText);

    }


    public void post(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Posts : ");
        writeNewUser(messageText.getText().toString(), messageText.getText().toString() );
        text = (TextView) findViewById(R.id.textView2);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                text.setText(post.message);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        myRef.addValueEventListener(postListener);
    }

    private void writeNewUser(String userId, String message ) {
        User user = new User(userId, message);

        myRef.child("posts").child(userId).setValue(message);
    }
}
