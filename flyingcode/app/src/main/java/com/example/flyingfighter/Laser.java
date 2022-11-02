package com.example.flyingfighter;

import static com.example.flyingfighter.GameView.screenRatioX;
import static com.example.flyingfighter.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Laser {

    int x,y, width,height;
    Bitmap laser;

    Laser(Resources res) {

        laser = BitmapFactory.decodeResource(res, R.drawable.laser);

        width = laser.getWidth();
        height = laser.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX );
        height = (int) (height * screenRatioY );

        laser = Bitmap.createScaledBitmap(laser, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }
}
