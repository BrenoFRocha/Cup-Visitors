package com.example.breno.cup_visitors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Breno on 02/08/2016.
 */

public class Shoot
{
    private Bitmap shootImage,resizedShootImage;
    public static boolean thereIsAShoot;
    public float posX, posY, newPosX;
    public int sizeX,sizeY;
    public Shoot(Context ctx,float SizeYPLayer)
    {
        thereIsAShoot = false;
        shootImage = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.shoot);
        sizeX = (int)(GameView.screenX/(6.5f*5.5f));
        sizeY = (int)(GameView.screenY/20);
        resizedShootImage = GameView.getResizedBitmap(shootImage, sizeX, sizeY);
        posY = GameView.screenY - SizeYPLayer * 1.2f;
    }
    public void Update(float PosX,float SizeYPLayer)
    {
        if(Enemies.destroyShoot)
        {
            thereIsAShoot = false;
            Enemies.destroyShoot = false;
            posY = GameView.screenY - SizeYPLayer * 1.2f;
        }
        if(posY < -sizeY && thereIsAShoot)
        {
            thereIsAShoot = false;
            posY = GameView.screenY - SizeYPLayer * 1.2f;
        }
        if(thereIsAShoot)
        {
            posY -= 15f;
        }
        else
        {
            posX = PosX;
            posY = GameView.screenY - SizeYPLayer * 1.2f;
        }
    }

    public void Draw(Canvas canvas, Paint p)
    {
        if(thereIsAShoot)
        {
            canvas.drawBitmap(resizedShootImage, posX, posY, p);
        }
    }
}

