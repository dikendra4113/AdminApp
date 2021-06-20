package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    NavigationView nav;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    CardView scan_cardView,bill_cardView,order_cardView,seats_cardView,profile_cardView,party_cardView;
    public static String phone,hotel;
    ImageView profile_images;
    String image,name;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        View inflatedView = getLayoutInflater().inflate(R.layout.nav_header, null);
       // profile_images =  inflatedView.findViewById(R.id.header_image);
        nav = findViewById(R.id.nav_menu);
        final TextView nav_username = findViewById(R.id.nav_username);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        final Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        hotel = intent.getStringExtra("hotel");
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.child("image").exists())
                    {
                        image = dataSnapshot.child("image").getValue().toString();
                        name = dataSnapshot.child("fullname").getValue().toString();
                        View v;
//                        Picasso.get().load(image).placeholder(R.drawable.profile).into(profile_images);
//                        Picasso.get().load(image).into(profile_images);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
//                    case R.id.menu_home:
//                        Intent intent = new Intent(getApplicationContext(),UserActivity.class);
//                        intent.putExtra("phone",phone);
//                        startActivity(intent);
//                        Toast.makeText(HomeActivity.this, "Home Page Open", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//                    case R.id.menu_profile:
//                        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
//                        startActivity(i);
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
//
//                    case R.id.menu_setting:
//                        Intent intent2 = new Intent(getApplicationContext(),OrderViewActivity.class);
//                        startActivity(intent2);
//                        Toast.makeText(HomeActivity.this, "Setting Page Open", Toast.LENGTH_SHORT).show();
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;

                    case R.id.menu_logout:

                        Intent intent1 = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Paper.book().destroy();
                        finish();
                        break;

                }
                return false;
            }
        });


//
//        //++++++++++++++++++++++++++++++++++++++CardViews++++++++++++++++++++++++++++++++++++++++++++++++++++
        scan_cardView = findViewById(R.id.scan_cardView);
        bill_cardView = findViewById(R.id.bill_cardView);
        order_cardView = findViewById(R.id.order_cardView);
        seats_cardView = findViewById(R.id.seats_cardView);
        profile_cardView = findViewById(R.id.profile_cardView);
        party_cardView = findViewById(R.id.party_cardView);

        scan_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SeatGenActivity.class);
                startActivity(intent);
            }
        });

        order_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),OrdersActivity.class);
                startActivity(intent1);
            }
        });

        profile_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),UserViewActivity.class);
                startActivity(intent1);
            }
        });
        seats_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),SeatActivity.class);
                startActivity(intent1);
            }
        });
        party_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),AddProductActivity.class);
                startActivity(intent1);
            }
        });
        bill_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),EarningsActivity.class);
                startActivity(intent1);
            }
        });

    }
}