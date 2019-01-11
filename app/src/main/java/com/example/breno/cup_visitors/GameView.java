package com.example.breno.cup_visitors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Breno on 02/08/2016.
 */

public class GameView extends View implements Runnable
{
    Handler handler;
    public static int screenX,screenY;
    private int linesOfEnemies,XT,YT,XTA,YTA,XTA2,YTA2;
    private float actualShootPosX;
    private Background background;
    private Enemies[] enemies;
    private Player player;
    private Shoot shoot;
    private Paint p;
    private int posBSX,posBSY,firstShoot;
    private Bitmap shootButton,resizedShootButton;
    public GameView(Context ctx)
    {
        super(ctx);
        handler = new android.os.Handler();
        handler.post(this);
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screenX = display.getWidth();
        screenY = display.getHeight();
        p = new Paint();
        linesOfEnemies = 6;
        enemies = new Enemies[linesOfEnemies];
        for(int i = 0; i < linesOfEnemies; i++)
        {
            enemies[i] = new Enemies(ctx, i);
        }
        player = new Player(ctx);
        shoot = new Shoot(ctx,player.sizeY);
        background = new Background(screenX,screenY,ctx);
        shootButton = BitmapFactory.decodeResource(ctx.getResources(),R.drawable.shootbutton);
        resizedShootButton = getResizedBitmap(shootButton, (int)(screenX/6.5f),screenY/4);
        posBSX = screenX-resizedShootButton.getWidth() - screenX /150;
        posBSY = screenY-resizedShootButton.getHeight();
    }
    public static Bitmap getResizedBitmap(Bitmap btmp, int newWidth, int newHeight)
    {
        int width = btmp.getWidth();
        int height = btmp.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(btmp, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
    private int mActivePointerId;
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mActivePointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(mActivePointerId);
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        float xt = event.getX(0);
        float yt = event.getY(0);
        int index = MotionEventCompat.getActionIndex(event);
        int xPos = -1;
        int yPos = -1;
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
                y = (int) event.getY(0);
                if(event.getPointerCount() > 1)
                {
                    xPos = (int)MotionEventCompat.getX(event, index);
                    yPos = (int)MotionEventCompat.getY(event, index);
                }
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
            yPos = (int)MotionEventCompat.getY(event, index);
            if(x >= GameView.screenX/2 - enemies[0].sizeX*1.5f && x <= GameView.screenX - enemies[0].sizeX*3 || xPos >= GameView.screenX/2 - enemies[0].sizeX*1.5f && xPos <= GameView.screenX - enemies[0].sizeX*3)
            {
                player.MoveRight = true;
                player.MoveLeft = false;
            }
            else if(x > 0 && x < GameView.screenX/2 - enemies[0].sizeX*1.5f || xPos > 0 && xPos < GameView.screenX/2 - enemies[0].sizeX*1.5f)
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
            if (x >= GameView.screenX / 2 - enemies[0].sizeX * 1.5f && x <= GameView.screenX - enemies[0].sizeX * 3)
            {
                player.MoveRight = true;
                player.MoveLeft = false;
            }
            else if (x > 0 && x < GameView.screenX / 2 - enemies[0].sizeX * 1.5f)
            {
                player.MoveRight = false;
                player.MoveLeft = true;
            }
            if (xt >= posBSX && xt <= posBSX + resizedShootButton.getWidth() && yt >= posBSY && yt <= posBSY + resizedShootButton.getHeight() && !shoot.thereIsAShoot)
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
        background.Draw(canvas, p);
        canvas.drawRect(canvas.getWidth() - enemies[0].sizeX * 3, 0, canvas.getWidth(), canvas.getHeight(), p);
        for(int i = 0; i < linesOfEnemies; i++)
        {
            enemies[i].Draw(canvas, p);
        }
        canvas.drawBitmap(resizedShootButton, posBSX, posBSY, p);
        shoot.Draw(canvas,p);
        player.Draw(canvas,p);

    }

    private void Update()
    {
        background.Update();
        for(int i = 0; i < linesOfEnemies; i++)
        {
            enemies[i].Update(shoot.posX,shoot.posY,shoot.posX+shoot.sizeX,shoot.posY-shoot.sizeY);
        }
        player.Update();
        shoot.Update(player.posX + player.sizeX/4f,player.sizeY);
    }

    @Override
    public void run()
    {
        handler.postDelayed(this, 30);
        Update();
        invalidate();
    }
}
