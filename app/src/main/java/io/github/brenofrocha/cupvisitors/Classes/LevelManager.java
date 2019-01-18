package io.github.brenofrocha.cupvisitors.Classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.Timer;
import java.util.TimerTask;

import io.github.brenofrocha.cupvisitors.Views.MainView;

public class LevelManager
{
    private Context ctx;
    private Timer timer;
    private MainView mainView;
    private Paint paint;
    public boolean loadingLevel, levelFinished;
    private int secondsCounter;

    public LevelManager(Context ctx, MainView mainView) {
        this.ctx = ctx;
        this.mainView = mainView;
        timer = new Timer();
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(),"fonts/SoccerLeagueCollege.ttf");
        paint = new Paint();
        paint.setTypeface(tf);
        setTextSizeForWidth(paint, MainView.screenX/15, "5");
        counter();
    }

    public void draw(Canvas canvas, Paint p)
    {
        if(loadingLevel) {
            canvas.drawRect(0, 0, MainView.screenX - MainView.enemySizeX*3, MainView.screenY, p);
            canvas.drawText(Integer.toString(secondsCounter),(MainView.screenX - MainView.enemySizeX*3)/2,MainView.screenY/2,paint);
        }
    }

    private void counter() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(loadingLevel) {
                    if (secondsCounter > 0) {
                        secondsCounter -= 1;
                    }
                }
            }
        }, 1000, 1000);
    }

    public void update()
    {
        if(levelFinished)
        {
            levelFinished = false;
            MainView.pause = true;
            secondsCounter = 5;
            mainView.level += 1;
            mainView.newLevel();
            loadingLevel = true;
        }
        else if(loadingLevel)
        {
            if(secondsCounter <= 0)
            {
                loadingLevel = false;
                MainView.pause = false;
            }
        }
    }

    private void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {
        final float testTextSize = 48f;
        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();
        this.paint.setTextSize(desiredTextSize);
    }
}
