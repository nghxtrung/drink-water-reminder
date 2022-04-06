package com.example.drink_water_reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DrinkAlarm implements Comparable<DrinkAlarm> {
    private int image;
    private int volume;
    private String time;

    public DrinkAlarm(int image, int volume, String time) {
        this.image = image;
        this.volume = volume;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(DrinkAlarm drinkAlarm) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        int value = 0;
        try {
            value = simpleDateFormat.parse(this.time).compareTo(simpleDateFormat.parse(drinkAlarm.time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }
}
