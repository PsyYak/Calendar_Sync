package com.example.caltest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int calenderId=-1;
                String calenderEmailAddress="xxx@gmail.com"; // Enter your email here
                String[] projection = new String[]{
                        CalendarContract.Calendars._ID,
                        CalendarContract.Calendars.ACCOUNT_NAME};
                ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(Uri.parse("content://com.android.calendar/calendars"), projection,
                       CalendarContract.Calendars.ACCOUNT_NAME + "=? and (" +
                                CalendarContract.Calendars.NAME + "=? or " +
                                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME + "=?)",
                        new String[]{calenderEmailAddress, calenderEmailAddress,
                                calenderEmailAddress}, null);

                if (cursor.moveToFirst()) {
                    if (cursor.getString(1).equals(calenderEmailAddress))
                        calenderId=cursor.getInt(0); //your calender id to be inserted in above your code

                }
                Calendar cal = Calendar.getInstance();
                ContentValues event = new ContentValues();
                event.put("calendar_id", calenderId); // Get the user calendarID to where to insert the event
                event.put("title", "Test Event2"); // Replace with any String for the title
                event.put("description", "Hiii Buddy"); // Replace with any String for the content of the event
                long startTime = cal.getTimeInMillis(); // Replace with event start date + time
                long endTime = cal.getTimeInMillis() + 60 * 60 * 1000; // Replace with event end date + time
                event.put("dtstart", startTime);
                event.put("dtend", endTime);
                event.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getTimeZone("Asia/Jerusalem"))); // Replace with calendar timezone
                event.put("allDay", 0); // 0 no ( appointment ) , 1 yes ( task )
                event.put("eventStatus", 1);// tentative 0, confirmed 1 canceled 2
                // public 3
                event.put("hasAlarm", 1); // 0 false, 1 true


                Uri eventsUri = getCalendarURI(true); // Get the relevant URI based on user OS version
                Uri url = getContentResolver().insert(eventsUri, event); // Insert the event to the Calendar


            }
        });
    }

    public Uri getCalendarURI(boolean eventUri) {
        Uri calendarURI = null;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            calendarURI = (eventUri) ? Uri.parse("content://calendar/events")
                    : Uri.parse("content://calendar/calendars");
        } else {
            calendarURI = (eventUri) ? Uri
                    .parse("content://com.android.calendar/events") : Uri
                    .parse("content://com.android.calendar/calendars");
        }
        return calendarURI;
    }
}