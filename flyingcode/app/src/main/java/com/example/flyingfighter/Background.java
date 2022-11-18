package com.example.flyingfighter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Classe [Background]
 * @description cette classe permet la gestion du background en jeu. Cette classe possède un x, un y et le background.
 * on créé un constructeur qui va prendre la taille de l'écran en x et en y
 * On a également un attribut ressource qui va décoder le bitmap du drawable folder
 * On utilise un surface view pour changer de background rapidement
 *
 * (int) x > coordonnées x
 * (int) y > coordonnées y
 * (Bitmap) background > Format d'image
 */
public class Background {
    int x = 0 , y= 0;
    Bitmap background;

    Background (int screenX, int screenY, Resources res){
        background = BitmapFactory.decodeResource(res, R.drawable.background1);          //le background est instancié
        background = Bitmap.createScaledBitmap(background, screenX,screenY, false); //on le re size pour qu'il passe entièrement dans l'écran
    }
}
