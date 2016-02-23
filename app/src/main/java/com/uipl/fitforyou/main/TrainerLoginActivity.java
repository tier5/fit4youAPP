package com.uipl.fitforyou.main;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.uipl.fitforyou.R;
import com.uipl.fitforyou.base.GlobalVariable;
import com.uipl.fitforyou.utility.AsyncTaskCall;
import com.uipl.fitforyou.utility.CallbacksFromAsynctask;
import com.uipl.fitforyou.utility.ConnectionActivity;
import com.uipl.fitforyou.utility.Constants;
import com.uipl.fitforyou.utility.DialogView;

public class TrainerLoginActivity extends Activity {

	EditText et_pin;
	Button btn_trainer_login;
	DialogView d;

	private AsyncTaskCall callWebService;

	View v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.trainer_login);
		getActionBar().hide();

		initViews();

		getWindow().setBackgroundDrawableResource(R.drawable.splash_blur);

		ListenerForAsynctask();

		btn_trainer_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ConnectionActivity
						.isNetConnected(TrainerLoginActivity.this)) {

					doLogin();

				} else {
					d.showSingleButtonDialog(TrainerLoginActivity.this,
							"Please enable your internet connection");
				}
			}
		});
	}

	protected void doLogin() {
		// TODO Auto-generated method stub
		ArrayList<String> key = new ArrayList<String>();

		key.add("pin");

		ArrayList<String> value = new ArrayList<String>();

		value.add(et_pin.getText().toString().trim());

		d.showCustomSpinProgress(TrainerLoginActivity.this);

		String url = Constants.WEBSERVICE_URL+"/webservice/trainerlogin";

		callWebService = new AsyncTaskCall(
				TrainerLoginActivity.this,
				TrainerLoginActivity.this,
				2,
				url,
				key, value);
		callWebService.execute();

		DialogView.customSpinProgress
				.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						callWebService.cancel(true);
					}
				});
	}

	private void ListenerForAsynctask() {
		// TODO Auto-generated method stub
		AsyncTaskCall.setCallback(new CallbacksFromAsynctask() {
			@Override
			public void onCallbackRecieved(int requestCode, int statuscode,
					String result) {
				// TODO Auto-generated method stub
				d.dismissCustomSpinProgress();
				System.out.println("s" + statuscode + "r" + requestCode);
				if (statuscode == 1) {
					if (requestCode == 2) {
						try {
							JSONObject obj = new JSONObject(result);
							System.out.println("success"
									+ obj.optString("success"));
							if (obj.optString("success").equals("1")) {
								System.out.println("service login success...."
										+ result);

								GlobalVariable.TrainerSessionId = obj
										.optString("id");
								GlobalVariable.ytime = obj.optString("ytime");

								Log.d("ABHI", GlobalVariable.ytime);

								GlobalVariable.TrainerMissedButtonStatus = obj
										.optString("status");

								GlobalVariable.trainerStartTime = obj
										.optString("start_time");
								GlobalVariable.trainerEndTime = obj
										.optString("end_time");
								GlobalVariable.trainerPageClientId = obj
										.optString("client_id");

								JSONObject jo = obj.getJSONObject("client");
								GlobalVariable.trainerPageClientName = jo
										.optString("firstName")
										+ " "
										+ jo.optString("lastName");

								GlobalVariable.trainerPageClientPhoto = jo
										.optString("photo");

								JSONObject jo1 = obj.getJSONObject("trainer");
								GlobalVariable.trainerPageTrainerName = jo1
										.optString("firstName")
										+ " "
										+ jo1.optString("lastName");
								GlobalVariable.trainerPageTrainerPhoto = jo1
										.optString("photo");

								Intent i = new Intent(
										TrainerLoginActivity.this,
										TrainerHomeActivity.class);
								startActivity(i);
								finish();
								overridePendingTransition(
										R.anim.open_translate,
										R.anim.close_translate);

							} else {
								// if failed
								// d.dialogShow(v, TrainerLoginActivity.this,
								// "Sorry", "Invalid Pin");

								d.showSingleButtonDialog(
										TrainerLoginActivity.this,
										"Invalid Pin");

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// d.dialogShow(v,
									// TrainerLoginActivity.this,
									// "Sorry", "Internal Error");

									d.showSingleButtonDialog(
											TrainerLoginActivity.this,
											"Internal Error");
								}
							});
						}
					}
				}
			}
		});
	}

	private void initViews() {
		// TODO Auto-generated method stub

		Typeface teko = Typeface.createFromAsset(getAssets(), "Teko_Light.ttf");

		et_pin = (EditText) findViewById(R.id.et_pin);
		et_pin.setTypeface(teko);
		btn_trainer_login = (Button) findViewById(R.id.btn_trainer_login);
		btn_trainer_login.setTypeface(teko);

		d = new DialogView();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.open_scale, R.anim.close_translate);
	}

}
