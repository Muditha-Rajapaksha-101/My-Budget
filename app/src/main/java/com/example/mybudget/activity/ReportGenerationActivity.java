package com.example.mybudget.activity;

import static androidx.core.content.FileProvider.getUriForFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.mybudget.db.DBSQLiteHelper;
import com.example.mybudget.controllers.DatePickerController;
import com.example.mybudget.R;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportGenerationActivity extends AppCompatActivity {

    private final String TAG = "XXXXX";
    Button btnReport;
    Spinner spReportType;
    Button btnStartDate;
    Button btnEndDate;

    //Other objects
    private DatePickerDialog datePickerDialogStartDate;
    private DatePickerDialog datePickerDialogEndDate;

    DBSQLiteHelper dbsqLiteHelper;

    //ArrayLists to populate
    ArrayList<String> descriptions;
    ArrayList<String> amounts;
    ArrayList<String> dates;
    ArrayList<String> types;
    ArrayList<String> categories;
    ArrayList<String> timeStamps;
    ArrayList<String> transactionIds;

    DatePickerController datePickerControllerForStartDate;
    DatePickerController datePickerControllerForEndDate;

    String[][]  arrayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_generation);

        //References
        btnReport = findViewById(R.id.btnReport);
        spReportType = findViewById(R.id.spRerportType);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEnDate);

        datePickerControllerForStartDate = new DatePickerController();
        datePickerControllerForEndDate= new DatePickerController();

        //ASk permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        setTypeSpinnerAdapter();

        //init
        this.setTitle("Report Generation");
        initDatePicker(datePickerDialogStartDate,btnStartDate,datePickerControllerForStartDate);
        initDatePicker(datePickerDialogEndDate,btnEndDate,datePickerControllerForEndDate);

        //Event
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnReportClickEvent();

            }
        });
        spReportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 String selectedType = spReportType.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void btnReportClickEvent() {
        String selectedType = spReportType.getSelectedItem().toString();
        populateArrayLists(selectedType);
        //Toast.makeText(ReportGenerationActivity.this, String.valueOf(dates.size()), Toast.LENGTH_SHORT).show();
        loadToExcel("",selectedType);


        String myFilePath = getFilesDir()+"/DataSheet.xls";
        File file = new File(myFilePath);
        //openFile(ReportGenerationActivity.this,file);
        shareintent3();

        //Toast.makeText(ReportGenerationActivity.this, "chart created", Toast.LENGTH_SHORT).show();
    }

    private void populateArrayLists(String selectedType){

        dbsqLiteHelper = new DBSQLiteHelper(this);

        descriptions = new ArrayList<>();
        amounts = new ArrayList<>();
        dates = new ArrayList<>();
        types = new ArrayList<>();
        categories = new ArrayList<>();
        timeStamps = new ArrayList<>();
        transactionIds = new ArrayList<>();


        //Load Data to cursor
        Cursor cursorData = dbsqLiteHelper.readAllDataOnCondition(datePickerControllerForStartDate.getDateStringForDB() , datePickerControllerForEndDate.getDateStringForDB() , selectedType);
        //Cursor cursor = dbsqLiteHelper.readAllDataOnCondition("2022-5-4","2022-5-8","Income");

        //Initialize array based on row and column count
        int rowCount = cursorData.getCount();
        arrayDate = new String[rowCount][7]; // initialize daya array based on row and column count



        //Row counter
        int i =  0;

        if(cursorData.getCount() == 0){
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }else{
            while(cursorData.moveToNext()){
                arrayDate[i][0] = cursorData.getString(0);
                arrayDate[i][1] = cursorData.getString(1);
                arrayDate[i][2] = cursorData.getString(2);
                arrayDate[i][3] = cursorData.getString(3);
                arrayDate[i][4] = cursorData.getString(4);
                arrayDate[i][5] = cursorData.getString(5);
                arrayDate[i][6] = cursorData.getString(6);

                i++;

                descriptions.add(cursorData.getString(4));
                amounts.add(cursorData.getString(5));
                types.add(cursorData.getString(2));
                categories.add(cursorData.getString(3));
                dates.add(cursorData.getString(1));
                timeStamps.add(cursorData.getString(6));
                transactionIds.add(cursorData.getString(0));
            }
        }

    }

    private void loadToExcel(String fileName, String type) {
        Workbook workbook =  new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(type);

        //Set First Row
        Row firstRow = sheet.createRow(0);
        firstRow.createCell(0).setCellValue("ID");
        firstRow.createCell(1).setCellValue("Date");
        firstRow.createCell(2).setCellValue("Type");
        firstRow.createCell(3).setCellValue("Category");
        firstRow.createCell(4).setCellValue("Des");
        firstRow.createCell(5).setCellValue("Amount");
        firstRow.createCell(6).setCellValue("Time Stamp");


        for(int row = 1 ;row<arrayDate.length;row++){
            Row r = sheet.createRow(row);
            for(int col = 0 ; col < arrayDate[0].length; col++ ){
                Cell c = r.createCell(col);
                c.setCellValue(arrayDate[row-1][col]);
            }

        }


        File filePath = new File(getFilesDir()+"/DataSheet.xls");

        //write
        try (OutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);

            if(fileOut != null){
                fileOut.flush();
                fileOut.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private void shareIntentExcel(){
        String myFilePath = getFilesDir()+"/DataSheet.xls";
        File fileWithinMyDir = new File(myFilePath);

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);


        if(fileWithinMyDir.exists()) {
            intentShareFile.setType("application/vnd.ms-excel");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+myFilePath));

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    private void shareIntent2(){
        String myFilePath = getFilesDir()+"/DataSheet.xls";
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(myFilePath));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        startActivity(intent);
    }

    private void shareintent3(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String myFilePath = getFilesDir()+"/DataSheet.xls";
        File file = new File(myFilePath);

        //File file = new File("/storage/emulated/0/Samsung"+File.separator + race.getName()+".xls");
        Uri path = getUriForFile(getApplicationContext(), "com.example.mybudget.fileprovider", file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(path, "application/vnd.ms-excel");
        startActivity(intent);
    }


    public  void openFile(Context context, File file){


        // Create URI
        Uri url = FileProvider.getUriForFile(context.getApplicationContext(), "com.example.mybudget.fileprovider",file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Intent intent = new Intent();
        //Intent intent = new Intent(Intent.ACTION_SEND);
        //Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(url);
        Toast.makeText(context,url.toString(),Toast.LENGTH_LONG).show();
        intent.setType("application/vnd.ms-excel");
        intent.addCategory("android.intent.category.DEFAULT");
        //intent.setDataAndType(url, "application/vnd.ms-excel");
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //context.grantUriPermission("com.example.mybudget",url, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //context.grantUriPermission("com.example.mybudget",url, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        ReportGenerationActivity.this.setResult(Activity.RESULT_OK,intent);
        Intent shareIntent = Intent.createChooser(intent,"Share the data sheet");
        context.startActivity(shareIntent);

    }



    private void setTypeSpinnerAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.strReportType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spReportType.setAdapter(adapter);
        spReportType.setSelection(0);
    }

    private void initDatePicker(DatePickerDialog datePickerDialog , Button btnDate ,DatePickerController datePickerController) {
        String dateString;


        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Month is set to zero fix
                month = month + 1;


                String dateString = datePickerController.makeDateString(year , month , day);
                //Toast.makeText(ReportGenerationActivity.this,datePickerController.getDateStringForDB(),Toast.LENGTH_LONG).show();
                btnDate.setText(dateString);


            }
        };
        //Get today's date details
        Calendar cal =  Calendar.getInstance();
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        //Calander Style
        int style = AlertDialog.THEME_HOLO_LIGHT;


        //First load date picker dialog and first text
        datePickerDialog = new DatePickerDialog(this ,style , onDateSetListener , year , month , day );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        //Set Initial Date to the button
        btnDate.setText(new DatePickerController().getTodaysDate());

        DatePickerDialog finalDatePickerDialog = datePickerDialog;
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDatePickerDialog.show();
            }
        });

    }



}