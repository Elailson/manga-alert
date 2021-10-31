package br.com.algorit.manga_alert.ui.activity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import br.com.algorit.manga_alert.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        animate();
    }

    private void animate() {
        RelativeLayout relativeLayout = findViewById(R.id.activity_about_relative);
        ImageView imageView = findViewById(R.id.activity_about_image);

        int durationMillis = 5000;

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(durationMillis);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        relativeLayout.startAnimation(rotateAnimation);

        RotateAnimation rotateImage = new RotateAnimation(360, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateImage.setDuration(durationMillis);
        rotateImage.setInterpolator(new LinearInterpolator());
        rotateImage.setRepeatMode(Animation.RESTART);
        rotateImage.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(rotateImage);
    }
}