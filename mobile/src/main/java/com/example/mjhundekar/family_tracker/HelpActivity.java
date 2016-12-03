package com.example.mjhundekar.family_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);


    }

    public void select_start_time(View view) {
        Toast.makeText(this,"Help request sent!!",Toast.LENGTH_SHORT).show();
    }
}
