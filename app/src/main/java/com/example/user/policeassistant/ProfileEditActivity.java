package com.example.user.policeassistant;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ProfileEditActivity extends AppCompatActivity {

    private StorageReference storageRef;
    private DatabaseReference ProfileRef;
    private EditText ProfileNameEdit, ProfilePhoneEdit, ProfilePasswordEdit,ProfileAddressEdit;
    private TextView ProfileEmailEdit;
    private CircleImageView ProfileImageEdit;
    DatabaseReference reference;
    private Button Save;
    String SplitUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        storageRef = FirebaseStorage.getInstance().getReference();
        ProfileEmailEdit = findViewById(R.id.ProfileEditEmail);
        ProfileNameEdit = findViewById(R.id.ProfileEditName);
        ProfilePhoneEdit = findViewById(R.id.ProfileEditPhone);
        ProfilePasswordEdit = findViewById(R.id.ProfileEditPassword);
        ProfileImageEdit=findViewById(R.id.ProfilePhotoIdEdit);
        ProfileAddressEdit=findViewById(R.id.ProfileAddressEdit);
        reference=FirebaseDatabase.getInstance().getReference("Users");
        Save=findViewById(R.id.SaveButton);

        ProfileRef= FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        ProfileEmailEdit.setText(email);

        String[] part = email.split("@");
        SplitUsername = part[0];

        ProfileRef.child("Users").child(SplitUsername).child("Phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Phone = dataSnapshot.getValue().toString();
                ProfilePhoneEdit.setText(Phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileRef.child("Users").child(SplitUsername).child("Password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Password=dataSnapshot.getValue().toString();
                ProfilePasswordEdit.setText(Password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileRef.child("Users").child(SplitUsername).child("FullName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Name=dataSnapshot.getValue().toString();
                ProfileNameEdit.setText(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileRef.child("Users").child(SplitUsername).child("Address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String address=dataSnapshot.getValue().toString();
                ProfileAddressEdit.setText(address);
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
                    Glide.with(getApplicationContext()).load(ur).into(ProfileImageEdit);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }catch (Exception e)
        {

        }


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    SaveInformation();
                    Toast.makeText(getApplicationContext(),"Profile Information Saved",Toast.LENGTH_SHORT).show();

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Saving Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    void SaveInformation()
    {
        String FullName=ProfileNameEdit.getText().toString().trim();
        String Phone=ProfilePhoneEdit.getText().toString().trim();
        String Password=ProfilePasswordEdit.getText().toString().trim();
        String Address=ProfileAddressEdit.getText().toString().trim();

        reference.child(SplitUsername).child("FullName").setValue(FullName);
        reference.child(SplitUsername).child("Phone").setValue(Phone);
        reference.child(SplitUsername).child("Password").setValue(Password);
        reference.child(SplitUsername).child("Address").setValue(Address);
    }

}
