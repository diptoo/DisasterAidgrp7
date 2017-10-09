package com.example.user.firebasedemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import org.w3c.dom.Text;

public class CollectDonate extends AppCompatActivity {

    private RecyclerView mBlogList;//LIST VIEW
    private DatabaseReference mDatabase;
    private Query mCur;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //String num;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_donate);


        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add:
                        // Toast.makeText(BlogSingleActivity.this,"clicked",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CollectDonate.this,PostActivity.class));
                        break;
                    case R.id.icon_home:
                        // Toast.makeText(BlogSingleActivity.this,"home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CollectDonate.this,blog_app.class));
                        break;
                    case R.id.action_person:
                        startActivity(new Intent(CollectDonate.this,Check.class));
                        break;
                }
                return true;
            }
        });

        mAuth= FirebaseAuth.getInstance();
        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Intent loginIntent=new Intent(CollectDonate.this,Login.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
                else
                {
                    System.out.println("null apy ni");
                }
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference().child("donator_info");
        String currentUserid=mAuth.getCurrentUser().getUid();
      //BLOG child er under a all data save
        mCur=mDatabase.orderByChild("postuserid").equalTo(currentUserid);
      //  Toast.makeText(CollectDonate.this,currentUserid,Toast.LENGTH_LONG).show();
        mBlogList = (RecyclerView) findViewById(R.id.dinfo);//LIST VIEW VABE SHOW KORBE AJONNE
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));//VERTICAL FORMAT




    }
    public void onBackPressed()
    {
        startActivity(new Intent(CollectDonate.this,blog_app.class));
    }


    protected void onStart() { //blog class
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        //model class Blog,viewholder view te value set from blog
        FirebaseRecyclerAdapter<Donate, DonateViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donate, DonateViewHolder>(
                Donate.class,
                R.layout.collect_donate,
                DonateViewHolder.class,
                mCur
        ) {
            @Override
            protected void populateViewHolder(DonateViewHolder viewHolder, Donate model, int position) {
                final String post_key = getRef(position).getKey();

                viewHolder.setDonatorname(model.getDonatorname());//TITLE ER VITOR JETA TYPRE KORA HOISE FUNCTION  PASS KORE blog cls a jay
                viewHolder.setDonatorcontact(model.getDonatorcontact());
                viewHolder.setDonatoradd(model.getDonatoradd()); //context Picasso is a library and not an application.application er moto kaj korar jonno
                viewHolder.setDtype(model.getDtype());
                viewHolder.setDmethod(model.getDmethod());
                viewHolder.setDonatoramount(model.getDonatoramount());


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(CollectDonate.this,"you clicked a view",Toast.LENGTH_LONG).show();
                        Intent SingleBlogIntent=new Intent(CollectDonate.this,CollectSingle.class);
                        SingleBlogIntent.putExtra("collect_id",post_key);
                        startActivity(SingleBlogIntent);


                    }
                });

                /*Button mMap;
              //  Button mCall;

                mMap=(Button) findViewById(R.id.gmap);
               // mCall=(Button) findViewById(R.id.phonecall);
                viewHolder.mMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CollectDonate.this,"Button clicked",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CollectDonate.this,MapsActivity.class));
                    }
                }); */
           /*   viewHolder.mCall.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Intent intent = new Intent(Intent.ACTION_DIAL);
                      Toast.makeText(CollectDonate.this,num,Toast.LENGTH_LONG).show();
                      intent.setData(Uri.parse("tel:"+num));
                      startActivity(intent);
                  }
              });*/
                /*mMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CollectDonate.this,"Button clicked",Toast.LENGTH_LONG).show();
                    }
                });*/
                }

        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class DonateViewHolder extends RecyclerView.ViewHolder {

        View mView;
        FirebaseAuth mAuth;
       // Button mMap,mCall;

        public DonateViewHolder(View itemView) {
            super(itemView);
            mView = itemView;  //ITEM VIEW TE SOB DATA CHOLE ASBE MVIEW OBJ A SAVE KORLAM
            mAuth=FirebaseAuth.getInstance();
            //mMap=(Button) mView.findViewById(R.id.gmap);
           /* mCall=(Button) mView.findViewById(R.id.phonecall);
            mCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("donator","Some text");
                }
            });*/


        }


        public void setDonatorname(String donatorname) {
            TextView post_donator_name = (TextView) mView.findViewById(R.id.donator_name);
            post_donator_name.setText(donatorname);
        }

        public void setDonatorcontact(String donatorcontact) {
            //num=donatorcontact;
            TextView  post_donator_contact = (TextView) mView.findViewById(R.id.donator_contact);
            post_donator_contact.setText(donatorcontact);
        }

        public void setDonatoradd(String donatoradd) {
            TextView post_donator_add = (TextView) mView.findViewById(R.id.donator_address);
            post_donator_add.setText(donatoradd);
        }

        public void setDtype(String dtype) {
            TextView post_donation_type = (TextView) mView.findViewById(R.id.donate_type);
            post_donation_type.setText(dtype);
        }
        public void setDmethod(String dmethod)
        {
            TextView post_donation_method = (TextView) mView.findViewById(R.id.donate_method);
            post_donation_method.setText(dmethod);
        }
        public void setDonatoramount(String donatoramount)
        {
            TextView post_donation_amount = (TextView) mView.findViewById(R.id.donator_amount);
            post_donation_amount.setText(donatoramount);
        }
    }

    }
