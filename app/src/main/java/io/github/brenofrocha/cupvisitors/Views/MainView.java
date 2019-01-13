package io.github.brenofrocha.cupvisitors.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import io.github.brenofrocha.cupvisitors.Activities.MenuActivity;
import io.github.brenofrocha.cupvisitors.Classes.Background;
import io.github.brenofrocha.cupvisitors.Classes.Button;
import io.github.brenofrocha.cupvisitors.Classes.Enemy;
import io.github.brenofrocha.cupvisitors.Classes.Player;
import io.github.brenofrocha.cupvisitors.Classes.Shoot;
import io.github.brenofrocha.cupvisitors.R;

/**
 * Created by Breno on 02/08/2016.
 */

public class MainView extends View implements Runnable
{
    Handler handler;
    private int columnsOfEnemies, linesOfEnemies, sBPosY, mBPosY, sdBPosY, bSizeX, bSizeY, bPosX;
    public static int screenX,screenY, enemiesVelocityX;
    private float[] enemyPosY;
    public static float enemySizeX;
    private boolean sound, shootPressed;
    private Background background;
    private Enemy[][] enemies;
    private Player player;
    private Shoot shoot;
    private Button menuButton, shootButton, shootButtonPressed, soundButton, noSoundButton;
    private Paint p;
    private Context ctx;
    private Bitmap backgroundGMImage;

    //Fade
    public int alpha;
    public boolean fadeIn, fadeOut;
    public String sceneFade;
    private Paint paintFade;

