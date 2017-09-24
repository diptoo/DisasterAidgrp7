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

public class BlogSingleActivity extends AppCompatActivity {
    private ImageButton edit_image;
    private String mPost_key=null;
    private DatabaseReference mDatabase;
    private Query mCur;
    private StorageReference mStorage;
    private Uri mImageUri=null;// DIRECTORY OF DATA
    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle,mBlogSingleDesc;
    private Button mRemoveBtn,mEditPost;
    public static final int GALLERY_REQUEST=1;//IMAGE FROM GALLERY, REQST CODE
    private FirebaseAuth mAuth;
    String post_title,post_desc,post_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");

        mAuth=FirebaseAuth.getInstance();
         mPost_key = getIntent().getExtras().getString("blog_id");
       //layout
        mBlogSingleImage=(ImageView) findViewById(R.id.singleimage);
        mBlogSingleTitle=(TextView) findViewById(R.id.singletitle);
        mBlogSingleDesc=(TextView) findViewById(R.id.singledesc);
        mRemoveBtn=(Button) findViewById(R.id.button4);
        mEditPost=(Button) findViewById(R.id.edit_post);
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
                Picasso.with(BlogSingleActivity.this).load(post_image).into(mBlogSingleImage);
                //jevabe current user id pabo
                //post uid er sathe match korlee sudhu matro remove button dekhte pabo and remove korte parbo


                if(mAuth.getCurrentUser().getUid().equals(post_uid))
                {
                    mRemoveBtn.setVisibility(View.VISIBLE);
                    mEditPost.setVisibility(View.VISIBLE);
                }
                mEditPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(BlogSingleActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView=inflater.inflate(R.layout.edit_post,null);
                        dialogBuilder.setView(dialogView);

                        final EditText edit_title=(EditText) dialogView.findViewById(R.id.edittitle);
                        final EditText edit_desc=(EditText) dialogView.findViewById(R.id.editdesc);
                        edit_image=(ImageButton) dialogView.findViewById(R.id.editimage);
                        final Button Edit_update=(Button)  dialogView.findViewById(R.id.update_btn);

                        edit_title.setText(post_title);
                        edit_desc.setText(post_desc);
                        Picasso.with(BlogSingleActivity.this).load(post_image).into(edit_image);
                        edit_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // THIS FUNCTION IS FOR SELECTION OF IMAGE
                                Toast.makeText(BlogSingleActivity.this,"dhukse",Toast.LENGTH_LONG).show();
                                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT); // USER K CHOOSE KORTE DAOA KON IMG SELECT KORBE
                                galleryIntent.setType("image/*"); // ONLY IMAGES NO VIDEO
                                startActivityForResult(galleryIntent,GALLERY_REQUEST);
                                //CALLS onActivityResult LAST A

                            }
                        });
                        Edit_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String new_title=edit_title.getText().toString().trim();
                                String new_desc=edit_desc.getText().toString().trim();

                                System.out.println(new_title);
                                mDatabase.child(mPost_key).child("title").setValue(new_title);
                                mDatabase.child(mPost_key).child("desc").setValue(new_desc);

                                if(!TextUtils.isEmpty(new_title)&&!TextUtils.isEmpty(new_desc)&& mImageUri!=null) {
                                    StorageReference filepath = mStorage.child("Blog Images").child(mImageUri.getLastPathSegment());

                                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            //STORAGE THEKE RETRIEVE KORA IMAGE
                                            @SuppressWarnings("VisibleForTests") final Uri downloadUri = taskSnapshot.getDownloadUrl();//RETRIEVE THE IMAGE
                                            //STORAGE TO DATABASE
                                            mDatabase.child(mPost_key).child("image").setValue(downloadUri.toString());// image
                                            startActivity(new Intent(BlogSingleActivity.this,blog_app.class));
                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(BlogSingleActivity.this, "image null", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(BlogSingleActivity.this,blog_app.class));
                                }
                            }
                        });

                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();



                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //remove post er function
        mRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //key dlt korle all dlt hoa jabe
                mDatabase.child(mPost_key).removeValue();
                Intent mainIntent=new Intent(BlogSingleActivity.this,blog_app.class);
                startActivity(mainIntent);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //THIS FUNCTION IS FOR SET IMAGE INTO THE IMAGE BUTTON
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK)
        {
            mImageUri=data.getData();
            //getData() is URI containing a directory of data (vnd.android.cursor.dir/*) from which to pick an item.
            //J DATA PICK KORLAM SETA mImageUri TE SAVE KORLAM
            edit_image.setImageURI(mImageUri); // IMAGE BUTTON A IMAGE SET KORLAM
        }


    }

}
