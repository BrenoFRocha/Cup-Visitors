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

public class AboutView extends View implements Runnable
{
    Handler handler;
    private int bSizeX, bSizeY, mBPosX, mBPosY;
    public static int screenX, screenY;
    private Background background;
    private Button menuButton;
    private Paint p;
    private Bitmap aboutArt;
    private Context ctx;

    //Fade
    public int alpha;
    public boolean fadeIn, fadeOut;
    public String sceneFade;
    private Paint paintFade;

    public AboutView(Context ctx, Activity act)
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
        bSizeX = (int)((screenX*0.557f)/4.5f);
        bSizeY = (int)((screenY*1.0694f)/4.5f);
        mBPosX = (int)(screenX - bSizeX*1.75f);
        mBPosY = (int)(screenY - bSizeY*1.3f);
        Bitmap menuBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.menu_button), bSizeX, bSizeY, false);
        menuButton = new Button(mBPosX, mBPosY, menuBImage);

        //About
        aboutArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.about_background), screenX, screenY, false);
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
        canvas.drawBitmap(aboutArt, 0, 0, p);
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
