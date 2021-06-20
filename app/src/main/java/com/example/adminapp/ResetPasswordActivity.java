package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText phonenumber,password;
    private ProgressDialog loadingBar;
    String otpId;
    static FirebaseAuth mAuth;
    DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ImageView resetBtn = findViewById(R.id.reset_bg);
        phonenumber= findViewById(R.id.phone_no);
        password = findViewById(R.id.password_et);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String phoneNumber = phonenumber.getText().toString();
        String newPassword = password.getText().toString();

        if(phoneNumber.isEmpty()){
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else if(newPassword.isEmpty()){
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else {
            loadingBar.setTitle("Authenticate Mobile Number");
            loadingBar.setMessage("Please Wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            validatePass(phoneNumber,newPassword);
        }
    }

    private void validatePass(final String phoneNumber, final String password) {

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").child(phoneNumber).exists()){
                    //Store new User to DataBAse
                    phoneAuthentication(phoneNumber,password);
                    //  saveUserInDataBase(phoneNumber,fullName,emailId,password);


                }else{
                    Toast.makeText(ResetPasswordActivity.this, "This "+phoneNumber+"is Not Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(ResetPasswordActivity.this, "Please Try Again Using another Phone Number", Toast.LENGTH_SHORT).show();

                    Intent intent =new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void phoneAuthentication(final String phoneNumber, final String password) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                ResetPasswordActivity.this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential,phoneNumber, password);
                        saveUserInDataBase(phoneNumber,password);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(ResetPasswordActivity.this, "Error:"+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent( String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        otpId = s;
                        super.onCodeSent(s, forceResendingToken);
                        Dialog dialog = new Dialog(ResetPasswordActivity.this);
                        dialog.setContentView(R.layout.varify_popup);
                        final EditText otp = dialog.findViewById(R.id.otpEditTExt);
                        Button varifyButton = dialog.findViewById(R.id.otpButton);
                        varifyButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String varificationCode = otp.getText().toString();
                                if(varificationCode.isEmpty())return;
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,varificationCode);
                                signInWithPhoneAuthCredential(credential,phoneNumber,password);
                            }
                        });
                        dialog.show();
                    }
                });
    }

    private void saveUserInDataBase(String phoneNumber, String password) {
        HashMap<String,Object> userData = new HashMap<>();
        userData.put("phone",phoneNumber);

        userData.put("password",password);
        RootRef.child("Users").child(phoneNumber).updateChildren(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loadingBar.dismiss();

                            Toast.makeText(ResetPasswordActivity.this, "Congratulation You have Succefully Reset Password", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String phoneNumber, final String password) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserInDataBase(phoneNumber, password);
                            startActivity(new Intent(ResetPasswordActivity.this,MainActivity.class));
                            finish();
                            // ...
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Error:"+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}