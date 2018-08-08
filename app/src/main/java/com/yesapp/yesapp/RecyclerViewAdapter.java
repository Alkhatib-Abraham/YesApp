package com.yesapp.yesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

 public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "RecylerViewAdapter"; // for Debugging
    private Context mContext;
    ArrayList<ListItem> PostsArrayList= new ArrayList<ListItem>();


       //constructor
        RecyclerViewAdapter(Context mContext, ArrayList<ListItem> PostsArrayList ) {
                this.PostsArrayList=PostsArrayList;
                this.mContext = mContext;
            }



            @Override
        //the inflater method
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                    View mView=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent,false);
                    ViewHolder viewHolder = new ViewHolder(mView);

                    return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {   //adds Items
                   holder.variableTextViewCity.setText(PostsArrayList.get(position).CityName);
                   holder.variableTextViewAction.setText(PostsArrayList.get(position).ActionName);
                   holder.variableTextViewUsersName.setText(PostsArrayList.get(position).UsersName);
                   holder.variableTextViewDescription.setText(PostsArrayList.get(position).Description);
                   holder.variableTextViewPostId.setText(PostsArrayList.get(position).PostId);

                   holder.itemView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           Toast.makeText(mContext,"Clicked",Toast.LENGTH_LONG);
                       }
                   });




    }

    @Override
    public int getItemCount() {
        return PostsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

      TextView variableTextViewCity;
      TextView variableTextViewAction;
      TextView variableTextViewUsersName;
      TextView variableTextViewDescription;
      TextView variableTextViewPostId;


        public ViewHolder(View itemView){
            super(itemView);
            variableTextViewCity = (TextView) itemView.findViewById(R.id.variableTextViewCity);
            variableTextViewAction = (TextView) itemView.findViewById(R.id.variableTextViewAction);
            variableTextViewUsersName = (TextView) itemView.findViewById(R.id.variableTextViewUsersName);
            variableTextViewDescription = (TextView) itemView.findViewById(R.id.variableTextViewDescription);
            variableTextViewPostId = (TextView) itemView.findViewById(R.id.variableTextViewPostId);


        }


    }


}
