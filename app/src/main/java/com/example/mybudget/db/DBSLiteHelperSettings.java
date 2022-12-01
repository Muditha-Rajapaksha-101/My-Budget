package com.example.mybudget.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mybudget.dto.DTOSettings;
import com.example.mybudget.dto.DTOTransaction;

import java.util.ArrayList;

public class DBSLiteHelperSettings extends SQLiteOpenHelper {
    //Instance
    Context context;

    //static
    private static final String DATABASE_NAME = "settings.db";
    private static final  int DATABASE_VERSION = 1;

    private static final String TBL_SETTINGS = "tblSettings";
    private static final String COLUMN_SETTING = "setting";
    private static final String COLUMN_VALUE = "value";


    private static final String TBL_INCOME = "tblIncome";
    private static final String COLUMN_INO= "ino";
    private static final String COLUMN_INCOME_NAME= "iname";

    private static final String TBL_EXPENSE = "tblexpense";
    private static final String COLUMN_ENO= "eno";
    private static final String COLUMN_EXPENSE_NAME= "ename";





    public DBSLiteHelperSettings(@Nullable Context context) {
        super(context,DATABASE_NAME , null , DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1  = String.format("CREATE TABLE %s ( %s TEXT PRIMARY KEY NOT NULL , %s Text); "  ,TBL_SETTINGS,COLUMN_SETTING,COLUMN_VALUE);
        String sql2  = String.format("CREATE TABLE %s (  %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , %s Text); "  ,TBL_INCOME,COLUMN_INO,COLUMN_INCOME_NAME);
        String sql3  = String.format("CREATE TABLE %s (  %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , %s Text); "  ,TBL_EXPENSE,COLUMN_ENO,COLUMN_EXPENSE_NAME);

        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql1 = String.format("DROP TABLE IF EXISTS %s ;",TBL_SETTINGS);
        String sql2 = String.format("DROP TABLE IF EXISTS %s ;",TBL_INCOME);
        String sql3 = String.format("DROP TABLE IF EXISTS %s ;",TBL_EXPENSE);


        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);


        onCreate(sqLiteDatabase);
    }

    //Settings
    public boolean addSetting(DTOSettings setting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SETTING, setting.getSetting());
        cv.put(COLUMN_VALUE,String.valueOf(setting.getValue()));

        long result = db.insert(TBL_SETTINGS,null,cv);

        if(result == -1){
            //Toast.makeText(context , "Fail" , Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //Toast.makeText(context , "Updated" , Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    //Settings
    public boolean updateSetting(DTOSettings setting){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //cv.put(COLUMN_TRANSACTION_ID , transaction.getTransactionId());
        cv.put(COLUMN_SETTING, setting.getSetting());
        cv.put(COLUMN_VALUE,String.valueOf(setting.getValue()));

        if(rowVerificationSettings(setting)){
            long result = db.update(TBL_SETTINGS,cv , COLUMN_SETTING + " = ? " , new String[]{setting.getSetting()});

            if(result == -1){
                return false;
                //Toast.makeText(context , "Fail" , Toast.LENGTH_SHORT).show();
            }else{
                return true;
                //Toast.makeText(context , "Added" , Toast.LENGTH_SHORT).show();
            }
        }else{
            return  false;
        }




    }

    //Settings
    public int readSettings(String setting) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = '%s' ;", COLUMN_VALUE,TBL_SETTINGS , COLUMN_SETTING,setting);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }

        int result = 0;
        if (cursor.moveToFirst()) result = cursor.getInt(0);
        //Toast.makeText(context,String.valueOf(result),Toast.LENGTH_SHORT).show();
        return result;

    }

    //Settings
    public boolean rowVerificationSettings(DTOSettings settings){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_SETTINGS + " where " + COLUMN_SETTING + " = " + "?" , new String[]{settings.getSetting()});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }


    }




    //Income
    public boolean addIncome(String income){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_INCOME_NAME, income);

        long result = db.insert(TBL_INCOME,null,cv);

        if(result == -1){
            //Toast.makeText(context , "Fail" , Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //Toast.makeText(context , "Updated" , Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    //Income
    public ArrayList<String> readAllIncome() {
        String sql = String.format("SELECT * FROM %s ORDER BY %s;", TBL_INCOME,COLUMN_INO);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }

        ArrayList<String> incomeList = new ArrayList<>();

        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(1);
                incomeList.add(data);

            }while(cursor.moveToNext());
        }
        cursor.close();

        return incomeList;
    }

    //Income
    public boolean updateIncome(String newIncome , int position){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //cv.put(COLUMN_TRANSACTION_ID , transaction.getTransactionId());
        cv.put(COLUMN_INCOME_NAME, newIncome);


        if(rowVerificationIncome(position)){
            long result = db.update(TBL_INCOME,cv , COLUMN_INO + " = ? " , new String[]{String.valueOf(position + 1)});

            if(result == -1){
                return false;
                //Toast.makeText(context , "Fail" , Toast.LENGTH_SHORT).show();
            }else{
                return true;
                //Toast.makeText(context , "Added" , Toast.LENGTH_SHORT).show();
            }
        }else{
            return  false;
        }




    }

    //Income
    public boolean rowVerificationIncome(int position){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_INCOME + " where " +COLUMN_INO + " = " + "?" , new String[]{String.valueOf(position+1)});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }


    }




    //Expense
    public boolean addExpense(String income){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EXPENSE_NAME, income);

        long result = db.insert(TBL_EXPENSE,null,cv);

        if(result == -1){
            //Toast.makeText(context , "Fail" , Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //Toast.makeText(context , "Updated" , Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    //Expense
    public ArrayList<String> readAllExpense() {
        String sql = String.format("SELECT * FROM %s ORDER BY %s;", TBL_EXPENSE,COLUMN_ENO);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }

        ArrayList<String> expenseList = new ArrayList<>();

        if (cursor.moveToFirst()){
            do{
                String data = cursor.getString(1);
                expenseList.add(data);

            }while(cursor.moveToNext());
        }
        cursor.close();

        return expenseList;
    }

    //Expense
    public boolean updateExpense(String newExpense , int position){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //cv.put(COLUMN_TRANSACTION_ID , transaction.getTransactionId());
        cv.put(COLUMN_EXPENSE_NAME, newExpense);


        if(rowVerificationExpense(position)){
            long result = db.update(TBL_EXPENSE,cv , COLUMN_ENO + " = ? " , new String[]{String.valueOf(position + 1)});

            if(result == -1){
                return false;
                //Toast.makeText(context , "Fail" , Toast.LENGTH_SHORT).show();
            }else{
                return true;
                //Toast.makeText(context , "Added" , Toast.LENGTH_SHORT).show();
            }
        }else{
            return  false;
        }


    }

    //Expense
    public boolean rowVerificationExpense(int position){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_EXPENSE + " where " +COLUMN_ENO + " = " + "?" , new String[]{String.valueOf(position+1)});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }


    }
}
