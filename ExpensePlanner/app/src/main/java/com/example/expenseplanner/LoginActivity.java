package com.example.expenseplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText loginEmail,loginPassword;
    private FirebaseAuth authProfile;
    private TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Find the register text view and set its click listener
        registerText = findViewById(R.id.register_text);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginEmail=findViewById(R.id.email_input);
        loginPassword=findViewById(R.id.password_input);
        authProfile=FirebaseAuth.getInstance();

        //user login button

        Button buttonLogin= findViewById(R.id.login_button);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=loginEmail.getText().toString();
                String password=loginPassword.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "{Please enter the email ", Toast.LENGTH_LONG).show();
                    loginEmail.setError("email is required");
                    loginEmail.requestFocus();
                    

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Please Re-enter the email ", Toast.LENGTH_LONG).show();
                    loginEmail.setError("valid email is required");
                    loginEmail.requestFocus();
                    
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password ", Toast.LENGTH_LONG).show();
                    loginEmail.setError("password is required");
                    loginEmail.requestFocus();

                }else{
                    userLogin(email,password);
                }
            }
        });

    }

    private void userLogin(String email, String password) {
        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                    //open profile

                    //open user Profile
                    Intent intent=new Intent(LoginActivity.this,DashboardActivity.class);

                    //Prevent user returning from profile to register activity after registration
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();
                  //get instance of the current user
                    FirebaseUser firebaseUser=authProfile.getCurrentUser();
                    //check email is varified or not
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "you are logged in now", Toast.LENGTH_LONG).show();
                    }else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }


                }
                else {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        loginEmail.setError("User doesn't exist or no longer valid.please register agian ");
                        loginEmail.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            }



        });

    }

    private void showAlertDialog() {
        //set up the alert builder

        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not varified");
        builder.setMessage("please varity your email .you can not login without email verification  ");
        //open email app if user taps on continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //create alert dialog
        AlertDialog alertDialog=builder.create();

        //show the alert dialog
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser()!=null){
            Toast.makeText(LoginActivity.this,"Already logged in ",Toast.LENGTH_SHORT).show();
            ///start usr profileactivity
            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
            finish();

        }else{
            Toast.makeText(LoginActivity.this," you can login now ",Toast.LENGTH_SHORT).show();
        }
    }
}