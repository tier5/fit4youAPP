package com.uipl.fitforyou.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.uipl.fitforyou.R;
import com.uipl.fitforyou.utility.Preferences;

public class HomeActivity extends Activity {

	@SuppressWarnings("unused")
	private Preferences pref;

	ImageView iv_client, iv_admin, iv_trainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		getActionBar().hide();

		initViews();

		// hello world

		iv_client.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(HomeActivity.this,
						ClientLoginActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.open_translate,
						R.anim.close_scale);
			}
		});

		iv_trainer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(HomeActivity.this,
						TrainerLoginActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.open_translate,
						R.anim.close_scale);
			}
		});

		iv_admin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(HomeActivity.this,
						AdminLoginActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.open_translate,
						R.anim.close_scale);
			}
		});

	}

	private void initViews() {
		// TODO Auto-generated method stub
		pref = new Preferences(HomeActivity.this);

		iv_client = (ImageView) findViewById(R.id.iv_client);
		iv_trainer = (ImageView) findViewById(R.id.iv_trainer);
		iv_admin = (ImageView) findViewById(R.id.iv_admin);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	
}
