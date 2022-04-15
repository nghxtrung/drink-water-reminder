package com.example.drink_water_reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DrinkAlarm implements Comparable<DrinkAlarm> {
    private String timeCode;
    private String time;
    private int volume;
    private String dateCode;
    private int image;

    public DrinkAlarm(String timeCode, String time, int volume, String dateCode, int image) {
        this.timeCode = timeCode;
        this.time = time;
        this.image = image;
        this.volume = volume;
        this.dateCode = dateCode;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getDateCode() {
        return dateCode;
    }

    public void setDateCode(String dateCode) {
        this.dateCode = dateCode;
    }

    @Override
    public int compareTo(DrinkAlarm drinkAlarm) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        int value = 0;
        try {
            value = simpleDateFormat.parse(this.time).compareTo(simpleDateFormat.parse(drinkAlarm.time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }
}
