package com.sristi.birthdayreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String ringtoneUri = intent.getStringExtra("ringtoneUri");

        Toast.makeText(context, "Reminder: " + title, Toast.LENGTH_LONG).show();

        if (ringtoneUri != null && !ringtoneUri.isEmpty()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(ringtoneUri));
            mediaPlayer.start();
        }
    }
}
