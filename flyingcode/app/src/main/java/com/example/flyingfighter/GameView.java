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

/**
 * Classe [GameView]
 * @description Classe qui va permettre l'affichage du jeu lorsqu'on est en pleine partie. le moteur est géré par GameActivity
 *
 * (thread) > thread du jeu
 * (boolean) isPlaying, isGameOver > booléens pour gérer la fin du jeu
 * (float) screenRatioX, screenRatioY > Le screenRatio permet de re calculer la taille de l'écran en fonction du tel utilisé, il est en public pour y avoir accès dans flight
 * (Paint) paint > Objet de la classe Paint pour dessiner le background et le reste
 * (Ennemi[]) ennemis > Array d'ennemis
 * (Background) background1, background2 > Il y a deux background pour gérer le mouvement de fond pendant le jeu
 *
 *
 */

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

    private SensorManager mSensorManager; //gestionnaire de capteurs (sensorManager)
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

        lasers = new ArrayList<>(); //On initialise la liste des lasers dans le constructeur

        background2.x = screenX; //les background sont instanciés au dessus et on met le bg2 en screenX comme ça il est placé juste après la fin de l'écran sur l'axe X, le x et y sont initialisés à 0 dans la classe Bg (bg=background)

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        ennemis = new Ennemi[4]; //4 ennemis sur l'écran

        for(int i = 0; i < 4 ; i++){

            Ennemi ennemi = new Ennemi(getResources());
            ennemis[i] = ennemi;
        }

        random = new Random();
    }

    /**
     * Cette méthode sert pour le déplacement de haut en bas de l'avion en utilisant le gyroscope du téléphone.
     */
    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            //On récupère les valeurs du capteur, on utilisera que le z
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

    /**
     * Méthode avec un loop tant que le jeu est lancé
     * On donne des positions initiales a l'objet volant sur l'écran et ensuite on le "draw" sur ces coordonnées de l'écran et on met à jour à chaque fois l'objet sur 5 points X puis on le re "draw"
     */
    @Override
    public void run() {

        while (isPlaying){
            update ();
            draw ();
            sleep ();
        }

    }

    private void update (){

        background1.x -= 10; //On modifie la position du BG sur l'axe X de 10 pixels et on modifie pas y car on veut que le bouger sur l'axe X
        background2.x -= 10; //Maintenant à chaque fois que la méthode update est appelée le bg se déplace de 10 pixels

        if(background1.x + background1.background.getWidth() < 5 ) {
            background1.x = screenX;
        }

        if(background2.x + background2.background.getWidth() <  5 ) { //On vérifie dès que le BG dépasse les 5 px avant la fin et si ça dépasse on le replace juste après la fin de l'écran à droite
            background2.x = screenX;
        }

        List<Laser> trash = new ArrayList<>(); //Liste des lasers supprimés

        for(Laser laser: lasers) {
            if (laser.x > screenX) //si le laser est en dehors de l'écran
                trash.add(laser); //On le supprime

            laser.x += 50 * screenRatioX;

            for(Ennemi ennemi : ennemis) { //ici on gère la collision des lasers et des ennemis pour tuer les ennemis
                if(Rect.intersects(ennemi.getCollisionShape(), laser.getCollisionShape())) {
                    score++;
                    ennemi.x = -500; //Dès que l'ennemi est touché il est déplacé hors du cadre du téléphone et donc il est renvoyé car en dessous on a écrit une ligne pour éviter qu'un ennemi sorte de l'écran donc à chaque fois qu'un ennemi est touché il sort de l'écran puis revient
                    laser.x = screenX + 500; //Ici le laser est aussi envoyé en dehors de l'écran et au dessus on a paramétré le fait que si un laser est en dehors de l'écran il est ajouté à la liste trash
                    ennemi.wasShot = true; //On confirme que l'ennemi est touché car on doit gérer le fait que si un ennemi n'est pas touché et passe à gauche de l'écran derrière flight c'est game over. On doit donc gérer cette situation.
                }

            }

        }

        for(Laser laser: trash) {
            lasers.remove(laser); //On supprime la liste trash à la fin
        }

        for (Ennemi ennemi : ennemis) {

            ennemi.x -= ennemi.speed; //les ennemis s'approchent du flight

            if (ennemi.x + ennemi.width < 0) {

                if (!ennemi.wasShot) { //Si l'ennemi n'est pas touché et est en dehors de l'écran alors c'est game over.
                    isGameOver = true;
                    return;
                }

                int bound = (int) (30 * screenRatioX); //speed limit
                ennemi.speed = random.nextInt(bound); //créé une vitesse aléatoire pour les ennemis suivants

                if(ennemi.speed < 10 * screenRatioX) //Pour éviter qu'un ennemi ne bouge pas du tout on met une vitesse minimale et on augmente speed si ça descend trop bas
                    ennemi.speed = (int) (10 * screenRatioX);

                ennemi.x = screenX;
                ennemi.y = random.nextInt(screenY - ennemi.height); //Pour ne pas que l'ennemi soit placé en dehors de l'écran

                ennemi.wasShot = false;
            }



            if(Rect.intersects(ennemi.getCollisionShape(), flight.getCollisionShape())) { //On utilise la classe Rectangle et si il y a collision entre ennemi et flight c'est la fin du jeu
                isGameOver = true;
                return;
            }
        }
    }


    private void draw (){ //On utilise les canvas avec surface view pour dessiner les objets

        if(getHolder().getSurface().isValid()) { //On va s'assurer que le surfaceview object a été initié

            Canvas canvas = getHolder().lockCanvas(); //retourne le canvas courant qui a été affiché sur l'écran et sur ce canvas on va dessiner bg1 et bg2
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint); //prêt à être affiché sur l'écran

            for (Ennemi ennemi : ennemis) {
                canvas.drawBitmap(ennemi.getEnnemi(), ennemi.x, ennemi.y, paint);
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

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint); //on dessine le flight

            for(Laser laser: lasers) {
                canvas.drawBitmap(laser.laser, laser.x, laser.y, paint);

            }

            getHolder().unlockCanvasAndPost(canvas); //On les affiche sur l'écran
        }
    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000); //thread en sleep pendant 3 secondes
            activity.startActivity(new Intent(activity, MainActivity.class)); //revenir sur le mainactivity
            activity.finish(); //fermer gameactivity
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() { //pour le highscore

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void sleep (){
        try {
            Thread.sleep(17); //attend 17 millisecondes et si on divise 1000/17 on a environ 60 donc en 1 seconde on va mettre à jour 60 fois les positions de l'image
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette méthode démarre le Thread
     */
    public void resume(){
        isPlaying = true;
        mSensorManager.registerListener( //on s'enregistre en tant qu'écouteur
                accelerometerListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME  //On utilise un rythme moins rapide pour utiliser moins de CPU
        );
        thread = new Thread(this);
        thread.start();

    }
    /**
     * Cette méthode met en pause le Thread
     */
    public void pause (){
        try {
            isPlaying = false;
            mSensorManager.unregisterListener(accelerometerListener); //On désenregistre
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
        laser.y = flight.y + (flight.height / 2); //on place le laser prêt du flight
        lasers.add(laser);
    }

    /**
     * Cette méthode permet de tirer en cliquant sur l'écran
     */
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
