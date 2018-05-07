package com.example.lykia.roommate;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView image = (ImageView) findViewById(R.id.imageView3);

        Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.animation_in_goes_here);
        image.setAnimation(zoomin);

     splashThread =new Thread()
     {
         @Override
         public void run () {
             try {
                 int waited = 0;
                 while (waited < 1000) {
                     sleep(400);
                     waited += 100;

                 }
                 Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                 intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                 startActivity(intent);
                 SplashScreen.this.finish();

             } catch (InterruptedException e) {

             } finally {
                 SplashScreen.this.finish();
             }

         }
     };
     splashThread.start();
    }
}


