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

public class PlanExpenseActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private TextView totalBalanceTextView;
    private TextView mFoodExpenseTextView;
    private TextView mTravelExpenseTextView;
    private TextView mPaymentExpenseTextView;
    private TextView mEntertainmentExpenseTextView;

    private EditText mFoodExpenseEditText;
    private EditText mTravelExpenseEditText;
    private EditText mPaymentExpenseEditText;
    private EditText mEntertainmentExpenseEditText;
    double totalBalance = 0;
    private double mTotalAvailableBalance;
    private Button mGeneratePlanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_expense);

        // Get a reference to the Firebase database
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("bank_details");


        // Initialize UI elements
        mFoodExpenseEditText = findViewById(R.id.food_expense);
        mTravelExpenseEditText = findViewById(R.id.travel_expense);
        mPaymentExpenseEditText = findViewById(R.id.payment_expense);
        mEntertainmentExpenseEditText = findViewById(R.id.entertainment_ex);

        // Get references to the UI elements
        totalBalanceTextView = findViewById(R.id.total_available_balance_text_view);
        mFoodExpenseTextView = findViewById(R.id.food_expense_text_view);
        mTravelExpenseTextView = findViewById(R.id.travel_expense_text_view);
        mPaymentExpenseTextView = findViewById(R.id.payment_expense_text_view);
        mEntertainmentExpenseTextView = findViewById(R.id.entertainment_expense_text_view);



// Set a listener for changes to the Firebase database
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                // Loop through all the bank details in the Firebase database
                for (DataSnapshot bankSnapshot : dataSnapshot.getChildren()) {
                    BankDetails bankDetails = bankSnapshot.getValue(BankDetails.class);
                    totalBalance += bankDetails.getBalance();
                }

                // Calculate the default plan expenses
                double Result = totalBalance / 4;
                // Update the UI with the total balance
                totalBalanceTextView.setText(String.format(Locale.getDefault(), "$%.2f", totalBalance));

                if(TextUtils.isEmpty(mFoodExpenseEditText.getText())
                        || TextUtils.isEmpty(mTravelExpenseEditText.getText())
                        || TextUtils.isEmpty(mPaymentExpenseEditText.getText())
                        || TextUtils.isEmpty(mEntertainmentExpenseEditText.getText())) {

                    // Update the UI with the default plan expenses
                    mFoodExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", Result));
                    mTravelExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", Result));
                    mPaymentExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", Result));
                    mEntertainmentExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", Result));
                    // At least one of the EditTexts is empty
                }else{

                    mGeneratePlanButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            generatePlan();
                        }
                    });
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PlanExpenseActivity", "Failed to read value.", databaseError.toException());
            }
        });


    }

    private void generatePlan() {
     /*   // Get the amounts of expenses entered by the user
        double foodExpense = Double.parseDouble(mFoodExpenseEditText.getText().toString().trim());
        double travelExpense = Double.parseDouble(mTravelExpenseEditText.getText().toString().trim());
        double paymentExpense = Double.parseDouble(mPaymentExpenseEditText.getText().toString().trim());
        double entertainmentExpense = Double.parseDouble(mEntertainmentExpenseEditText.getText().toString().trim());

        // Calculate the total outcome amount and the total available balance
        double totalOutcome = foodExpense + travelExpense + paymentExpense + entertainmentExpense;
        double totalAvailableBalance = Double.parseDouble(totalBalanceTextView.getText().toString().substring(1));

        // Calculate the best outcome amounts for each expense
        double foodOutcome = Math.min(foodExpense, totalAvailableBalance / 4);
        double travelOutcome = Math.min(travelExpense, totalAvailableBalance / 4);
        double paymentOutcome = Math.min(paymentExpense, totalAvailableBalance / 4);
        double entertainmentOutcome = Math.min(entertainmentExpense, totalAvailableBalance / 4);

        // Update the UI with the best outcome amounts for each expense
        mFoodExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", foodOutcome));
        mTravelExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", travelOutcome));
        mPaymentExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", paymentOutcome));
        mEntertainmentExpenseTextView.setText(String.format(Locale.getDefault(), "$%.2f", entertainmentOutcome));
            */
    }

}