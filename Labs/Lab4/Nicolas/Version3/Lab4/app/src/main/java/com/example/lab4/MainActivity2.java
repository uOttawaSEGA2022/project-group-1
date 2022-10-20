package com.example.lab4;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

    }
    String custom = "Null";

    public void SetTeamIcon(View view) {
        //Creating a Return intent to pass to the Main Activity
        Intent returnIntent = new Intent();

        //Figuring out which image was clicked
        ImageView selectedImage = (ImageView) view;

        //Adding stuff to the return intent
        if(custom != "Null"){
            System.out.println("notnull");
            returnIntent.putExtra("imageID", custom);
            setResult(RESULT_OK, returnIntent);
        }
        else {
            System.out.println("nddull");
            returnIntent.putExtra("imageID", selectedImage.getId());
            setResult(RESULT_OK, returnIntent);
        }
        //Finishing Activity and return to main screen!
        finish();
    }

    public void CustomImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 3);

        System.out.println(intent);
        System.out.println(intent.getData());
        setResult(RESULT_OK, intent);
        System.out.println("customiage");
        System.out.println(custom);
        //finish();
    }

    private static int RESULT_LOAD_IMAGE = 1;
    Intent test = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
        System.out.println(custom);
        custom = data.getData().toString();
        System.out.println(custom);
        System.out.println("test");
        System.out.println("test");
        System.out.println(resultCode);
        System.out.println(RESULT_OK);
        System.out.println(data);
        System.out.println(data.getData());
        //putExtra("imagePath", data.toString());
        setResult(RESULT_OK, data);
        //i.putExtra("imagePath", data.toString());

        System.out.println("te4st");

        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        ImageView avatarImage = findViewById(R.id.teamid05);
        avatarImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        System.out.println("endCode");
        System.out.println(custom);

        System.out.println(picturePath);
        Intent intent=new Intent();
        intent.putExtra("RESULT_STRING", picturePath);
        setResult(RESULT_OK, intent);
        finish();

    }//}
}
