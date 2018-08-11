package com.yesapp.yesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// This is to read data from a database and place it in a list view
public class MainActivity extends AppCompatActivity {

    RecyclerView mainScreenRecyclerView;           //to contain the posts
    SwipeRefreshLayout swipeRefreshLayout; //to refresh
    private int onBackPressed = 0;         //to track how many times back was pressed
    public static SharedPreferences sp;//to save if the user has been logged in


    @Override
    public void onCreate(Bundle savedInstanceState) {

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if(currentUser ==null){

            if (sp.getBoolean("logged", false)) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(sp.getString("name", ""),
                        sp.getString("password","")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        } else {

                            sp.edit().putBoolean("logged", false).apply();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));

                            finish();
                        }
                    }
                });
            }
            else{
                sp.edit().putBoolean("logged", false).apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

                finish();
                

            }
            }





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        refresh(); //to get the new content



        //======================SwipeRefresh Layout Listener to track the refreshing================
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }
    //-------------------------End of the SwipeRefresh Layout Listener------------------------------






    //================================to prevent unwanted exiting of the app========================
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Press Back again to Exit",Toast.LENGTH_SHORT).show();
        if(onBackPressed==1){
            onBackPressed =0;
            super.onBackPressed();
        }
        onBackPressed ++;
    }
    //----------------------------------------------------------------------------------------------




    //===============the Void which loads new content and put it in the listView====================

    public void refresh(){
        //necessary References
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("posts");

        // Pull the posts from the cloud and put them in a listView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {
                    Iterable<DataSnapshot> posts = dataSnapshot.getChildren(); //get all posts

//                    //pulls from the cloud
//                    HashMap<String, Posts> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Posts>>() {
//                    });
//                    List<Posts> posts = new ArrayList<>(results.values());
//

                    //defines an ArrayList
                    final ArrayList<ListItem> ItemsArrayList;
                    ItemsArrayList = new ArrayList<ListItem>();
                    // iterates through the posts and put them in the Adapter


                    for (DataSnapshot post : posts) {

                        Posts postObj = post.getValue(Posts.class);
                        postObj.setPostId(post.getKey());
                        //add a new post to the arrayList with help of the posts class
                        ItemsArrayList.add(new ListItem(postObj.getCityName(), postObj.getAction(),postObj.getName(),postObj.getDescription(),postObj.getPostId(),postObj.getAuthorsEmail()));




                    }
                    initRecyclerView(ItemsArrayList);
                }}//end of the add On DataChange listener


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //end of the Database.addValueEventListener


        Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false); //stop refreshing

    }
    //----------------------------------End of refresh()--------------------------------------------





    //=============================go to the Create Activity========================================
    public void goToCreateActivity(View view) {
        Intent i = new Intent(MainActivity.this, Create.class);
        startActivity(i);
    }
    //----------------------------------------------------------------------------------------------


    //================================Go to the Settings Activity===================================
    public void goToSettingsActivity(View view) {
        Intent intent = new Intent(MainActivity.this,Settings.class);
        startActivity(intent);

    }

    //this void initialises the RecyclerView
    public void initRecyclerView(ArrayList<ListItem> ItemsArrayList){

        // transfers the ListArray into the RecyclerView with the CustomAdapter
        mainScreenRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        RecyclerViewAdapter recyclerViewAdapter= new RecyclerViewAdapter(this,ItemsArrayList);
        mainScreenRecyclerView.setAdapter(recyclerViewAdapter);
        mainScreenRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}