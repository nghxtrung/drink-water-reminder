package com.example.drink_water_reminder;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MyAdapter  extends BaseAdapter {

    private List<RemindersSetting> listData;
    private LayoutInflater layoutInflater;
    private Context context;


    public MyAdapter(Context aContext, List<RemindersSetting> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_clock_row, null);
            holder = new ViewHolder();
            holder.hourAndminutes = (TextView) convertView.findViewById(R.id.item_alarm_time);
            holder.day = (TextView) convertView.findViewById(R.id.item_alarm_day);
            holder.statusSwitch = (Switch) convertView.findViewById(R.id.item_alarm_status);
            holder.moreButton = (ImageButton) convertView.findViewById(R.id.item_alarm_more);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        RemindersSetting remindersSetting = this.listData.get(position);
        holder.hourAndminutes.setText(remindersSetting.getHour() + ":" + remindersSetting.getMinute());
        String day = "";
        if (remindersSetting.isMonday() == true) {
            day += "Mon,";
        }
        if (remindersSetting.isThursday() == true) {
            day += "Thurs,";
        }
        if (remindersSetting.isWednesday() == true) {
            day += "Wed,";
        }
        if (remindersSetting.isTuesday() == true) {
            day += "Tues,";
        }
        if (remindersSetting.isFriday() == true) {
            day += "Fri,";
        }
        if (remindersSetting.isSaturday() == true) {
            day += "Satur,";
        }
        if (remindersSetting.isSunday() == true) {
            day += "Sun,";
        }
        holder.day.setText(day);
        holder.statusSwitch.setChecked(remindersSetting.isStatus());
        holder.statusSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                remindersSetting.setStatus(holder.statusSwitch.isChecked());
            }
        });
        holder.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu optionsMenu;
                PopupMenu popup = new PopupMenu(context, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.option_menu_clock, popup.getMenu());
                popup.show();
                optionsMenu = popup.getMenu();
                MenuItem item = optionsMenu.findItem(R.id.itemEdit);
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setView(R.layout.activity_update_reminder_setting);
                        Dialog dialog = builder.create();
                        dialog.show();
                        Button buttonCancle = dialog.findViewById(R.id.buttonCancle);
                        buttonCancle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        RemindersSetting remindersSetting = listData.get(position);
                        EditText editTextTime = dialog.findViewById(R.id.editTextTimeDialog);
                        editTextTime.setText(remindersSetting.getHour()+":"+remindersSetting.getMinute());
                        editTextTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final Calendar calendar = Calendar.getInstance();
                                int gio = calendar.get(Calendar.HOUR_OF_DAY);
                                int phut = calendar.get(Calendar.MINUTE);
                                TimePickerDialog timePickerDialog  = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i,  int i1) {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                                        calendar.set(0,0,0,i,i1);
                                        editTextTime.setText(simpleDateFormat.format(calendar.getTime()));
                                    }
                                },gio,phut,true);
                                timePickerDialog.show();
                            }
                        });
                        CheckBox checkBoxMonday = dialog.findViewById(R.id.checkBoxMonDay);
                        CheckBox checkBoxTuesday = dialog.findViewById(R.id.checkBoxTuesday);
                        CheckBox checkWednesday = dialog.findViewById(R.id.checkBoxWed);
                        CheckBox checkBoxThursday = dialog.findViewById(R.id.checkBoxThurs);
                        CheckBox checkBoxFriday = dialog.findViewById(R.id.checkBoxFriday);
                        CheckBox checkBoxStaturday = dialog.findViewById(R.id.checkBoxSaturday);
                        CheckBox checkBoxSunday = dialog.findViewById(R.id.checkBoxSunday);

                        checkBoxMonday.setChecked(remindersSetting.isMonday());
                        checkBoxTuesday.setChecked(remindersSetting.isTuesday());
                        checkWednesday.setChecked(remindersSetting.isWednesday());
                        checkBoxThursday.setChecked(remindersSetting.isThursday());
                        checkBoxFriday.setChecked(remindersSetting.isFriday());
                        checkBoxStaturday.setChecked(remindersSetting.isSaturday());
                        checkBoxSunday.setChecked(remindersSetting.isSunday());

                        Button buttonUpdate = dialog.findViewById(R.id.buttonUpdate);
                        buttonUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String time = editTextTime.getText().toString();
                                String[] result = time.split(":");
                                remindersSetting.setHour(Integer.parseInt(result[0]));
                                remindersSetting.setMinute(Integer.parseInt(result[1]));
                                remindersSetting.setMonday(checkBoxMonday.isChecked());
                                remindersSetting.setThursday(checkBoxTuesday.isChecked());
                                remindersSetting.setWednesday(checkWednesday.isChecked());
                                remindersSetting.setThursday(checkBoxThursday.isChecked());
                                remindersSetting.setFriday(checkBoxFriday.isChecked());
                                remindersSetting.setSaturday(checkBoxStaturday.isChecked());
                                remindersSetting.setSunday(checkBoxSunday.isChecked());
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

                        return true;
                    }
                });
                MenuItem item1 = optionsMenu.findItem(R.id.itemDelete);
                item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                         listData.remove(position);
                                         notifyDataSetChanged();
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                        return true;
                    }
                });
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView hourAndminutes;
        TextView day;
        Switch statusSwitch;
        ImageButton moreButton;
    }


}