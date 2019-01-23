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

import io.github.brenofrocha.cupvisitors.Activities.GameOverActivity;
import io.github.brenofrocha.cupvisitors.Activities.MenuActivity;
import io.github.brenofrocha.cupvisitors.Activities.VictoryActivity;
import io.github.brenofrocha.cupvisitors.Classes.Background;
import io.github.brenofrocha.cupvisitors.Classes.Button;
import io.github.brenofrocha.cupvisitors.Classes.Enemy;
import io.github.brenofrocha.cupvisitors.Classes.LevelManager;
import io.github.brenofrocha.cupvisitors.Classes.Player;
import io.github.brenofrocha.cupvisitors.Classes.Shoot;
import io.github.brenofrocha.cupvisitors.R;

/**
 * Created by Breno on 02/08/2016.
 */

public class MainView extends View implements Runnable
{
    private Handler handler;
    private Paint p;
    private Context ctx;
    private Bitmap backgroundGMImage, lifeImage, resizedEnemyImage;
    private Bitmap[] enemyImages;
    private Random r;

    private Background background;
    private Enemy[][] enemies;
    private Player player;
    private Shoot shoot;
    private Button menuButton, shootButton, shootButtonPressed, soundButton, noSoundButton, pauseButton, pausedButton;
    private LevelManager levelManager;

    private int columnsOfEnemies, linesOfEnemies, sBPosY, mBPosY, sdBPosY, bSizeX, bSizeY, bPosX, cSSizeX, min, max, pBPosX, pBPosY, pBSizeX, pBSizeY;
    public int level;
    public static int screenX,screenY, enemiesVelocityX;
    private float enemySizeY;
    private float[] enemyPosY;
    public static float enemySizeX;
    private boolean sound, shootPressed, playerWin;
    public static boolean pause;

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
        r = new Random();

        //Fade
        alpha = 255;
        paintFade = new Paint();
        paintFade.setColor(Color.BLACK);
        paintFade.setAlpha(alpha);
        fadeIn = true;

