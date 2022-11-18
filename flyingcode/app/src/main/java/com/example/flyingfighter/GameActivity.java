package com.example.flyingfighter;

import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe [GameActivity]
 * @description cette classe représente le moteur de GameView.
 */
public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Mettre en fullscreean le gameactivity
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView(this, mSensorManager, point.x, point.y);

        setContentView(gameView); //Afficher le gameview sur l'écran mais il faut aussi faire la pause et le resume

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause(); //Affichage
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume(); //Affichage
    }
}