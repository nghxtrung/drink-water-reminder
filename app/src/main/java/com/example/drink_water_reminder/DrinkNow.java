package com.example.drink_water_reminder;

public class DrinkNow {
    private int image;
    private int volume;

    public DrinkNow(int image, int volume) {
        this.image = image;
        this.volume = volume;
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
}
