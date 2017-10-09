package com.example.user.firebasedemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.example.user.firebasedemo.R.id.imageView;

public class CaptureImage extends AppCompatActivity {
    private Button mUploadButton;
    private StorageReference mStorage;
    private static final int CAMERA_REQUEST_CODE=1;
    private ProgressDialog mProgressDialog;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);
        mStorage= FirebaseStorage.getInstance().getReference();
        mUploadButton=(Button) findViewById(R.id.submit);
        mProgressDialog= new ProgressDialog(this);
        mImageView=(ImageView) findViewById(R.id.imagebutton) ;
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

              //  intent.setType("image/*");
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK)
        {
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();
            Uri uri=data.getData();
            StorageReference filepath= mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(UpImage.this,"Upload done",Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    @SuppressWarnings("VisibleForTests")  Uri downloadUri=taskSnapshot.getDownloadUrl();
                    Picasso.with(CaptureImage.this).load(downloadUri).fit().centerCrop().into(mImageView);

                }
            });
        }
    }
}
