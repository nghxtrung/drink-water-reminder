package com.example.drink_water_reminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    ListView listView;
    String mTitle[]={"Reminder","General","Backup & Restore","Connect Apps","Widgets"
            ,"Rate us","Restore purchase","Bug report & Feedback","Other","Version: 4.326.265"};
    int images[] = {R.drawable.bell,R.drawable.settings,R.drawable.backup,R.drawable.link
            ,R.drawable.menu,R.drawable.star,R.drawable.synchronize,R.drawable.folder
            ,R.drawable.more,R.drawable.exclamation};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//action return to menu toolbar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_toolbar)));

        listView = findViewById(R.id.listViewDrinkLog);
        MyAdapter adapter = new MyAdapter(this,mTitle,images);
        listView.setAdapter(adapter);


    }
    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String rTitle[];
        int rImgs[];

        MyAdapter (Context c,String title[],int imgs[]){
            super(c,R.layout.listview_setting,R.id.textView1,title);
            this.context=c;
            this.rTitle=title;
            this.rImgs=imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listview_setting =layoutInflater.inflate(R.layout.listview_setting,parent,false);
            ImageView images = listview_setting.findViewById(R.id.image);
            TextView myTitle =listview_setting.findViewById(R.id.textView1);

            //set our resource on view
            images.setImageResource(rImgs[position]);
            myTitle.setText((rTitle[position]));

            return listview_setting;
        }
    }
}