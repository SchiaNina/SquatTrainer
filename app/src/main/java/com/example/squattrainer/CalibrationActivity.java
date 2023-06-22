package com.example.squattrainer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.squattrainer.interfaces.IGravitySensor;
import com.example.squattrainer.sensors.GravitySensor;
import com.example.squattrainer.utils.Position;

import java.util.ArrayList;
import java.util.List;


public class CalibrationActivity extends AppCompatActivity implements IGravitySensor {

    private Button start = null;
    private Button next = null;
    private TextView results = null;
    private ImageView arrow = null;
    private ImageView check = null;
    private GravitySensor gravitySensor = null;
    private Position startPos;
    private Position endPos;
    private boolean startClick = true;
    private boolean endCal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        start = findViewById(R.id.start);
        next = findViewById(R.id.next);
        arrow = findViewById(R.id.arrowCal);
        check = findViewById(R.id.check);
        results = findViewById(R.id.result);

        next.setEnabled(false);

        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        gravitySensor = new GravitySensor(this);
        start.setOnClickListener(view -> {
            next.setEnabled(false);
            check.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
            CountDownTimer countDownTimer = new CountDownTimer(4000,1000){
                @Override
                public void onTick(long l) {
                    //do nothing...
                }
                @Override
                public void onFinish() {
                    endCal = true;
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,1000);
                }
            };
            toneGen1.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT,1000);
            countDownTimer.start();
            results.setText("Calibrazione in corso...");
            gravitySensor.start();
        });

        next.setOnClickListener(view -> {
            Intent intent = new Intent(CalibrationActivity.this, CounterActivity.class);
            intent.putExtra("startPos",startPos.toArray());
            intent.putExtra("endPos",endPos.toArray());
            startActivity(intent);
        });
    }

    @Override
    public void onNewValuesAvailable(float x,float y,float z) {
        if (endCal) {
            if (startClick) {
                startPos = new Position(x,y,z);
                arrow.setRotation(90);
                endCal = false;
                startClick = false;
                start.performClick();
            } else {
                endPos = new Position(x,y,z);
                results.setText("Calibrazione terminata!");
                start.setText("Reset");
                gravitySensor.stop();
                startClick = true;
                next.setEnabled(true);
                arrow.setVisibility(View.GONE);
                check.setVisibility(View.VISIBLE);
                arrow.setRotation(270);
            }
            endCal = false;
        }
    }
}