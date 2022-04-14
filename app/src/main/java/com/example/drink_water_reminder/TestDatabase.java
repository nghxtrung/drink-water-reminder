package com.example.drink_water_reminder;

import java.util.ArrayList;
import java.util.List;

public class TestDatabase {
    private static List<DrinkAlarm> DRINKALARMLIST = new ArrayList<>();

    static {
        createData();
    }

    private static void createData() {
        DRINKALARMLIST.add(new DrinkAlarm(R.drawable.ic_bottle_1, 300, "12:00"));
        DRINKALARMLIST.add(new DrinkAlarm(R.drawable.ic_bottle_1, 300, "13:00"));
        DRINKALARMLIST.add(new DrinkAlarm(R.drawable.ic_bell_color, 0, "14:00"));
    }

    public static void addData(DrinkAlarm drinkAlarm) {
        DRINKALARMLIST.add(drinkAlarm);
    }

    public static List<DrinkAlarm> getData() {
        return DRINKALARMLIST;
    }
}
