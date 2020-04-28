package com.example.image_resize;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editText;
    public static final int PICK_IMAGE = 1;
    Bitmap bitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        editText = findViewById(R.id.newSize);
    }

    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    public void saveImage(View view) {
        if(imageView.getDrawable()!=null) {
            imageView.buildDrawingCache();
            Bitmap toSave = imageView.getDrawingCache();
            MediaStore.Images.Media.insertImage(getContentResolver(), toSave, "img_height_"+imageView.getHeight(), null);

            Toast.makeText(getBaseContext(), "Image saved to Pictures", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "No picture", Toast.LENGTH_SHORT).show();
        }
    }

    public void resizeImage(View view){
        if (!editText.getText().toString().matches("")) {
            double q = 100;
            try {
                q = Integer.parseInt(editText.getText().toString());
                if(q>200) {
                    q = 200;
                    System.out.println("no");
                } else if (q<1) {
                    q = 1;
                    System.out.println("no");
                }
                q /= 100;
            } catch (NumberFormatException e) {
                System.out.println("Could not parse " + e);
            }

            int newHeight = (int) (imageView.getHeight() * q);
            int newWidth = (int) (imageView.getWidth() * q);

            try {
                Bitmap resized = Bitmap.createScaledBitmap(bitMap, newWidth, newHeight, true);
                imageView.setImageBitmap(resized);
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "No image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            if (data == null){
                Toast.makeText(getBaseContext(), "no image", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Uri imageUri = data.getData();
                bitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitMap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
