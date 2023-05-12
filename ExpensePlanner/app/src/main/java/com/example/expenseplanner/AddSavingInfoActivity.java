package com.example.expenseplanner;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class AddSavingInfoActivity extends AppCompatActivity {

    private EditText mBankNameEditText;
    private EditText mBalanceEditText;
    private TextView mTotalBalanceTextView;
    private TextView mBankInfoTextView;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_saving_info);

        mBankNameEditText = findViewById(R.id.bank_name_text);
        mBalanceEditText = findViewById(R.id.balance_edittext);
        mTotalBalanceTextView = findViewById(R.id.balance_amount_textview);
        mBankInfoTextView = findViewById(R.id.bank_info_textview);

        // Get a reference to the Firebase database
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("bank_details");

        // Set a listener for the "add" button
        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBankDetails();
            }
        });

        // Set a listener for changes to the Firebase database
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalBalance = 0;
                StringBuilder bankInfo = new StringBuilder();

                // Loop through all the bank details in the Firebase database
                for (DataSnapshot bankSnapshot : dataSnapshot.getChildren()) {
                    BankDetails bankDetails = bankSnapshot.getValue(BankDetails.class);
                    totalBalance += bankDetails.getBalance();
                    bankInfo.append(bankDetails.getBankName()).append("\n");
                }

                // Update the UI with the total balance and bank name list
                mTotalBalanceTextView.setText(String.format(Locale.getDefault(), "$%.2f", totalBalance));
                mBankInfoTextView.setText(bankInfo.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AddSavingInfoActivity", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void addBankDetails() {
        String bankName = mBankNameEditText.getText().toString().trim();
        String balanceString = mBalanceEditText.getText().toString().trim();
        if (TextUtils.isEmpty(bankName)) {
            Toast.makeText(this, "Please enter a bank name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(balanceString)) {
            Toast.makeText(this, "Please enter a balance.", Toast.LENGTH_SHORT).show();
            return;
        }
        double balance = Double.parseDouble(balanceString);
        if (balance <= 0) {
            Toast.makeText(this, "Please enter a positive balance.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a new ID for the bank details in the Firebase database
        String id = mDatabaseRef.push().getKey();

        // Create a new BankDetails object and save it to the Firebase database
        BankDetails bankDetails = new BankDetails(id, bankName, balance);
        mDatabaseRef.child(id).setValue(bankDetails);

        // Clear the input fields
        mBankNameEditText.getText().clear();
        mBalanceEditText.getText().clear();


    }
}