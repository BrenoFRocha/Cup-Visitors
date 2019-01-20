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
import android.view.MotionEvent;
import android.view.View;

import io.github.brenofrocha.cupvisitors.Activities.MenuActivity;
import io.github.brenofrocha.cupvisitors.Classes.Background;
import io.github.brenofrocha.cupvisitors.Classes.Button;
import io.github.brenofrocha.cupvisitors.R;

/**
 * Created by Breno on 12/01/2019.
 */

public class GameOverView extends View implements Runnable
{
    Handler handler;
    private int bSizeX, bSizeY, mBPosX, mBPosY;
    public static int screenX, screenY;
    private Background background;
    private Button menuButton;
    private Paint p;
    private Bitmap gameOverArt;
    private Context ctx;

    //Fade
    public int alpha;
    public boolean fadeIn, fadeOut;
    public String sceneFade;
    private Paint paintFade;

    public GameOverView(Context ctx, int level)
    {
        super(ctx);
        this.ctx = ctx;

        //General
        handler = new Handler();
        handler.post(this);
        p = new Paint();
        DisplayMetrics display = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(display);
        screenX = display.widthPixels;
        screenY = display.heightPixels;

        //Fade
        alpha = 255;
        paintFade = new Paint();
        paintFade.setColor(Color.BLACK);
        paintFade.setAlpha(alpha);
        fadeIn = true;

        //Background
        background = new Background(ctx, screenX, screenY);

        //Buttons
        bSizeX = (int)((screenX*0.557f)/5.5f);
        bSizeY = (int)((screenY*1.0694f)/5.5f);
        mBPosX = (int)(screenX/2) - (bSizeX/2);
        mBPosY = (int)(screenY - bSizeY*1.35f);
        Bitmap menuBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.menu_button), bSizeX, bSizeY, false);
        menuButton = new Button(mBPosX, mBPosY, menuBImage);

        //Game Over
        switch (level)
        {
            case 1:
                gameOverArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.gameover0), screenX, screenY, false);
                break;
            case 2:
                gameOverArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.gameover1), screenX, screenY, false);
                break;
            case 3:
                gameOverArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.gameover2), screenX, screenY, false);
                break;
            case 4:
                gameOverArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.gameover3), screenX, screenY, false);
                break;
            case 5:
                gameOverArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.gameover4), screenX, screenY, false);
                break;
            case 6:
                gameOverArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.gameover5), screenX, screenY, false);
                break;
            case 7:
                gameOverArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.gameoverspecial), screenX, screenY, false);
                break;
        }
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
                if(touchX >= mBPosX &&
                        touchX <= mBPosX + bSizeX &&
                        touchY >= mBPosY &&
                        touchY <= mBPosY + bSizeY)
                {
                    if(!fadeOut && !fadeIn) {
                        alpha = 0;
                        sceneFade = "menu";
                        fadeOut = true;
                    }
                }
                break;
        }
        return true;
    }

    private void update()
    {
        background.update();
        if(fadeIn) {
            setFadeIn();
        }
        else if(fadeOut) {
            setFadeOut();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        background.draw(canvas, p);
        canvas.drawBitmap(gameOverArt, 0, 0, p);
        menuButton.draw(canvas, p);
        canvas.drawRect(0,0,screenX,screenY,paintFade);
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
            }
            ctx.startActivity(i);
        }
    }
}
