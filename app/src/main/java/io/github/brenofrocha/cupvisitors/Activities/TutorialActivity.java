package io.github.brenofrocha.cupvisitors.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import io.github.brenofrocha.cupvisitors.Views.TutorialView;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean sound = false;
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
        setContentView(new TutorialView(this, sound));
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
}
