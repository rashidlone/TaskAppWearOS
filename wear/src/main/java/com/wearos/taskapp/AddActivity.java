package com.wearos.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    FloatingActionButton btnAddTask, btnCancel;
    EditText etName, etRemind;
    int reqCode = 12345;
    ArrayList<Tasks> tasksList;
    private Calendar calendar;
    private long selectedTimeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        calendar = Calendar.getInstance();

        btnCancel = findViewById(R.id.btnCancel);
        btnAddTask = findViewById(R.id.btnAddTask);
        etName = findViewById(R.id.etName);
        etRemind = findViewById(R.id.etRemind);
       etRemind.setFocusable(false);
        etRemind.setOnClickListener(view -> {

            showDateTimePickerDialog();

        });

        btnAddTask.setOnClickListener(view -> {


            String dataName = etName.getText().toString();
            String dt = etRemind.getText().toString();

            if(dataName.isEmpty())
            {
                Toast.makeText(AddActivity.this, "Add task name first!", Toast.LENGTH_SHORT).show();

            }else {
                DBHelper dbh = new DBHelper(AddActivity.this);
                long inserted_id = dbh.addTask(dataName, dt);
                tasksList = new ArrayList<Tasks>();
                tasksList.addAll(dbh.getAllTasks());
                int indexId = tasksList.size()-1;
                Tasks object = tasksList.get(indexId);
                int id = object.getId();
                dbh.close();
                if (inserted_id != -1) {

                    Intent i = new Intent(AddActivity.this, NotificationReceiver.class);
                    i.putExtra("task", dataName);
                    i.putExtra("date", dt);
                    i.putExtra("id", id);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivity.this, reqCode, i, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

                    AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, selectedTimeInMillis, pendingIntent);

                    Toast.makeText(AddActivity.this, "Task added!", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }//else


        });

        btnCancel.setOnClickListener(view -> finish());


    }


    private void showDateTimePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, year, month, day) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    // Show time picker after date is set
                    showTimePickerDialog();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (timePicker, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);

                    // Update UI or perform any other action with the selected date and time
                    updateDateTimeButtonText();

                    // Get the time in milliseconds
                    selectedTimeInMillis = calendar.getTimeInMillis();
                    // Now you can use `selectedTimeInMillis` as needed.
                    // For example, you can store it in a variable, pass it to another method, etc.

                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );

        timePickerDialog.show();
    }

    private void updateDateTimeButtonText() {
        // Update your UI with the selected date and time
        Date date = calendar.getTime();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        etRemind.setText(String.format("%s", format1.format(date)));
    }


}