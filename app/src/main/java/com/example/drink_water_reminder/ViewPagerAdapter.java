package com.example.drink_water_reminder;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WeekFragment();

            case 1:
                return new MonthFragment();
            case 2:
                return new YearFragment();
            default:
                return new WeekFragment();
        }
    }


    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle (int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Week";
                break;
            case 1:
                title = "Month";
                break;
            case 2:
                title = "Year";
                break;
        }
        return title;
    }
}
