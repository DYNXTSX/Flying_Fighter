package com.example.flyingfighter;

import static com.example.flyingfighter.GameView.screenRatioX;
import static com.example.flyingfighter.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Classe [Laser]
 * @description cette classe représente les lasers que l'avion allié tire lorsqu'on clique sur l'écran.
 */
public class Laser {

    int x,y, width,height;
    Bitmap laser;

    Laser(Resources res) {
        laser = BitmapFactory.decodeResource(res, R.drawable.laser); //On créé le bitmap

        width = laser.getWidth();
        height = laser.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX );
        height = (int) (height * screenRatioY );

        laser = Bitmap.createScaledBitmap(laser, width, height, false); //remise à l'échelle du bitmap existant

    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    } //Gestion des collisions avec Rect
}
