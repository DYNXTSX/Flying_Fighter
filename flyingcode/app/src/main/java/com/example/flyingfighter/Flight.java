package com.example.flyingfighter;

import static com.example.flyingfighter.GameView.screenRatioX;
import static com.example.flyingfighter.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Classe [Flight]
 * @description cette classe représente le personnage principal (l'avion) que le joueur incarne
 *
 *
 * (int) toLaser > tir de l'avion
 * (int) x > position avion x
 * (int) y > position avion y
 * (int) width > largeur ennemi
 * (int) height > taille ennemi
 * (int) wingCounter > compteur
 * (int) laserCounter > compteur de tirs
 * (int) deadCounter > compteur de morts
 * (boolean) isGoingup > booléen qui nous permet de savoir si l'avion prend de l'altitude ou non
 * (Bitmap) flight1, laser1, dead1, dead2, dead3, dead4, dead5, dead6, dead7, dead8, dead9, dead10, dead11; > Format d'image
 *
 */

public class Flight {
    int toLaser = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, laserCounter = 1, deadCounter = 1;
    Bitmap flight1, laser1, dead1, dead2, dead3, dead4, dead5, dead6, dead7, dead8, dead9, dead10, dead11;
    private GameView gameView;

    Flight(GameView gameView,int screenY, Resources res) {
        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.avion_rouge_2);

        width = flight1.getWidth();
        height = flight1.getHeight();

        width /= 40; //réduire taille de l'image
        height /= 40;

        width = (int) (width * screenRatioX * 8);
        height = (int) (height * screenRatioY * 8);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false); //on la resize

        y = screenY / 2; //position de flight au démarrage du jeu
        x = (int) (64 * screenRatioX);

        laser1 = BitmapFactory.decodeResource(res, R.drawable.avion_rouge_tire_1);

        laser1 = Bitmap.createScaledBitmap(laser1, width, height, false);

        dead1 = BitmapFactory.decodeResource(res, R.drawable.dead1);
        dead2 = BitmapFactory.decodeResource(res, R.drawable.dead2);
        dead3 = BitmapFactory.decodeResource(res, R.drawable.dead3);
        dead4 = BitmapFactory.decodeResource(res, R.drawable.dead4);
        dead5 = BitmapFactory.decodeResource(res, R.drawable.dead5);
        dead6 = BitmapFactory.decodeResource(res, R.drawable.dead6);
        dead7 = BitmapFactory.decodeResource(res, R.drawable.dead7);
        dead8 = BitmapFactory.decodeResource(res, R.drawable.dead8);
        dead9 = BitmapFactory.decodeResource(res, R.drawable.dead9);
        dead10 = BitmapFactory.decodeResource(res, R.drawable.dead10);
        dead11 = BitmapFactory.decodeResource(res, R.drawable.dead11);

        dead1 = Bitmap.createScaledBitmap(dead1, width, height, false);
        dead2 = Bitmap.createScaledBitmap(dead2, width, height, false);
        dead3 = Bitmap.createScaledBitmap(dead3, width, height, false);
        dead4 = Bitmap.createScaledBitmap(dead4, width, height, false);
        dead5 = Bitmap.createScaledBitmap(dead5, width, height, false);
        dead6 = Bitmap.createScaledBitmap(dead6, width, height, false);
        dead7 = Bitmap.createScaledBitmap(dead7, width, height, false);
        dead8 = Bitmap.createScaledBitmap(dead8, width, height, false);
        dead9 = Bitmap.createScaledBitmap(dead9, width, height, false);
        dead10 = Bitmap.createScaledBitmap(dead10, width, height, false);
        dead11 = Bitmap.createScaledBitmap(dead11, width, height, false);
    }

    /**
     *  Cette fonction va être appelée quand on va dessiner l'avion sur les canvas,
     *  pour wingcounter : la premiere fois que la fonction va etre appelée, il va démarrer à 0 et monter puis continuer tant que le jeu est démarré.
     *
     */
    Bitmap getFlight () {
        if(toLaser != 0){

            if(laserCounter == 1){
                laserCounter ++;
                return  laser1;
            }


            laserCounter = 1;
            toLaser--;
            gameView.newBullet();

        }

        if(wingCounter == 0){
            wingCounter++;
            return flight1;
        }

        wingCounter --;

        return flight1;
    }

    /**
     * Retourne un objet de Rect (cela créé un rectangle autour de flight pour gérer les collisions)
     */
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    /**
     * Cette fonction retourne le bitmap Dead
     */
    Bitmap getDead() {
        return dead6;
    }
}
