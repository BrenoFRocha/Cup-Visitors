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

import io.github.brenofrocha.cupvisitors.Activities.MainActivity;
import io.github.brenofrocha.cupvisitors.Activities.MenuActivity;
import io.github.brenofrocha.cupvisitors.Classes.Background;
import io.github.brenofrocha.cupvisitors.Classes.Button;
import io.github.brenofrocha.cupvisitors.R;

/**
 * Created by Breno on 12/01/2019.
 */

public class TutorialView extends View implements Runnable
{
    Handler handler;
    private int bSizeX, bSizeY, mgBPosX, mnBPosY, nbBPosX, gbBPosY, screenNumber;
    public int screenX, screenY;
    private Background background;
    private Button menuButton, nextButton, backButton, gameButton;
    private Paint p;
    private Bitmap tutorialArt;
    private Context ctx;

    //Fade
    public int alpha;
    public boolean fadeIn, fadeOut;
    public String sceneFade;
    private Paint paintFade;

    public TutorialView(Context ctx)
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
        screenNumber = 0;

        //Buttons
        bSizeX = (int)((screenX*0.557f)/5.5f);
        bSizeY = (int)((screenY*1.0694f)/5.5f);

        nbBPosX = (int)(screenX - bSizeX*2f);
        mgBPosX = (int)(bSizeX);
        mnBPosY = (int)(screenY - bSizeY*1.4f);
        gbBPosY = (int)(screenY - ((bSizeY*1.3f)*2f));
        Bitmap menuBImage, gameBImage, nextBImage, backBImage;
        menuBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.menu_button), bSizeX, bSizeY, false);
        gameBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.play_button), bSizeX, bSizeY, false);
        nextBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.next_button), bSizeX, bSizeY, false);
        backBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.back_button), bSizeX, bSizeY, false);

        menuButton = new Button(mgBPosX, mnBPosY, menuBImage);
        gameButton = new Button(mgBPosX, gbBPosY,gameBImage);
        nextButton = new Button(nbBPosX, mnBPosY,nextBImage);
        backButton = new Button(nbBPosX, gbBPosY,backBImage);
        //Tutorial
        tutorialArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tutorial0), screenX, screenY, false);
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
                if(touchX >= mgBPosX &&
                        touchX <= mgBPosX + bSizeX &&
                        touchY >= mnBPosY &&
                        touchY <= mnBPosY + bSizeY)
                {
                    if(!fadeOut && !fadeIn) {
                        alpha = 0;
                        sceneFade = "menu";
                        fadeOut = true;
                    }
                }
                else if(touchX >= mgBPosX &&
                        touchX <= mgBPosX + bSizeX &&
                        touchY >= gbBPosY &&
                        touchY <= gbBPosY + bSizeY)
                {
                    if(!fadeOut && !fadeIn) {
                        alpha = 0;
                        sceneFade = "game";
                        fadeOut = true;
                    }
                }
                else if(touchX >= nbBPosX &&
                        touchX <= nbBPosX + bSizeX &&
                        touchY >= mnBPosY &&
                        touchY <= mnBPosY + bSizeY && screenNumber < 4)
                {
                    screenNumber += 1;
                    changeScreenImage(screenNumber);
                }
                else if(touchX >= nbBPosX &&
                        touchX <= nbBPosX + bSizeX &&
                        touchY >= gbBPosY &&
                        touchY <= gbBPosY + bSizeY && screenNumber > 0)
                {
                    screenNumber -= 1;
                    changeScreenImage(screenNumber);
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
        canvas.drawBitmap(tutorialArt, 0, 0, p);
        menuButton.draw(canvas, p);
        gameButton.draw(canvas, p);
        if(screenNumber > 0)
        {
            backButton.draw(canvas, p);
        }
        if(screenNumber < 4)
        {
            nextButton.draw(canvas, p);
        }
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
                case "game":
                    i = new Intent(ctx, MainActivity.class);
                    break;
                case "menu":
                    i = new Intent(ctx, MenuActivity.class);
                    break;
            }
            ctx.startActivity(i);
        }
    }

    private void changeScreenImage(int screen)
    {
        switch (screen)
        {
            case 0:
                tutorialArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tutorial0), screenX, screenY, false);
                break;
            case 1:
                tutorialArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tutorial1), screenX, screenY, false);
                break;
            case 2:
                tutorialArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tutorial2), screenX, screenY, false);
                break;
            case 3:
                tutorialArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tutorial3), screenX, screenY, false);
                break;
            case 4:
                tutorialArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.tutorial4), screenX, screenY, false);
                break;
        }
    }
}