        Bitmap sBImage, sPBImage, mBImage, sdBImage, nosdBImage;
        cSSizeX = (((screenX/30)/4)*3);
        int cSSizeY = (screenX/30);
        lifeImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.cup_symbol), cSSizeX, cSSizeY, false);


        //Shoot
        shoot = new Shoot(ctx);

        //Enemy
        enemiesVelocityX = 1;
        linesOfEnemies = 7;
        columnsOfEnemies = 9;
        enemyImages = new Bitmap[11];
        enemyPosY = new float[linesOfEnemies];
        enemyImages[0] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.usa);
        enemyImages[1] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.netherlands);
        enemyImages[2] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.portugal);
        enemyImages[3] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.spain);
        enemyImages[4] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.france);
        enemyImages[5] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.england);
        enemyImages[6] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.argentina);
        enemyImages[7] = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.uruguay);
        enemyImages[8] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.germany);
        enemyImages[9] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.italy);
        enemyImages[10] = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.brazil);
        enemySizeX = screenX/enemyImages[0].getWidth()*2.5f;
        //Player
        player = new Player(ctx, this);
        //Enemy
        enemySizeY = screenY/enemyImages[0].getHeight()*2.2f;
        enemies = new Enemy[linesOfEnemies][columnsOfEnemies];
        min = 0;
        max = 2;
        for(int i = 0; i < linesOfEnemies; i++)
        {
            for(int j = 0; j < columnsOfEnemies; j++)
            {
                int id = r.nextInt(max - min + 1) + min;
                resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                enemyPosY[i] = ((resizedEnemyImage.getHeight() + enemySizeY/4) * i) + ((screenY*1.0694f)/7f);
                enemies[i][j] = new Enemy(ctx, ((screenX/12f)*j) + screenX/19.5f, enemyPosY[i], resizedEnemyImage, id, shoot);
            }
        }
        level = 0;

        //Buttons
        bSizeX = (int)((screenX*0.557f)/5f);
        bSizeY = (int)((screenY*1.0694f)/5f);
        bPosX = (int)(((screenX - (enemySizeX * 3)) + ((enemySizeX * 3)/2)) - (bSizeX/2));
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
        //Pause Button
        pBSizeX = (int)((screenX*0.557f)/8f);
        pBSizeY = (int)((screenY*1.0694f)/8f);
        pBPosX = (int)(0);
        pBPosY = (int)(0);
        Bitmap pBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.pause_button), pBSizeX, pBSizeY, false);
        Bitmap npBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.paused_button), pBSizeX, pBSizeY, false);
        pauseButton = new Button(pBPosX, pBPosY, pBImage);
        pausedButton = new Button(pBPosX, pBPosY, npBImage);

        //Background
        background = new Background(ctx, screenX, screenY);
        backgroundGMImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.game_menu_background), (int)(enemySizeX * 3),screenY, false);

        //Level Manager
        levelManager = new LevelManager(ctx, this);
        levelManager.levelFinished = true;
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
                        touchY <= sBPosY + bSizeY && !pause && player.life > 0)
                {
                    if(!shoot.thereIsAShoot) {
                        shoot.posX = (player.posX + player.sizeX/2f) - (shoot.sizeX/2f);
                        shoot.thereIsAShoot = true;
                    }
                    shootPressed = true;
                }
                else if(touchX >= pBPosX &&
                        touchX <= pBPosX + pBSizeX &&
                        touchY >= pBPosY &&
                        touchY <= pBPosY + pBSizeY && !fadeIn && !fadeOut)
                {
                    pause = !pause;
                }
                else if(touchX >= 0 &&
                        touchX < (screenX/2 - (enemySizeX*3/2)) && !pause)
                {
                    player.MoveRight = false;
                    player.MoveLeft = true;
                }
                else if(touchX >= (screenX/2 - (enemySizeX*3/2)) &&
                        touchX < (screenX - (enemySizeX*3)) && !pause)
                {
                    player.MoveLeft = false;
                    player.MoveRight = true;
                }
                else if(touchX > (screenX - (enemySizeX*3)) && !pause)
                {
                    player.MoveRight = false;
                    player.MoveLeft = false;
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
                if(touchX >= 0 &&
                touchX < (screenX/2 - (enemySizeX*3/2)) && !pause)
                {
                    player.MoveRight = false;
                    player.MoveLeft = true;
                }
                if(touchX >= (screenX/2 - (enemySizeX*3/2)) &&
                touchX < (screenX - (enemySizeX*3)) && !pause)
                {
                    player.MoveLeft = false;
                    player.MoveRight = true;
                }
                if(touchX > (screenX - (enemySizeX*3)) && !pause)
                {
                    player.MoveRight = false;
                    player.MoveLeft = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                shootPressed = false;
                player.MoveRight = false;
                player.MoveLeft = false;
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
                enemies[i][j].draw(canvas, p, enemyPosY[i]);
            }
        }
        shoot.draw(canvas,p);
        player.draw(canvas,p);
        if(player.life >= 1) {
            for (int i = 0; i < player.life; i++) {
                canvas.drawBitmap(lifeImage, ((cSSizeX*1.2f)*i), (screenY - lifeImage.getHeight()*1.2f), p);
            }
        }
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
        if(pause && !levelManager.loadingLevel && !levelManager.levelFinished && !fadeIn && !fadeOut)
        {
            pausedButton.draw(canvas, p);
        }
        else
        {
            pauseButton.draw(canvas, p);
        }
        levelManager.draw(canvas,p);
        canvas.drawRect(0,0,screenX,screenY,paintFade);
    }

    private void update()
    {
        background.update();
        if(!pause && !levelManager.levelFinished && !levelManager.loadingLevel && !fadeIn && !fadeOut)
        {
            //Shoot
            shoot.update((player.posX + player.sizeX/2) - (shoot.sizeX/2), player.posY, player.sizeY);

            //Enemies
            boolean closer = false;
            for(int i = 0; i < linesOfEnemies; i++)
            {
                for(int j = 0; j < columnsOfEnemies; j++) {
                    if(!enemies[i][j].isDestroyed) {
                        if (enemies[i][j].posX <= 0 ||
                                enemies[i][j].posX + enemySizeX > (screenX - (enemySizeX * 3))) {
                            enemiesVelocityX *= -1;
                            for (int k = 0; k < linesOfEnemies; k++) {
                                for (int l = 0; l < columnsOfEnemies; l++) {
                                    if (enemiesVelocityX < 1) {
                                        enemies[k][l].posX -= 2;
                                    } else {
                                        enemies[k][l].posX += 2;
                                    }
                                }
                            }
                            closer = true;
                        }
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
                    enemies[i][j].update(shoot, player);
                }
            }

            //Player
            player.update();
        }
        checkDestruction();
        levelManager.update(player);
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
            alpha -= 15;
            paintFade.setAlpha(alpha);
            if(alpha == 0)
            {
                fadeIn = false;
            }
        }
    }

    private void setFadeOut()
    {
        alpha += 15;
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
                case "gameover":
                    i = new Intent(ctx, GameOverActivity.class);
                    i.putExtra("level", level);
                    break;
                case "victory":
                    i = new Intent(ctx, VictoryActivity.class);
                    break;
            }
            System.gc();
            ((Activity) ctx).finish();
            ctx.startActivity(i);
        }
    }

    public void newLevel()
    {
        switch (level)
        {
            case 2:
                min = 3;
                max = 5;
                for(int i = 0; i < linesOfEnemies; i++)
                {
                    for(int j = 0; j < columnsOfEnemies; j++)
                    {
                        int id = r.nextInt(max - min + 1) + min;
                        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                        enemyPosY[i] = ((resizedEnemyImage.getHeight() + enemySizeY/4) * i) + ((screenY*1.0694f)/7f);
                        enemies[i][j] = new Enemy(ctx, ((screenX/12f)*j) + screenX/19.5f, enemyPosY[i], resizedEnemyImage, id, shoot);
                    }
                }
                break;
            case 3:
                min = 6;
                max = 7;
                for(int i = 0; i < linesOfEnemies; i++)
                {
                    for(int j = 0; j < columnsOfEnemies; j++)
                    {
                        int id = r.nextInt(max - min + 1) + min;
                        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                        enemyPosY[i] = ((resizedEnemyImage.getHeight() + enemySizeY/4) * i) + ((screenY*1.0694f)/7f);
                        enemies[i][j] = new Enemy(ctx, ((screenX/12f)*j) + screenX/19.5f, enemyPosY[i], resizedEnemyImage, id, shoot);
                    }
                }
                break;
            case 4:
                for(int i = 0; i < linesOfEnemies; i++)
                {
                    for(int j = 0; j < columnsOfEnemies; j++)
                    {
                        int id = 8;
                        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                        enemyPosY[i] = ((resizedEnemyImage.getHeight() + enemySizeY/4) * i) + ((screenY*1.0694f)/7f);
                        enemies[i][j] = new Enemy(ctx, ((screenX/12f)*j) + screenX/19.5f, enemyPosY[i], resizedEnemyImage, id, shoot);
                    }
                }
                break;
            case 5:
                for(int i = 0; i < linesOfEnemies; i++)
                {
                    for(int j = 0; j < columnsOfEnemies; j++)
                    {
                        int id = 9;
                        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                        enemyPosY[i] = ((resizedEnemyImage.getHeight() + enemySizeY/4) * i) + ((screenY*1.0694f)/7f);
                        enemies[i][j] = new Enemy(ctx, ((screenX/12f)*j) + screenX/19.5f, enemyPosY[i], resizedEnemyImage, id, shoot);
                    }
                }
                break;
            case 6:
                min = 0;
                max = 9;
                for(int i = 0; i < linesOfEnemies; i++)
                {
                    for(int j = 0; j < columnsOfEnemies; j++)
                    {
                        int id = r.nextInt(max - min + 1) + min;
                        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                        enemyPosY[i] = ((resizedEnemyImage.getHeight() + enemySizeY/4) * i) + ((screenY*1.0694f)/7f);
                        enemies[i][j] = new Enemy(ctx, ((screenX/12f)*j) + screenX/19.5f, enemyPosY[i], resizedEnemyImage, id, shoot);
                    }
                }
                break;
            case 7:
                for(int i = 0; i < linesOfEnemies; i++)
                {
                    for(int j = 0; j < columnsOfEnemies; j++)
                    {
                        int id = 10;
                        resizedEnemyImage = Bitmap.createScaledBitmap(enemyImages[id], (int) enemySizeX, (int) enemySizeY, false);
                        enemyPosY[i] = ((resizedEnemyImage.getHeight() + enemySizeY/4) * i) + ((screenY*1.0694f)/7f);
                        enemies[i][j] = new Enemy(ctx, ((screenX/12f)*j) + screenX/19.5f, enemyPosY[i], resizedEnemyImage, id, shoot);
                    }
                }
                break;
        }
    }

    private void checkDestruction()
    {
        if(!levelManager.levelFinished && !levelManager.loadingLevel) {
            int destroyedEnemies = 0;
            for (int i = 0; i < linesOfEnemies; i++) {
                for (int j = 0; j < columnsOfEnemies; j++) {
                    if (enemies[i][j].isDestroyed) {
                        destroyedEnemies += 1;
                    }
                }
            }
            if (destroyedEnemies == (linesOfEnemies * columnsOfEnemies)) {
                if(level < 7) {
                    System.gc();
                    shoot.thereIsAShoot = false;
                    player.posX = (screenX / 2) - ((enemySizeX * 3) / 2) - player.sizeX / 2;
                    levelManager.levelFinished = true;
                }
                else
                {
                    victoryScene();
                }
            }
        }
    }

    public void gameOver()
    {
        if(!fadeOut && !fadeIn && !pause)
        {
            pause = true;
            alpha = 0;
            sceneFade = "gameover";
            fadeOut = true;
        }
    }

    public void victoryScene()
    {
        if(!fadeOut && !fadeIn && !playerWin) {
            playerWin = true;
            pause = true;
            alpha = 0;
            sceneFade = "victory";
            fadeOut = true;
        }
    }
}
