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
    private Bitmap playerImage,playerResizedImage;
    public float posX,sizeX,sizeY, velocity;
    public boolean MoveRight,MoveLeft;

    public Player(Context c)
    {
        playerImage = BitmapFactory.decodeResource(c.getResources(), R.drawable.spaceship);
        playerResizedImage = Bitmap.createScaledBitmap(playerImage, (int) (MainView.screenX / 15.5f), (int) (MainView.screenY / 8f), false);
        sizeX = playerResizedImage.getWidth();
        sizeY = playerResizedImage.getHeight();
        posX = MainView.screenX/2 - sizeX/2 - (MainView.enemySizeX*1.5f);
        velocity = 5f;
    }

    public void Draw(Canvas canvas, Paint p)
    {
        canvas.drawBitmap(playerResizedImage,posX, MainView.screenY - sizeY*1.05f, p);
    }

    public void Update()
    {
        if(MoveRight && posX + sizeX <= MainView.screenX - MainView.enemySizeX*3 - MainView.screenX/250)
        {
            posX += velocity;
        }
        else if(MoveLeft && posX >= MainView.screenX/250)
        {
            posX -= velocity;
        }
    }
}
