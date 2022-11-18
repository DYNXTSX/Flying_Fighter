package com.example.flyingfighter;

import static com.example.flyingfighter.GameView.screenRatioX;
import static com.example.flyingfighter.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

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

        y = -height;
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

        Ennemicounter = 1;

        return ufo4;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
