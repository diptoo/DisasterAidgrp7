package com.example.user.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PostActivity extends AppCompatActivity {
    private ImageButton mSelectImage; // image from gallery
    private EditText mposttitle;
    private EditText mpostdesc;
    private Button msubmit;
    private Button mUploadButton;
    private ProgressDialog mProgressDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase,mDatabaseUser;
    private Uri mImageUri=null;// DIRECTORY OF DATA
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private ImageView mImageView;

public static final int GALLERY_REQUEST=1;//IMAGE FROM GALLERY, REQST CODE
    private static final int CAMERA_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth=FirebaseAuth.getInstance();
        mCurrentUser=mAuth.getCurrentUser();//assign current user
        mSelectImage=(ImageButton) findViewById(R.id.imagebutton);//GALLERY IMAGE
        mImageView=(ImageView) findViewById(R.id.imagebutton) ;
        mUploadButton=(Button) findViewById(R.id.submit2);
        mStorage= FirebaseStorage.getInstance().getReference();//Storage a j store hobe etar jonno root directory indicate kore
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");//DATABASE ROOT A STORE KORBO NA AJNNE ROOT A CHILD CREATE KORLAM THN NOW ADD KORBO
        mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());//current user er id te save hobe
        // ^Get a reference to our posts
        mProgressDialog= new ProgressDialog(this);

        mposttitle=(EditText)findViewById(R.id.posttitle); //TITLE
        mpostdesc=(EditText) findViewById(R.id.postdesc); //DESC
        msubmit=(Button) findViewById(R.id.submit);//SUBMIT



        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //  intent.setType("image/*");
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });


        //IMAGE SELECT A TAP KORLE KI HOBE
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // THIS FUNCTION IS FOR SELECTION OF IMAGE
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT); // USER K CHOOSE KORTE DAOA KON IMG SELECT KORBE
                galleryIntent.setType("image/*"); // ONLY IMAGES NO VIDEO
                startActivityForResult(galleryIntent,GALLERY_REQUEST);
                //CALLS onActivityResult LAST A

            }
        });
        //SUBMIT BUTTON A CLICK KORLE
        msubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startposting();



            }
        });
    }


//AFTER CLICK THE SUBMIT BUTTON.. image storage + database save .. others database a store direct
    private void startposting() {
        mProgressDialog.setMessage("posting to Blog...");
 mProgressDialog.show();
        final String title_val=mposttitle.getText().toString().trim();
        final String desc_val=mpostdesc.getText().toString().trim();

        //POST SUCCESS CHECK
        if(!TextUtils.isEmpty(title_val)&&!TextUtils.isEmpty(desc_val)&& mImageUri!=null)
        {
            //BLOG IMAGES FOLDER A PICS SAVE HOBE.. kothay save hobe pth dekhano..STORE KORA ALSO
            StorageReference filepath= mStorage.child("Blog Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //STORAGE THEKE RETRIEVE KORA IMAGE
                    @SuppressWarnings("VisibleForTests") final Uri downloadUri=taskSnapshot.getDownloadUrl();//RETRIEVE THE IMAGE
                    //STORAGE TO DATABASE
                    final DatabaseReference newPost=mDatabase.push(); //new key FOR EVERY NEW POST
                    final String postid=newPost.toString();
                    System.out.println("newPost  a dhukse  ");
                    //j user submit dibe sei user er name
                  //  mDatabaseUser=FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());//current user er id te save hobe
               //uporer comment line er madhome j id dia login kortesi seta peye jachi.. suppose mDatabaseUsers dipto er id er hoa gelo
//Attach a listener to read the data at posts reference
   //. A snapshot is a picture of the data at a particular database reference at a single point in tim
                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //NEW POST RANDOM KEY FOR EVERY NEW POST
                            System.out.println("Event listener a dhukse");
                            newPost.child("postid").setValue(postid);
                            newPost.child("title").setValue(title_val);//key er child er vitor data save
                            newPost.child("desc").setValue(desc_val);
                            newPost.child("image").setValue(downloadUri.toString());// image
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        startActivity(new Intent(PostActivity.this,blog_app.class));
                                    }
                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mProgressDialog.dismiss();

                }
            });
        }

    }

  /// CALLED FROM startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //THIS FUNCTION IS FOR SET IMAGE INTO THE IMAGE BUTTON
        super.onActivityResult(requestCode, resultCode, data);
    if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
    {
         mImageUri=data.getData();
        //getData() is URI containing a directory of data (vnd.android.cursor.dir/*) from which to pick an item.
        //J DATA PICK KORLAM SETA mImageUri TE SAVE KORLAM
        mSelectImage.setImageURI(mImageUri); // IMAGE BUTTON A IMAGE SET KORLAM
    }
    else  if (requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK)
    {
        mProgressDialog.setMessage("Uploading by capture...");
        mProgressDialog.show();
        Uri uri=data.getData();
        StorageReference filepath= mStorage.child("Photos").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Toast.makeText(UpImage.this,"Upload done",Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();
                @SuppressWarnings("VisibleForTests")  Uri downloadUri=taskSnapshot.getDownloadUrl();
                Picasso.with(PostActivity.this).load(downloadUri).fit().centerCrop().into(mImageView);

            }
        });
    }


    }
}
