package com.sristi.birthdayreminder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewRemindersActivity extends AppCompatActivity {

    private ReminderAdapter reminderAdapter;
    private ArrayList<Reminder> reminders;
    private ReminderDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminders);

        ListView remindersListView = findViewById(R.id.remindersListView);
        Button backButton = findViewById(R.id.backButton);

        databaseHelper = new ReminderDatabaseHelper(this);

        // Fetch all reminders from the database
        reminders = databaseHelper.getAllReminders();

        reminderAdapter = new ReminderAdapter(this, reminders);
        remindersListView.setAdapter(reminderAdapter);

        backButton.setOnClickListener(v -> finish());
    }

    public void deleteReminder(int position) {
        Reminder reminder = reminders.get(position);

        // Delete the reminder from the database
        databaseHelper.deleteReminder(reminder.getId());

        // Remove the reminder from the list and refresh the adapter
        reminders.remove(position);
        reminderAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Reminder deleted!", Toast.LENGTH_SHORT).show();
    }

    public void editReminder(int position) {
        Reminder reminder = reminders.get(position);

        Intent intent = new Intent(this, SetReminderActivity.class);
        // Pass the reminder details to SetReminderActivity
        intent.putExtra("id", reminder.getId());  // Pass reminder ID for editing
        intent.putExtra("title", reminder.getTitle());
        intent.putExtra("date", reminder.getDate());  // Pass the date as well
        intent.putExtra("time", reminder.getTime());  // Pass the time as well

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the reminders list after returning to this activity
        reminders.clear();
        reminders.addAll(databaseHelper.getAllReminders());
        reminderAdapter.notifyDataSetChanged();
    }
}
