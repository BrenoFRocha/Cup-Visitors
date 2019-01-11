package com.example.breno.cup_visitors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Breno on 02/08/2016.
 */

public class Enemies
{
    private Bitmap[] enemyImage,enemyResizedBit;
    private float[] posX;
    private float posY,velocity;
    public static float sizeX,sizeY;
    private int numberOfEnemies;
    private boolean[] isDestroyed;
    public static boolean destroyShoot;
    public Enemies(Context c, float indexPosY)
    {
        numberOfEnemies = 10;
        enemyImage = new Bitmap[numberOfEnemies];
        enemyResizedBit = new Bitmap[numberOfEnemies];
        posX = new float[numberOfEnemies];
        isDestroyed = new boolean[numberOfEnemies];
        velocity = 1f;
        enemyImage[0] = BitmapFactory.decodeResource(c.getResources(),R.drawable.germany);
        enemyImage[1] = BitmapFactory.decodeResource(c.getResources(),R.drawable.italy);
        enemyImage[2] = BitmapFactory.decodeResource(c.getResources(),R.drawable.brazil);
        enemyImage[3] = BitmapFactory.decodeResource(c.getResources(),R.drawable.argentina);
        enemyImage[4] = BitmapFactory.decodeResource(c.getResources(),R.drawable.portugal);
        enemyImage[5] = BitmapFactory.decodeResource(c.getResources(),R.drawable.spain);
        enemyImage[6] = BitmapFactory.decodeResource(c.getResources(),R.drawable.england);
        enemyImage[7] = BitmapFactory.decodeResource(c.getResources(), R.drawable.france);
        enemyImage[8] = BitmapFactory.decodeResource(c.getResources(), R.drawable.usa);
        enemyImage[9] = BitmapFactory.decodeResource(c.getResources(), R.drawable.netherlands);
        for(int i = 0; i < numberOfEnemies; i++)
        {
            enemyResizedBit[i] = GameView.getResizedBitmap(enemyImage[i],(int)((GameView.screenX/enemyImage[i].getWidth())*2.5f), (int)((GameView.screenY/enemyImage[i].getHeight())*2.2f));
            posX[i] = (GameView.screenX/13.5f)*i;
        }
        sizeX = enemyResizedBit[0].getWidth();
        sizeY = enemyResizedBit[0].getHeight();
        posY = (sizeY + GameView.screenY/75)*indexPosY;
    }
    public void Draw(Canvas canvas,Paint p)
    {
        for(int i = 0; i < numberOfEnemies; i++)
        {
            if(!isDestroyed[i])
            {
                canvas.drawBitmap(enemyResizedBit[i], posX[i], posY, p);
            }
        }
    }
    public void Update(float posShootX,float posShootY,float posSizeX,float posSizeY)
    {
        for(int i = 0; i < numberOfEnemies; i++)
        {
            posX[i] += velocity;
            if(!isDestroyed[i])
            {
                if(posShootY >= posY && posSizeY <= posY &&  posShootX >= posX[i] - sizeX/2.4f && posSizeX <= posX[i] + sizeX*1.4f)
                {
                    destroyShoot = true;
                    isDestroyed[i] = true;
                }
            }
        }
        if(posX[9] + (sizeX*4) >= GameView.screenX  || posX[0] <= 0)
        {
            velocity *= -1;
            posY += GameView.screenY/300;
        }
    }
}
