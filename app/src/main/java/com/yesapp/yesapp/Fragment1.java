package com.yesapp.yesapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {
SwipeRefreshLayout swipeRefreshLayout;
RecyclerView mainScreenRecyclerView;

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //the view is already created
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


    }

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
                    //initRecyclerView(ItemsArrayList);
                }}//end of the add On DataChange listener


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); //end of the Database.addValueEventListener


        swipeRefreshLayout.setRefreshing(false); //stop refreshing

    }
//    public void initRecyclerView(ArrayList<ListItem> ItemsArrayList){
//
//        // transfers the ListArray into the RecyclerView with the CustomAdapter
//        mainScreenRecyclerView = (RecyclerView) getView().findViewById(R.id.RecyclerView0);
//        RecyclerViewAdapter recyclerViewAdapter= new RecyclerViewAdapter(this,ItemsArrayList);
//        mainScreenRecyclerView.setAdapter(recyclerViewAdapter);
//        mainScreenRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//    }

}
