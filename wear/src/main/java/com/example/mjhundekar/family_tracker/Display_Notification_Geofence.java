package com.example.mjhundekar.family_tracker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Display_Notification_Geofence extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification_geofence);
        mTextView = (TextView) findViewById(R.id.text);
    }
}
