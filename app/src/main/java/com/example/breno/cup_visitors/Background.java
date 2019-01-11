package com.example.breno.cup_visitors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Breno on 02/08/2016.
 */

public class Background
{
    private Bitmap backgroundImage, resizedBackground;
    private float posY1, posY2;

    public Background(int ScreenX, int ScreenY, Context c) {
        posY1 = 0;
        posY2 = -ScreenY;
        backgroundImage = BitmapFactory.decodeResource(c.getResources(), R.drawable.space);
        resizedBackground = GameView.getResizedBitmap(backgroundImage, ScreenX, ScreenY);
    }

    public void Draw(Canvas canvas, Paint p) {
        canvas.drawBitmap(resizedBackground, 0, posY1, p);
        canvas.drawBitmap(resizedBackground, 0, posY2, p);
    }

    public void Update() {
        if (posY1 >= GameView.screenY) {
            posY1 = -GameView.screenY;
        } else if (posY2 >= GameView.screenY) {
            posY2 = -GameView.screenY;
        }
        posY1 += GameView.screenY / 100;
        posY2 += GameView.screenY / 100;

    }
}