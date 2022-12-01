package com.example.mybudget.dto;

public class DTOSettings {
    private String setting;
    private int value;

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    //Constructor
    public DTOSettings(String setting, int value) {
        this.setting = setting;
        this.value = value;
    }

    //Constructor
    public DTOSettings() {

    }
}
