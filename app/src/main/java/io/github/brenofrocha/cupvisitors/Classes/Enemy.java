package io.github.brenofrocha.cupvisitors.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.github.brenofrocha.cupvisitors.R;
import io.github.brenofrocha.cupvisitors.Views.MainView;

/**
 * Created by Breno on 02/08/2016.
 */

public class Enemy
{
    private Timer timer;
    private Context ctx;
    private Bitmap enemyImage, enemyShootImage;
    private Shoot shoot;
    public float posX, posY;
    private float sizeX,sizeY, shootVelocity, shootX, shootY, shootDamage;
    private int sizeShootX, sizeShootY, max, number, period;
    public boolean isDestroyed;
    private boolean isShooting;
    private final Random random;

    public Enemy(Context ctx, float posX, float posY, Bitmap image, int index, Shoot shoot)
    {
        this.ctx = ctx;
        this.shoot = shoot;
        this.posX = posX;
        this.posY = posY;
        number = 10000;
        sizeShootX = (int)(MainView.screenX/30);
        sizeShootY = (int)(MainView.screenX/30);
        setShootProperties(index);
        isDestroyed = false;
        isShooting = false;
        timer = new Timer();
        random = new Random();
        period = random.nextInt(10001 - 5000) + 5000;
        randomShoot();
        enemyImage = image;
        sizeX = enemyImage.getWidth();
        sizeY = enemyImage.getHeight();
        max = 100;
    }
    public void draw(Canvas canvas, Paint p, float posY)
    {
        this.posY = posY;
        if(!isDestroyed)
        {
            canvas.drawBitmap(enemyImage, posX, posY, p);
            if(isShooting)
            {
                canvas.drawBitmap(enemyShootImage, shootX, shootY, p);
            }
        }

    }
    public void update(Shoot shoot, Player player)
    {
        posX += MainView.enemiesVelocityX;
        if(!isDestroyed)
        {
            if(shoot.posX >= posX - sizeX/2f &&
                    shoot.posX + sizeShootX <= posX + sizeX + sizeX/2f &&
                    shoot.posY >= posY - sizeY/2f &&
                    shoot.posY + sizeShootY <= posY + sizeY + sizeY/2f)
            {
                isDestroyed = true;
                destroyShoot();
            }
            if(!isShooting)
            {
                if(number < 15)
                {
                    number = 1000;
                    shootX = ((posX + (sizeX/2)) - sizeShootX/2);
                    shootY = posY + sizeY;
                    isShooting = true;
                }
            }
            else
            {
                shootY += shootVelocity;
                if(shootY > MainView.screenY)
                {
                    isShooting = false;
                }
                else if(shoot.posX >= shootX - sizeShootX &&
                        shoot.posX <= shootX + sizeShootX &&
                        shoot.posY >= shootY - sizeShootY &&
                        shoot.posY + shoot.sizeY <= shootY + sizeShootY + sizeShootY/2)
                {
                    isShooting = false;
                    shoot.thereIsAShoot = false;
                }
                else if(shootX >= player.posX - (player.sizeX/1.8f) &&
                        shootX <= player.posX + (player.sizeX/1.2f) &&
                        shootY + sizeShootY >= player.posY &&
                        shootY <= player.posY + player.sizeY)
                {
                    isShooting = false;
                    if(shootDamage == 0.5f) {
                        player.rangeLife += shootDamage;
                    }
                    else
                    {
                        player.life -= shootDamage;
                    }
                }
            }
        }
    }

    private void randomShoot() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isShooting) {
                    number = random.nextInt(max + 1);
                }
            }
        }, period, period);
    }

    private void destroyShoot()
    {
        shoot.thereIsAShoot = false;
        shoot.posY = MainView.screenY - (MainView.screenY/8f) * 1.2f;
    }

    private void setShootProperties(int index)
    {
        switch (index)
        {
            case 0:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.usa_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 30f;
                shootDamage = 0.5f;
                break;
            case 1:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.netherlands_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 30f;
                shootDamage = 0.5f;
                break;
            case 2:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.portugal_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 30f;
                shootDamage = 0.5f;
                break;
            case 3:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.spain_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 25f;
                shootDamage = 1f;
                break;
            case 4:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.france_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 25f;
                shootDamage = 1f;
                break;
            case 5:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.england_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 25f;
                shootDamage = 1f;
                break;
            case 6:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.argentina_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 20f;
                shootDamage = 2f;
                break;
            case 7:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.uruguay_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 20f;
                shootDamage = 2f;
                break;
            case 8:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.germany_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 15f;
                shootDamage = 3f;
                break;
            case 9:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.italy_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 10f;
                shootDamage = 4f;
                break;
            case 10:
                enemyShootImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(), R.drawable.brazil_ball), sizeShootX, sizeShootY, false);
                shootVelocity = 5f;
                shootDamage = 5f;
                break;
        }
    }
}
