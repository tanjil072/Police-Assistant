package com.example.user.policeassistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

private Button Signup;
private Button Signin;
private EditText user;
private  EditText password;
public  FirebaseAuth firebaseAuth1;
private FirebaseDatabase mDatabase;
private DatabaseReference mDb;
public static String obj;
private RadioGroup radioGroup;
private FirebaseUser muser;
private int RadioSelect;



public void SignIn(){



    final String username=user.getText().toString().trim();
     String pass=password.getText().toString().trim();

    firebaseAuth1.signInWithEmailAndPassword(username,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                if(RadioSelect==1)
                {
                    FirebaseUser User=firebaseAuth.getCurrentUser();
                    if(User.isEmailVerified()){
                        Toast.makeText(MainActivity.this,"Logged In",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),NavigationActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Please Verify Your Email",Toast.LENGTH_SHORT).show();
                    }


                }
                else if(RadioSelect==2)
                {

                    Toast.makeText(MainActivity.this,username,Toast.LENGTH_SHORT).show();

                    /*Toast.makeText(MainActivity.this,"Logged In",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),SignupActivity.class);
                    startActivity(intent);*/
                }
                else{
                    Toast.makeText(MainActivity.this,"Please Select General or Admin",Toast.LENGTH_SHORT).show();
                }


            }else{

                Toast.makeText(MainActivity.this,"Email or Password Error!",Toast.LENGTH_SHORT).show();
            }
        }
    });

}
    public static FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Signup=findViewById(R.id.buttonSignupID);
        Signin=findViewById(R.id.buttonSigninId);
        user=findViewById(R.id.usernameID);
        password=findViewById(R.id.passwordID);
        firebaseAuth1=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance();
        mDb=mDatabase.getReference();
        muser=firebaseAuth1.getCurrentUser();
        radioGroup=findViewById(R.id.RadioGroup);
        firebaseAuth=FirebaseAuth.getInstance();





        Signup.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v==Signup) {

                    Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }

            }
        }));


        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    SignIn();

                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Email or Password",Toast.LENGTH_SHORT).show();
                }

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.RadioGen)
                {
                    RadioSelect=1;

                }
                if(checkedId==R.id.RadioAdmin)
                {
                    RadioSelect=2;

                }
            }
        });


    }






    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

}
