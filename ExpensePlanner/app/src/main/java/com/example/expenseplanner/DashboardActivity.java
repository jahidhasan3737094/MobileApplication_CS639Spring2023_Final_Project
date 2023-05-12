package com.example.expenseplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DashboardActivity extends AppCompatActivity {
    private Button addSavingInfoButton;
    private Button planExpenseButton;
    private Button checkUpcomingExpenseButton;
    private Button profileDetailsButton;
    private Button userGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dasboard);

        // Find the buttons and set their click listeners
        addSavingInfoButton = findViewById(R.id.add_saving_info_button);
        planExpenseButton = findViewById(R.id.plan_expense_button);
        checkUpcomingExpenseButton = findViewById(R.id.check_upcoming_expense_button);
        profileDetailsButton = findViewById(R.id.profile_details_button);
        userGuide=findViewById(R.id.User_guide);

        addSavingInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AddSavingInfoActivity.class);
                startActivity(intent);
            }
        });

        planExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PlanExpenseActivity.class);
                startActivity(intent);
            }
        });

        checkUpcomingExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CheckUpcomingExpensesActivity.class);
                startActivity(intent);
            }
        });

        profileDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        userGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, UserGuideActivity.class);
                startActivity(intent);
            }
        });
    }
}
