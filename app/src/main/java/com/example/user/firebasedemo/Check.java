package com.example.user.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.Map;

public class Check extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Query mCur;
    private TextView mName;
    private TextView mdept;
    private TextView mUni;
    private TextView mContact;
    private FirebaseAuth mAuth;
    private EditText group_name,leader,address,contact;
    private Button mEditProfile;
    String key=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        BottomNavigationView bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add:
                       // Toast.makeText(BlogSingleActivity.this,"clicked",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Check.this,PostActivity.class));
                        break;
                    case R.id.icon_home:
                       // Toast.makeText(BlogSingleActivity.this,"home",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Check.this,blog_app.class));
                        break;
                    case R.id.action_person:
                        startActivity(new Intent(Check.this,Check.class));
                        break;
                }
                return true;
            }
        });

        mAuth=FirebaseAuth.getInstance();

        mEditProfile=(Button) findViewById(R.id.edit_profile2);

        mName=(TextView) findViewById(R.id.textView9);
        mdept=(TextView) findViewById(R.id.textView11);
        mUni=(TextView) findViewById(R.id.textView13);
        mContact=(TextView) findViewById(R.id.textView15) ;





        String Cuid=mAuth.getCurrentUser().getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Info");
        mCur=mDatabase.orderByChild("uid").equalTo(Cuid);

        mCur.addChildEventListener(new ChildEventListener() {
            String mGroupName,lead,add,con;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             key=dataSnapshot.getKey();
              //  Toast.makeText(Check.this,key,Toast.LENGTH_LONG).show();

                FireApp newPost = dataSnapshot.getValue(FireApp.class);
                System.out.println("name: " + newPost.name);
                System.out.println("department: " + newPost.department);
                System.out.println("university: " + newPost.university);
                System.out.println("contact: " + newPost.contact);
                mName.setText(newPost.name);
                mdept.setText(newPost.department);
                mUni.setText(newPost.university);
                mContact.setText(newPost.contact);

                mGroupName=newPost.name;
                lead=newPost.department;
                add=newPost.university;
                con=newPost.contact;


                mEditProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(Check.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView=inflater.inflate(R.layout.edit_profile,null);
                        dialogBuilder.setView(dialogView);


                        group_name=(EditText) dialogView.findViewById(R.id.editText);
                        leader=(EditText) dialogView.findViewById(R.id.editText2);
                        address=(EditText) dialogView.findViewById(R.id.editText3);
                        contact=(EditText) dialogView.findViewById(R.id.editcontact);

                        group_name.setText(mGroupName);
                        leader.setText(lead);
                        address.setText(add);
                        contact.setText(con);

                        final Button Edit_update=(Button)  dialogView.findViewById(R.id.button5);
                        Edit_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(Check.this,"edit update",Toast.LENGTH_LONG).show();
                                String new_group=group_name.getText().toString().trim();
                                String new_leader=leader.getText().toString().trim();
                                String new_address=address.getText().toString().trim();
                                String new_contact=contact.getText().toString().trim();

                                mDatabase.child(key).child("name").setValue(new_group);
                                mDatabase.child(key).child("department").setValue(new_leader);
                                mDatabase.child(key).child("university").setValue(new_address);
                                mDatabase.child(key).child("contact").setValue(new_contact);

                                startActivity(new Intent(Check.this,Check.class));
                            }
                        });


                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();

                    }
                });
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

    public void onBackPressed()
    {
        startActivity(new Intent(Check.this,blog_app.class));
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
            startActivity(new Intent(Check.this,PostActivity.class));
        }
        if(item.getItemId()==R.id.action_logout)
        {
            logout();
        }
        if(item.getItemId()==R.id.myPost)
        {
            startActivity(new Intent(Check.this,ProfilePage.class));
        }
        if(item.getItemId()==R.id.member_details)
        {
            startActivity(new Intent(Check.this,MemberDetails.class));
        }
        if(item.getItemId()==R.id.my_profile)
        {
            startActivity(new Intent(Check.this,Check.class));
        }
        if(item.getItemId()==R.id.donation_list)
        {
            startActivity(new Intent(Check.this,CollectDonate.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        mAuth.signOut();
    }
}
