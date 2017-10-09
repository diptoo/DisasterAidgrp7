package com.example.user.firebasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonateInfo extends AppCompatActivity {
    private String mPost_key=null;
    private DatabaseReference mDatabase,mDatabaseDonator;
    String postuseruid;
    private Button mConfirm;
    private EditText dName,dContact,dAdd,dAmount;
    private Spinner donate_type,donate_method;
    String dtype,dmethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_info);

        dName=(EditText) findViewById(R.id.donator_name);
        dContact=(EditText) findViewById(R.id.donator_contact);
        dAdd=(EditText) findViewById(R.id.donator_address);
        dAmount=(EditText) findViewById(R.id.donator_amount);

        mConfirm=(Button) findViewById(R.id.button5);

        donate_type=(Spinner) findViewById(R.id.donate_type);
        donate_method=(Spinner) findViewById(R.id.donate_method);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mDatabaseDonator=FirebaseDatabase.getInstance().getReference().child("donator_info");

        mPost_key = getIntent().getExtras().getString("blog_id");

       // Toast.makeText(DonateInfo.this,mPost_key,Toast.LENGTH_SHORT).show();

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                postuseruid=(String)dataSnapshot.child("uid").getValue();

               // Toast.makeText(DonateInfo.this,postuseruid,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String donatorname=dName.getText().toString().trim();
                String donatorcontact=dContact.getText().toString().trim();
                String donatoradd=dAdd.getText().toString().trim();
                String donatoramount=dAmount.getText().toString();

                String dtype=donate_type.getSelectedItem().toString();
                String dmethod=donate_method.getSelectedItem().toString();

                final DatabaseReference newPost=mDatabaseDonator.push();


                newPost.child("donatorname").setValue(donatorname);
                newPost.child("donatorcontact").setValue(donatorcontact);
                newPost.child("donatoradd").setValue(donatoradd);
                newPost.child("postuserid").setValue(postuseruid);
                newPost.child("dtype").setValue(dtype);
                newPost.child("dmethod").setValue(dmethod);
                newPost.child("donatoramount").setValue(donatoramount);
                newPost.child("count").setValue("1");

                startActivity(new Intent(DonateInfo.this,DonateHome.class));
            }
        });

    }
}
