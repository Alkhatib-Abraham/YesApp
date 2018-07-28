package com.yesapp.yesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    ListView mainScreenListView;           //to contain the posts
    SwipeRefreshLayout swipeRefreshLayout; //to refresh
    private int onBackPressed = 0;         //to track how many times back was pressed


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        refresh(); //to get the new content

     //============================to open a screen when clicking on an Item========================
        mainScreenListView = (ListView) findViewById(R.id.list);
        mainScreenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,PostView.class);

                TextView cityName =(TextView) view.findViewById(R.id.txtcity);
                TextView actionName =(TextView) view.findViewById(R.id.txtaction);
                TextView userName =(TextView) view.findViewById(R.id.txtuser);
                TextView discr =(TextView) view.findViewById(R.id.textView14);

                intent.putExtra("city",cityName.getText().toString().trim());
                intent.putExtra("action",actionName.getText().toString().trim());
                intent.putExtra("user",userName.getText().toString().trim());
                intent.putExtra("discreption",discr.getText().toString().trim());
                startActivity(intent);

            }
        });
        //--------------------------------The End of the Listener-----------------------------------




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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference posts = database.getReference("posts");

        // Pull the posts from the cloud and put them in a listView
        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null) {

                    //pulls from the cloud
                    HashMap<String, Posts> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Posts>>() {
                    });
                    List<Posts> posts = new ArrayList<>(results.values());
                    //defines an Arraylist
                    final ArrayList<ListItem> Items = new ArrayList<ListItem>();
                    // iterates through the posts and put them in the Adapter

                    for (Posts post : posts) {
                        //add a new post to the arrayList with help of the posts class
                        Items.add(new ListItem(post.getCityName(), post.getAction(),post.getName(),post.getDescription()));

                        // transfers the ListArray into the ListView with the CustomAdapter
                        final MyCustomAdapter myadpter = new MyCustomAdapter(Items);
                        mainScreenListView = (ListView) findViewById(R.id.list);
                        mainScreenListView.setAdapter(myadpter);
                    }

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
    //----------------------------------------------------------------------------------------------





    //===========================The Custom Adapter Class===========================================

    class MyCustomAdapter extends BaseAdapter
        //this class defines how the ViewItems of the Arraylist are inflated
    {
        ArrayList<ListItem> PostsArrayList= new ArrayList<ListItem>();


        MyCustomAdapter(ArrayList<ListItem> PostsArrayList ) {
            this.PostsArrayList=PostsArrayList;
        }

        @Override
        public int getCount() {
            return PostsArrayList.size();
        }

        @Override
        public String getItem(int position) {
            return PostsArrayList.get(position).Name;
        }

        @Override
        public long getItemId(int position) {
            return  position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater linflater = getLayoutInflater();
            View view1=linflater.inflate(R.layout.row_view, null);
            //the Textviews which are used to present the data
            TextView txtname =(TextView) view1.findViewById(R.id.txtcity);
            TextView txtdes =(TextView) view1.findViewById(R.id.txtaction);
            TextView txtuser =(TextView) view1.findViewById(R.id.txtuser);
            TextView txtdiscreption =(TextView) view1.findViewById(R.id.textView14);



            //get the data from Items and put it in the right places
            txtname.setText(PostsArrayList.get(i).Name);
            txtdes.setText(PostsArrayList.get(i).Desc);
            txtuser.setText(PostsArrayList.get(i).User);
            txtdiscreption.setText(PostsArrayList.get(i).Discription);

            return view1;

        }
    }
}