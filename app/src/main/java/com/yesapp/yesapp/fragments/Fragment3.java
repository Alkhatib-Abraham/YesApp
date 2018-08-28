package com.yesapp.yesapp.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yesapp.yesapp.R;
import com.yesapp.yesapp.classes.Friend;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment3 extends ListFragment {


    ListView friendsListView;
    SimpleAdapter adapter;

    Friend friend ;

    public Fragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

      // friendItemArrayList = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_fragment3, container, false);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        friend = new Friend();
        friend.setFriend_uid("doksk");
        friend.setProfilePhotoLink("https://firebasestorage.googleapis.com/v0/b/yesapp-6897c.appspot.com/o/profile_images%2F6mkvDuKnqCP97FZUVmAuY8RWpJB2.jpg?alt=media&token=54aff94e-dc0d-4bb3-bb06-b64986cb6aad");
        friend.setFriend_status("Ya lahui");
        friend.setFriend_name("Fat7i");
       // friendItemArrayList.add(friend);

//        ArrayAdapter friendItemArrayAdapter = new ArrayAdapter<Friend>(getActivity(),R.layout.fragment_fragment3,friendsListView);
//
//        MyCustomAdapter adapter = new MyCustomAdapter(friendsListView);
//               View view= adapter.getView(1);
//
//       Inflater inflater = getLayoutInflater().inflate(adapter);
    }
}




class MyCustomAdapter extends ArrayAdapter{


    private Context context;


    public MyCustomAdapter(Context context, List items){


        super(context, R.layout.fragment_fragment3 ,items);
        this.context = context;
    }



    private class ViewHolder{

        TextView friendName;
        TextView friendStatus;
        CircleImageView friendImage;
    }
    public ArrayList<Friend> friendItemArrayList ;


    @Override
    public int getCount() {
        return friendItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }




    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;


        Friend itemFriend  = friendItemArrayList.get(i);

        View viewToUse = null;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
        {

            viewToUse = layoutInflater.inflate(R.layout.friends_row_view,null);

        }


        holder = new ViewHolder();


        holder.friendName = viewToUse.findViewById(R.id.friend_display_name);
        holder.friendImage = viewToUse.findViewById(R.id.friend_circle);
        holder.friendStatus = viewToUse.findViewById(R.id.friend_status);

        holder.friendName = viewToUse.findViewById(R.id.friend_display_name);
        holder.friendStatus = viewToUse.findViewById(R.id.friend_status);
        holder.friendImage = viewToUse.findViewById(R.id.friend_circle);

        holder.friendName.setText("fajlfj");
        holder. friendStatus.setText("jdlda");
        Picasso.get().load("odjk").into(holder.friendImage);


        return viewToUse;
    }
}
