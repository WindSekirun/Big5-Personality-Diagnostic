package com.github.windsekirun.big5personalitydiagnostic;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.github.windsekirun.big5personalitydiagnostic.util.Consts;
import com.github.windsekirun.big5personalitydiagnostic.util.Material;

/**
 * Big5 Personality Diagnostic
 * class: IntroActivity
 * Created by WindSekirun on 2015. 11. 11..
 */
public class IntroActivity extends AppCompatActivity implements Consts {
    RelativeLayout intro_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        intro_root = (RelativeLayout) findViewById(R.id.intro_root);
        View vwBkg = intro_root.findViewById(R.id.intro_bkg);
        vwBkg.setBackgroundColor(Material.getMaterialCyanColor(300));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vwBkg.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(21)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    // get the center for the clipping circle
                    int cx = v.getWidth() / 2;
                    int cy = v.getHeight() / 2;

                    // get the final radius for the clipping circle
                    int finalRadius = Math.max(v.getWidth(), v.getHeight());

                    // create the animator for this view (the start radius is zero)
                    Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                    anim.setDuration(500);
                    // make the view visible and start the animation
                    v.setVisibility(View.VISIBLE);
                    if (anim != null) {
                        anim.start();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setNavigationBarColor(Material.getMaterialCyanColor(700));
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 530);
    }

}
