package com.yesapp.yesapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
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

                  Intent intent = new Intent(mContext,PostView.class);

                           TextView cityName =(TextView) view.findViewById(R.id.variableTextViewCity);
                                 TextView actionName =(TextView) view.findViewById(R.id.variableTextViewAction);
                                 TextView userName =(TextView) view.findViewById(R.id.variableTextViewUsersName);
                                 TextView discr =(TextView) view.findViewById(R.id.variableTextViewDescription);
                                 TextView postId =(TextView) view.findViewById(R.id.variableTextViewPostId);



                                 intent.putExtra("city",cityName.getText().toString().trim());
                                 intent.putExtra("action",actionName.getText().toString().trim());
                                 intent.putExtra("user",userName.getText().toString().trim());
                                 intent.putExtra("description",discr.getText().toString().trim());
                                 intent.putExtra("postId",postId.getText().toString().trim());
                                 mContext.startActivity(intent);







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
