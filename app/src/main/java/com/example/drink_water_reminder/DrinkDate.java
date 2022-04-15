package com.example.drink_water_reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DrinkDate implements Comparable<DrinkDate> {
    private String code;
    private String date;
    private int target;

    public DrinkDate(String code, String date, int target) {
        this.code = code;
        this.date = date;
        this.target = target;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public int compareTo(DrinkDate drinkDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int value = 0;
        try {
            value = simpleDateFormat.parse(this.date).compareTo(simpleDateFormat.parse(drinkDate.date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }
}
