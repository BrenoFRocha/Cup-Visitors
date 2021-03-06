package io.github.brenofrocha.cupvisitors.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import io.github.brenofrocha.cupvisitors.Activities.AboutActivity;
import io.github.brenofrocha.cupvisitors.Activities.MenuActivity;
import io.github.brenofrocha.cupvisitors.Classes.Background;
import io.github.brenofrocha.cupvisitors.Classes.Button;
import io.github.brenofrocha.cupvisitors.R;

/**
 * Created by Breno on 12/01/2019.
 */

public class VictoryView extends View implements Runnable
{
    Handler handler;
    private int bSizeX, bSizeY, bPosY, mBPosX, aBPosX;
    public int screenX, screenY;
    private boolean sound;
    private Background background;
    private Button menuButton, aboutButton;
    private Paint p;
    private Bitmap victoryArt;
    private Context ctx;
    private final MediaPlayer song;

    //Fade
    public int alpha;
    public boolean fadeIn, fadeOut;
    public String sceneFade;
    private Paint paintFade;

    public VictoryView(Context ctx, boolean sound)
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

        //Sound
        this.sound = sound;
        song = MediaPlayer.create(ctx, R.raw.victory_sound);
        song.setLooping(true);
        if(this.sound)
        {
            if(song.isPlaying()) {
                song.pause();
                song.seekTo(0);
            }
            song.start();
        }

        //Background
        background = new Background(ctx, screenX, screenY);

        //Buttons
        bSizeX = (int)((screenX*0.557f)/4f);
        bSizeY = (int)((screenY*1.0694f)/4f);
        bPosY = (int)(screenY - bSizeY*1.5f);

        mBPosX = (int)(((screenX/2) - (bSizeX/2)) + (bSizeX*2f));
        Bitmap menuBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.menu_button), bSizeX, bSizeY, false);
        menuButton = new Button(mBPosX, bPosY, menuBImage);
        aBPosX = (int)(((screenX/2) - (bSizeX/2)) - (bSizeX*2f));
        Bitmap aboutBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.about_button), bSizeX, bSizeY, false);
        aboutButton = new Button(aBPosX, bPosY, aboutBImage);

        victoryArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.victory_background), screenX, screenY, false);
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
                        touchY >= bPosY &&
                        touchY <= bPosY + bSizeY)
                {
                    if(!fadeOut && !fadeIn) {
                        alpha = 0;
                        sceneFade = "menu";
                        fadeOut = true;
                    }
                }
                if(touchX >= aBPosX &&
                        touchX <= aBPosX + bSizeX &&
                        touchY >= bPosY &&
                        touchY <= bPosY + bSizeY)
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
        canvas.drawBitmap(victoryArt, 0, 0, p);
        menuButton.draw(canvas, p);
        aboutButton.draw(canvas, p);
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
            song.stop();
            fadeOut = false;
            Intent i = new Intent();
            switch (sceneFade)
            {
                case "menu":
                    i = new Intent(ctx, MenuActivity.class);
                    break;

                case "about":
                    i = new Intent(ctx, AboutActivity.class);
                    break;
            }
            i.putExtra("sound", sound);
            ctx.startActivity(i);
        }
    }
}
