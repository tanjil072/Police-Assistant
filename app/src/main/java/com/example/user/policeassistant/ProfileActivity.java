package com.example.user.policeassistant;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView ProfileName,ProfileEmail,ProfilePhone,ProfilePassword;
    private CircleImageView ProfileImage;
    private DatabaseReference ProfileRef;
    private StorageReference storageRef;
    private Button ProfileEditButton;
    String SplitUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        ProfileName=findViewById(R.id.ProfileNameid);
        ProfileEmail=findViewById(R.id.ProfileEmailId);
        ProfilePhone=findViewById(R.id.ProfilePhoneId);
        ProfilePassword=findViewById(R.id.ProfilePasswordId);
        ProfileImage=findViewById(R.id.ProfilePhotoId);
        ProfileEditButton=findViewById(R.id.ProfileEditButtonId);
        ProfileRef= FirebaseDatabase.getInstance().getReference();
        storageRef= FirebaseStorage.getInstance().getReference();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        ProfileEmail.setText(email);

        String[] part =email.split("@");
        SplitUsername=part[0];

        ProfileRef.child("Users").child(SplitUsername).child("Phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Phone=dataSnapshot.getValue().toString();
                ProfilePhone.setText(Phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileRef.child("Users").child(SplitUsername).child("Password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Password=dataSnapshot.getValue().toString();
                ProfilePassword.setText(Password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileRef.child("Users").child(SplitUsername).child("FullName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Name=dataSnapshot.getValue().toString();
                ProfileName.setText(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try {
            storageRef.child("ProfilePicture").child(SplitUsername).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String ur = uri.toString();
                    Glide.with(getApplicationContext()).load(ur).into(ProfileImage);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }catch (Exception e)
        {

        }


        ProfileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(getApplicationContext(),ProfileEditActivity.class);

                Pair[] pairs=new Pair[3];
                pairs[0]=new Pair<View,String>(ProfileName,"nameTransition");
                pairs[1]=new Pair<View,String>(ProfilePassword,"PasswordTransition");
                pairs[2]=new Pair<View,String>(ProfilePhone,"PhoneTransition");
                ActivityOptions activityOptions=ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this,pairs);
                startActivity(intent,activityOptions.toBundle());


            }
        });








    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
        startActivity(intent);
    }
}
