package com.yesapp.yesapp;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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

    ListView ls;
    SwipeRefreshLayout swipeRefreshLayout;
    private int onBackPressed = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        Refresh();
        ls = (ListView) findViewById(R.id.list);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,PostView.class);

                TextView cityName =(TextView) view.findViewById(R.id.txtcity);
                TextView actionName =(TextView) view.findViewById(R.id.txtaction);
                TextView userName =(TextView) view.findViewById(R.id.txtuser);
                TextView discr =(TextView) view.findViewById(R.id.textView14);


                String city = cityName.getText().toString().trim();
                String action = actionName.getText().toString().trim();
                String user = userName.getText().toString().trim();
                String disc = discr.getText().toString().trim();


                intent.putExtra("city",city);
                intent.putExtra("action",action);
                intent.putExtra("user",user);
                intent.putExtra("discreption",disc);




                startActivity(intent);

            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();

            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    //=====================================================================================================
// this function triggeres when user presses the go to create button  to post posts in the database
    public void gotocreate(View view) {
        Intent i = new Intent(MainActivity.this, Create.class);
        startActivity(i);
    }

//=====================================================================================================




    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Press Back again to Exit",Toast.LENGTH_SHORT).show();
        if(onBackPressed==1){
            onBackPressed =0;
            super.onBackPressed();
        }
        onBackPressed ++;

    }
    public void Refresh(){
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
                         ls = (ListView) findViewById(R.id.list);
                        ls.setAdapter(myadpter);
                    }

                }}//end of the add On DataChange listener

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //end of the Database.addValueEventListener
      Toast.makeText(MainActivity.this,"Refreshed",Toast.LENGTH_SHORT).show();
    swipeRefreshLayout.setRefreshing(false); //stop refreshing

    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this,Settings.class);
        startActivity(intent);
    }




    //==============================================================================================


    class MyCustomAdapter extends BaseAdapter
        //this class defines how the ViewItems of the Arraylist are inflated
    {
        ArrayList<ListItem> Items= new ArrayList<ListItem>();


        MyCustomAdapter(ArrayList<ListItem> Items ) {
            this.Items=Items;
        }

        @Override
        public int getCount() {
            return Items.size();
        }

        @Override
        public String getItem(int position) {
            return Items.get(position).Name;
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
            txtname.setText(Items.get(i).Name);
            txtdes.setText(Items.get(i).Desc);
            txtuser.setText(Items.get(i).User);
            txtdiscreption.setText(Items.get(i).Discription);

            return view1;

        }
    }
}