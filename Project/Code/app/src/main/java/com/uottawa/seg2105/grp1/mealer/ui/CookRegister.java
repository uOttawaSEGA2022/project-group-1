package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.uottawa.seg2105.grp1.mealer.R;

public class CookRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_register);
    }

    //opens the VoidCheck creator
    public void btnVoidCheck(View view){
        Intent intent = new Intent(getApplicationContext(), ImageCheck.class);
        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) return;
        //if it returns a "custom" string, then change the logo to the image
        if (data.getStringExtra("RESULT_STRING") != null) {
            //Use Data to get "custom" string that says the locaiton of photo
            String custom = data.getStringExtra("RESULT_STRING");

            //sets the image as the custom photo
            ImageView avatarImage = (ImageView) findViewById(R.id.imageView);
            avatarImage.setImageBitmap(BitmapFactory.decodeFile(custom));

        }
    }



}