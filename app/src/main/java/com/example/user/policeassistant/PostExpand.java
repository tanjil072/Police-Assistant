package com.example.user.policeassistant;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

public class PostExpand extends AppCompatActivity {

    TextView CriminalName,Father,Mother,PresentAddress,Description,PermanentAddress,Rewards;
    private ImageView Criminalpic;
    String CName,Fname,Mname,Dst,description,permanentAddress,rewards,title;
    ImageButton download;
    public String url;

    Drawable drawable;
    Bitmap bitmap;
    String ImagePath;
    Uri URI;

    FirebaseStorage storage1 = FirebaseStorage.getInstance();
    StorageReference DownRef1 = storage1.getReferenceFromUrl("gs://police-assistant-d85ca.appspot.com/PostImage");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_expand);

        CriminalName=findViewById(R.id.nameboxcrim);
        Father=findViewById(R.id.nameboxfath);
        Mother=findViewById(R.id.nameboxmoth);
        PresentAddress=findViewById(R.id.nameboxpreadd);
        Description=findViewById(R.id.nameboxdescrip);
        PermanentAddress=findViewById(R.id.nameboxperadd);
        Rewards=findViewById(R.id.nameboxrewards);
        Criminalpic=findViewById(R.id.criminalpic);
        download=findViewById(R.id.downloadpic);


        Fname=getIntent().getStringExtra("father");
        Dst=getIntent().getStringExtra("PresentAddress");
        description=getIntent().getStringExtra("Description");
        CName=getIntent().getStringExtra("name");
        Mname=getIntent().getStringExtra("mother");
        permanentAddress=getIntent().getStringExtra("PermanentAddress");
        rewards=getIntent().getStringExtra("rewards");
        title=getIntent().getStringExtra("Title");

        CriminalName.setText(CName);
        Father.setText(Fname);
        PresentAddress.setText(Dst);
        Description.setText(description);
        Mother.setText(Mname);
        PermanentAddress.setText(permanentAddress);
        Rewards.setText(rewards);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String[] parts = user.getEmail().toString().split("@");
        String img = parts[0];
        String imageDown=img+title;


        try{
            if(title.isEmpty())
            {

                title="NO_IMAGE.jpg";
            }
        DownRef1.child(title).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                url= uri.toString();
                Glide.with(getApplicationContext()).load(url).into(Criminalpic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error getting image in Post expanded",Toast.LENGTH_SHORT).show();
        }


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
        startActivity(intent);
        finish();
    }
}
