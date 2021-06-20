package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminapp.model.UserDb;
import com.example.adminapp.utils.Prevelents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private ImageView signin;
    private TextView registerTextView,forgetPassword;
    private  TextView adminTextView,notAdminTextView,signup;
    private EditText loginMobileNumber,loginPassword;
    private ProgressDialog loadingBar;
    private CheckBox Rememberme;
    static FirebaseAuth mAuth;


    private String currentDb = "Admins";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signin = findViewById(R.id.signinbg);

        loginMobileNumber = findViewById(R.id.phone_no);
        loginPassword = findViewById(R.id.editTextPassword);
        loadingBar = new ProgressDialog(this);
        Rememberme = findViewById(R.id.remember_me);
        forgetPassword = findViewById(R.id.forgetpassword);
        signup = findViewById(R.id.signup_tv);




        mAuth = FirebaseAuth.getInstance();

        Paper.init(this);


        //signup navigation
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Signup.class);
                startActivity(intent);
            }
        });

        // AdminButton = findViewById(R.id.AdminButton);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();

            }

        });


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });



        String phoneKey = Paper.book().read(Prevelents.phoneKey);
        String passwordKey = Paper.book().read(Prevelents.passwordKey);
        if(phoneKey !="" && passwordKey != ""){
            if(!TextUtils.isEmpty(phoneKey) && !TextUtils.isEmpty(passwordKey)){
                pleaseWait();
                allowAccessToAccount(phoneKey,passwordKey);
            }
        }
    }

    private void loginUser() {

        String phoneNumber = loginMobileNumber.getText().toString();
        String password = loginPassword.getText().toString();

        if(phoneNumber.isEmpty()){
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
        }else if(password.isEmpty()){
            Toast.makeText(this, "Please Enter Correct Password", Toast.LENGTH_SHORT).show();
        }else {
            pleaseWait();
            allowAccessToAccount(phoneNumber,password);
        }

    }

    private void pleaseWait() {
        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    private void allowAccessToAccount(final String phoneNumber, final String password) {

        if(Rememberme.isChecked()){
            Paper.book().write(Prevelents.phoneKey,phoneNumber);
            Paper.book().write(Prevelents.passwordKey,password);

        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(currentDb).child(phoneNumber).exists() ){
                    //Allow login to

                    UserDb userData = snapshot.child(currentDb).child(phoneNumber).getValue(UserDb.class);
                    if(userData.getRestaurant_phone().equals(phoneNumber) && userData.getPassword().equals(password)){
                        if(currentDb.equals("Admins")){
                            String hotelName = snapshot.child(currentDb).child(phoneNumber).child("restaurant_owner").getValue().toString();
                            Toast.makeText(MainActivity.this, "Logged in Succesfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            intent.putExtra("phone",phoneNumber);
                            intent.putExtra("hotel",hotelName);
                            Prevelents.currentUser = userData;
                            Paper.book().write(Prevelents.restaurant,userData.getRestaurant_owner());
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Toast.makeText(MainActivity.this, "Please Enter Correct Password...", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }else {
                    Toast.makeText(MainActivity.this, "This "+phoneNumber+" is Not Exist as Admin Account", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}