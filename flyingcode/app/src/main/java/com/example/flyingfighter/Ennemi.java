package com.example.flyingfighter;

import static com.example.flyingfighter.GameView.screenRatioX;
import static com.example.flyingfighter.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Classe [Ennemi]
 * @description cette classe représente les ennemis dans le jeu
 *
 *
 * (int) speed > vitesse des ennemis
 * (int) x > position ennemi x
 * (int) y > position ennemi y
 * (int) width > largeur ennemi
 * (int) height > taille ennemi
 * (int) Ennemicounter > compteur d'ennemis
 * (boolean) wasShot > booléen qui nous permet de savoir si l'ennemi a été shooté ou non
 * (Bitmap) ufo1,ufo2,ufo3,ufo4 > Format d'image, (il y en a 4 pour la petite animation)
 *
 */

public class Ennemi {


    public int speed = 20;
    public boolean wasShot = true;
    int x = 0, y, width, height, Ennemicounter = 1;
    Bitmap ufo1, ufo2, ufo3, ufo4;

    Ennemi (Resources res) {

        ufo1 = BitmapFactory.decodeResource(res, R.drawable.ufo1);
        ufo2 = BitmapFactory.decodeResource(res, R.drawable.ufo2);
        ufo3 = BitmapFactory.decodeResource(res, R.drawable.ufo3);
        ufo4 = BitmapFactory.decodeResource(res, R.drawable.ufo4);

        width = ufo1.getWidth();
        height = ufo1.getHeight();

        width /= 6;
        height /= 6;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        ufo1 = Bitmap.createScaledBitmap(ufo1, width, height, false);
        ufo2 = Bitmap.createScaledBitmap(ufo2, width, height, false);
        ufo3 = Bitmap.createScaledBitmap(ufo3, width, height, false);
        ufo4 = Bitmap.createScaledBitmap(ufo4, width, height, false);

        y = -height; //Pour que l'ennemi soit placé en bas de l'écran au début du jeu
    }

    Bitmap getEnnemi () {

        if (Ennemicounter == 1) {
            Ennemicounter++;
            return ufo1;
        }

        if (Ennemicounter == 2) {
            Ennemicounter++;
            return ufo2;
        }

        if (Ennemicounter == 3) {
            Ennemicounter++;
            return ufo3;
        }

        Ennemicounter = 1; //recommencer l'animation une fois qu'elle est finie comme on reprend à 1

        return ufo4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height); //cette fonction retourne un objet de Rect (cela créé un rectangle autour de l'ennemi pour gérer les collisions)
    }
}
