package com.example.mybudget.controllers;

import java.util.ArrayList;
import java.util.Calendar;

public class DatePickerController {
    private  int year = 2022;
    private  int month = 1;
    private  int day = 1;
    private  String time;


    //constructor 1
    public DatePickerController() {
        Calendar cal = Calendar.getInstance();
        this.year = cal.get(Calendar.YEAR);
        this.month = cal.get(Calendar.MONTH) + 1;
        this.day  = cal.get(Calendar.DAY_OF_MONTH);
        time = DatePickerController.getTimeStampForDB();

    }

    //Set out the date to the class
    public String makeDateString(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;
        return getMonthString(month) + " - " + String.valueOf(day) + " - " + String.valueOf(year);
    }

    public  String getDateStringForDB(){
        // Format = yyyy-mm-dd
        String dateStringDB = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
        return  dateStringDB;
    }


    //Static Methods
    public static String getMonthString(int month) {
        if (month == 1){
            return "JAN";
        }else if (month == 2){
            return "FEB";
        }else if (month == 3){
            return "MAR";
        }else if (month == 4){
            return "APR";
        }else if (month == 5){
            return "MAY";
        }else if (month == 6){
            return "JUN";
        }else if (month == 7){
            return "JUL";
        }else if (month == 8){
            return "AUG";
        }else if (month == 9){
            return "SEP";
        }else if (month == 10){
            return "OCT";
        }else if (month == 11){
            return "NOV";
        }else if (month == 12){
            return "DEC";
        }

        //Default return value
        return "Jan";
    }

    public static String getTodaysDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day  = cal.get(Calendar.DAY_OF_MONTH);

        String dateString = getMonthString(month) + " - " + String.valueOf(day) + " - " + String.valueOf(year);
        return  dateString;

    }

    public static String getTimeStampForDB(){
        Calendar cal = Calendar.getInstance();
        String currentTime = cal.getTime().toString();
        return currentTime;
    }

    public static ArrayList<Integer> getDateFromString(String dateStr){
        ArrayList<Integer> intDateArrayList= new ArrayList<>();
        String[] strDateArray = dateStr.split("-");
        intDateArrayList.add(Integer.valueOf(strDateArray[0]));
        intDateArrayList.add(Integer.valueOf(strDateArray[1]));
        intDateArrayList.add(Integer.valueOf(strDateArray[2]));

        return intDateArrayList;
    }

    public static String getFormattedStrDate(String dateStr){
        String[] strDateArray = dateStr.split("-");
        int year  = Integer.valueOf(strDateArray[0]);
        int month  = Integer.valueOf(strDateArray[1]);
        int day = Integer.valueOf(strDateArray[2]);

        String dateString = getMonthString(month) + " - " + String.valueOf(day) + " - " + String.valueOf(year);
        return  dateString;
    }
}
