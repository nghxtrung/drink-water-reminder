package com.example.drink_water_reminder;

public class DrinkLog {
    private String timeCode;
    private String time;
    private int volume;
    private String dateCode;
    private int image;

    public DrinkLog(String timeCode, String time, int volume, String dateCode, int image) {
        this.timeCode = timeCode;
        this.time = time;
        this.volume = volume;
        this.dateCode = dateCode;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
