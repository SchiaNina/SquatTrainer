package com.example.squattrainer;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.squattrainer.interfaces.IGravitySensor;
import com.example.squattrainer.sensors.GravitySensor;
import com.example.squattrainer.utils.Position;

import java.util.Locale;

public class CounterActivity extends AppCompatActivity implements IGravitySensor {

    private Position startPos;
    private Position endPos;
    private Button start = null;
    private Button stop = null;
    private TextView counter = null;
    private TextView pausedCounter = null;
    private ImageView arrow = null;
    private GravitySensor gravitySensor = null;
    private boolean discesa = true;
    private boolean isStopped = false;
    private int reps = 0;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        start = findViewById(R.id.startCounter);
        stop = findViewById(R.id.stopCounter);
        counter = findViewById(R.id.counter);
        pausedCounter = findViewById(R.id.pausedCounter);
        arrow = findViewById(R.id.arrowCounter);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            startPos = new Position(extras.getFloatArray("startPos"));
            endPos = new Position(extras.getFloatArray("endPos"));
        }

        tts=new TextToSpeech(getApplicationContext(), status -> {
            if(status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.UK);
            }
        });

        counter.setText("0");
        stop.setEnabled(false);

        gravitySensor = new GravitySensor(this);

        start.setOnClickListener(view -> {
            if (!isStopped)
                gravitySensor.stop();

            counter.setText("0");
            reps = 0;
            pausedCounter.setVisibility(View.GONE);
            start.setEnabled(false);
            stop.setEnabled(false);

            tts.speak("3,2,1,go", TextToSpeech.QUEUE_FLUSH, null);

            CountDownTimer countDownTimer = new CountDownTimer(2800,1000){

                @Override
                public void onTick(long l) {
                    //do nothing...
                }
                @Override
                public void onFinish() {
                    start.setEnabled(true);
                    stop.setEnabled(true);
                    arrow.setVisibility(View.VISIBLE);
                    gravitySensor.start();
                    start.setText("RESET");
                    arrow.setRotation(90);
                    tts.speak("down", TextToSpeech.QUEUE_FLUSH, null);
                }
            };
            countDownTimer.start();
        });

        stop.setOnClickListener(view -> {
            if (isStopped) {
                arrow.setVisibility(View.VISIBLE);
                pausedCounter.setVisibility(View.GONE);
                gravitySensor.start();
                stop.setText("STOP");
                isStopped = false;
            } else {
                arrow.setVisibility(View.GONE);
                pausedCounter.setVisibility(View.VISIBLE);
                gravitySensor.stop();
                stop.setText("RESUME");
                isStopped = true;
            }
        });
    }

    @Override
    public void onNewValuesAvailable(float x, float y, float z) {
        if (discesa) {
            if (endPos.isEqualWithTolerance(x,y,z)) {
                discesa = false;
                arrow.setRotation(270);
                tts.speak("up", TextToSpeech.QUEUE_FLUSH, null);
            }
        } else {
            if (startPos.isEqualWithTolerance(x,y,z)) {
                discesa = true;
                reps++;
                counter.setText(""+reps);
                arrow.setRotation(90);
                tts.speak(reps+",down", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }
}