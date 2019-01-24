package io.github.brenofrocha.cupvisitors.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.github.brenofrocha.cupvisitors.R;

public class Explosion
{
    //Explosion
    private Bitmap[] frames;
    private Timer timer;
    private int currentFrame;
    private boolean run;
    private float posX, posY;
    private final MediaPlayer explosionSound;

    public Explosion(Context ctx, float posX, float posY, int sizeX, int sizeY)
    {
        this.posX = posX;
        this.posY = posY;
        frames = new Bitmap[20];
        frames[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_0), sizeX, sizeY, false);
        frames[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_1), sizeX, sizeY, false);
        frames[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_2), sizeX, sizeY, false);
        frames[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_3), sizeX, sizeY, false);
        frames[4] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_4), sizeX, sizeY, false);
        frames[5] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_5), sizeX, sizeY, false);
        frames[6] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_6), sizeX, sizeY, false);
        frames[7] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_7), sizeX, sizeY, false);
        frames[8] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_8), sizeX, sizeY, false);
        frames[9] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_9), sizeX, sizeY, false);
        frames[10] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_10), sizeX, sizeY, false);
        frames[11] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_11), sizeX, sizeY, false);
        frames[12] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_12), sizeX, sizeY, false);
        frames[13] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_13), sizeX, sizeY, false);
        frames[14] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_14), sizeX, sizeY, false);
        frames[15] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_15), sizeX, sizeY, false);
        frames[16] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_16), sizeX, sizeY, false);
        frames[17] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_17), sizeX, sizeY, false);
        frames[18] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_18), sizeX, sizeY, false);
        frames[19] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(ctx.getResources(),R.drawable.explosion_19), sizeX, sizeY, false);
        timer = new Timer();
        explosionSound = MediaPlayer.create(ctx, R.raw.explosion_sound);
        explosionSound.setLooping(false);
    }

    private void updateFrame() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(run && currentFrame < frames.length-1)
                {
                    currentFrame += 1;
                }
                else if(currentFrame >= frames.length)
                {
                    run = false;
                }
            }
        }, 50, 50);
    }

    public void draw(Canvas canvas, Paint p)
    {
        if(run && currentFrame < frames.length-1) {
            canvas.drawBitmap(frames[currentFrame], posX, posY, p);
        }
    }

    public void startAnimation(boolean sound)
    {
        updateFrame();
        run = true;
        if(sound) {
            try {
                explosionSound.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(explosionSound.isPlaying()) {
                explosionSound.pause();
                explosionSound.seekTo(0);
            }
            explosionSound.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    explosionSound.start();
                }
            });
        }
    }
}
