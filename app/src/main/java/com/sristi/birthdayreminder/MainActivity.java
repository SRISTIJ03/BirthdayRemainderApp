package com.sristi.birthdayreminder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button setReminderButton = findViewById(R.id.setReminderButton);
        Button viewRemindersButton = findViewById(R.id.viewRemindersButton);

        setReminderButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetReminderActivity.class);
            startActivity(intent);
        });

        viewRemindersButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewRemindersActivity.class);
            startActivity(intent);
        });
    }
}
