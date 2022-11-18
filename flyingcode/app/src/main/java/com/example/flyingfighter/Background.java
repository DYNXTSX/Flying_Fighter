package com.example.flyingfighter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Classe [Background]
 * @description cette classe permet la gestion du background en jeu.
 *
 * (int) x > coordonnées x
 * (int) y >
 * (Bitmap) background > Format d'image
 */
public class Background {
    int x = 0 , y= 0;
    Bitmap background;

    Background (int screenX, int screenY, Resources res){
        background = BitmapFactory.decodeResource(res, R.drawable.background1);
        background = Bitmap.createScaledBitmap(background, screenX,screenY, false);
    }
}
