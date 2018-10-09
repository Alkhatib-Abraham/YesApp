package com.yesapp.yesapp.viewInflatorsAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yesapp.yesapp.activities.MainActivity0;
import com.yesapp.yesapp.activities.ProfileActivity;
import com.yesapp.yesapp.activities.StartActivity;
import com.yesapp.yesapp.classes.ListItem;
import com.yesapp.yesapp.activities.PostView;
import com.yesapp.yesapp.classes.Posts;
import com.yesapp.yesapp.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "RecylerViewAdapter"; // for Debugging
    private Context mContext;
    ArrayList<ListItem> PostsArrayList= new ArrayList<ListItem>();


       //constructor
       public RecyclerViewAdapter(Context mContext, ArrayList<ListItem> PostsArrayList) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {   //adds Items
                   holder.variableTextViewCity.setText(PostsArrayList.get(position).CityName);
                   holder.variableTextViewAction.setText(PostsArrayList.get(position).ActionName);
                   holder.variableTextViewUsersName.setText(PostsArrayList.get(position).UsersName);
                   holder.variableTextViewDescription.setText(PostsArrayList.get(position).Description);
                   holder.variableTextViewPostId.setText(PostsArrayList.get(position).PostId);



                   //to get people who said Yes
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("posts/" +PostsArrayList.get(position).PostId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if(dataSnapshot.child("/Yes/name1").getValue()!=null) {
               holder.variableTextViewYesPerson.setText(dataSnapshot.child("/Yes/name1").getValue().toString()+" said Yes!");
                   }
               String authorsUid =dataSnapshot.child("authorsEmail").getValue().toString();
               DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child(authorsUid).child("thumb_image");
               databaseReference1.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.getValue()!=null){
                       Picasso.get().load(dataSnapshot.getValue().toString()).into(holder.imageView);
                   }
                   else {
                           Picasso.get().load(R.drawable.no_avatar).into(holder.imageView);

                       }
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                   }
               });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // holder.variableTextViewYesPerson.setText(PostsArrayList.get(position).gt);

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
                       }});

        holder.YesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                * I used 3 dataBaseReferences
                * the first one to save the email of the Yes person
                * THe third was for his name
                * the second was to get the Author's Email to check who wrote the post
                *
                * */

//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                final DatabaseReference databaseReference = database.getReference("posts/" +holder.variableTextViewPostId.getText().toString()).child("Yes");
//                //to get the author's email
//                final DatabaseReference databaseReference2 = database.getReference("posts/" +holder.variableTextViewPostId.getText().toString());
//
//               databaseReference2.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.getValue() ==null){ //e7tyat
//                            return;
//                        }
//                        Posts posts  =dataSnapshot.getValue(Posts.class);
//                        if(!user.getUid().equals(posts.getAuthorsEmail())) { // to check if the user isn't saying yes to his own post
//                            databaseReference.child("email1").setValue(user.getUid());
//                            databaseReference.child("name1").setValue(user.getDisplayName());
//                            holder.variableTextViewYesPerson.setText(user.getDisplayName() + " said Yes!");
//                        }
//                        else{
//                            Toast.makeText(mContext,"can't say yes to your own post!",Toast.LENGTH_SHORT).show();
//                        }
//                        String uid =posts.getAuthorsEmail();
//
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//             });

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // we need three things
                // 1. to mark the post as said yes to
                // 2. to add this post as yesed to the authors profile(users tree)
                // 3. to add this post as yes to the yes sayer profile(users tree)




                //to make 2 possible we need the uid of the Author
                //to get the author's email
                final DatabaseReference databaseReference4 = database.getReference("posts/" +holder.variableTextViewPostId.getText().toString());
                final String current_date = DateFormat.getDateTimeInstance().format(new Date());

                databaseReference4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() ==null){ //e7tyat
                            return;
                        }
                        Posts posts  =dataSnapshot.getValue(Posts.class);
                        if(user.getUid().equals(posts.getAuthorsEmail())) { // to check if the user isn't saying yes to his own post

                            Toast.makeText(mContext,"can't say yes to your own post!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String uid =posts.getAuthorsEmail(); // it get's the UID just the name is false


                        //2
                        DatabaseReference databaseReference3 = database.getReference("yesed/" + uid)
                                .child(holder.variableTextViewPostId.getText().toString());
                        databaseReference3.setValue(user.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext,"YES!",Toast.LENGTH_SHORT);
                                    }
                                });

                        //1
                        DatabaseReference databaseReference = database.getReference("posts/" +holder.variableTextViewPostId.getText().toString())
                                .child("Yes");
                        databaseReference.setValue(user.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext,"YES!",Toast.LENGTH_SHORT);
                            }
                        });
                        //TODO: do id better with more people possible
                        //3
                        DatabaseReference databaseReference2 = database.getReference("yes/" + user.getUid())
                                .child(holder.variableTextViewPostId.getText().toString());
                        databaseReference2.setValue(current_date).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(mContext,"YES!",Toast.LENGTH_SHORT);
                            }
                        });



                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });






















        }
        });



        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference databaseReference3 = database.getReference("posts/" +holder.variableTextViewPostId.getText().toString());

                databaseReference3.child("authorsEmail").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() ==null){ //e7tyat
                            return;
                        }

                        String uid =dataSnapshot.getValue().toString();
                        Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                        profileIntent.putExtra("uid",uid);
                        mContext.startActivity(profileIntent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }); }
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
      TextView variableTextViewYesPerson;
      Button YesBtn;
      CircleImageView imageView;


        public ViewHolder(View itemView){
            super(itemView);
            variableTextViewCity = (TextView) itemView.findViewById(R.id.variableTextViewCity);
            variableTextViewAction = (TextView) itemView.findViewById(R.id.variableTextViewAction);
            variableTextViewUsersName = (TextView) itemView.findViewById(R.id.variableTextViewUsersName);
            variableTextViewDescription = (TextView) itemView.findViewById(R.id.variableTextViewDescription);
            variableTextViewPostId = (TextView) itemView.findViewById(R.id.variableTextViewPostId);
            variableTextViewYesPerson =(TextView) itemView.findViewById(R.id.variableTextViewYesPerson);
            YesBtn                 = (Button) itemView.findViewById(R.id.yesBtn);
             imageView = (CircleImageView) itemView.findViewById(R.id.variableImageView);


        }


    }


}
