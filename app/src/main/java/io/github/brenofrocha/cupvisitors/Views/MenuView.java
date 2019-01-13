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

import io.github.brenofrocha.cupvisitors.Activities.AboutActivity;
import io.github.brenofrocha.cupvisitors.Activities.MainActivity;
import io.github.brenofrocha.cupvisitors.Classes.Background;
import io.github.brenofrocha.cupvisitors.Classes.Button;
import io.github.brenofrocha.cupvisitors.R;

/**
 * Created by Breno on 12/01/2019.
 */

public class MenuView extends View implements Runnable
{
    Handler handler;
    private int bSizeX, bSizeY, pBPosX, pBPosY, aBPosX, aBPosY, iBPosX, iBPosY;
    public static int screenX, screenY;
    private Background background;
    private Button playButton, aboutButton, instructionsButton;
    private Paint p;
    private Bitmap menuArt;
    private Context ctx;

    //Fade
    public int alpha;
    public boolean fadeIn, fadeOut;
    public String sceneFade;
    private Paint paintFade;

    public MenuView(Context ctx)
    {
        super(ctx);
        this.ctx = ctx;

        //General
        handler = new android.os.Handler();
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
        bSizeX = (int)((screenX*0.557f)/3.5f);
        bSizeY = (int)((screenY*1.0694f)/3.5f);
        pBPosX = (int)(screenX/2 - bSizeX/2);
        pBPosY = (int)(screenY/2);
        aBPosX = (int)(bSizeX);
        aBPosY = (int)(screenY/2 + bSizeY/2);
        iBPosX = (int)(screenX - bSizeX*2);
        iBPosY = (int)(screenY/2 + bSizeY/2);
        Bitmap playBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.play_button), bSizeX, bSizeY, false);
        playButton = new Button(pBPosX, pBPosY, playBImage);
        Bitmap aboutBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.about_button), bSizeX, bSizeY, false);
        aboutButton = new Button(aBPosX, aBPosY, aboutBImage);
        Bitmap instructionsBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.instructions_button), bSizeX, bSizeY, false);
        instructionsButton = new Button(iBPosX, iBPosY, instructionsBImage);

        //Menu
        menuArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.background), screenX, screenY, false);
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
                if(touchX >= pBPosX &&
                touchX <= pBPosX + bSizeX &&
                touchY >= pBPosY &&
                touchY <= pBPosY + bSizeY)
                {
                    if(!fadeOut && !fadeIn) {
                        alpha = 0;
                        sceneFade = "game";
                        fadeOut = true;
                    }
                }
                else if(touchX >= aBPosX &&
                touchX <= aBPosX + bSizeX &&
                touchY >= aBPosY &&
                touchY <= aBPosY + bSizeY)
                {
                    if(!fadeOut && !fadeIn) {
                        alpha = 0;
                        sceneFade = "about";
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
        canvas.drawBitmap(menuArt, 0, 0, p);
        playButton.draw(canvas, p);
        aboutButton.draw(canvas, p);
        instructionsButton.draw(canvas, p);
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
                case "about":
                    i = new Intent(ctx, AboutActivity.class);
                    break;
            }
            ctx.startActivity(i);
        }
    }
}
