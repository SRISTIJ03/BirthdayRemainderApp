package com.sristi.birthdayreminder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ReminderAdapter extends ArrayAdapter<Reminder> {
    private final ViewRemindersActivity activity;

    public ReminderAdapter(ViewRemindersActivity activity, ArrayList<Reminder> reminders) {
        super(activity, 0, reminders);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_reminder, parent, false);
        }

        Reminder reminder = getItem(position);

        TextView titleTextView = convertView.findViewById(R.id.reminderTitle);
        TextView dateTextView = convertView.findViewById(R.id.reminderDate);
        TextView timeTextView = convertView.findViewById(R.id.reminderTime);
        Button editButton = convertView.findViewById(R.id.editButton);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        titleTextView.setText(reminder.getTitle());
        dateTextView.setText(reminder.getDate());  // Ensure the date is set
        timeTextView.setText(reminder.getTime());

        editButton.setOnClickListener(v -> activity.editReminder(position));  // Correct method call
        deleteButton.setOnClickListener(v -> activity.deleteReminder(position));

        return convertView;
    }

    public void deleteReminder(int position) {
        Reminder reminder = getItem(position);
        ((ViewRemindersActivity) getContext()).deleteReminder(position);
    }

}
