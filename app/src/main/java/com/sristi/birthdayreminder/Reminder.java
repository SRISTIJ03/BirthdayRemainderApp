package com.sristi.birthdayreminder;

public class Reminder {
    private int id;
    private String title;
    private String date;  // Added date field
    private String time;  // Added time field

    public Reminder(int id, String title, String date, String time) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

