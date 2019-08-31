package com.example.user.policeassistant;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;


public class Post extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;

    DatabaseReference db3;
    public int childcount;


    private ListView listView;
    private List<Information> list;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public String mEmailAddress;
    public String user;
    public String imagePath;

    String s="Posts";



    static int PReqCode=1;
    static int REQUESCODE=1;
    private EditText title;
    private EditText body;
    private EditText PresentAdd;
    private EditText Name;
    private EditText Father;
    private EditText Mother;
    private EditText PermanentAdd;
    private Button imview;
    private EditText Rewards;
    private Button post;
    public String val;
    public static int val2;
    private TextView selectedImagePath;


    Uri pickedImageUri;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference DownRef = storage.getReferenceFromUrl("gs://police-assistant-d85ca.appspot.com/PostImage");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);

        list=new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        title = findViewById(R.id.titlemain);
        body = findViewById(R.id.descripmain);
        PresentAdd=findViewById(R.id.preaddmain);
        PermanentAdd=findViewById(R.id.peraddmain);
        Name=findViewById(R.id.criminalmain);
        Father=findViewById(R.id.criminalfathermain);
        Mother=findViewById(R.id.criminalmothermain);
        imview=findViewById(R.id.uppicbutton);
        Rewards=findViewById(R.id.rewardmain);
        post=findViewById(R.id.postbutton);
        selectedImagePath=findViewById(R.id.uppicname);



        imview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=22)
                {
                    checkAndRequestForPermission();
                }else
                {
                   openGallery();

                }
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference();

        databaseReference2= FirebaseDatabase.getInstance().getReference();
        db3=FirebaseDatabase.getInstance().getReference("Posts");



        try {

            if (mFirebaseUser == null){
                //Not signed in, launch the Sign In Activity

                Toast.makeText(Post.this, "Null user", Toast.LENGTH_LONG).show();
                finish();
                return;
            }else {


                mEmailAddress = mFirebaseUser.getEmail();

                String[] parts = mEmailAddress.split("@");
                user=parts[0];


            }



           post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                   /* db3.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            childcount= (int) dataSnapshot.getChildrenCount();

                            Toast.makeText(getApplicationContext(),String.valueOf(childcount),Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/






                   final String Title = title.getText().toString();
                    String CriminalsName=Name.getText().toString();
                    String FathersName=Father.getText().toString();
                    String MothersName=Mother.getText().toString();
                    String PresentAddress=PresentAdd.getText().toString();
                    String PermanentAddress=PermanentAdd.getText().toString();
                    String Description = body.getText().toString();
                    String rewards=Rewards.getText().toString();

                   if(Title.isEmpty() || CriminalsName.isEmpty() || FathersName.isEmpty() || MothersName.isEmpty() || PresentAddress.isEmpty() || PermanentAddress.isEmpty() || Description.isEmpty() || rewards.isEmpty())
                   {
                       Toast.makeText(Post.this, "Please Fill all the informations properly.", Toast.LENGTH_LONG).show();
                   }else {

                       String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                       Information info = new Information(Title, CriminalsName, FathersName, MothersName, PresentAddress, PermanentAddress, Description, rewards + " XP", mydate,childcount+1);




                       databaseReference.child(s).push().setValue(info);
                       databaseReference2.child("User Posts").child(user).push().setValue(info);


                       Toast.makeText(Post.this, "Post Saved", Toast.LENGTH_LONG).show();

                       Name.setText("");
                       Father.setText("");
                       Mother.setText("");
                       PresentAdd.setText("");
                       PermanentAdd.setText("");
                       body.setText("");
                       Rewards.setText("");



                       upload();

                      Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                       startActivity(intent);
                   }







                }
            });
        }catch (Exception e)
        {
            Toast.makeText(Post.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        }



    void upload()
    {

        if(pickedImageUri != null) {


           /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String[] parts = user.getEmail().toString().split("@");
            final String img = parts[0];*/
            try{

            final String T = title.getText().toString();

            StorageReference childRef = DownRef.child(T);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(pickedImageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Post.this, "Post Saving Failed" + e, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e)
            {
                Toast.makeText(Post.this, "Post Saving Failed" + e, Toast.LENGTH_SHORT).show();
            }

        }
        else {

        }
    }



    //Upload Image


    private void openGallery() {

        Intent galleryIntent=new Intent((Intent.ACTION_GET_CONTENT));
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);

    }

    private void checkAndRequestForPermission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Post.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),"Please accept permission",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(Post.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }else{
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data !=null){


            pickedImageUri=data.getData();
            imagePath=pickedImageUri.getPath().toString();
            selectedImagePath.setText(imagePath);
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
        startActivity(intent);
        finish();
    }
}
