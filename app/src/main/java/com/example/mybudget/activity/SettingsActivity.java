package com.example.mybudget.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mybudget.R;
import com.example.mybudget.db.DBSLiteHelperSettings;
import com.example.mybudget.dto.DTOSettings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    Spinner spCurrency;
    DBSLiteHelperSettings dbsLiteHelperSettings;
    Button btnAddNewIncomeCategory;
    Button btnAddNewExpenseCategory;


    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //References
        spCurrency = findViewById(R.id.spCurrency);
        dbsLiteHelperSettings = new DBSLiteHelperSettings(this);
        btnAddNewExpenseCategory =(Button) findViewById(R.id.btnAddE);
        btnAddNewIncomeCategory = (Button) findViewById(R.id.btnAddI);

        setCurrencyAdapter();
        InitSettings();

        btnAddNewExpenseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this , ExpenseListActivity.class);
                intent.putExtra("type","expense");
                startActivity(intent);
            }
        });

        btnAddNewIncomeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this , IncomeListActivity.class);
                intent.putExtra("type","income");
                startActivity(intent);
            }
        });

        spCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spCurrency.getItemAtPosition(i).toString();
                saveCurrencySetting(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

    private void InitSettings() {
        this.setTitle("Settings");
        int selectedPosition =  dbsLiteHelperSettings.readSettings("currency");
        spCurrency.setSelection(selectedPosition);
    }

    private void setCurrencyAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.strCurrency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurrency.setAdapter(adapter);
        spCurrency.setSelection(0);
    }

    private void saveCurrencySetting(int selectedCurrencyPosition){
        DTOSettings dtoSettings = new DTOSettings("currency",selectedCurrencyPosition);


        if(!dbsLiteHelperSettings.rowVerificationSettings(dtoSettings)){
            boolean result = dbsLiteHelperSettings.addSetting(dtoSettings);
            if(result){
                Toast.makeText(this,"Settings Saved.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Settings Failed.",Toast.LENGTH_SHORT).show();
            }
        }else{
            //call update
            boolean  result = dbsLiteHelperSettings.updateSetting(dtoSettings);
            if(result){
                Toast.makeText(this,"Settings Updated.",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Settings Update Failed.",Toast.LENGTH_SHORT).show();
            }
        }



    }
}