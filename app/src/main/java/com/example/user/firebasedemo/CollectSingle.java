package com.example.user.firebasedemo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollectSingle extends AppCompatActivity {
    private Button google_map,don_contact;
    private String mPost_key=null;
    private DatabaseReference mDatabase;
    private TextView post_donator_name,post_donator_contact,post_donator_add,post_donation_type,post_donation_method,post_donation_amount;
    String don_name,dona_contact,don_add,don_type,don_method,don_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_single);


        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add:
                       // Toast.makeText(CollectSingle.this,"clicked",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CollectSingle.this,PostActivity.class));
                        break;
                    case R.id.icon_home:
                       // Toast.makeText(BlogSingleActivity.this,"home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CollectSingle.this,blog_app.class));
                        break;
                    case R.id.action_person:
                        startActivity(new Intent(CollectSingle.this,Check.class));
                        break;
                }
                return true;
            }
        });


        post_donator_name = (TextView) findViewById(R.id.donator_name);
       post_donator_contact = (TextView) findViewById(R.id.donator_contact);
         post_donator_add = (TextView) findViewById(R.id.donator_address);
        post_donation_type = (TextView) findViewById(R.id.donate_type);
         post_donation_method = (TextView) findViewById(R.id.donate_method);
         post_donation_amount = (TextView) findViewById(R.id.donator_amount);

        mPost_key = getIntent().getExtras().getString("collect_id");

        mDatabase= FirebaseDatabase.getInstance().getReference().child("donator_info");

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                don_name=(String)dataSnapshot.child("donatorname").getValue();
                dona_contact=(String)dataSnapshot.child("donatorcontact").getValue();
                don_add=(String)dataSnapshot.child("donatoradd").getValue();
                don_type=(String)dataSnapshot.child("dtype").getValue();
                don_method=(String)dataSnapshot.child("dmethod").getValue();
                don_amount=(String)dataSnapshot.child("donatoramount").getValue();

                post_donator_name.setText(don_name);
                        post_donator_contact.setText(dona_contact);
                post_donator_add.setText(don_add);
                        post_donation_type.setText(don_type);

                post_donation_method.setText(don_method);
                        post_donation_amount.setText(don_amount);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      //  Toast.makeText(CollectSingle.this,mPost_key,Toast.LENGTH_LONG).show();

        google_map=(Button) findViewById(R.id.gmap);
        don_contact=(Button) findViewById(R.id.phonecall);
        google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CollectSingle.this,MapsActivity.class));
            }
        });

        don_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                //Toast.makeText(CollectSingle.this,"dhukse", Toast.LENGTH_LONG).show();
                intent.setData(Uri.parse("tel:"+dona_contact));
                startActivity(intent);
            }
        });
    }

    public void onBackPressed()
    {
        startActivity(new Intent(CollectSingle.this,CollectDonate.class));
    }


}
