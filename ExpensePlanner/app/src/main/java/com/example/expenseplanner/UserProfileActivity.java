package com.example.expenseplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome,textViewName,textViewEmail,textViewDob,textViewGender,textViewMobile;
    private ProgressBar progressBar;
    private String fullname,email,dob,gender,mobile;
    private ImageView imageView;
    private Button logoutButton;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        // Set up the ActionBar
       /* ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Home");
        }*/
        textViewWelcome=findViewById(R.id.text_view_show_welcome);
        textViewName=findViewById(R.id.textView_show_full_name);
        textViewEmail=findViewById(R.id.textView_show_email);
        textViewDob=findViewById(R.id.textView_show_dob);
        textViewGender=findViewById(R.id.textView_show_gender);
        textViewMobile=findViewById(R.id.textView_show_mobile);
        progressBar=findViewById(R.id.progress_bar);

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        if(firebaseUser==null){
            Toast.makeText(UserProfileActivity.this,"Something went wrong ,user information are not available..",Toast.LENGTH_LONG).show();


        }else{
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }


        // Find the logout button and set its click listener
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProfile.signOut();
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showUserProfile( FirebaseUser firebaseUser) {
        String userId=firebaseUser.getUid();
        //Extracting user reference from database for registered user

        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered user");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserInfo readUserDetails=snapshot.getValue(ReadWriteUserInfo.class);
                if(readUserDetails!=null){
                    fullname=readUserDetails.name;
                    email=firebaseUser.getEmail();
                    dob=readUserDetails.dob;
                    gender=readUserDetails.gender;
                    mobile=readUserDetails.mobile;

                    textViewWelcome.setText("Welcome"+fullname+"!");
                    textViewName.setText(fullname);
                    textViewEmail.setText(email);
                    textViewDob.setText(dob);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);


                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this,"Something went wrong ..",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}