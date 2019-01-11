package io.github.brenofrocha.cupvisitors.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.brenofrocha.cupvisitors.R;
import io.github.brenofrocha.cupvisitors.Views.MainView;

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
        resizedBackground = MainView.getResizedBitmap(backgroundImage, ScreenX, ScreenY);
    }

    public void Draw(Canvas canvas, Paint p) {
        canvas.drawBitmap(resizedBackground, 0, posY1, p);
        canvas.drawBitmap(resizedBackground, 0, posY2, p);
    }

    public void Update() {
        if (posY1 >= MainView.screenY) {
            posY1 = -MainView.screenY;
        } else if (posY2 >= MainView.screenY) {
            posY2 = -MainView.screenY;
        }
        posY1 += MainView.screenY / 100;
        posY2 += MainView.screenY / 100;

    }
}