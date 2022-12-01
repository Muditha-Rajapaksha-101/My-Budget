package com.example.mybudget.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class DTOTransaction implements Parcelable {

    private String category;
    private String type;
    private Float amount;
    private String date;
    private  String description;

    //Optional in transaction entry
    private String transactionId;
    private String timeStamp;


    //Constructor for the class
    public DTOTransaction(String category, String type, Float amount, String date, String description) {
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;

        //Optional in transaction entry

    }

    //Constructor for the class
    public DTOTransaction(String category, String type, Float amount, String date, String description , String transactionId , String timeStamp) {
        this.category = category;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.description = description;

        //Optional in transaction entry
        this.transactionId = transactionId;
        this.timeStamp = timeStamp;
    }

    //Constructor for the class
    public DTOTransaction() {
    }

    protected DTOTransaction(Parcel in) {
        category = in.readString();
        type = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readFloat();
        }
        date = in.readString();
        description = in.readString();
        transactionId = in.readString();
        timeStamp = in.readString();
    }

    public static final Creator<DTOTransaction> CREATOR = new Creator<DTOTransaction>() {
        @Override
        public DTOTransaction createFromParcel(Parcel in) {
            return new DTOTransaction(in);
        }

        @Override
        public DTOTransaction[] newArray(int size) {
            return new DTOTransaction[size];
        }
    };

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeString(type);
        if (amount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(amount);
        }
        parcel.writeString(date);
        parcel.writeString(description);
        parcel.writeString(transactionId);
        parcel.writeString(timeStamp);
    }
}