    public MainView(Context ctx)
    {
        super(ctx);
        this.ctx = ctx;
        handler = new android.os.Handler();
        handler.post(this);
        DisplayMetrics display = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                                 .getDefaultDisplay()
                                 .getMetrics(display);
        screenX = display.widthPixels;
        screenY = display.heightPixels;
        p = new Paint();

        //Fade
        alpha = 255;
        paintFade = new Paint();
        paintFade.setColor(Color.BLACK);
        paintFade.setAlpha(alpha);
        fadeIn = true;

        Bitmap sBImage, sPBImage, mBImage, sdBImage, nosdBImage, resizedEnemyImage;

        //Buttons
        bSizeX = (int)((screenX*0.557f)/4.5f);
        bSizeY = (int)((screenY*1.0694f)/4.5f);
        bPosX = (int)((screenX - (enemySizeX * 3)) + (enemySizeX * 3/2) - (bSizeX/2));
        //Menu Button
        mBPosY = (int)(bSizeY*0.20f);
        mBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.menu_button), bSizeX,bSizeY, false);
        menuButton = new Button(bPosX, mBPosY, mBImage);
        //Shoot Button
        sBPosY = (int)(screenY - (bSizeY*1.20f));
        sBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.shoot_button), bSizeX,bSizeY, false);
        sPBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.shoot_button_pressed), bSizeX,bSizeY, false);
        shootButton = new Button(bPosX, sBPosY, sBImage);
        shootButtonPressed = new Button(bPosX, sBPosY, sPBImage);
        //Sound Buttons
        sound = true;
        sdBPosY = (int)((screenY/2) - bSizeY/2);
        sdBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.sound_button), bSizeX,bSizeY, false);
        soundButton = new Button(bPosX, sdBPosY, sdBImage);
        nosdBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.nosound_button), bSizeX,bSizeY, false);
        noSoundButton = new Button(bPosX, sdBPosY, nosdBImage);

        //Enemy
        enemiesVelocityX = 1;
        linesOfEnemies = 7;
        columnsOfEnemies = 9;
        Bitmap[] enemyImages = new Bitmap[10];
        enemyPosY = new float[linesOfEnemies];
        enemyImages[0] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.germany);
        enemyImages[1] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.italy);
        enemyImages[2] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.brazil);
        enemyImages[3] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.argentina);
        enemyImages[4] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.portugal);
        enemyImages[5] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.spain);
        enemyImages[6] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.england);
        enemyImages[7] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.france);
        enemyImages[8] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.usa);
        enemyImages[9] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.netherlands);
        enemySizeX = screenX/enemyImages[0].getWidth()*2.5f;
        float enemySizeY = screenY/enemyImages[0].getHeight()*2.2f;
        enemies = new Enemy[linesOfEnemies][columnsOfEnemies];
        for(int i = 0; i < linesOfEnemies; i++)
        {
            for(int j = 0; j < columnsOfEnemies; j++)
            {
                Random r = new Random();
                int id = r.nextInt(10);
                resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                enemyPosY[i] = (resizedEnemyImage.getHeight() + enemySizeY/4) * i;
                enemies[i][j] = new Enemy(((screenX/12f)*j) + screenX/15, enemyPosY[i], resizedEnemyImage, id);
            }
        }

        //Player
        player = new Player(ctx);

        //Shoot
        shoot = new Shoot(ctx,player.sizeY);

        //Background
        background = new Background(ctx, screenX, screenY);
        backgroundGMImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.game_menu_background), (int)(enemySizeX * 3),screenY, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int eventAction = event.getAction();

        int touchX = (int)event.getX();
        int touchY = (int)event.getY();

        switch (eventAction)
        {
            case MotionEvent.ACTION_DOWN:
                if(touchX >= bPosX &&
                        touchX <= bPosX + bSizeX &&
                        touchY >= mBPosY &&
                        touchY <= mBPosY + bSizeY)
                {
                    if(!fadeOut && !fadeIn) {
                        alpha = 0;
                        sceneFade = "menu";
                        fadeOut = true;
                    }
                }
                else if(touchX >= bPosX &&
                        touchX <= bPosX + bSizeX &&
                        touchY >= sdBPosY &&
                        touchY <= sdBPosY + bSizeY)
                {
                    sound = !sound;
                }
                else if(touchX >= bPosX &&
                        touchX <= bPosX + bSizeX &&
                        touchY >= sBPosY &&
                        touchY <= sBPosY + bSizeY)
                {
                    shootPressed = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(touchX < bPosX ||
                    touchX > bPosX + bSizeX ||
                    touchY < sBPosY ||
                    touchY > sBPosY + bSizeY)
                {
                    shootPressed = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                shootPressed = false;
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        background.draw(canvas, p);
        p.setColor(Color.BLUE);
        canvas.drawBitmap(backgroundGMImage, screenX - enemySizeX * 3, 0, p);
        for(int i = 0; i < linesOfEnemies; i++)
        {
            for(int j = 0; j < columnsOfEnemies; j++) {
                enemies[i][j].Draw(canvas, p, enemyPosY[i]);
            }
        }
        shoot.Draw(canvas,p);
        player.Draw(canvas,p);
        menuButton.draw(canvas,p);
        if(shootPressed)
        {
            shootButtonPressed.draw(canvas,p);
        }
        else
        {
            shootButton.draw(canvas,p);
        }
        if(sound)
        {
            soundButton.draw(canvas,p);
        }
        else
        {
            noSoundButton.draw(canvas,p);
        }
        canvas.drawRect(0,0,screenX,screenY,paintFade);
    }

    private void update()
    {
        background.update();

        //Shoot
        shoot.Update(player.posX + player.sizeX/4f,player.sizeY);

        //Enemies
        boolean closer = false;
        for(int i = 0; i < linesOfEnemies; i++)
        {
            for(int j = 0; j < columnsOfEnemies; j++) {
                if (enemies[i][j].posX < screenX/50 ||
                        enemies[i][j].posX + enemySizeX > (screenX - enemySizeX * 3) - screenX/50) {
                    enemiesVelocityX *= -1;
                    closer = true;
                }
            }
        }
        if(closer)
        {
            for(int i = 0; i < linesOfEnemies; i++)
            {
                enemyPosY[i] += 5;
            }
        }
        for(int i = 0; i < linesOfEnemies; i++)
        {
            for(int j = 0; j < columnsOfEnemies; j++) {
                enemies[i][j].Update(shoot.posX, shoot.posY, shoot.sizeX, shoot.sizeY);
            }
        }

        //Player
        player.Update();

        if(fadeIn) {
            setFadeIn();
        }
        else if(fadeOut) {
            setFadeOut();
        }
    }

    @Override
    public void run()
    {
        handler.postDelayed(this, 30);
        update();
        invalidate();
    }

    //Fade
    private void setFadeIn()
    {
        if(fadeOut)
        {
            fadeIn = false;
        }
        if(fadeIn)
        {
            alpha -= 5;
            paintFade.setAlpha(alpha);
            if(alpha == 0)
            {
                fadeIn = false;
            }
        }
    }

    private void setFadeOut()
    {
        alpha += 5;
        paintFade.setAlpha(alpha);
        if(alpha >= 255)
        {
            fadeOut = false;
            Intent i = new Intent();
            switch (sceneFade)
            {
                case "menu":
                    i = new Intent(ctx, MenuActivity.class);
                    break;
            }
            ctx.startActivity(i);
        }
    }
}
