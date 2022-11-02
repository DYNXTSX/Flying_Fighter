package com.example.flyingfighter;

import static com.example.flyingfighter.GameView.screenRatioX;
import static com.example.flyingfighter.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Flight {

    int toLaser = 0;
    boolean isGoingUp = false;
    int x, y, width, height, wingCounter = 0, laserCounter = 1;
    Bitmap flight1, flight2, laser1, laser2, laser3, laser4, laser5, dead;
    private GameView gameView;

    Flight(GameView gameView,int screenY, Resources res) {

        this.gameView = gameView;

        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly1);

        width = flight1.getWidth();
        height = flight1.getHeight();

        width /= 4;
        height /= 4;

        width = (int) (width * screenRatioX * 8);
        height = (int) (height * screenRatioY * 8);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);

        laser1 = BitmapFactory.decodeResource(res, R.drawable.tir1);
        laser2 = BitmapFactory.decodeResource(res, R.drawable.tir2);
        laser3 = BitmapFactory.decodeResource(res, R.drawable.tir3);
        laser4 = BitmapFactory.decodeResource(res, R.drawable.tir4);
        laser5 = BitmapFactory.decodeResource(res, R.drawable.tir5);

        laser1 = Bitmap.createScaledBitmap(laser1, width, height, false);
        laser2 = Bitmap.createScaledBitmap(laser2, width, height, false);
        laser3 = Bitmap.createScaledBitmap(laser3, width, height, false);
        laser4 = Bitmap.createScaledBitmap(laser4, width, height, false);
        laser5 = Bitmap.createScaledBitmap(laser5, width, height, false);

        dead = BitmapFactory.decodeResource(res, R.drawable.dead);
        dead = Bitmap.createScaledBitmap(dead, width, height, false);

    }

    Bitmap getFlight () {

        if(toLaser != 0){

            if(laserCounter == 1){
                laserCounter ++;
                return  laser1;
            }

            if(laserCounter == 2){
                laserCounter ++;
                return  laser2;
            }

            if(laserCounter == 3){
                laserCounter ++;
                return  laser3;
            }

            if(laserCounter == 4){
                laserCounter ++;
                return  laser4;
            }

            laserCounter = 1;
            toLaser--;
            gameView.newBullet();

            return laser5;
        }

        if(wingCounter == 0){
            wingCounter++;
            return flight1;
        }

        wingCounter --;

        return flight2;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead() {
        return dead;
    }
}
