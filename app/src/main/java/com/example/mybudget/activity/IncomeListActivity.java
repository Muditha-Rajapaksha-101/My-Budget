package com.example.mybudget.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybudget.R;
import com.example.mybudget.db.DBSLiteHelperSettings;
import com.example.mybudget.rvAdapter.FeedRvAdapter;
import com.example.mybudget.rvAdapter.IncomeRVAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class IncomeListActivity extends AppCompatActivity implements IncomeRVAdapter.OnRowClickListener {

    RecyclerView rvIcnome;
    Button btnAddNewIncome;
    ArrayList<String> incomes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list);

        //References
        rvIcnome = findViewById(R.id.rvIncome);
        btnAddNewIncome = findViewById(R.id.btnAddNewIncomeCategory);

        //init
        initRvIncome();
        this.setTitle("Income Categories");

        btnAddNewIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IncomeListActivity.this , AddIncomeActivity.class);
                intent.putExtra("type","income");
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        initRvIncome();
    }

    private void initRvIncome() {
        DBSLiteHelperSettings dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
        incomes = new ArrayList<>();
        incomes = dbsLiteHelperSettings.readAllIncome();

        //set adapter
        IncomeRVAdapter incomeRVAdapter = new IncomeRVAdapter(incomes,this);
        rvIcnome.setAdapter(incomeRVAdapter);
        rvIcnome.setLayoutManager(new LinearLayoutManager(this));

        //Get a decorator to a item decorator
        RecyclerView.ItemDecoration  lineDecorator = new DividerItemDecoration(this , DividerItemDecoration.VERTICAL);
        //Set the decorator
        rvIcnome.addItemDecoration(lineDecorator);

    }

    @Override
    public void onRVRowClick(View view, int position) {
        Intent intent = new Intent(this,EditIncomeActivity.class);
        intent.putExtra("selectedIncomeExpense",incomes.get(position));
        intent.putExtra("type","income");
        intent.putExtra("selectedIncomeExpensePosition",String.valueOf(position));
        startActivity(intent);
        Toast.makeText(this,String.valueOf(position),Toast.LENGTH_SHORT).show();
    }
}