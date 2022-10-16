package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnOpenInGoogleMaps(View view) {
        EditText teamAddress = (EditText) findViewById(R.id.teamLocation);

        // Create a URI from an intent string.
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + teamAddress.getText());

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    public void OnSetAvatarButton(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }
/*    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) return;

        //Getting the Avatar Image we show to our users
        ImageView avatarImage = (ImageView) findViewById(R.id.avatarImage);

        //Figuring out the correct image
        String drawableName = "ic_logo_00";
        switch (data.getIntExtra("imageID",R.id.teamid01)) {
            case R.id.teamid01:
                System.out.println("test0");
                drawableName = "ic_logo_01";
                break;
            case R.id.teamid02:
                drawableName = "ic_logo_02";
                System.out.println("test1");
                break;
            case R.id.teamid03:
                drawableName = "ic_logo_03";
                System.out.println("test2");
                break;
            case R.id.teamid04:
                drawableName = "ic_logo_04";
                System.out.println("test#");
                break;
            case R.id.teamid05:
                drawableName = "ic_logo_05";
                System.out.println("test4");
                break;
            case R.id.teamid00:
                drawableName = "ic_logo_00";
                System.out.println("test5");
                break;
            default:
                drawableName = "ic_logo_00";
                System.out.println("test00");;
                break;
        }
        int resID = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        avatarImage.setImageResource(resID);
    }

  */
    private static int RESULT_LOAD_IMAGE = 1;
    Intent test = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            System.out.println("test");
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView avatarImage = findViewById(R.id.avatarImage);
            avatarImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }//}


}