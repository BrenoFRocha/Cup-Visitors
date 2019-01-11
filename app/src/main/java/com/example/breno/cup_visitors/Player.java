package com.example.breno.cup_visitors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Breno on 02/08/2016.
 */

public class Player
{
    private Bitmap playerImage,playerResizedImage;
    public float posX,sizeX,sizeY, velocity;
    public boolean MoveRight,MoveLeft;

    public Player(Context c)
    {
        playerImage = BitmapFactory.decodeResource(c.getResources(), R.drawable.spaceship);
        playerResizedImage = GameView.getResizedBitmap(playerImage, (int) (GameView.screenX / 15.5f), (int) (GameView.screenY / 8f));
        sizeX = playerResizedImage.getWidth();
        sizeY = playerResizedImage.getHeight();
        posX = GameView.screenX/2 - sizeX/2 - (Enemies.sizeX*1.5f);
        velocity = 5f;
    }

    public void Draw(Canvas canvas, Paint p)
    {
        canvas.drawBitmap(playerResizedImage,posX, GameView.screenY - sizeY*1.05f, p);
    }

    public void Update()
    {
        if(MoveRight && posX + sizeX <= GameView.screenX - Enemies.sizeX*3 - GameView.screenX/250)
        {
            posX += velocity;
        }
        else if(MoveLeft && posX >= GameView.screenX/250)
        {
            posX -= velocity;
        }
    }

}
