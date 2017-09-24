package com.example.user.firebasedemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DonateHome extends AppCompatActivity {
    private RecyclerView mBlogList;//LIST VIEW
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_home);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");//BLOG child er under a all data save

        mBlogList=(RecyclerView) findViewById(R.id.blogdonate_list);//LIST VIEW VABE SHOW KORBE AJONNE
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void onStart()
    { //blog class
        super.onStart();


        //model class Blog,viewholder view te value set from blog
        FirebaseRecyclerAdapter<Blog,DonateView> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, DonateView>(
                Blog.class,
                R.layout.donate_row,
                DonateView.class,
                mDatabase
        )

        {

            @Override
            protected void populateViewHolder(DonateView viewHolder, Blog model, int position) {
                final String post_key=getRef(position).getKey();


                viewHolder.setTitle(model.getTitle());//TITLE ER VITOR JETA TYPRE KORA HOISE FUNCTION  PASS KORE blog cls a jay

                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage()); //context Picasso is a library and not an application.application er moto kaj korar jonno
                viewHolder.setUsername(model.getUsername());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(blog_app.this,"you clicked a view",Toast.LENGTH_LONG).show();
                        Intent SingleBlogIntent=new Intent(DonateHome.this,DonateSingleActivity.class);
                        SingleBlogIntent.putExtra("blog_id",post_key);
                        startActivity(SingleBlogIntent);


                    }
                });
               // Toast.makeText(DonateHome.this,"dhkse",Toast.LENGTH_LONG).show();

            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }
   public static class DonateView extends RecyclerView.ViewHolder
   {
       View mView;
       TextView post_title;


       public DonateView(View itemView) {
           super(itemView);
           mView=itemView;
           post_title=(TextView) mView.findViewById(R.id.post_title);//mView object er post title er layput indicate korlam

       }

       public void setTitle(String title)
       {   //PARAMETER STRING TITLE A THAKBE title a JETA TYPE KORA HOISE

           post_title.setText(title); //BLOG ROW / RECYCLER VIEW layout a TITLE value SET KORBE

       }
       public void setDesc(String desc)
       {
           TextView post_desc=(TextView) mView.findViewById(R.id.post_desc);
           post_desc.setText(desc);
       }
       public void setImage(Context ctx,String image)
       {
           ImageView post_image=(ImageView) mView.findViewById(R.id.post_image);
           Picasso.with(ctx).load(image).into(post_image);
       }
       public void setUsername(String username)
       {
           TextView post_username=(TextView) mView.findViewById(R.id.post_username);
           post_username.setText(username);
       }
   }

    }

