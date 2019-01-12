package io.github.brenofrocha.cupvisitors.Classes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Debug;
import android.util.Log;

import io.github.brenofrocha.cupvisitors.Views.MainView;

/**
 * Created by Breno on 02/08/2016.
 */

public class Enemy
{
    private Bitmap enemyImage;
    public float posX, posY;
    private float sizeX,sizeY;
    public boolean isDestroyed;
    public static boolean destroyShoot;

    public Enemy(float posX, float posY, Bitmap image, int index)
    {
        this.posX = posX;
        this.posY = posY;
        isDestroyed = false;
        enemyImage = image;
        sizeX = enemyImage.getWidth();
        sizeY = enemyImage.getHeight();
    }
    public void Draw(Canvas canvas, Paint p, float posY)
    {
        this.posY = posY;
        if(!isDestroyed)
        {
            canvas.drawBitmap(enemyImage, posX, posY, p);
        }
    }
    public void Update(float posShootX,float posShootY,float sizeShootX,float sizeShootY)
    {
        posX += MainView.enemiesVelocityX;
        if(!isDestroyed)
        {
            if(posShootX >= posX - sizeX/3 &&
                    posShootX + sizeShootX <= posX + sizeX + sizeX/3 &&
                    posShootY >= posY - sizeY/3 &&
                    posShootY + sizeShootY <= posY + sizeY + sizeY/3)
            {
                destroyShoot = true;
                isDestroyed = true;
            }
        }
    }
}
