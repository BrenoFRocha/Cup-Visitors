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
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import io.github.brenofrocha.cupvisitors.Activities.AboutActivity;
import io.github.brenofrocha.cupvisitors.Activities.MainActivity;
import io.github.brenofrocha.cupvisitors.Classes.Background;
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
    private int columnsOfEnemies, linesOfEnemies, posBSX,posBSY;
    public static int screenX,screenY, enemiesVelocityX;
    private float[] enemyPosY;
    public static float enemySizeX;
    private Background background;
    private Enemy[][] enemies;
    private Player player;
    private Shoot shoot;
    private Paint p;
    private Bitmap shootButton,resizedShootButton, resizedEnemyImage;
    private Context ctx;

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
        shootButton = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.shootbutton);
        resizedShootButton = Bitmap.createScaledBitmap(shootButton, (int)(screenX/6.5f),screenY/4, false);
        posBSX = screenX-resizedShootButton.getWidth() - screenX /150;
        posBSY = screenY-resizedShootButton.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int mActivePointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(mActivePointerId);
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        float xt = event.getX(0);
        float yt = event.getY(0);
        int index = MotionEventCompat.getActionIndex(event);
        int xPos;
        float xtPos;
        float ytPos;
        if(event.getPointerCount() > 1)
        {
            xtPos = event.getX(1);
            ytPos = event.getX(1);
        }
        else
        {
            xtPos = -1;
            ytPos = -1;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX(0);
                break;
            case MotionEvent.ACTION_DOWN:
                if(xt >= posBSX && xt <= posBSX + resizedShootButton.getWidth() && yt >= posBSY && yt <= posBSY + resizedShootButton.getHeight() && !shoot.thereIsAShoot || xtPos >= posBSX && xtPos <= posBSX + resizedShootButton.getWidth() && ytPos >= posBSY && ytPos <= posBSY + resizedShootButton.getHeight() && !shoot.thereIsAShoot)
                {
                    shoot.thereIsAShoot = true;
                }
                break;
        }

        if (event.getPointerCount() > 1)
        {
            xPos = (int)MotionEventCompat.getX(event, index);
            if(x >= MainView.screenX/2 - enemySizeX*1.5f &&
                    x <= MainView.screenX - enemySizeX*3 ||
                    xPos >= MainView.screenX/2 - enemySizeX*1.5f &&
                    xPos <= MainView.screenX - enemySizeX*3)
            {
                player.MoveRight = true;
                player.MoveLeft = false;
            }
            else if(x > 0 &&
                    x < MainView.screenX/2 - enemySizeX*1.5f ||
                    xPos > 0 &&
                    xPos < MainView.screenX/2 - enemySizeX*1.5f)
            {
                player.MoveRight = false;
                player.MoveLeft = true;
            }
            if(xtPos >= posBSX && xtPos <= posBSX + resizedShootButton.getWidth() && ytPos >= posBSY && ytPos <= posBSY + resizedShootButton.getHeight() && !shoot.thereIsAShoot || xt >= posBSX && xt <= posBSX + resizedShootButton.getWidth() && yt >= posBSY && yt <= posBSY + resizedShootButton.getHeight() && !shoot.thereIsAShoot)
            {
                shoot.thereIsAShoot = true;
            }
        }
        else
        {
            if (x >= MainView.screenX / 2 - enemySizeX * 1.5f &&
                    x <= MainView.screenX - enemySizeX * 3)
            {
                player.MoveRight = true;
                player.MoveLeft = false;
            }
            else if (x > 0 &&
                    x < MainView.screenX / 2 - enemySizeX * 1.5f)
            {
                player.MoveRight = false;
                player.MoveLeft = true;
            }
            if (xt >= posBSX &&
                    xt <= posBSX + resizedShootButton.getWidth() &&
                    yt >= posBSY &&
                    yt <= posBSY + resizedShootButton.getHeight() && !shoot.thereIsAShoot)
            {
                shoot.thereIsAShoot = true;
            }
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
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
        canvas.drawRect(screenX - enemySizeX * 3, 0, screenX, screenY, p);
        for(int i = 0; i < linesOfEnemies; i++)
        {
            for(int j = 0; j < columnsOfEnemies; j++) {
                enemies[i][j].Draw(canvas, p, enemyPosY[i]);
            }
        }
        canvas.drawBitmap(resizedShootButton, posBSX, posBSY, p);
        shoot.Draw(canvas,p);
        player.Draw(canvas,p);
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
                case "game":
                    i = new Intent(ctx, MainActivity.class);
                    break;
            }
            ctx.startActivity(i);
        }
    }
}
