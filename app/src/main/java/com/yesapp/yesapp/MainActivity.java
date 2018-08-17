package com.yesapp.yesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


// This is to read data from a database and place it in a list view
public class MainActivity extends AppCompatActivity {

    RecyclerView mainScreenRecyclerView;           //to contain the posts
    SwipeRefreshLayout swipeRefreshLayout; //to refresh
    private int onBackPressed = 0;         //to track how many times back was pressed
    public static SharedPreferences sp;//to save if the user has been logged in
    private Toolbar mToolbar; //my custom toolbar


    @Override
    public void onCreate(Bundle savedInstanceState) {

        checkIfLogged();//+++++++++++++Check if Logged+++++++++++++++++++++++++++++++++++++++++++++++++++




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout0);
        refresh(); //to get the new content



        //======================SwipeRefresh Layout Listener to track the refreshing================
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        //===================================ToolBar================================================



        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        mToolbar.setTitle("Yes App Baby!");
//        mToolbar.setCollapsible(true);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(mToolbar);



    }

    @Override
    protected void onResume() {
        checkIfLogged();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.main_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if(item.getItemId()==R.id.menu_settings_btn1){
             //================================Go to the Settings Activity===================================
                 Intent intent = new Intent(MainActivity.this,Settings.class);
                 startActivity(intent);

             }
             else if(item.getItemId()==R.id.menu_settings_btn2){
             //=============================go to the Create Activity========================================
                 Intent i = new Intent(MainActivity.this, Create.class);
                 startActivity(i);

         }

         return true;
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

    void checkIfLogged(){
        //to check for the Login++++++++++++++++++++++++++++++++++
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
                startActivity(new Intent(MainActivity.this, StartActivity.class));

                finish();


            }
        }
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