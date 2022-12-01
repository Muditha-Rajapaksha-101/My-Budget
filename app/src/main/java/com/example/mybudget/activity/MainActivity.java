package com.example.mybudget.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mybudget.R;
import com.example.mybudget.db.DBSLiteHelperSettings;

public class MainActivity extends AppCompatActivity {

    //References
    Button btnFeed;
    Button btnMarketData;
    Button btnReports;
    Button btnExit;
    Button btnAbout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set References
        btnFeed = findViewById(R.id.btnFeed);
        btnMarketData = findViewById(R.id.btnMarketData);
        btnReports = findViewById(R.id.btnReports);
        btnExit = findViewById(R.id.btnExit);
        btnAbout = findViewById(R.id.btnAbout);



        //init
        firstTimeLoading();
        this.setTitle("My Budget Home");

        //Event
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFeedActivity();
            }
        });

        btnMarketData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMarketDataActivity();
            }
        });

        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReportGenerationActivity(view);
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAbout();
            }
        });


    }

    private void firstTimeLoading() {
        //set shared preference
        SharedPreferences sharedPreferences = getSharedPreferences("BasicSettings", Context.MODE_PRIVATE);
        String firstTime = sharedPreferences.getString("firstTime" , "true");

        //Set Actions
        if(firstTime.equals("true")){
            Toast.makeText(this , "First Time User Welcome..!!" ,  Toast.LENGTH_LONG);

            DBSLiteHelperSettings dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
            dbsLiteHelperSettings.addIncome("Salary");
            dbsLiteHelperSettings.addIncome("Bonus");
            dbsLiteHelperSettings.addIncome("Gift");

            dbsLiteHelperSettings.addExpense("General");
            dbsLiteHelperSettings.addExpense("Transport");
            dbsLiteHelperSettings.addExpense("Entertainment");
            dbsLiteHelperSettings.addExpense("Food");
            dbsLiteHelperSettings.addExpense("Health");
            dbsLiteHelperSettings.addExpense("Shopping");
            dbsLiteHelperSettings.addExpense("Social");


            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("firstTime" , "false");
            editor.apply();
        }else{
            Toast.makeText(this, "Welcome Back...!!" , Toast.LENGTH_SHORT);

        }

    }

    //Manu Setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmanu , menu);
        return true;
    }

    //Set Up Manu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Integer i = item.getItemId();
        if(i.equals(R.id.btnManuAbout) ){
            openAbout();
        }else if(i.equals(R.id.btnManuExit)) {
            finish();
            System.exit(0);
        } else if(i.equals(R.id.btnManuSettings)){
            Intent intent = new Intent(this , SettingsActivity.class);
            startActivity(intent);
        } else{
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);


    }

    private void openReportGenerationActivity(View view) {
        Intent intent = new Intent(this , ReportGenerationActivity.class);
        startActivity(intent);

    }

    private void openMarketDataActivity() {
        Intent intent = new Intent(this , LoadMarketDataActivity.class);
        startActivity(intent);

    }

    private void openAbout() {
        Intent intent = new Intent(this , AboutActivity.class);
        startActivity(intent);

    }

    private void openFeedActivity() {
        Intent intent = new Intent(this , FeedActivity.class);
        startActivity(intent);

    }



}