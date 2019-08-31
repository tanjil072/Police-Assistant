package com.example.user.policeassistant;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    String[] districtNames;
    private Button SignupSignin;
    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private EditText EditTextFullname;
    private EditText EditTextUsername;
    private EditText EditTextPhone;
    private ImageView uploadImg;
    private EditText Repassword;
    private EditText Address;
    private Spinner District;
    private Button SignupSignup;
    private FirebaseUser currentUser;
    private DatabaseReference reference;
    private String SplitEmail;
    private EditText date;
    private EditText month;
    private EditText year;
    private Button genderMale;
    private Button genderFemale;
    private ImageButton calendarButton;
    private CheckBox checker;
    public String Dates;



    static int PReqCode=1;
    static int REQUESCODE=1;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Uri pickedImageUri;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    StorageReference storageRef = storage.getReferenceFromUrl("gs://police-assistant-d85ca.appspot.com/");


    public static FirebaseAuth firebaseAuth;

    ProgressDialog pd;

    private Spinner spinnerid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

       Repassword=findViewById(R.id.repass);
        districtNames=getResources().getStringArray(R.array.districtNames);
        spinnerid= findViewById(R.id.spinnerId);
        ArrayAdapter <String> adapter=new ArrayAdapter<String>(this,R.layout.sub_activity_spinner,R.id.spinnerText,districtNames);
        spinnerid.setAdapter(adapter);
        SignupSignin=findViewById(R.id.buttonSignupSignInId);
        SignupSignup=findViewById(R.id.buttonSignupSignupId);
        EditTextFullname=findViewById(R.id.EditTextFullnameId);
        Address=findViewById(R.id.editAddressDetails);
        uploadImg=findViewById(R.id.uploadImg);
        District=findViewById(R.id.spinnerId);
        checker=findViewById(R.id.checkboxSignup);
        EditTextUsername=findViewById(R.id.EditTextUsernameId);
        reference=FirebaseDatabase.getInstance().getReference("Users");
        EditTextEmail=findViewById(R.id.editTextEmail);
        EditTextPassword=findViewById(R.id.editTextPassword);
        EditTextPhone=findViewById(R.id.phoneID);
        date=findViewById(R.id.date);
        month=findViewById(R.id.month);
        year=findViewById(R.id.year);
        genderMale=findViewById(R.id.gendermale);
        genderFemale=findViewById(R.id.genderfemale);
        calendarButton=findViewById(R.id.calendarButton);
        Address=findViewById(R.id.editAddressDetails);


        firebaseAuth=FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        pd.setMessage("Registering User..");

        uploadImg.setOnClickListener(new View.OnClickListener() {
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

        SignupSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                SignupActivity.this.finish();
            }
        });

        SignupSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try{

                   SignUp();
                   upload();


               }catch (Exception e){
                   Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
               }
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });

        genderMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderMale.setEnabled(true);
                genderFemale.setEnabled(false);

            }
        });

        genderFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderMale.setEnabled(false);
                genderFemale.setEnabled(true);
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String currentDateString= Integer.toString(c.get(Calendar.DATE));
        String currentMonthString= Integer.toString(c.get(Calendar.MONTH)+1);
        String currentYearString= Integer.toString(c.get(Calendar.YEAR));

        Dates=currentDateString+"."+currentMonthString+"."+currentYearString;

        TextView dateText=findViewById(R.id.date);
        dateText.setText(currentDateString);

        TextView monthText=findViewById(R.id.month);
        monthText.setText(currentMonthString);

        TextView yearText=findViewById(R.id.year);
        yearText.setText(currentYearString);
    }

    void upload()
    {
        if(pickedImageUri != null) {
            pd.show();
            String U=EditTextEmail.getText().toString();

            String[] parts = U.split("@");
            final String img = parts[0]; // 004
            StorageReference childRef = storageRef.child("ProfilePicture").child(img);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(pickedImageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Registration Successful!!Verification Email Send",Toast.LENGTH_LONG).show();
                    pd.dismiss();

                    EditTextFullname.setText("");
                    EditTextEmail.setText("");
                    EditTextUsername.setText("");
                    EditTextPassword.setText("");
                    spinnerid.setSelection(1);
                    Repassword.setText("");
                    EditTextPhone.setText("");

                    Intent intent=new Intent(getApplicationContext(),SignupActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(SignupActivity.this, "Registration Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });
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
            if(ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),"Please accept permission",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(SignupActivity.this,
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
            uploadImg.setImageURI(pickedImageUri);


        }
    }








    @Override
    public void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();

    }


    //Saving information to Firebase database

    public void SaveToDatabase(){
        String FullName=EditTextFullname.getText().toString().trim();
        String Username=EditTextUsername.getText().toString().trim();
        String Email=EditTextEmail.getText().toString().trim();
        String Password=EditTextPassword.getText().toString().trim();
        String District=spinnerid.getSelectedItem().toString();
        String Repass=Repassword.getText().toString();
        String Phone=EditTextPhone.getText().toString();
        String address=Address.getText().toString();



        String[] part =Email.split("@");
        SplitEmail=part[0];


        Information information=new Information(FullName,Password,Username,Email,District,Phone,address,Dates);


        reference.child(SplitEmail).setValue(information);

    }

//The whole signup function here.It will save email & password to firebase auth


    public void SignUp()
    {


        String emailString=EditTextEmail.getText().toString();
        String passwordString=EditTextPassword.getText().toString();
        String passRe=Repassword.getText().toString();


        if(passwordString.equals(passRe)  ){

            if(checker.isChecked())
            {
            if(pickedImageUri!=null) {
                firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {

                            if (task.isSuccessful()) {
                                SaveToDatabase();
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                       // Toast.makeText(getApplicationContext(),"Registration Successful!!Verification Email Send",Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Email Sending Failed",Toast.LENGTH_LONG).show();
                                    }
                                });


                            } else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "Email already registered.", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        } catch (Exception e) {

                            //Toast.makeText(getApplicationContext(),"Error!!",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }else{
                Toast.makeText(SignupActivity.this, "Select an image", Toast.LENGTH_SHORT).show();

            }
    }else{

                Toast.makeText(SignupActivity.this,"Please Accept Terms & Conditions",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(SignupActivity.this,"Password Not Matched!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}

