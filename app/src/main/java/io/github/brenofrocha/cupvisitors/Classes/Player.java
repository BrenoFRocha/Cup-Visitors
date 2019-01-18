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

public class Player
{
    private Bitmap playerImage;
    public float posX, posY, sizeX,sizeY, velocity, rangeLife;
    public int life;
    public boolean MoveRight,MoveLeft;
    private boolean dead;
    public Player(Context c)
    {
        dead = false;
        life = 5;
        playerImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(c.getResources(), R.drawable.player), (int) (MainView.screenX/15f), (int) (MainView.screenX/15f), false);
        sizeX = playerImage.getWidth();
        sizeY = playerImage.getHeight();
        posX = MainView.screenX/2 - sizeX/2 - (MainView.enemySizeX*1.5f);
        posY = MainView.screenY - sizeY * 1.05f;
        velocity = 10f;
    }

    public void draw(Canvas canvas, Paint p)
    {
        if(!dead) {
            canvas.drawBitmap(playerImage, posX, posY, p);
        }
    }

    public void update()
    {
        if(life <= 0)
        {
            dead = true;
        }
        if(MoveRight && posX + sizeX <= MainView.screenX - MainView.enemySizeX*3 - MainView.screenX/250)
        {
            posX += velocity;
        }
        else if(MoveLeft && posX >= MainView.screenX/250)
        {
            posX -= velocity;
        }
    }

    private void checkRange()
    {
        if(rangeLife >= 1)
        {
            rangeLife = 0;
            life -= 1;
        }
    }
}
