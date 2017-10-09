package com.example.user.firebasedemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText nEmailField;
    private EditText nPasswordField;
    private TextView textViewSignup,text;
  private DatabaseReference mDatabaseUsers;
    private Button mLogInButton;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers= FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        nEmailField = (EditText) findViewById(R.id.usern);
        nPasswordField = (EditText) findViewById(R.id.pass);
        textViewSignup = (TextView) findViewById(R.id.textsign);




        mLogInButton = (Button) findViewById(R.id.signup);

        mLogInButton.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
       // text=(TextView)findViewById(R.id.textView8);
       // Typeface custom=Typeface.createFromAsset(getAssets(),"fonts/font.ttf");
       // text.setTypeface(custom);

    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(Login.this,firstPage.class));
    }

    @Override
    public void onClick(View view) {
        if(view == mLogInButton){

            startLogIn();
        }
        if (view == textViewSignup){
            //Toast.makeText(SignInActivity.this,"thulla",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Login.this, register_activity.class)); // DOESN'T WORK
        }
    }




    private void startLogIn() {

        final String email = nEmailField.getText().toString();
        final String password = nPasswordField.getText().toString();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter email!",Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Logging In, Please Wait...");
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()) {

                    Toast.makeText(Login.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    checkUserExist();

                   // Intent i=new Intent(Login.this,Homepage.class);
                  //  i.putExtra("Email",email);
                   // startActivity(i);
                }
                else {
                    Toast.makeText(Login.this, email, Toast.LENGTH_SHORT).show();
                    Toast.makeText(Login.this, password, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(Login.this, "Error Login", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void checkUserExist() {
        final String user_id=mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(Login.this,"pass wrong",Toast.LENGTH_LONG).show();
                if(dataSnapshot.hasChild(user_id))
                {
                    Intent mainIntent=new Intent(Login.this,blog_app.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else
                {
                    Intent setupIntent=new Intent(Login.this,SetupActivity.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}