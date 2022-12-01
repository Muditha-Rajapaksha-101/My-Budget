package com.example.mybudget.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybudget.db.DBSLiteHelperSettings;
import com.example.mybudget.db.DBSQLiteHelper;
import com.example.mybudget.dto.DTOTransaction;
import com.example.mybudget.rvAdapter.FeedRvAdapter;
import com.example.mybudget.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity implements FeedRvAdapter.OnRvFeedRowClick {
    //References
    Button btnAddNewTransaction;
    RecyclerView rvFeed;
    TextView txtRemainingBalance;

    DBSQLiteHelper dbsqLiteHelper;

    //ArrayLists to populate
    ArrayList<String> descriptions;
    ArrayList<String> amounts;
    ArrayList<String> dates;
    ArrayList<String> types;
    ArrayList<String> categories;
    ArrayList<String> timeStamps;
    ArrayList<String> transactionIds;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //References
        btnAddNewTransaction = findViewById(R.id.btnAddTrasaction);
        rvFeed = findViewById(R.id.rvFeed);
        txtRemainingBalance = findViewById(R.id.txtRemainingBalance);

        //Transaction Recycler View Settings
        this.setTitle("Transaction Feed");
        populateArrayLists();
        setTotal();
        setRecyclerView();


        //btnAddNewTransactions ClickEvent
        btnAddNewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddNewTransactionClickEvent(view);
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();
        //Transaction Recycler View Settings
        populateArrayLists();
        setTotal();
        setRecyclerView();
    }
    //Set up menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmanu , menu);
        return true;
    }

    //Set Up Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Integer i = item.getItemId();
        if(i.equals(R.id.btnManuAbout) ){
            Intent intent = new Intent(this , AboutActivity.class);
            startActivity(intent);
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

    private void populateArrayLists(){
        dbsqLiteHelper = new DBSQLiteHelper(this);

        descriptions = new ArrayList<>();
        amounts = new ArrayList<>();
        dates = new ArrayList<>();
        types = new ArrayList<>();
        categories = new ArrayList<>();
        timeStamps = new ArrayList<>();
        transactionIds = new ArrayList<>();


        Cursor cursor = dbsqLiteHelper.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                descriptions.add(cursor.getString(4));
                amounts.add(cursor.getString(5));
                types.add(cursor.getString(2));
                categories.add(cursor.getString(3));
                dates.add(cursor.getString(1));
                timeStamps.add(cursor.getString(6));
                transactionIds.add(cursor.getString(0));
                }
            }
    }

    private void setTotal(){
        int totIncome  = dbsqLiteHelper.getTotalIncomeOrExpense("Income");
        int totExpense  = dbsqLiteHelper.getTotalIncomeOrExpense("Expense");
        double amount = (double) totIncome-totExpense;
        //double amount = Double.parseDouble(currentAmount1);
        DecimalFormat formatter = new DecimalFormat("#,###");
        String dif = formatter.format(amount);
        txtRemainingBalance.setText(dif);

    }

    private void setRecyclerView() {
        //load currency
        DBSLiteHelperSettings dbsLiteHelperSettings=  new DBSLiteHelperSettings(this);
        int settingNo = dbsLiteHelperSettings.readSettings("currency");
        String[] curencies = getResources().getStringArray(R.array.strCurrency);
        String currency = curencies[settingNo];

        //transactions =new String[] {"Class fee", "Ara fee","mee fee"};
        FeedRvAdapter feedRvAdapter = new FeedRvAdapter(descriptions,amounts,dates,types,categories,this,currency);
        rvFeed.setAdapter(feedRvAdapter);
        rvFeed.setLayoutManager(new LinearLayoutManager(this));

        //Get a decorator to a item decorator
        RecyclerView.ItemDecoration  lineDecorator = new DividerItemDecoration(this , DividerItemDecoration.VERTICAL);
        //Set the decorator
        rvFeed.addItemDecoration(lineDecorator);

    }

    private void btnAddNewTransactionClickEvent(View view) {
        Intent intent = new Intent(this , AddTransactionActivity.class);
        startActivity(intent);
    }

    @Override //Click of a row in Recycler View
    public void onRvFeedRowClickEvent(View view ,int position) {
        DTOTransaction dtoTransaction = new DTOTransaction();

        dtoTransaction.setTransactionId(transactionIds.get(position));
        dtoTransaction.setDate(dates.get(position));
        dtoTransaction.setType(types.get(position));
        dtoTransaction.setCategory(categories.get(position));
        dtoTransaction.setDescription(descriptions.get(position));
        dtoTransaction.setAmount(Float.valueOf(amounts.get(position)));
        dtoTransaction.setTimeStamp(timeStamps.get(position));

        //String selected = descriptions.get(position);
        //Toast.makeText(FeedActivity.this,selected,Toast.LENGTH_SHORT).show();
        //txtRemainingBalance.setText(selected);

        Intent intent = new Intent(this , EditTransactionActivity.class);
        intent.putExtra("selectedTransaction",dtoTransaction);
        startActivity(intent);



    }
}