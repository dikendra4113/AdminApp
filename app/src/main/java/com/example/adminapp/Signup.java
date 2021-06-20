package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminapp.utils.ECValidattion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Signup extends AppCompatActivity {
    private EditText ownerName;
    private EditText email;
    private EditText restName;
    private EditText restPhone;
    private EditText addressEt;
    private ProgressDialog loadingBar;
    private EditText cityEt;
    private EditText passwordEt;
    String otpId;

    private ImageView signup;
    static FirebaseAuth mAuth;
    DatabaseReference RootRef;
    private String currentdb ="Admins";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = findViewById(R.id.signinbg);
        ownerName = findViewById(R.id.fullname_et);
        email = findViewById(R.id.email_id_et);
        restName = findViewById(R.id.restaurant_name_et);
        restPhone = findViewById(R.id.phone_no_et);
        addressEt = findViewById(R.id.address_et);
        cityEt = findViewById(R.id.city_et);
        passwordEt = findViewById(R.id.editTextTextPassword);
        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }

       signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               signInWithPhone();
           }
       });

    }
    public void signInWithPhone(){
        String phoneNumber = restPhone.getText().toString();
        String fullName = restName.getText().toString();
        String password = passwordEt.getText().toString();
        String emailId = email.getText().toString();
        String city = cityEt.getText().toString();
        String address = addressEt.getText().toString();


        if(phoneNumber.isEmpty()){
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else if(city.isEmpty()){
            Toast.makeText(this, "Please Enter City Number", Toast.LENGTH_SHORT).show();
        }else if(address.isEmpty()){
            Toast.makeText(this, "Please Enter Address", Toast.LENGTH_SHORT).show();
        }
        else if(fullName.isEmpty()){
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(this, "Please Enter please enter password", Toast.LENGTH_SHORT).show();
        }else if(emailId.isEmpty()) {
            Toast.makeText(this, "Please Enter valid email id", Toast.LENGTH_SHORT).show();

        }else{
            ECValidattion.PassValidator v1 = new ECValidattion.PassValidator();
            String err = v1.validate(password);
            if(err!=null) {
                Toast.makeText(this, ""+err, Toast.LENGTH_SHORT).show();

            }else{

                loadingBar.setTitle("Authenticate Mobile Number");
                loadingBar.setMessage("Please Wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                validateUser(phoneNumber,fullName,password,emailId);


                // OnVerificationStateChangedCallbacks
            }
        }

    }




    private void validateUser(final String phoneNumber, final String fullName, final String password, final String emailId) {

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.child("Admins").child(phoneNumber).exists()){
                    //Store new User to DataBAse
                    phoneAuthentication(phoneNumber,fullName,emailId,password);
                    //  saveUserInDataBase(phoneNumber,fullName,emailId,password);


                }else{
                    Toast.makeText(Signup.this, "This "+phoneNumber+"is already Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(Signup.this, "Please Try Again Using another Phone Number", Toast.LENGTH_SHORT).show();

                    Intent intent =new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void phoneAuthentication(final String phoneNumber, final String fullName, final String emailId, final String password) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                Signup.this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential,phoneNumber, fullName, emailId, password);
                        saveUserInDataBase(phoneNumber, fullName, emailId, password);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(Signup.this, "Error:"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                    @Override
                    public void onCodeSent( String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpId = s;
                        super.onCodeSent(s, forceResendingToken);
                        Dialog dialog = new Dialog(Signup.this);
                        dialog.setContentView(R.layout.varify_popup);
                        final EditText otp = dialog.findViewById(R.id.otpEditTExt);
                        Button varifyButton = dialog.findViewById(R.id.otpButton);
                        varifyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String varificationCode = otp.getText().toString();
                                if(varificationCode.isEmpty())return;
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,varificationCode);
                                signInWithPhoneAuthCredential(credential,phoneNumber,fullName,emailId,password);
                            }
                        });
                        dialog.show();
                    }
                });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String phoneNumber, final String fullName, final String emailId, final String password) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInDataBase(phoneNumber, fullName, emailId, password);
                            startActivity(new Intent(Signup.this,MainActivity.class));
                            finish();
                            // ...
                        } else {
                            Toast.makeText(Signup.this, "Error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserInDataBase(String phoneNumber, String fullName, String emailId, String password){


        HashMap<String,Object> userData = new HashMap<>();
        userData.put("restaurant_phone",phoneNumber);
        userData.put("restaurant_owner",fullName);
        userData.put("email",emailId);
        userData.put("password",password);
        userData.put("address",addressEt.getText().toString());
        userData.put("city",cityEt.getText().toString());
        userData.put("verified","false");
        RootRef.child("Admins").child(phoneNumber).updateChildren(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "Congratulation You have Succefully Create an Account", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    }
                });

    }

}