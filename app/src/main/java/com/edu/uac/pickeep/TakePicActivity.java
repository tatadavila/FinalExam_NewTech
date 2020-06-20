package com.edu.uac.pickeep;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class TakePicActivity extends AppCompatActivity {

    Button uploadBtn;
    ImageView picIV;

    private static final int CAMERA_REQUEST_CODE = 1;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);

        storageReference = FirebaseStorage.getInstance().getReference();

        uploadBtn = findViewById(R.id.uploadBtn);
        picIV = findViewById(R.id.picScreenIV);

        progressDialog = new ProgressDialog(this);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, CAMERA_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressDialog.setMessage("Uploading Image");
        progressDialog.show();

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            StorageReference filePath = storageReference.child("Photo").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    progressDialog.dismiss();

                    Toast.makeText(TakePicActivity.this, "Uploading finished", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}