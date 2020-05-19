package com.example.user.policeassistant;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
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

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class PostExpand extends AppCompatActivity {

    TextView CriminalName,Father,Mother,PresentAddress,Description,PermanentAddress,Rewards;
    private ImageView Criminalpic;
    String CName,Fname,Mname,Dst,description,permanentAddress,rewards,title;
    ImageButton download,share;
    public String url;
    public String down;

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
        share=findViewById(R.id.sharepic);


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

        String[] parts = user.getEmail().split("@");
        String img = parts[0];
        final String imageDown=img+title;


        try{
            if(title.isEmpty())
            {

                title="NO_IMAGE.jpg";
            }
            DownRef1.child(title).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'PostImage/title'
                    url= uri.toString();
                    //down=url;
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
            Toast.makeText(getApplicationContext(),"Error in getting image",Toast.LENGTH_SHORT).show();
        }


        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //download();
                Toast.makeText(getApplicationContext(),"Downloading Image...",Toast.LENGTH_SHORT).show();
                try{
                    downloadfiles(PostExpand.this,imageDown,".jpg",DIRECTORY_DOWNLOADS,url);
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Image loading problem",Toast.LENGTH_SHORT).show();
                }

            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody="If you find any clue about this criminal please contact your nearest police station please.";
                String shareSub="Police Case";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Share Via"));
            }
        });

    }

    public void downloadfiles(Context context,String filename,String fileExtension,String dest,String url){
        DownloadManager downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(url);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,dest,filename+fileExtension);
        downloadManager.enqueue(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
        startActivity(intent);
        finish();
    }
}
