package com.uottawa.seg2105.grp1.mealer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.uottawa.seg2105.grp1.mealer.R;

public class ImageCheck extends AppCompatActivity {
    //This custom String, will be the string that says where the photo is located
    // ex: "/storage/emulated/0/Pictures/Screenshots/Screenshot_20221014-003855.jpg"
    String custom = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_check);
    }
    public void CustomImage(View view) {
        //opens a Intent that searchs for a photo
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //changes custom to the name of the location
        custom = data.getData().toString();

        setResult(RESULT_OK, data);

        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        ImageView avatarImage = findViewById(R.id.imageView3);
        avatarImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        //returns the area that the picture is located (picturePath)
        Intent intent=new Intent();
        intent.putExtra("RESULT_STRING", picturePath);
        setResult(RESULT_OK, intent);
        finish();

    }


}