package com.example.expenseplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class UserGuideActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private UserGuideAdapter mAdapter;
    private List<UserGuideItem> mItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mItems = new ArrayList<>();
        mItems.add(new UserGuideItem("step 1", "First of all click on the Saving Info button and provide Information of Bank and Balance Example :Bank Name: Td bank,The Balance you have in your bank account Example Balance: 400 click on add button which will add the balance with your total balance and will have List of the bank you added."));
        mItems.add(new UserGuideItem("Step 2", "Next go back to the main Expense planner dashboard and click on the Plan Expense Button.It will take you to the Expense planning page where you will get a auto generated monthly expense plan based on you Total available balance amount. or you can make your own plan by providing Required field value Example: Food:200$,Travelling : 100$,Payment:200% and Entertainment:100$ and then if you click on the generate button ,it will create a plan according to your Preference "));
        mItems.add(new UserGuideItem("Step 3", "Then go back to the main Expense planner dashboard Click on the Check upcoming Expenses if you want to track the due Date and Expanse"));
        mItems.add(new UserGuideItem("Step 4", "Then go back to the main Expense planner dashboard Click on the profile Details button to see your profile and update or change profile Information"));
        mAdapter = new UserGuideAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);

    }
}