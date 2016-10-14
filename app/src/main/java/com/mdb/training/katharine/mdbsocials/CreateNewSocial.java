package com.mdb.training.katharine.mdbsocials;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateNewSocial extends AppCompatActivity {

    private EditText name;
    private EditText date;
    private EditText description;
    private Button choosePic;
    private Button createSocial;
    private ArrayList<SocialsList.Social> socialsList;
    private Uri selectedImage;

    private FirebaseAuth mAuth;
    // private FirebaseDatabase mData;
    private DatabaseReference mDatabase;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_social);

        name = (EditText) findViewById(R.id.name);
        date = (EditText) findViewById(R.id.date);
        description = (EditText) findViewById(R.id.description);
        choosePic = (Button) findViewById(R.id.pic);
        createSocial = (Button) findViewById(R.id.create);


        mAuth = FirebaseAuth.getInstance();
        // mData = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSocial();
                startActivity(new Intent(CreateNewSocial.this, FeedActivity.class));
            }
        });

        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 0);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (resultCode == RESULT_OK) {
            selectedImage = imageReturnedIntent.getData();
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("socialPics/");


        }
    }




    public void createSocial() {
        String n = name.getText().toString();
        String d = date.getText().toString();
        String descrip = description.getText().toString();
        String author = mAuth.getCurrentUser().getEmail();
        ArrayList<String> interested = new ArrayList<>();
        SocialsList.Social social = new SocialsList.Social(n, author, descrip, d);
        Map<String, Object> post = new HashMap<>();
        post.put("name", n);
        post.put("author", author);
        post.put("description", descrip);
        post.put("date", d);
        post.put("interested", interested);
        post.put("uri", selectedImage);
        mDatabase.child("Socials").push().setValue(post);
    }
}
