package com.example.mybudget.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mybudget.R;
import com.example.mybudget.db.DBSLiteHelperSettings;
import com.example.mybudget.rvAdapter.ExpenseRVAdapter;
import com.example.mybudget.rvAdapter.IncomeRVAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ExpenseListActivity extends AppCompatActivity  implements ExpenseRVAdapter.OnRowClickListener {

    RecyclerView rvExpense;
    Button btnAddNewExpense;
    ArrayList<String> expenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);


        //References
        rvExpense = findViewById(R.id.rvExpense);
        btnAddNewExpense = findViewById(R.id.btnAddNewExpenseCategory);

        //init
        initRvExpense();
        this.setTitle("Expense Categories");

        //Events
        btnAddNewExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpenseListActivity.this , AddIncomeActivity.class);
                intent.putExtra("type","expense");
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        initRvExpense();
    }

    private void initRvExpense() {
        DBSLiteHelperSettings dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
        expenses = new ArrayList<>();
        expenses = dbsLiteHelperSettings.readAllExpense();

        //set adapter
        ExpenseRVAdapter expenseRVAdapter = new ExpenseRVAdapter(expenses,this);
        rvExpense.setAdapter(expenseRVAdapter);
        rvExpense.setLayoutManager(new LinearLayoutManager(this));

        //Get a decorator to a item decorator
        RecyclerView.ItemDecoration  lineDecorator = new DividerItemDecoration(this , DividerItemDecoration.VERTICAL);
        //Set the decorator
        rvExpense.addItemDecoration(lineDecorator);
    }

    @Override
    public void onRVRowClick(View view, int position) {
        Intent intent = new Intent(this,EditIncomeActivity.class);
        intent.putExtra("selectedExpense",expenses.get(position));
        intent.putExtra("type","expense");
        intent.putExtra("selectedExpensePosition",String.valueOf(position));
        startActivity(intent);
        Toast.makeText(this,String.valueOf(position),Toast.LENGTH_SHORT).show();

    }
}