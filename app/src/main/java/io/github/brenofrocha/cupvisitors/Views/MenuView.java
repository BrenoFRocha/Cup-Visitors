package io.github.brenofrocha.cupvisitors.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

import io.github.brenofrocha.cupvisitors.Classes.Background;
import io.github.brenofrocha.cupvisitors.Classes.Button;
import io.github.brenofrocha.cupvisitors.R;

/**
 * Created by Breno on 12/01/2019.
 */

public class MenuView extends View implements Runnable
{
    Handler handler;
    private int bSizeX, bSizeY;
    public static int screenX, screenY;
    private Background background;
    private Button playButton, aboutButton, instructionsButton;
    private Paint p;
    private Bitmap playBImage,aboutBImage, instructionsBImage, menuArt;

    public MenuView(Context ctx)
    {
        super(ctx);
        handler = new android.os.Handler();
        handler.post(this);
        p = new Paint();
        DisplayMetrics display = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(display);
        screenX = display.widthPixels;
        screenY = display.heightPixels;
        bSizeX = (int)((screenX*0.557f)/3.5f);
        bSizeY = (int)((screenY*1.0694f)/3.5f);
        background = new Background(ctx, screenX, screenY);
        playBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.play_button), bSizeX, bSizeY, false);
        playButton = new Button((int)(screenX/2 - bSizeX/2),(int)(screenY/2), bSizeX, bSizeY, playBImage);
        aboutBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.about_button), bSizeX, bSizeY, false);
        aboutButton = new Button((int)(bSizeX),(int)(screenY/2 + bSizeY/2), bSizeX, bSizeY, aboutBImage);
        instructionsBImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.instructions_button), bSizeX, bSizeY, false);
        instructionsButton = new Button((int)(screenX - bSizeX*2),(int)(screenY/2 + bSizeY/2), bSizeX, bSizeY, instructionsBImage);
        menuArt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.background), screenX, screenY, false);
    }

    private void update()
    {
        background.update();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        background.draw(canvas, p);
        canvas.drawBitmap(menuArt, 0, 0, p);
        playButton.draw(canvas, p);
        aboutButton.draw(canvas, p);
        instructionsButton.draw(canvas, p);
    }

    @Override
    public void run()
    {
        handler.postDelayed(this, 30);
        update();
        invalidate();
    }
}
