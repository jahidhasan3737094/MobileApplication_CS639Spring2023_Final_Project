package com.example.expenseplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText dobEditText;

    private ProgressBar progressBar;
    private RadioGroup genderRadioGroup;

    private RadioButton radioButtonRegisterGenderSelected;
    private EditText mobileEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private CheckBox termsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toast.makeText(RegisterActivity.this, "you can register now", Toast.LENGTH_LONG).show();

        // Get references to the views
        fullNameEditText = findViewById(R.id.editText_register_full_name);
        emailEditText = findViewById(R.id.editText_register_email);
        dobEditText = findViewById(R.id.editText_register_dob);
        mobileEditText = findViewById(R.id.editText_register_mobile);
        passwordEditText = findViewById(R.id.editText_register_password);
        confirmPasswordEditText = findViewById(R.id.editText_register_confirm_password);
        termsCheckBox = findViewById(R.id.checkBox_terms_conditions);
        progressBar = findViewById(R.id.progressBar);
        //Redio button
        genderRadioGroup = findViewById(R.id.radio_group_register_gender);
        genderRadioGroup.clearCheck();

        Button buttonRegister = findViewById(R.id.button_register);
        // Set click listener for register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                //get the entered data
                String fullName = fullNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String dob = dobEditText.getText().toString().trim();
                // int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
                //RadioButton selectedGenderRadioButton = findViewById(selectedGenderId);
                //String gender = selectedGenderRadioButton.getText().toString();
                String mobile = mobileEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();
                boolean termsChecked = termsCheckBox.isChecked();
                String textGender;
                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter your full name", Toast.LENGTH_LONG).show();
                    fullNameEditText.setError("full name is required");
                    fullNameEditText.requestFocus();

                } else if (TextUtils.isEmpty(email)) {

                    Toast.makeText(RegisterActivity.this, "Please Enter your email", Toast.LENGTH_LONG).show();
                    emailEditText.setError("email is required");
                    emailEditText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    Toast.makeText(RegisterActivity.this, "Please re-enter email", Toast.LENGTH_LONG).show();
                    emailEditText.setError("valid email is required");
                    emailEditText.requestFocus();

                } else if (TextUtils.isEmpty(dob)) {

                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    dobEditText.setError("Date of birth is required");
                    dobEditText.requestFocus();

                } else if (genderRadioGroup.getCheckedRadioButtonId() == -1) {

                    Toast.makeText(RegisterActivity.this, "Please Select your gender", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();

                } else if (TextUtils.isEmpty(mobile)) {

                    Toast.makeText(RegisterActivity.this, "Please enter your mobile number", Toast.LENGTH_LONG).show();
                    mobileEditText.setError("Mobile no is required");
                    mobileEditText.requestFocus();


                } else if (mobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter mobile no", Toast.LENGTH_LONG).show();
                    mobileEditText.setError("mobile no should be 10 digit");
                    mobileEditText.requestFocus();


                } else if (TextUtils.isEmpty(password)) {

                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    passwordEditText.setError("Password is required");
                    passwordEditText.requestFocus();

                } else if (password.length() < 6) {

                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    passwordEditText.setError("weak password");
                    passwordEditText.requestFocus();


                } else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm password", Toast.LENGTH_LONG).show();
                    confirmPasswordEditText.setError("Password confirmation is required");
                    confirmPasswordEditText.requestFocus();


                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    confirmPasswordEditText.setError("Password confirmation is required");
                    confirmPasswordEditText.requestFocus();
                } else if (!termsChecked) {
                    Toast.makeText(RegisterActivity.this, "Please agree to the Terms of Service and Privacy Policy", Toast.LENGTH_SHORT).show();
                    termsCheckBox.requestFocus();
                } else {

                    // Create a new user object
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(fullName, email, dob, textGender, mobile, password);


                }

            }
        });
    }

    private void registerUser(String fullName, String email, String dob, String textGender, String mobile, String password) {
        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser= mAuth.getCurrentUser();

                    //  Enter data into the database
                    ReadWriteUserInfo writeuserdetails= new ReadWriteUserInfo(fullName,dob,textGender,mobile);
                    //extracting data reference from database.
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference refProfile = database.getReference("Registered user");
                    refProfile.child(firebaseUser.getUid()).setValue(writeuserdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //send email verification
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegisterActivity.this,"User registered successfully",Toast.LENGTH_LONG).show();
                                //open user Profile
                                Intent intent=new Intent(RegisterActivity.this,UserProfileActivity.class);

                                //Prevent user returning from profile to register activity after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);
                                finish();

                            }else{
                                Toast.makeText(RegisterActivity.this,"Registration failed ,try again !",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });



                }else{
                    try {
                        throw task.getException();
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}