package com.example.user.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MemberDetails extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Query mCur;
    private TextView acc_name;
    private TextView acc_contact;
    private TextView jsc_name;
    private TextView jsc_contact;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add:
                        // Toast.makeText(BlogSingleActivity.this,"clicked",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MemberDetails.this,PostActivity.class));
                        break;
                    case R.id.icon_home:
                        // Toast.makeText(BlogSingleActivity.this,"home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MemberDetails.this,blog_app.class));
                        break;
                    case R.id.action_person:
                        startActivity(new Intent(MemberDetails.this,Check.class));
                        break;
                }
                return true;
            }
        });



        mAuth=FirebaseAuth.getInstance();
        acc_name=(TextView) findViewById(R.id.accn);
        acc_contact=(TextView) findViewById(R.id.accc);
        jsc_name=(TextView) findViewById(R.id.jscn);
        jsc_contact=(TextView) findViewById(R.id.jscc) ;
        String Cuid=mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Info");
        mCur=mDatabase.orderByChild("uid").equalTo(Cuid);

        mCur.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FireApp newPost = dataSnapshot.getValue(FireApp.class);
                System.out.println("name: " + newPost.name);
                System.out.println("department: " + newPost.department);
                System.out.println("university: " + newPost.university);
                System.out.println("contact: " + newPost.contact);
                acc_name.setText(newPost.accname);
                acc_contact.setText(newPost.accontact);
                jsc_name.setText(newPost.jscname);
                jsc_contact.setText(newPost.jsccontact);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        //menu resource file read kore
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_add)
        {
            startActivity(new Intent(MemberDetails.this,PostActivity.class));
        }
        if(item.getItemId()==R.id.action_logout)
        {
            logout();
        }
        if(item.getItemId()==R.id.myPost)
        {
            startActivity(new Intent(MemberDetails.this,ProfilePage.class));
        }
        if(item.getItemId()==R.id.member_details)
        {
            startActivity(new Intent(MemberDetails.this,MemberDetails.class));
        }
        if(item.getItemId()==R.id.my_profile)
        {
            startActivity(new Intent(MemberDetails.this,Check.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        mAuth.signOut();
    }
}
