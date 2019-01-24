package io.github.brenofrocha.cupvisitors.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import io.github.brenofrocha.cupvisitors.R;
import io.github.brenofrocha.cupvisitors.Views.MenuView;

/**
 * Created by Breno on 12/01/2019.
 */

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean sound = true;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            sound = bundle.getBoolean("sound");
        }
        //Setting landscape orientation
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //FullScreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Tela
        setContentView(new MenuView(this, sound));
    }

    @Override
    public void onBackPressed() {
    }
}
