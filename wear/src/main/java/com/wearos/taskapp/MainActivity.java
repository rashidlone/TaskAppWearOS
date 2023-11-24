package com.wearos.taskapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.RemoteInput;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvTask;
    Button btnAddTask;
    ArrayAdapter aa;
    ArrayList<Tasks> tasksList;
    DBHelper dbh = new DBHelper(MainActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvTask = findViewById(R.id.lvTask);
        btnAddTask = findViewById(R.id.btnAddTask);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        CharSequence reply = null;
        Intent intent = getIntent();
        int replyId = intent.getIntExtra("id", 0);

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null){
            reply = remoteInput.getCharSequence("status");
        }

        if(reply != null){
            Toast.makeText(MainActivity.this, "You have indicated: " + reply,
                    Toast.LENGTH_SHORT).show();
            dbh.deleteTask(replyId);
            dbh.close();
            finish();
        }
        tasksList = new ArrayList<Tasks>();
        tasksList.addAll(dbh.getAllTasks());
        dbh.close();

        aa = new TaskAdapter(MainActivity.this, R.layout.row, tasksList);
        lvTask.setAdapter(aa);
        aa.notifyDataSetChanged();
    }
}