package com.example.mybudget.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybudget.R;
import com.example.mybudget.db.DBSLiteHelperSettings;

public class EditIncomeActivity extends AppCompatActivity {

    EditText extIncome;
    TextView txtIncomeCategoryNameUpdate;

    String selectedIncomeExpense;
    int selectedIncomeExpensePosition;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_income);


        //References
        extIncome = findViewById(R.id.etxtIncomeUpdate);
        txtIncomeCategoryNameUpdate = findViewById(R.id.txtIncomeCateogoryNameUpdate);

        //init
        setExpenseIncome();
        setAppreance();


    }

    private void setExpenseIncome() {
        //get type
        if(getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }

        //set program base on type
        if(type.equals("income")){
            if(getIntent().hasExtra("selectedIncomeExpense")){

                this.selectedIncomeExpense = getIntent().getStringExtra("selectedIncomeExpense");
                this.selectedIncomeExpensePosition = Integer.parseInt(getIntent().getStringExtra("selectedIncomeExpensePosition"));

            }
            extIncome.setText(selectedIncomeExpense);

        }else{
            //for expense
            if(getIntent().hasExtra("selectedExpense")){

                this.selectedIncomeExpense = getIntent().getStringExtra("selectedExpense");
                this.selectedIncomeExpensePosition = Integer.parseInt(getIntent().getStringExtra("selectedExpensePosition"));

            }
            extIncome.setText(selectedIncomeExpense);

        }

    }

    private void setAppreance() {
        if(type.equals("income")){
            this.setTitle("Update Income Category");
            txtIncomeCategoryNameUpdate.setText("Income Category Name");
        }else{
            this.setTitle("Update Expense Category");
            txtIncomeCategoryNameUpdate.setText("Expense Category Name");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addresoucemanu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.btnManuAdd:
                if(type.equals("income")){
                    //for Income
                    DBSLiteHelperSettings dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
                    String income = extIncome.getText().toString();
                    if(income.equals("")){
                        Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show();
                    }else{
                        if( dbsLiteHelperSettings.updateIncome(income, selectedIncomeExpensePosition)){
                            Toast.makeText(this ,"Updated" , Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this ,"Failed" , Toast.LENGTH_SHORT).show();
                        }
                    }

                }else{
                    //For Expense
                    DBSLiteHelperSettings dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
                    String expense = extIncome.getText().toString();
                    if(expense.equals("")){
                        Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show();
                    }else{
                        if( dbsLiteHelperSettings.updateExpense(expense, selectedIncomeExpensePosition)){
                            Toast.makeText(this ,"Updated" , Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this ,"Failed" , Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}