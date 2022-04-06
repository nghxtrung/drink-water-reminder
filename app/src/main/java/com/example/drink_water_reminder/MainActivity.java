package com.example.drink_water_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    DrinkAdapter drinkAdapter;
    GridView drinkGridView;
    TextView drinkTargetTextView;
    ProgressBar drinkProgressBar;
    int sumOfVolume = 0;
    int yearDrink, monthDrink, dayDrink;
    int hourDrinkDetail, minuteDrinkDetail;
    ImageView drinkNowImageView;
    RecyclerView drinkNowRecyclerView, drinkAddRecyclerView;
    DrinkItemNowAdapter drinkItemNowAdapter;
    DrinkItemAddAdapter drinkItemAddAdapter;
    List<DrinkNow> drinkNowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Done")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this,"Get Started!",Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        LayoutInflater inflater = getLayoutInflater();
        View welcomeAlertView = inflater.inflate(R.layout.alert_welcome, null);
        builder.setView(welcomeAlertView);
        builder.show();

        Toolbar mainToolBar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Menu");
        mainToolBar.setNavigationIcon(R.drawable.ic_menu);
        mainToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "menu", Toast.LENGTH_SHORT).show();
            }
        });
        mainToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_calendar));

        drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationHeader);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mainToolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        List<DrinkAlarm> drinkAlarmList = new ArrayList<DrinkAlarm>();
        drinkAlarmList.add(new DrinkAlarm(R.drawable.ic_bottle_1, 300, "12:00"));
        drinkAlarmList.add(new DrinkAlarm(R.drawable.ic_bottle_1, 300, "13:00"));
        drinkAlarmList.add(new DrinkAlarm(R.drawable.ic_bell_color, 0, "14:00"));

        drinkProgressBar = findViewById(R.id.drinkTargetProgressBar);
        drinkTargetTextView = findViewById(R.id.drinkTargetTextView);
        setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList);
        findViewById(R.id.drinkTargetGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinkTargetAlert(drinkTargetTextView, drinkProgressBar, sumOfVolume, true, "");
            }
        });

        drinkGridView = findViewById(R.id.drinkGridView);
        drinkAdapter = new DrinkAdapter(this, drinkAlarmList);
        drinkGridView.setAdapter(drinkAdapter);
        drinkGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position < drinkAlarmList.size() - 1)
                    showDrinkDetailAlert(drinkAlarmList, position, true, "", "");
            }
        });

        ImageView addImageView = findViewById(R.id.addImageView);
        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drinkAlarmList.add(new DrinkAlarm(TestDB.image, TestDB.volume, "13:30"));
                Collections.sort(drinkAlarmList);
                drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                drinkGridView.setAdapter(drinkAdapter);
                setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList);
            }
        });

        drinkNowList = new ArrayList<DrinkNow>();
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_1, 300));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_2, 400));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_3, 600));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_4, 700));
        drinkNowList.add(new DrinkNow(R.drawable.ic_bottle_5, 1000));
        drinkNowImageView = findViewById(R.id.drinkNowImageView);
        drinkNowImageView.setImageResource(TestDB.image);
        drinkNowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDrinkNowAlert();
            }
        });
    }

    private void setDrinkTarget(TextView drinkTargetTextView, ProgressBar drinkProgressBar, List<DrinkAlarm> drinkAlarmList) {
        sumOfVolume = 0;
        for (int i = 0; i < drinkAlarmList.size() - 1; i++) {
            sumOfVolume += drinkAlarmList.get(i).getVolume();
        }
        int percentVolume = Math.round(((float) sumOfVolume/2145) * 100);
        drinkProgressBar.setProgress(percentVolume);
        drinkTargetTextView.setText(sumOfVolume + "/" + "2145ml");
    }

    private void showDrinkTargetAlert(TextView drinkTargetTextView, ProgressBar drinkProgressBar, int sumOfVolume, boolean checkError, String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View drinkTargetAlertView = inflater.inflate(R.layout.drink_target_alert, null);
        EditText drinkTargetEditText = drinkTargetAlertView.findViewById(R.id.drinkTargetEditText);
        if (!checkError) {
            drinkTargetEditText.setError(error);
            drinkTargetEditText.requestFocus();
        }
        builder.setView(drinkTargetAlertView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (validateTargetVolume(drinkTargetEditText).equals("")) {
                    drinkTargetTextView.setText(sumOfVolume + "/" + drinkTargetEditText.getText().toString() + "ml");
                    int volume = Integer.parseInt(drinkTargetEditText.getText().toString());
                    int percentVolume = Math.round(((float) sumOfVolume/volume) * 100);
                    drinkProgressBar.setProgress(percentVolume);
                } else {
                    String error = validateTargetVolume(drinkTargetEditText);
                    showDrinkTargetAlert(drinkTargetTextView, drinkProgressBar, sumOfVolume, false, error);
                }
            }
        });
        builder.show();
    }

    private void showDrinkDetailAlert(List<DrinkAlarm> drinkAlarmList, int position, boolean checkError, String error, String currentTimeSet) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View drinkDetailAlertView = inflater.inflate(R.layout.drink_detail_alert, null);
        builder.setView(drinkDetailAlertView);
        TextView drinkTimeDetailTextView = drinkDetailAlertView.findViewById(R.id.drinkTimeDetailTextView);
        EditText drinkVolumeEditText = drinkDetailAlertView.findViewById(R.id.drinkVolumeEditText);
        drinkVolumeEditText.setText(Integer.toString(drinkAlarmList.get(position).getVolume()));
        drinkTimeDetailTextView.setText(drinkAlarmList.get(position).getTime());
        if (!checkError) {
            drinkTimeDetailTextView.setText(currentTimeSet);
            drinkVolumeEditText.setError(error);
            drinkVolumeEditText.requestFocus();
        }
        drinkTimeDetailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeItemAlert(drinkTimeDetailTextView);
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (validateVolume(drinkVolumeEditText).equals("")) {
                    String time = drinkTimeDetailTextView.getText().toString();
                    int volume = Integer.parseInt(drinkVolumeEditText.getText().toString());
                    drinkAlarmList.get(position).setTime(time);
                    drinkAlarmList.get(position).setVolume(volume);
                    Collections.sort(drinkAlarmList);
                    drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                    drinkGridView.setAdapter(drinkAdapter);
                } else {
                    String currentTimeSet = drinkTimeDetailTextView.getText().toString();
                    String error = validateVolume(drinkVolumeEditText);
                    showDrinkDetailAlert(drinkAlarmList, position, false, error, currentTimeSet);
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ImageView copyImageView = drinkDetailAlertView.findViewById(R.id.copyImageView);
        copyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int image = drinkAlarmList.get(position).getImage();
                String time = drinkAlarmList.get(position).getTime();
                int volume = drinkAlarmList.get(position).getVolume();
                drinkAlarmList.add(new DrinkAlarm(image, volume, time));
                Collections.sort(drinkAlarmList);
                drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                drinkGridView.setAdapter(drinkAdapter);
                setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList);
                alertDialog.dismiss();
            }
        });
        ImageView deleteImageView = drinkDetailAlertView.findViewById(R.id.deleteImageView);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drinkAlarmList.remove(position);
                Collections.sort(drinkAlarmList);
                drinkAdapter = new DrinkAdapter(MainActivity.this, drinkAlarmList);
                drinkGridView.setAdapter(drinkAdapter);
                setDrinkTarget(drinkTargetTextView, drinkProgressBar, drinkAlarmList);
                alertDialog.dismiss();
            }
        });
    }

    private String validateTargetVolume(EditText editText) {
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if (value <= 0)
                return "Bắt buộc lớn 0";
            else if (value > 2145)
                return "Bắt buộc nhỏ hơn";
            return "";
        } catch (NumberFormatException e) {
            return "Bắt buộc nhập số";
        }
    }

    private String validateVolume(EditText editText) {
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if (value <= 0)
                return "Bắt buộc lớn 0";
            return "";
        } catch (NumberFormatException e) {
            return "Bắt buộc nhập số";
        }
    }

    private String validateVolumeAddDrinkNow(EditText editText, int image) {
        try {
            int value = Integer.parseInt(editText.getText().toString());
            if (value <= 0)
                return "Bắt buộc lớn 0";
            else if (!checkDuplicateDrinkNow(image, value))
                return "Bắt buộc không được trùng";
            return "";
        } catch (NumberFormatException e) {
            return "Bắt buộc nhập số";
        }
    }

    private boolean checkDuplicateDrinkNow(int image, int volume) {
        boolean check = true;
        for (DrinkNow drinkNow : drinkNowList) {
            if (drinkNow.getImage() == image && drinkNow.getVolume() == volume) {
                check = false;
                break;
            }
        }
        return check;
    }

    private void showTimeItemAlert(TextView drinkTimeDetailTextView) {
        String drinkTimeDetail = drinkTimeDetailTextView.getText().toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        try {
            Date date = simpleDateFormat.parse(drinkTimeDetail);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            hourDrinkDetail = calendar.get(Calendar.HOUR_OF_DAY) == 0 ? 12 : calendar.get(Calendar.HOUR_OF_DAY);
            minuteDrinkDetail = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimePickerDialog.OnTimeSetListener drinkDetailOTSListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Calendar calendar = Calendar.getInstance();
                int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
                int minuteNow = calendar.get(Calendar.MINUTE);
                if (selectedHour > hourNow || (selectedHour <= hourNow && selectedMinute > minuteNow)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Thông báo")
                            .setMessage("Vui lòng chọn thời gian nhỏ hơn")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showTimeItemAlert(drinkTimeDetailTextView);
                                }
                            });
                    builder.show();
                } else {
                    hourDrinkDetail = selectedHour;
                    minuteDrinkDetail = selectedMinute;
                    String formatHour = hourDrinkDetail < 10 ? ("0" + hourDrinkDetail) : ("" + hourDrinkDetail);
                    String formatMinute = minuteDrinkDetail < 10 ? ("0" + minuteDrinkDetail) : ("" + minuteDrinkDetail);
                    drinkTimeDetailTextView.setText(formatHour + ":" + formatMinute);
                }
            }
        };
        TimePickerDialog drinkDetailTPD = new TimePickerDialog(MainActivity.this,
                drinkDetailOTSListener, hourDrinkDetail, minuteDrinkDetail, true);
        drinkDetailTPD.show();
    }

    private void showDrinkNowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View addListDrinkAlertView = inflater.inflate(R.layout.list_drink_now_alert, null);
        builder.setView(addListDrinkAlertView);
        drinkItemNowAdapter = new DrinkItemNowAdapter(MainActivity.this, drinkNowList);
        LinearLayoutManager drinkItemNowLLM = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        drinkNowRecyclerView = addListDrinkAlertView.findViewById(R.id.drinkItemNowRecyclerView);
        drinkNowRecyclerView.setAdapter(drinkItemNowAdapter);
        drinkNowRecyclerView.setLayoutManager(drinkItemNowLLM);
        builder.setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TestDB.indexNow = drinkItemNowAdapter.row_index;
                TestDB.image = drinkNowList.get(drinkItemNowAdapter.row_index).getImage();
                TestDB.volume = drinkNowList.get(drinkItemNowAdapter.row_index).getVolume();
                drinkNowImageView.setImageResource(TestDB.image);
            }
        })
        .setNegativeButton("Xoá", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                drinkNowList.remove(drinkItemNowAdapter.row_index);
                drinkItemNowAdapter.notifyItemRemoved(drinkItemNowAdapter.row_index);
                showDrinkNowAlert();
            }
        })
        .setNeutralButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDrinkAddAlert(true, "");
            }
        });
        builder.show();
    }

    private void showDrinkAddAlert(boolean checkError, String error) {
        LayoutInflater inflater = getLayoutInflater();
        View listDrinkNowAlertView = inflater.inflate(R.layout.add_list_drink_alert, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(listDrinkNowAlertView);
        Integer[] drinkItemAddImageList = {R.drawable.ic_bottle_1, R.drawable.ic_bottle_2,
                R.drawable.ic_bottle_3, R.drawable.ic_bottle_4,
                R.drawable.ic_bottle_5};
        drinkItemAddAdapter = new DrinkItemAddAdapter(MainActivity.this, drinkItemAddImageList);
        LinearLayoutManager drinkItemAddLLM = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        drinkAddRecyclerView = listDrinkNowAlertView.findViewById(R.id.drinkItemAddRecyclerView);
        drinkAddRecyclerView.setAdapter(drinkItemAddAdapter);
        drinkAddRecyclerView.setLayoutManager(drinkItemAddLLM);
        EditText drinkVolumeItemEditText = listDrinkNowAlertView.findViewById(R.id.drinkVolumeItemEditText);
        if (!checkError) {
            drinkVolumeItemEditText.setError(error);
            drinkVolumeItemEditText.requestFocus();
        }
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int image = drinkItemAddImageList[drinkItemAddAdapter.row_index];
                if (validateVolumeAddDrinkNow(drinkVolumeItemEditText, image).equals("")) {
                    String volume = ((TextView) listDrinkNowAlertView.findViewById(R.id.drinkVolumeItemEditText)).getText().toString();
                    drinkNowList.add(new DrinkNow(image, Integer.parseInt(volume)));
                    drinkItemNowAdapter.notifyItemInserted(drinkNowList.size() - 1);
                    closeKeyboard(listDrinkNowAlertView);
                    showDrinkNowAlert();
                } else {
                    String error = validateVolumeAddDrinkNow(drinkVolumeItemEditText, image);
                    showDrinkAddAlert(false, error);
                }
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showDrinkNowAlert();
            }
        });
        builder.show();
    }

    private void closeKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showDrinkDateAlert() {
        Calendar calendar = Calendar.getInstance();
        yearDrink = calendar.get(Calendar.YEAR);
        monthDrink = calendar.get(Calendar.MONTH);
        dayDrink = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = simpleDateFormat.parse(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    if (date.after(new Date())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Thông báo")
                                .setMessage("Vui lòng chọn thời gian nhỏ hơn")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        showDrinkDateAlert();
                                    }
                                });
                        builder.show();
                    } else {
                        yearDrink = selectedYear;
                        monthDrink = selectedMonth + 1;
                        dayDrink = selectedDay;
                        if (dayDrink == calendar.get(Calendar.DAY_OF_MONTH) &&
                            monthDrink == (calendar.get(Calendar.MONTH) + 1) &&
                            yearDrink == calendar.get(Calendar.YEAR))
                            getSupportActionBar().setTitle("Menu");
                        else if (dayDrink == (calendar.get(Calendar.DAY_OF_MONTH) - 1) &&
                                monthDrink == (calendar.get(Calendar.MONTH) + 1) &&
                                yearDrink == calendar.get(Calendar.YEAR))
                            getSupportActionBar().setTitle("Yesterday");
                        else {
                            getSupportActionBar().setTitle(dayDrink + "/" + monthDrink + "/" + yearDrink);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener,
                yearDrink, monthDrink, dayDrink);
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reminder:
                Toast.makeText(this, "remider", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.weight:
                return true;
            case R.id.today:
                getSupportActionBar().setTitle("Menu");
                return true;
            case R.id.yesterday:
                getSupportActionBar().setTitle("Yesterday");
                return true;
            case R.id.customDate:
                showDrinkDateAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.drinkMain:
                Toast.makeText(this, "drink main", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drinkLog:
                Toast.makeText(this, "drink log", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drinkReport:
                Toast.makeText(this, "drink report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.weightReport:
                Toast.makeText(this, "weight report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.reminder2:
                Toast.makeText(this, "remider2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}