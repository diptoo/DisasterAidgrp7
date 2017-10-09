package com.example.user.firebasedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class blog_app extends AppCompatActivity {
//LAYOUT A TOOLS LINE MISSING
//MAIN MENU ICON ER KAJ LAST A

    private RecyclerView mBlogList;//LIST VIEW
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers,mDatabaseLike; //user for current user
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mProcessLike=false;
    private TextView mNoti;
    private EditText mSearch;
    private ImageButton mSearchNow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_blog_app);



        mSearch=(EditText) findViewById(R.id.search_id);
        mSearchNow=(ImageButton) findViewById(R.id.search_now);



        mSearchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String search_key=mSearch.getText().toString();
                //Toast.makeText(blog_app.this,search_key,Toast.LENGTH_LONG).show();
                Intent SingleBlogIntent=new Intent(blog_app.this,Search.class);
                SingleBlogIntent.putExtra("search_id",search_key);
                startActivity(SingleBlogIntent);

            }
        });

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add:
                        //Toast.makeText(blog_app.this,"clicked",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(blog_app.this,PostActivity.class));
                        break;
                    case R.id.icon_home:
                        //Toast.makeText(blog_app.this,"home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(blog_app.this,blog_app.class));
                        break;
                    case R.id.action_person:
                        startActivity(new Intent(blog_app.this,Check.class));
                        break;
                }
                return true;
            }
        });

        mAuth=FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Intent loginIntent=new Intent(blog_app.this,Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
                else
                {
                    System.out.println("null apy ni");
                }
            }
        };

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");//BLOG child er under a all data save
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users");//users is a child
        mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseUsers.keepSynced(true);
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);
        //LIST VIEW DETAILS
        //blog row te card view cause  which might have different lengths/heights depending on their content (like pictures with descriptions and comments)
        mBlogList=(RecyclerView) findViewById(R.id.blog_list);//LIST VIEW VABE SHOW KORBE AJONNE
        mBlogList.setHasFixedSize(true);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mBlogList.setLayoutManager(mLayoutManager);//VERTICAL FORMAT
      //  mBlogList.setLayoutManager(new LinearLayoutManager(this));//VERTICAL FORMAT

        //List view end

       // checkUserExist();
    }


    public void onBackPressed()
    {
        startActivity(new Intent(blog_app.this,firstPage.class));
    }



    protected void onStart()
    { //blog class
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
       //model class Blog,viewholder view te value set from blog
        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                final String post_key=getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());//TITLE ER VITOR JETA TYPRE KORA HOISE FUNCTION  PASS KORE blog cls a jay

                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage()); //context Picasso is a library and not an application.application er moto kaj korar jonno
                viewHolder.setUsername(model.getUsername());
                viewHolder.setLikeBtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(blog_app.this,"you clicked a view",Toast.LENGTH_LONG).show();
                        Intent SingleBlogIntent=new Intent(blog_app.this,BlogSingleActivity.class);
                        SingleBlogIntent.putExtra("blog_id",post_key);
                        startActivity(SingleBlogIntent);


                    }
                });
                viewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mProcessLike=true;
                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mProcessLike) {
                                       // Toast.makeText(blog_app.this,"dhukse",Toast.LENGTH_LONG).show();
                                        if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                            mProcessLike = false;
                                        } else {
                                            mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Randomvalue");
                                            mProcessLike = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    }
                });
            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }
    private void checkUserExist() {
        Toast.makeText(blog_app.this,"user exist a dhukse",Toast.LENGTH_LONG).show();
        final String user_id=mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(Login.this,"pass wrong",Toast.LENGTH_LONG).show();
                if(!dataSnapshot.hasChild(user_id))
                {
                    Intent setupIntent=new Intent(blog_app.this,SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

  //RECYCLER VIEW ER JONNE  VIEW HOLDER LAGE value set korbe
    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageButton mLikebtn;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        TextView post_title;
        public BlogViewHolder(View itemView) {
            super(itemView);
           mView=itemView;  //ITEM VIEW TE SOB DATA CHOLE ASBE MVIEW OBJ A SAVE KORLAM

            mLikebtn=(ImageButton) mView.findViewById(R.id.like_btn);
           mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");
           mAuth=FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);

            post_title=(TextView) mView.findViewById(R.id.post_title);//mView object er post title er layput indicate korlam

            post_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.v("blog_app","Some text");

                }
            });
        }
        public void setLikeBtn(final String post_key)
        {
            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid()))
               {
                   mLikebtn.setImageResource(R.mipmap.like_red);
               }
               else{
                   mLikebtn.setImageResource(R.mipmap.like_gray);
               }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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


//MAIN MENU ICON ER KAJ EKHAN THEKE
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu resource file read kore
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add)
        {
            startActivity(new Intent(blog_app.this,PostActivity.class));
        }
        if(item.getItemId()==R.id.action_logout)
        {
            logout();
        }
        if(item.getItemId()==R.id.myPost)
        {
            startActivity(new Intent(blog_app.this,MyPost.class));
        }
        if(item.getItemId()==R.id.member_details)
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

/*if(item.getItemId()==R.id.action_notification)
{
    startActivity(new Intent(blog_app.this,Search.class));
}
*/




        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(blog_app.this,firstPage.class));
    }
}
