package io.github.brenofrocha.cupvisitors.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.Timer;
import java.util.TimerTask;

import io.github.brenofrocha.cupvisitors.R;
import io.github.brenofrocha.cupvisitors.Views.MainView;

public class LevelManager
{
    private Context ctx;
    private Timer timer;
    private MainView mainView;
    private Paint paint;
    private Bitmap background;
    public boolean loadingLevel, levelFinished;
    private int secondsCounter;
    final int color = 0xffFEDF00;

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
            canvas.drawBitmap(background, 0,0,p);
            paint.setColor(color);
            canvas.drawText(Integer.toString(secondsCounter),((MainView.screenX - MainView.enemySizeX*3)/2 - MainView.screenX/27.5f),MainView.screenY/2 + MainView.screenY/10,paint);
        }
    }

    private void counter() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(loadingLevel && !mainView.fadeOut && !mainView.fadeIn) {
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
            background = backgroundImage(mainView.level);
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

    private Bitmap backgroundImage(int level)
    {
        Bitmap image = null;
        switch (level)
        {
            case 1:
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.level0), (int)(MainView.screenX - MainView.enemySizeX*3), MainView.screenY,false);
                break;
            case 2:
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.level1), (int)(MainView.screenX - MainView.enemySizeX*3), MainView.screenY,false);
                break;
            case 3:
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.level2), (int)(MainView.screenX - MainView.enemySizeX*3), MainView.screenY,false);
                break;
            case 4:
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.level3), (int)(MainView.screenX - MainView.enemySizeX*3), MainView.screenY,false);
                break;
            case 5:
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.level4), (int)(MainView.screenX - MainView.enemySizeX*3), MainView.screenY,false);
                break;
            case 6:
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.level5), (int)(MainView.screenX - MainView.enemySizeX*3), MainView.screenY,false);
                break;
            case 7:
                image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.special_level), (int)(MainView.screenX - MainView.enemySizeX*3), MainView.screenY,false);
                break;
        }
        return image;
    }
}
