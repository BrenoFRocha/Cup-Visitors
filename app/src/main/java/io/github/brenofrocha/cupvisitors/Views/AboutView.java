package io.github.brenofrocha.cupvisitors.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
    private Activity act;

    public AboutView(Context ctx, Activity act)
    {
        super(ctx);
        this.ctx = ctx;
        this.act = act;

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

        //Background
        background = new Background(ctx, screenX, screenY);

        //Buttons
        bSizeX = (int)((screenX*0.557f)/7f);
        bSizeY = (int)((screenY*1.0694f)/7f);
        mBPosX = (int)(screenX/2 - bSizeX/1.8);
        mBPosY = (int)(screenY/2 + bSizeY*1.6f);
        Bitmap menuBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.play_button), bSizeX, bSizeY, false);
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
                    Intent i = new Intent(ctx, MenuActivity.class);
                    ctx.startActivity(i);
                    act.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                break;
        }
        return true;
    }

    private void update()
    {
        background.update();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        background.draw(canvas, p);
        canvas.drawBitmap(aboutArt, 0, 0, p);
        menuButton.draw(canvas, p);
    }

    @Override
    public void run()
    {
        handler.postDelayed(this, 30);
        update();
        invalidate();
    }
}
