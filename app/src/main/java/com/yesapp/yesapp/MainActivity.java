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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texts = (TextView) findViewById(R.id.textView);

        final   ArrayList<ListItem> Items=new  ArrayList<ListItem> ();
        Items.add(new ListItem("hussien","he is good man"));
        Items.add(new ListItem("ahmed","he is ban man"));
        Items.add(new ListItem("jasim","he is okey man"));
        Items.add(new ListItem("jena","he is well man"));
        final MyCustomAdapter myadpter= new MyCustomAdapter(Items);

        ListView ls=(ListView)findViewById(R.id.list);
        ls.setAdapter(myadpter);
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtname =(TextView) view.findViewById(R.id.txt_action);
                TextView txtdes =(TextView) view.findViewById(R.id.txt_city);
                Toast.makeText(getApplicationContext(),txtname.getText(),Toast.LENGTH_LONG).show();
                Items.add(new ListItem("rana", "he is okey "));
                Items.add(new ListItem("sama", "he is well "));
                myadpter.notifyDataSetChanged();

            }
        });

    }

    public void gotocreate(View view) {
        Intent i = new Intent(MainActivity.this, Create.class);
        startActivity(i);
    }

    public void Read(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference posts = database.getReference("posts");

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, Posts> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, Posts>>() {});

                List<Posts> posts = new ArrayList<>(results.values());

                for (Posts post : posts) {
                   texts.setText(post.getAction() + " " + post.getCityName());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class MyCustomAdapter extends BaseAdapter
    {
        ArrayList<ListItem> Items= new ArrayList<>();
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

            TextView txtname =(TextView) view1.findViewById(R.id.txt_city);
            TextView txtdes =(TextView) view1.findViewById(R.id.txt_action);
          //  txtname.setText(Items.get(i).Name);
          //  txtdes.setText(Items.get(i).Desc);
            return view1;

        }



    }
}
