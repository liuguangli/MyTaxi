package yx.taxi.splash;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.yangxiong.mytaxi.R;


/**
 * Created by yangxiong on 2018/4/25/025.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AnimatedVectorDrawable animDrawable = (AnimatedVectorDrawable) getResources( ).getDrawable(R.drawable.anim);
        ImageView ivLogo = (ImageView)findViewById(R.id.logo);
        ivLogo.setImageDrawable(animDrawable);
        animDrawable.start();
    }
}
