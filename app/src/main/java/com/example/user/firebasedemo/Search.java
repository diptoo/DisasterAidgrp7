package com.example.user.firebasedemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Search extends AppCompatActivity {
    private RecyclerView mBlogList;//LIST VIEW
    private DatabaseReference mDatabase;
    private Query mCur;
    private DatabaseReference mDatabaseUsers,mDatabaseLike,mDatabaseCurrentUser; //user for current user
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mProcessLike=false;
    private String search="123";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add:
                       // Toast.makeText(BlogSingleActivity.this,"clicked",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Search.this,PostActivity.class));
                        break;
                    case R.id.icon_home:
                        //Toast.makeText(BlogSingleActivity.this,"home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Search.this,blog_app.class));
                        break;
                    case R.id.action_person:
                        startActivity(new Intent(Search.this,Check.class));
                        break;
                }
                return true;
            }
        });

        search = getIntent().getExtras().getString("search_id");
//Toast.makeText(Search.this,search,Toast.LENGTH_LONG).show();

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");//BLOG child er under a all data save
        mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users");

        //String currentUserid=mAuth.getCurrentUser().getUid();
        mDatabaseCurrentUser=FirebaseDatabase.getInstance().getReference().child("Blog");
        //mCur=mDatabaseCurrentUser.orderByChild("uid").equalTo(currentUserid);
        String str="sh";
        mCur=mDatabaseCurrentUser.orderByChild("title").startAt(search)
                .endAt(search+"\uf8ff");
        mBlogList=(RecyclerView) findViewById(R.id.blog_list);//LIST VIEW VABE SHOW KORBE AJONNE
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));//VERTICAL FORMAT
    }
    protected void onStart()
    { //blog class
        super.onStart();


        //model class Blog,viewholder view te value set from blog
        FirebaseRecyclerAdapter<Blog,SearchViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, SearchViewHolder>(
                Blog.class,
                R.layout.donate_row,
                SearchViewHolder.class,
                mCur
        ) {
            @Override
            protected void populateViewHolder(SearchViewHolder viewHolder, Blog model, int position) {
                final String post_key=getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());//TITLE ER VITOR JETA TYPRE KORA HOISE FUNCTION  PASS KORE blog cls a jay

                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage()); //context Picasso is a library and not an application.application er moto kaj korar jonno
                viewHolder.setUsername(model.getUsername());
                //viewHolder.setLikeBtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(blog_app.this,"you clicked a view",Toast.LENGTH_LONG).show();
                        Intent SingleBlogIntent=new Intent(Search.this,BlogSingleActivity.class);
                        SingleBlogIntent.putExtra("blog_id",post_key);
                        startActivity(SingleBlogIntent);


                    }
                });

            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageButton mLikebtn;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        TextView post_title;
        public SearchViewHolder(View itemView) {
            super(itemView);
            mView=itemView;  //ITEM VIEW TE SOB DATA CHOLE ASBE MVIEW OBJ A SAVE KORLAM





            post_title=(TextView) mView.findViewById(R.id.post_title);//mView object er post title er layput indicate korlam

            post_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.v("blog_app","Some text");
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
        public void setImage(Context ctx, String image)
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
