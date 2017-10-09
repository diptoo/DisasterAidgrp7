package com.example.user.firebasedemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DonateHome extends AppCompatActivity {
    private RecyclerView mBlogList;//LIST VIEW
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_home);
        mAuth=FirebaseAuth.getInstance();

        /*@Override
        public void onBackPressed()
        {
            startActivity(new Intent(DonateHome.this,firstPage.class));
        } */
   /*     mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser()==null) {
                    Toast.makeText(DonateHome.this,"DHUKHSE 2",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(DonateHome.this, GoogleSign.class));

                }
            }
        };
 */
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");//BLOG child er under a all data save

        mBlogList=(RecyclerView) findViewById(R.id.blogdonate_list);//LIST VIEW VABE SHOW KORBE AJONNE
        mBlogList.setHasFixedSize(true);
       // mBlogList.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mBlogList.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(DonateHome.this,firstPage.class));
    }
    protected void onStart()
    { //blog class
        super.onStart();
      //  mAuth.addAuthStateListener(mAuthListener);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu resource file read kore
        getMenuInflater().inflate(R.menu.donate_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_logout)
        {
            logout();
        }

        /*if(item.getItemId()==R.id.member_details)
        {
            startActivity(new Intent(blog_app.this,MemberDetails.class));
        }
        if(item.getItemId()==R.id.my_profile)
        {
            startActivity(new Intent(blog_app.this,Check.class));
        }
        if(item.getItemId()==R.id.donation_list)
        {
            startActivity(new Intent(blog_app.this,CollectDonate.class));
        }

        if(item.getItemId()==R.id.action_notification)
        {
            startActivity(new Intent(blog_app.this,blog_app.class));
        }
*/




        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        mAuth.signOut();
        Toast.makeText(DonateHome.this,"Sign out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(DonateHome.this,firstPage.class));
    }
    }

