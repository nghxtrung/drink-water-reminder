package com.example.drink_water_reminder;

public class DrinkLog {
    private int image;
    private int volume;
    private String time;

    public DrinkLog(int image, int volume, String time) {
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
}
