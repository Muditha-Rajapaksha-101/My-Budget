package com.example.mybudget.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mybudget.dto.DTOTransaction;
import com.example.mybudget.controllers.DatePickerController;

public class DBSQLiteHelper extends SQLiteOpenHelper {
    //Instance
    Context context;

    //static
    private static final String DATABASE_NAME = "Transaction.db";
    private static final  int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "tblTrasaction";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_TRANSACTION_ID = "Trasaction_Id";
    private static final String COLUMN_TYPE = "Type";
    private static final String COLUMN_CATEGORY = "Category";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_AMOUNT = "Amount";
    private  static final String COLUMN_TIME = "Time";


    public DBSQLiteHelper(@Nullable Context context) {
        super(context,DATABASE_NAME , null , DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql  = String.format("CREATE TABLE %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , %s TEXT  , %s TEXT , %s TEXT , %s TEXT , %s TEXT , %s TEXT  ); "  ,TABLE_NAME,COLUMN_TRANSACTION_ID ,COLUMN_DATE, COLUMN_TYPE , COLUMN_CATEGORY , COLUMN_DESCRIPTION , COLUMN_AMOUNT,COLUMN_TIME);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = String.format("DROP TABLE IF EXISTS %s ;",TABLE_NAME);
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }


    public boolean addTransaction(DTOTransaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, transaction.getDate());
        cv.put(COLUMN_TYPE,transaction.getType());
        cv.put(COLUMN_CATEGORY,transaction.getCategory());
        cv.put(COLUMN_DESCRIPTION,transaction.getDescription());
        cv.put(COLUMN_AMOUNT,String.valueOf(transaction.getAmount()));
        cv.put(COLUMN_TIME, DatePickerController.getTimeStampForDB()); //directly access time for the record without being passed

        long result = db.insert(TABLE_NAME,null,cv);

        if(result == -1){
            return false;
            //Toast.makeText(context , "Fail" , Toast.LENGTH_SHORT).show();
        }else{
            return true;
            //Toast.makeText(context , "Updated" , Toast.LENGTH_SHORT).show();
        }

    }

    public Cursor readAllData() {
        String sql = String.format("SELECT * FROM %s ORDER BY Date DESC ", TABLE_NAME);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }
        return cursor;

    }

    public boolean updateTransaction(DTOTransaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //cv.put(COLUMN_TRANSACTION_ID , transaction.getTransactionId());
        cv.put(COLUMN_DATE, transaction.getDate());
        cv.put(COLUMN_TYPE,transaction.getType());
        cv.put(COLUMN_CATEGORY,transaction.getCategory());
        cv.put(COLUMN_DESCRIPTION,transaction.getDescription());
        cv.put(COLUMN_AMOUNT,String.valueOf(transaction.getAmount()));
        cv.put(COLUMN_TIME,transaction.getTimeStamp()); //directly access time for the record without being passed

        if(rowVerification(transaction)){
            long result = db.update(TABLE_NAME,cv , COLUMN_TRANSACTION_ID + " = ? " , new String[]{transaction.getTransactionId()});

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

    public boolean deleteTransaction(DTOTransaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();

        if(rowVerification(transaction)){
            long result = db.delete(TABLE_NAME , COLUMN_TRANSACTION_ID + " = ? " , new String[]{transaction.getTransactionId()});

            if(result == -1){
                return false;
                //Toast.makeText(context , "Failed" , Toast.LENGTH_SHORT).show();
            }else{
                return true;
                //Toast.makeText(context , "Deleted" , Toast.LENGTH_SHORT).show();
            }
        }else{
            return  false;
        }



    }

    //Verify the existence of a record
    public boolean rowVerification(DTOTransaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_NAME + " where " + COLUMN_TRANSACTION_ID + " = " + "?" ,new String[]{transaction.getTransactionId()});

        if(cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }


    }

    public Cursor readAllDataOnCondition(String startDate , String endDate , String type) {
        String sql = String.format("SELECT * FROM %s WHERE %s = '%s' and %s >= '%s' and %s <= '%s' ORDER BY DATE , TIME  DESC", TABLE_NAME,COLUMN_TYPE,type,COLUMN_DATE,startDate,COLUMN_DATE,endDate);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }
        return cursor;

    }

    public Cursor readCountOnCondition(String startDate , String endDate , String type) {
        String sql = String.format("SELECT count(*) FROM %s WHERE %s = '%s' and %s >= '%s' and %s <= '%s' ORDER BY DATE , TIME  DESC", TABLE_NAME,COLUMN_TYPE,type,COLUMN_DATE,startDate,COLUMN_DATE,endDate);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }
        return cursor;

    }

    public int getTotalIncomeOrExpense(String IncomeOrExpense){
        String sql = String.format("SELECT sum(amount) FROM %s WHERE %s = '%s' ;", TABLE_NAME,COLUMN_TYPE,IncomeOrExpense);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(sql, null);
        }

        int result = 0;
        if (cursor.moveToFirst()) result = cursor.getInt(0);
        return result;


    }

}
