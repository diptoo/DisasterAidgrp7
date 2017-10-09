package com.example.user.firebasedemo;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.example.user.firebasedemo.PostActivity.GALLERY_REQUEST;

public class DonateSingleActivity extends AppCompatActivity {
    private ImageButton edit_image;
    private String mPost_key=null;
    private DatabaseReference mDatabase;
    private Query mCur;
    private StorageReference mStorage;
    private Uri mImageUri=null;// DIRECTORY OF DATA
    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle,mBlogSingleDesc;
    private Button mRemoveBtn,mEditPost;
    private Button mDonate;
    public static final int GALLERY_REQUEST=1;//IMAGE FROM GALLERY, REQST CODE
    private FirebaseAuth mAuth;
    String post_title,post_desc,post_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_single);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");


        mPost_key = getIntent().getExtras().getString("blog_id");

        //layout
        mBlogSingleImage=(ImageView) findViewById(R.id.singleimage);
        mBlogSingleTitle=(TextView) findViewById(R.id.singletitle);
        mBlogSingleDesc=(TextView) findViewById(R.id.singledesc);
        mDonate=(Button) findViewById(R.id.donate);

        mDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent SingleBlogIntent=new Intent(DonateSingleActivity.this,DonateInfo.class);
                SingleBlogIntent.putExtra("blog_id",mPost_key);
                startActivity(SingleBlogIntent);

               // startActivity(new Intent(DonateSingleActivity.this,DonateInfo.class));
            }
        });
        mStorage= FirebaseStorage.getInstance().getReference();

        //Toast.makeText(BlogSingleActivity.this,post_key,Toast.LENGTH_LONG).show();
        //blog er key er vitor data save
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //we ccan get the image
                //data retrieve kora firebase theke
                post_title=(String)dataSnapshot.child("title").getValue();
                post_desc=(String)dataSnapshot.child("desc").getValue();
                post_image=(String)dataSnapshot.child("image").getValue();
                String post_uid=(String) dataSnapshot.child("uid").getValue();
                /// data boshano layout a
                mBlogSingleTitle.setText(post_title);
                mBlogSingleDesc.setText(post_desc);
                ///picture layout a boshano
                Picasso.with(DonateSingleActivity.this).load(post_image).into(mBlogSingleImage);
                //jevabe current user id pabo
                //post uid er sathe match korlee sudhu matro remove button dekhte pabo and remove korte parbo






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
    public void onBackPressed()
    {
        startActivity(new Intent(DonateSingleActivity.this,DonateHome.class));
    }


}
