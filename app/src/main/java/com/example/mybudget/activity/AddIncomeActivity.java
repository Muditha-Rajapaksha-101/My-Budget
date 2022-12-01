package com.example.mybudget.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mybudget.R;
import com.example.mybudget.db.DBSLiteHelperSettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AddIncomeActivity extends AppCompatActivity {

    EditText etxtIncome;
    TextView txtIncomeCateogoryName;
    String type; //holds whether dealing with an expense or income


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);


        //References
        etxtIncome = findViewById(R.id.etxtIncome);
        txtIncomeCateogoryName = findViewById(R.id.txtIncomeCateogoryName);

        //init

        setIncomeOrExpense();


    }

    void setIncomeOrExpense(){
        //Capture passed Data From FeedActivity
        if(getIntent().hasExtra("type")){
            type  = getIntent().getStringExtra("type");
        }

        if(type.equals("income")){
            this.setTitle("Add an Income Category");
            txtIncomeCateogoryName.setText("Income Category Name");
        }else{
            this.setTitle("Add an Expense Category");
            txtIncomeCateogoryName.setText("Expense Category Name");
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
                    DBSLiteHelperSettings dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
                    String newIncome = etxtIncome.getText().toString();
                    if(newIncome.equals("")){
                        Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show();
                    }else{
                        if( dbsLiteHelperSettings.addIncome(newIncome)){
                            Toast.makeText(this ,"Added" , Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this ,"Failed" , Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    //For Expense
                    DBSLiteHelperSettings dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
                    String newExpense = etxtIncome.getText().toString();
                    if(newExpense.equals("")){
                        Toast.makeText(this,"Empty",Toast.LENGTH_SHORT).show();
                    }else{
                        if( dbsLiteHelperSettings.addExpense(newExpense)){
                            Toast.makeText(this ,"Added" , Toast.LENGTH_SHORT).show();
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