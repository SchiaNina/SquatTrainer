package com.example.squattrainer.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.example.squattrainer.interfaces.IGravitySensor;

import java.util.Arrays;

public class GravitySensor implements SensorEventListener {

    private final String TAG = "Gravitometer";
    private SensorManager sensorManager = null;
    private Sensor gravitometer = null;
    private IGravitySensor iGravitySensor = null;


    public GravitySensor(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            gravitometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }
        this.iGravitySensor = (IGravitySensor) context;
    }

    public void start() {
        sensorManager.registerListener(this, gravitometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        Log.i(TAG, Arrays.toString(sensorEvent.values));

        iGravitySensor.onNewValuesAvailable(x,y,z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.i(TAG, "onAccuracyChanged");
    }
}
