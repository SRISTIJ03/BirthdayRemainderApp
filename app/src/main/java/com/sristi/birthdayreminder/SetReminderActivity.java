package com.sristi.birthdayreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SetReminderActivity extends AppCompatActivity {

    private Uri selectedRingtoneUri;
    private ReminderDatabaseHelper databaseHelper;
    private int reminderId = -1; // Default value for new reminders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        EditText reminderTitle = findViewById(R.id.reminderTitle);
        DatePicker datePicker = findViewById(R.id.datePicker);
        TimePicker timePicker = findViewById(R.id.timePicker);
        Button selectMusicButton = findViewById(R.id.selectMusicButton);
        Button saveReminderButton = findViewById(R.id.saveReminderButton);

        databaseHelper = new ReminderDatabaseHelper(this);

        // Retrieve the passed data from the intent for editing
        Intent intent = getIntent();
        reminderId = intent.getIntExtra("id", -1);
        if (reminderId != -1) {
            String title = intent.getStringExtra("title");
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");

            reminderTitle.setText(title);

            // Parse and set the date and time for editing
            String[] dateParts = date.split("-");
            String[] timeParts = time.split(":");
            datePicker.updateDate(Integer.parseInt(dateParts[0]),
                    Integer.parseInt(dateParts[1]) - 1,
                    Integer.parseInt(dateParts[2]));
            timePicker.setHour(Integer.parseInt(timeParts[0]));
            timePicker.setMinute(Integer.parseInt(timeParts[1]));
        }

        selectMusicButton.setOnClickListener(v -> {
            Intent ringtoneIntent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            startActivityForResult(ringtoneIntent, 999);
        });

        saveReminderButton.setOnClickListener(v -> {
            String title = reminderTitle.getText().toString();
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute, 0);

            String formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
            String formattedTime = String.format("%02d:%02d", hour, minute);

            if (reminderId == -1) {
                // Add new reminder
                databaseHelper.addReminder(title, formattedDate, formattedTime);
                setAlarm(calendar, title);
                Toast.makeText(this, "Reminder saved and alarm set!", Toast.LENGTH_SHORT).show();
            } else {
                // Update existing reminder
                databaseHelper.updateReminder(reminderId, title, formattedDate, formattedTime);
                setAlarm(calendar, title);
                Toast.makeText(this, "Reminder updated!", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == RESULT_OK) {
            selectedRingtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        }
    }

    private void setAlarm(Calendar calendar, String title) {
        try {
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("ringtoneUri", selectedRingtoneUri != null ? selectedRingtoneUri.toString() : "");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            Log.e("ReminderApp", "Error setting alarm", e);
        }
    }
}
