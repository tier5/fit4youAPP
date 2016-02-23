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

public class ClientLoginActivity extends Activity {

	EditText et_pin;
	Button btn_client_login;
	DialogView d;

	AsyncTaskCall callWebService;

	View v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.client_login);
		getActionBar().hide();

		initViews();

		getWindow().setBackgroundDrawableResource(R.drawable.splash_blur);

		ListenerForAsynctask();

		btn_client_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ConnectionActivity.isNetConnected(ClientLoginActivity.this)) {

					doLogin();

				} else {
					d.showSingleButtonDialog(ClientLoginActivity.this,
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

		d.showCustomSpinProgress(ClientLoginActivity.this);

		/*callWebService = new AsyncTaskCall(
				ClientLoginActivity.this,
				ClientLoginActivity.this,
				1,
				"http://unifiedinfotech.co.in/webroot/team1/gym/webservice/clientlogin",
				key, value);*/
		Log.d("Dib", key + " " + value);

		String url = Constants.WEBSERVICE_URL+"/webservice/clientlogin";

		callWebService = new AsyncTaskCall(
				ClientLoginActivity.this,
				ClientLoginActivity.this,
				1,
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

	private void initViews() {
		// TODO Auto-generated method stub

		Typeface face = Typeface.createFromAsset(getAssets(), "Teko-Light.ttf");

		et_pin = (EditText) findViewById(R.id.et_pin);
		et_pin.setTypeface(face);
		btn_client_login = (Button) findViewById(R.id.btn_client_login);
		btn_client_login.setTypeface(face);

		d = new DialogView();
	}

	private void ListenerForAsynctask() {
		// TODO Auto-generated method stub
		AsyncTaskCall.setCallback(new CallbacksFromAsynctask() {
			@Override
			public void onCallbackRecieved(int requestCode, int statuscode,
					String result) {
				// TODO Auto-generated method stub
				d.dismissCustomSpinProgress();
				Log.d("Dib", "Status: " + statuscode);
				Log.d("Dib", "requestCode: " + requestCode);
				if (statuscode == 1) {
					if (requestCode == 1) {
						try {


							JSONObject obj = new JSONObject(result);
							Log.d("Dib", "Success: " + obj.optString("success"));
							if (obj.optString("success").equals("1")) {
								System.out.println("service login success...."
										+ result);

								GlobalVariable.SessionId = obj.optString("id");

								GlobalVariable.ClientPresentButtonStatus = obj
										.optString("status");

								GlobalVariable.startTime = obj
										.optString("start_time");
								GlobalVariable.endTime = obj
										.optString("end_time");

								GlobalVariable.clientPageClientId = obj
										.optString("client_id");

								JSONObject jo = obj.getJSONObject("client");
								GlobalVariable.clientPageClientName = jo
										.optString("firstName")
										+ " "
										+ jo.optString("lastName");
								if (jo.optString("city").equals("")
										|| jo.optString("state").equals("")) {
									GlobalVariable.clientPageClientPlace = "";
								} else {
									GlobalVariable.clientPageClientPlace = jo
											.optString("city")
											+ ", "
											+ jo.optString("state");
								}
								GlobalVariable.clientPageClientPhoto = jo
										.optString("photo");

								GlobalVariable.clientPageClientAttendance = jo
										.optString("is_present_client");

								GlobalVariable.clientPageClientPin = jo
										.optString("userPin");

								JSONObject jo1 = obj.getJSONObject("trainer");
								GlobalVariable.clientPageTrainerName = jo1
										.optString("firstName")
										+ " "
										+ jo1.optString("lastName");
								GlobalVariable.clientPageTrainerPhoto = jo1
										.optString("photo");

								Intent i = new Intent(ClientLoginActivity.this,
										ClientAttendanceActivity.class);
								startActivity(i);
								finish();
								overridePendingTransition(
										R.anim.open_translate,
										R.anim.close_translate);

							} else {
								// if failed

								d.showSingleButtonDialog(
										ClientLoginActivity.this, "Invalid Pin");
								// d.dialogShow(v, ClientLoginActivity.this,
								// "Sorry", "Invalid Pin");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							System.out.println("excp: " + e.toString());
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									// d.dialogShow(v, ClientLoginActivity.this,
									// "Sorry", "Internal Error");

									d.showSingleButtonDialog(
											ClientLoginActivity.this,
											"Internal Error");

								}
							});
						}
					}
				} else {
					System.out.println("else");
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.open_scale, R.anim.close_translate);
	}

}
