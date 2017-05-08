package me.citrafa.asistenkuliahku;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.citrafa.asistenkuliahku.ActivityClass.Dashboard;
import me.citrafa.asistenkuliahku.ActivityClass.frmLogin;
import me.citrafa.asistenkuliahku.SessionManager.SessionManager;

public class SplashScreen extends AppCompatActivity {
    me.citrafa.asistenkuliahku.CustomWidget.TVLatoFontMedium txt1;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sessionManager = new SessionManager(SplashScreen.this);
        txt1 = (me.citrafa.asistenkuliahku.CustomWidget.TVLatoFontMedium)findViewById(R.id.splashlbl1);
        progressBar();

    }
    private void progressBar(){
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (!sessionManager.isLoggedIn()){
                    Intent i = new Intent(SplashScreen.this, frmLogin.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }else{
                    Intent i = new Intent(SplashScreen.this, Dashboard.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
