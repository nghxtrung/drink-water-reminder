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

public class DrinkLogActivity extends AppCompatActivity {
    ListView listViewDrinkLog;
    String mTitle[]={"2400 fl oz","300 fl oz","300 fl oz","300 fl oz","300 fl oz"
            ,"300 fl oz"};
    int images[] = {R.drawable.circumference, R.drawable.water,R.drawable.water,R.drawable.water
            ,R.drawable.water,R.drawable.water};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_log);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//action return to menu toolbar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.title_toolbar)));

        listViewDrinkLog = findViewById(R.id.listViewDrinkLog);
        MyAdapter adapter = new MyAdapter(this,mTitle,images);
        listViewDrinkLog.setAdapter(adapter);
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        int rImgs[];

        MyAdapter (Context c,String title[],int imgs[]){
            super(c,R.layout.listview_drinklog,R.id.textView1,title);
            this.context=c;
            this.rTitle=title;
            this.rImgs=imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listViewDrinkLog =layoutInflater.inflate(R.layout.listview_setting,parent,false);
            ImageView images = listViewDrinkLog.findViewById(R.id.image);
            TextView myTitle =listViewDrinkLog.findViewById(R.id.textView1);

            //set our resource on view
            images.setImageResource(rImgs[position]);
            myTitle.setText((rTitle[position]));

            return listViewDrinkLog;
        }
    }
}