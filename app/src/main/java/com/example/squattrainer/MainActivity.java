package com.example.squattrainer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Button startCalibration = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartCalibration();
    }

    private void buttonStartCalibration() {
        startCalibration = findViewById(R.id.startCalibration);

        startCalibration.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CalibrationActivity.class);
            startActivity(intent);
        });
    }
}