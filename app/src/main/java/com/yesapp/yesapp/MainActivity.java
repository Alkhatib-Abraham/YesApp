package com.yesapp.yesapp;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {

    TextView texts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texts = (TextView) findViewById(R.id.textView);


    }

    public void gotocreate(View view) {
        Intent i = new Intent(MainActivity.this, Create.class);
        startActivity(i);
    }


    public void Read(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference posts = database.getReference("posts");
        FirebaseUser use1r = FirebaseAuth.getInstance().getCurrentUser();
        String usernow = use1r.getDisplayName();
        
        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, Posts> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Posts>>() {});

                List<Posts> posts = new ArrayList<>(results.values());

                for (Posts post : posts) {


                    final   ArrayList<ListItem> Items=new  ArrayList<ListItem> ();
                    Items.add(new ListItem(post.getCityName(),post.getAction()));


                    final MyCustomAdapter myadpter= new MyCustomAdapter(Items);

                    ListView ls=(ListView)findViewById(R.id.list);
                    ls.setAdapter(myadpter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    class MyCustomAdapter extends BaseAdapter
    {
        ArrayList<ListItem> Items=new ArrayList<ListItem>();
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
            LayoutInflater linflater =getLayoutInflater();
            View view1=linflater.inflate(R.layout.row_view, null);

            TextView txtname =(TextView) view1.findViewById(R.id.txtcity);
            TextView txtdes =(TextView) view1.findViewById(R.id.txtaction);
            txtname.setText(Items.get(i).Name);
            txtdes.setText(Items.get(i).Desc);
            return view1;

        }



    }

}