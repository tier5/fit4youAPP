package com.uipl.fitforyou.main;

import com.uipl.fitforyou.R;
import com.uipl.fitforyou.base.GlobalVariable;
import com.uipl.fitforyou.utility.ConnectionActivity;
import com.uipl.fitforyou.utility.DialogView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminLoginActivity extends Activity {

	EditText et_pin;
	Button btn_admin_login;
	DialogView d;
	View v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_login);
		getActionBar().hide();

		initViews();
		
		getWindow().setBackgroundDrawableResource(R.drawable.splash_blur);

		btn_admin_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ConnectionActivity.isNetConnected(AdminLoginActivity.this)) {

					doAdminLogin();

				} else {
					d.showSingleButtonDialog( AdminLoginActivity.this,
							"Please enable your internet connection");
				}
			}
		});
	}

	protected void doAdminLogin() {
		// TODO Auto-generated method stub
		if (et_pin.getText().toString().trim().equals("")) {
//			d.dialogShow(v, AdminLoginActivity.this, "Sorry",
//					"Please enter your master pin");
			
			d.showSingleButtonDialog( AdminLoginActivity.this,
					"Please enter your master pin");
			
			
		} else {

			GlobalVariable.adminMasterPin = et_pin.getText().toString().trim();
			Intent i = new Intent(AdminLoginActivity.this,
					AdminConfirmLoginActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.open_translate,
					R.anim.close_translate);

		}
	}

	private void initViews() {
		// TODO Auto-generated method stub
		
		Typeface face = Typeface.createFromAsset(getAssets(), "Teko_Light.ttf");

		et_pin = (EditText) findViewById(R.id.et_pin);
		et_pin.setTypeface(face);
		btn_admin_login = (Button) findViewById(R.id.btn_admin_login);
		btn_admin_login.setTypeface(face);

		d = new DialogView();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.open_scale, R.anim.close_translate);
	}

}
