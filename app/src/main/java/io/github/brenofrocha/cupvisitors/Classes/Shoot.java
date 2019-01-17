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

public class Shoot
{
    private Bitmap shootImage;
    public boolean thereIsAShoot;
    public float posX, posY;
    public int sizeX, sizeY;

    public Shoot(Context ctx)
    {
        thereIsAShoot = false;
        sizeX = (int)(MainView.screenX/50);
        sizeY = (int)(MainView.screenX/50);
        shootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.brazil_ball), sizeX, sizeY, false);
        posY = MainView.screenY - (MainView.screenY/8f) * 1.2f;
    }
    public void Update(float PosX)
    {
        if(posY < -sizeY && thereIsAShoot)
        {
            thereIsAShoot = false;
            posY = MainView.screenY - (MainView.screenY/8f) * 1.2f;
        }
        if(thereIsAShoot)
        {
            posY -= 25f;
        }
        else
        {
            posX = PosX;
            posY = MainView.screenY - (MainView.screenY/8f) * 1.2f;
        }
    }

    public void Draw(Canvas canvas, Paint p)
    {
        if(thereIsAShoot)
        {
            canvas.drawBitmap(shootImage, posX, posY, p);
        }
    }
}

