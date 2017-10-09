package com.example.user.firebasedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class firstPage extends Activity implements View.OnClickListener{
    private Button collectButton;
    private Button donatetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first_page);

        Typeface myTypeFace=Typeface.createFromAsset(getAssets(),"abc.ttf");
        TextView ff=(TextView) findViewById(R.id.textView8);
        ff.setTypeface(myTypeFace);

        collectButton = (Button) findViewById(R.id.collect);
        donatetButton = (Button) findViewById(R.id.donate);

        collectButton.setOnClickListener(this);
        donatetButton.setOnClickListener(this);
    }
    @Override
    public void onBackPressed()
    {
        finishAffinity();
    }
    @Override
    public void onClick(View view) {
        if(view==collectButton) {

            startActivity(new Intent(firstPage.this, blog_app.class));
        }
        else if(view==donatetButton)
        {
            //Toast.makeText(firstPage.this,"Donate home a dhukhse",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(firstPage.this,GoogleSign.class));
        }
    }
}
