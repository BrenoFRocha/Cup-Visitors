package io.github.brenofrocha.cupvisitors.Classes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Breno on 12/01/2019.
 */

public class Button {

    private int posX, posY;
    private Bitmap image;

    public Button(int posX, int posY, Bitmap buttonImage)
    {
        this.posX = posX;
        this.posY = posY;
        this.image = buttonImage;
    }

    public void draw(Canvas canvas, Paint p) {
        canvas.drawBitmap(image, posX, posY, p);
    }
}
