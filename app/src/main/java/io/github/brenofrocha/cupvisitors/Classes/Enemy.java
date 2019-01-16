package io.github.brenofrocha.cupvisitors.Classes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import io.github.brenofrocha.cupvisitors.Views.MainView;

/**
 * Created by Breno on 02/08/2016.
 */

public class Enemy
{
    private Bitmap enemyImage;
    private Shoot shoot;
    public float posX, posY;
    private float sizeX,sizeY;
    private boolean isDestroyed;

    public Enemy(float posX, float posY, Bitmap image, int index, Shoot shoot)
    {
        this.shoot = shoot;
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
            if(posShootX >= posX - sizeX/2f &&
                    posShootX + sizeShootX <= posX + sizeX + sizeX/2f &&
                    posShootY >= posY - sizeY/2f &&
                    posShootY + sizeShootY <= posY + sizeY + sizeY/2f)
            {
                isDestroyed = true;
                destroyShoot();
            }
        }
    }

    private void destroyShoot()
    {
        shoot.thereIsAShoot = false;
        shoot.posY = MainView.screenY - (MainView.screenY/8f) * 1.2f;
    }
}
