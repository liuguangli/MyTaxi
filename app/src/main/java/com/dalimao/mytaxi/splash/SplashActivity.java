package com.dalimao.mytaxi.splash;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.dalimao.mytaxi.R;

/**
 * Created by vigroid on 11/13/17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            final AnimatedVectorDrawable anim =
                    (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.anim);
            final ImageView logo = (ImageView) findViewById(R.id.logo);
            logo.setImageDrawable(anim);
            anim.start();
        }
    }
}
