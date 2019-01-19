package io.github.brenofrocha.cupvisitors.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.brenofrocha.cupvisitors.R;
import io.github.brenofrocha.cupvisitors.Views.MainView;
import io.github.brenofrocha.cupvisitors.Views.MenuView;

/**
 * Created by Breno on 02/08/2016.
 */

public class Background
{
    private Bitmap backgroundImage;
    private float posY1, posY2;
    private int screenX, screenY;

    public Background(Context c, int screenX, int screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
        posY1 = 0;
        posY2 = -screenY;
        backgroundImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(c.getResources(), R.drawable.space), screenX,screenY, false);
    }

    public void draw(Canvas canvas, Paint p) {
        canvas.drawBitmap(backgroundImage, 0, posY1, p);
        canvas.drawBitmap(backgroundImage, 0, posY2, p);
    }

    public void update() {
        if (posY1 >= screenY) {
            posY1 = -screenY;
        } else if (posY2 >= screenY) {
            posY2 = -screenY;
        }
        posY1 += 15;
        posY2 += 15;
    }
}