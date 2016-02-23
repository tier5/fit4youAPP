package com.uipl.fitforyou.main;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.uipl.fitforyou.R;

public class SplashActivity extends Activity {

	private static int SPLASH_TIME_OUT = 5000;

	ActionBar ab;

	Animation animationFadeIn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		ab = getActionBar();
		ab.hide();

		// hello splash

		animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		findViewById(R.id.iv_logo).startAnimation(animationFadeIn);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// This method will be executed once the timer is over
				// Start your app main activity

				gotoHomeActivity();

			}
		}, SPLASH_TIME_OUT);

	}

	protected void gotoHomeActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.open_translate, R.anim.close_scale);
		finish();
	}

}
