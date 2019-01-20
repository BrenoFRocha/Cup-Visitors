package io.github.brenofrocha.cupvisitors.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import io.github.brenofrocha.cupvisitors.Views.MainView;

/**
 * Created by Breno on 02/08/2016.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting landscape orientation
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Tela
        setContentView(new MainView(this));
    }

    @Override
    public void onBackPressed() {
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        Intent i = new Intent(this, MenuActivity.class);
        this.finish();
        startActivity(i);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Intent i = new Intent(this, MenuActivity.class);
        this.finish();
        startActivity(i);
    }
}
