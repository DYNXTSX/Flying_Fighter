package com.example.flyingfighter;

import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;


public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myTag", "This is my message");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView(this, mSensorManager, point.x, point.y);

        setContentView(gameView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}