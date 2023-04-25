package com.example.diceroller2;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int delayTime = 20;
    private int rollAnimations = 40;
    private int [] diceImages = new int[] {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3, R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    private Random random = new Random();
    private TextView tvHelp;
    private ImageView dice1;
    private ImageView dice2;
    private LinearLayout diceContainer;

    private float minimumChange = 5f;

    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHelp = findViewById(R.id.tvHelp);
        diceContainer = findViewById(R.id.diceContainer);
        dice1 = findViewById(R.id.dice1);
        dice2 = findViewById(R.id.dice2);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


    }

    private SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.values[0]>minimumChange || sensorEvent.values[1]>minimumChange || sensorEvent.values[2]>minimumChange || sensorEvent.values[0]<-minimumChange || sensorEvent.values[1]<-minimumChange || sensorEvent.values[2]<-minimumChange){
                try {
                    rollDice();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private void rollDice() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i=0; i < rollAnimations; i++){
                    int dices1 = random.nextInt(6) + 1;
                    int dices2 = random.nextInt(6) + 1;
                    dice1.setImageResource(diceImages[dices1 - 1]);
                    dice2.setImageResource(diceImages[dices2 - 1]);

                    try {
                        Thread.sleep(delayTime);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeSensorListener);
    }
}