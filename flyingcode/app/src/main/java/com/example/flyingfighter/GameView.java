package com.example.flyingfighter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Ennemi[] ennemis;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Laser> lasers;
    private int sound;
    private Flight flight;
    private GameActivity activity;
    private Background background1, background2;

    private SensorManager mSensorManager;
    float tmpPos;

    public GameView(GameActivity activity, SensorManager sensor, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                    .build();


        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        sound = soundPool.load(activity, R.raw.shoot, 1);

        mSensorManager = sensor;
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        background1 = new Background(screenX,screenY,getResources());
        background2 = new Background(screenX,screenY,getResources());

        flight = new Flight(this, screenY, getResources());

        lasers = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        ennemis = new Ennemi[4];

        for(int i = 0; i < 4 ; i++){

            Ennemi ennemi = new Ennemi(getResources());
            ennemis[i] = ennemi;
        }

        random = new Random();
    }

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            if(isPlaying) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                if (z > 0.4) {
                    flight.isGoingUp = true;
                    tmpPos = flight.y - z * screenRatioY * 4;

                    if(tmpPos < 0) {
                        flight.y = 0;
                    }
                    else {
                        flight.y = Math.round(tmpPos);
                    }
                }
                else if(z < 0.4){
                    flight.isGoingUp = false;
                    tmpPos = flight.y + Math.abs(z) * screenRatioY * 4;

                    if(tmpPos >= screenY - flight.height){
                        flight.y = screenY - flight.height;
                    }
                    else{
                        flight.y = Math.round(tmpPos);
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Nothing special to do here
        }
    };

    @Override
    public void run() {

        while (isPlaying){
            update ();
            draw ();
            sleep ();
        }

    }

    private void update (){

        background1.x -= 10;
        background2.x -= 10;

        if(background1.x + background1.background.getWidth() < 5 ) {
            background1.x = screenX;
        }

        if(background2.x + background2.background.getWidth() <  5 ) {
            background2.x = screenX;
        }

        List<Laser> trash = new ArrayList<>();

        for(Laser laser: lasers) {
            if (laser.x > screenX)
                trash.add(laser);

            laser.x += 50 * screenRatioX;

            for(Ennemi ennemi : ennemis) {
                if(Rect.intersects(ennemi.getCollisionShape(), laser.getCollisionShape())) {
                    score++;
                    ennemi.x = -500;
                    laser.x = screenX + 500;
                    ennemi.wasShot = true;
                }

            }

        }

        for(Laser laser: trash) {
            lasers.remove(laser);
        }

        for (Ennemi ennemi : ennemis) {

            ennemi.x -= ennemi.speed;

            if (ennemi.x + ennemi.width < 0) {

                if (!ennemi.wasShot) {
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX);
                ennemi.speed = random.nextInt(bound);

                if(ennemi.speed < 10 * screenRatioX)
                    ennemi.speed = (int) (10 * screenRatioX);

                ennemi.x = screenX;
                ennemi.y = random.nextInt(screenY - ennemi.height);

                ennemi.wasShot = false;
            }



            if(Rect.intersects(ennemi.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true;
                return;
            }
        }
    }

    //Paint debugPaint = new Paint();

    private void draw (){

        if(getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Ennemi ennemi : ennemis) {
                canvas.drawBitmap(ennemi.getEnnemi(), ennemi.x, ennemi.y, paint);
                //debugPaint.setColor(Color.parseColor("#77FF0000"));
                //canvas.drawRect(ennemi.getCollisionShape(), debugPaint);
            }
            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if(isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }



            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);
            //debugPaint.setColor(Color.parseColor("#77FF0000"));
            //canvas.drawRect(flight.getCollisionShape(), debugPaint);

            for(Laser laser: lasers) {
                canvas.drawBitmap(laser.laser, laser.x, laser.y, paint);
                //debugPaint.setColor(Color.parseColor("#77FF0000"));
                //canvas.drawRect(laser.getCollisionShape(), debugPaint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void sleep (){
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaying = true;
        mSensorManager.registerListener(
                accelerometerListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME
        );
        thread = new Thread(this);
        thread.start();

    }

    public void pause (){
        try {
            isPlaying = false;
            mSensorManager.unregisterListener(accelerometerListener);
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void newBullet() {

        if(!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);

        Laser laser = new Laser(getResources());
        laser.x = flight.x + flight.width;
        laser.y = flight.y + (flight.height / 2);
        lasers.add(laser);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < screenX / 2);{
                  //  flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                    //flight.isGoingUp = false;
                if(event.getX() < screenX / 2);{
                    flight.toLaser++;
                }
                    break;

        }

        return true;
    }
}
