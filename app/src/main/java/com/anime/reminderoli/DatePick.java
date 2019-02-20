package com.anime.reminderoli;

import android.app.AppComponentFactory;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePick extends AppCompatActivity {

    TextView tv;
    Calendar mCurrent;
    int day,month,year;

            @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_admin);
                tv = (TextView) findViewById(R.id.tglgantiadmin);

                mCurrent = Calendar.getInstance();

                day = mCurrent.get(Calendar.DAY_OF_MONTH);
                month = mCurrent.get(Calendar.MONTH);
                year = mCurrent.get(Calendar.YEAR);

                month = month+1;
                tv.setText(day+"/"+month+"/"+year);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(DatePick.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                monthOfYear = monthOfYear + 1;
                                tv.setText(dayOfMonth+"/"+monthOfYear+"/"+year);
                            }
                        }, year,month, day);
                        datePickerDialog.show();
                    }
                });
            }

}
