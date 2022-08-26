package com.beatriz.navegaactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TerceiraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terceira);

        // enables u button to go back to previous page
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}