package com.example.mybudget.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mybudget.db.DBSLiteHelperSettings;
import com.example.mybudget.db.DBSQLiteHelper;
import com.example.mybudget.dto.DTOTransaction;
import com.example.mybudget.controllers.DatePickerController;
import com.example.mybudget.R;

import java.util.ArrayList;

public class EditTransactionActivity extends AppCompatActivity {

    //public variables
    private Button btnDate;
    private Button btnUpdate;
    private Button btnDelete;

    private EditText etxtAmount;
    private EditText etxtDescription;

    private Spinner spType;
    private Spinner spCategory;

    //Other objects
    private DatePickerDialog datePickerDialog;

    //DTO
    DTOTransaction dtoTransaction;

    //DatePicker Controller
    DatePickerController datePickerController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trasaction);


        //References
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete= findViewById(R.id.btnDelete);
        btnDate = findViewById(R.id.btnDate);
        etxtAmount = findViewById(R.id.etxtAmount);
        etxtDescription = findViewById(R.id.etxtDescription);
        spType = findViewById(R.id.spType);
        spCategory = findViewById(R.id.spCategory);

        //create datePickerController
        datePickerController = new DatePickerController();

        //Capture passed Data From FeedActivity
        if(getIntent().hasExtra("selectedTransaction")){
            this.dtoTransaction = getIntent().getParcelableExtra("selectedTransaction");
            Toast.makeText(this, dtoTransaction.getDate(), Toast.LENGTH_SHORT).show();
        }



        //initialize date picker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initDatePicker();
        }


        //load type list
        setTypeSpinnerAdapter();



        //Set Initial Value for Spinner
        if(dtoTransaction.getType().equals("Income")){
            spType.setSelection(0);
        }else{
            spType.setSelection(1);
        }

        //set initial value for edit texts
        this.setTitle("Edit Transaction");
        etxtAmount.setText(String.valueOf(dtoTransaction.getAmount()));
        etxtDescription.setText(dtoTransaction.getDescription());

        ArrayList<Integer> date = datePickerController.getDateFromString(dtoTransaction.getDate());
        btnDate.setText(datePickerController.makeDateString(date.get(0),date.get(1),date.get(2)));
        //datePickerDialog.updateDate(date.get(0),date.get(1),date.get(2));




        //Button Events
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTransaction(view);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTransaction(view);
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        //load type list EVENT & set the initial value for the spinner
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spTypeItemSelectionEvent(adapterView, view , i,l);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    //Manu Setup
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manuaddexpenseincome , menu);
        return true;
    }

    //Set Up Manu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Integer i = item.getItemId();
        if(i.equals(R.id.btnManuAddIncomeCategory) ){
            Intent intent = new Intent(this , IncomeListActivity.class);
            intent.putExtra("type","income");
            startActivity(intent);
        }else if(i.equals(R.id.btnManuExpenseCategory)){
            Intent intent2 = new Intent(this , ExpenseListActivity.class);
            intent2.putExtra("type","expense");
            startActivity(intent2);
        }else{
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private void spTypeItemSelectionEvent(AdapterView<?> adapterView, View view, int i, long l) {
        String selectedItem = spType.getItemAtPosition(i).toString();
        //Toast.makeText(this, selectedItem, Toast.LENGTH_SHORT).show();
        if(selectedItem.equals("Income")){
            setCategorySpinnerAdapter("Income"); // load Categories based on selected type
        }else{
            setCategorySpinnerAdapter("Expense");
        }
    }

    private void setCategorySpinnerAdapter(String type) {
        if(type.equals("Income")){
            DBSLiteHelperSettings dbsLiteHelperSettings =   new DBSLiteHelperSettings(this);
            ArrayList<String> incomes=  dbsLiteHelperSettings.readAllIncome();
            //ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this,R.array.strIncomeCategory, android.R.layout.simple_spinner_item);
            ArrayAdapter catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, incomes);
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(catAdapter);
            spCategory.setSelection(catAdapter.getPosition(dtoTransaction.getCategory()));
        }else if(type.equals("Expense")){
            DBSLiteHelperSettings dbsLiteHelperSettings =   new DBSLiteHelperSettings(this);
            ArrayList<String> expenses=  dbsLiteHelperSettings.readAllExpense();
            //ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(this,R.array.strExpenseCategory, android.R.layout.simple_spinner_item);
            ArrayAdapter catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, expenses);
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCategory.setAdapter(catAdapter);
            spCategory.setSelection(catAdapter.getPosition(dtoTransaction.getCategory()));
        }
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Month is set to zero fix
                month = month + 1;

                String dateString = datePickerController.makeDateString(year , month , day);
                btnDate.setText(dateString);

            }
        };

        //Get Date into numbers
        ArrayList<Integer> intDateArrayList =   DatePickerController.getDateFromString(dtoTransaction.getDate());
        int year  = intDateArrayList.get(0);
        int month  = intDateArrayList.get(1);
        int day  = intDateArrayList.get(2);


        //Calendar Style
        int style = AlertDialog.THEME_HOLO_LIGHT;


        //First load date picker dialog at entry date
        datePickerDialog = new DatePickerDialog(this ,style , onDateSetListener , year , month , day );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        //Set Initial Date to the button
        btnDate.setText(DatePickerController.getFormattedStrDate(dtoTransaction.getDate()));

    }

    private void setTypeSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.strType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(adapter);
        spType.setSelection(0);
    }

    private void updateTransaction(View view ){
        DTOTransaction transaction = new DTOTransaction();


        if(etxtDescription.getText().toString().equals("") || etxtAmount.getText().toString().equals("")){
            Toast.makeText(this, "Missing Fields..!!", Toast.LENGTH_SHORT).show();
        }else{
            transaction.setDate(datePickerController.getDateStringForDB());
            transaction.setType(spType.getSelectedItem().toString());
            transaction.setCategory(spCategory.getSelectedItem().toString());
            transaction.setDescription(etxtDescription.getText().toString());
            transaction.setAmount(Float.valueOf(etxtAmount.getText().toString()));

            //for updates and transaction recognition
            transaction.setTransactionId(dtoTransaction.getTransactionId());
            transaction.setTimeStamp(dtoTransaction.getTimeStamp());

            DBSQLiteHelper db = new DBSQLiteHelper(this);
            boolean result = db.updateTransaction(transaction);

            if(result){
                Toast.makeText(this,"Updated",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this , "Failed",Toast.LENGTH_SHORT).show();
            }
        }

        //TextView xx = findViewById(R.id.textView);
        //xx.setText(transaction.getCategory());


    }

    private void deleteTransaction(View view ){

        DTOTransaction transaction = new DTOTransaction();

        if(etxtDescription.getText().toString().equals("") || etxtAmount.getText().toString().equals("")){
            Toast.makeText(this, "Missing Fields..!!", Toast.LENGTH_SHORT).show();
        }else{
            transaction.setDate(datePickerController.getDateStringForDB());
            transaction.setType(spType.getSelectedItem().toString());
            transaction.setCategory(spCategory.getSelectedItem().toString());
            transaction.setDescription(etxtDescription.getText().toString());
            transaction.setAmount(Float.valueOf(etxtAmount.getText().toString()));

            //for updates and transaction recognition
            transaction.setTransactionId(dtoTransaction.getTransactionId());
            transaction.setTimeStamp(dtoTransaction.getTimeStamp());

            DBSQLiteHelper db = new DBSQLiteHelper(this);
            boolean result = db.deleteTransaction(transaction);

            if(result){
                Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
            }

        }




    }


}