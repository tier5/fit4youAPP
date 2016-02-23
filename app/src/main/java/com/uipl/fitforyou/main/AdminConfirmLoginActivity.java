package com.uipl.fitforyou.main;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.uipl.fitforyou.R;
import com.uipl.fitforyou.base.GlobalVariable;
import com.uipl.fitforyou.utility.AsyncTaskCall;
import com.uipl.fitforyou.utility.CallbacksFromAsynctask;
import com.uipl.fitforyou.utility.ConnectionActivity;
import com.uipl.fitforyou.utility.Constants;
import com.uipl.fitforyou.utility.DialogView;
import com.uipl.fitforyou.utility.GymSetGet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdminConfirmLoginActivity extends Activity {

	EditText et_pin;
	Button btn_admin_confirm_login;
	DialogView d;
	View v;

	AsyncTaskCall callWebService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_confirm_login);
		getActionBar().hide();

		initViews();

		getWindow().setBackgroundDrawableResource(R.drawable.splash_blur);

		ListenerForAsynctask();

		btn_admin_confirm_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (ConnectionActivity
						.isNetConnected(AdminConfirmLoginActivity.this)) {
					GlobalVariable.GYM_ARRAYLIST.clear();
					doAdminConfirmLogin();

				} else {
					// d.dialogShow(v, AdminConfirmLoginActivity.this, "Sorry",
					// "Please enable your internet connection");

					d.showSingleButtonDialog(AdminConfirmLoginActivity.this,
							"Please enable your internet connection");

				}
			}
		});

	}

	protected void doAdminConfirmLogin() {
		// TODO Auto-generated method stub
		if (et_pin.getText().toString().trim().equals("")) {
			// d.dialogShow(v, AdminConfirmLoginActivity.this, "Sorry",
			// "Please confirm your master pin");

			d.showSingleButtonDialog(AdminConfirmLoginActivity.this,
					"Please confirm your master pin");

		} else if (!et_pin.getText().toString().trim()
				.equals(GlobalVariable.adminMasterPin)) {
			// check both pins are equal or not

			// d.dialogShow(v, AdminConfirmLoginActivity.this, "Sorry",
			// "Master pin did not match");

			d.showSingleButtonDialog(AdminConfirmLoginActivity.this,
					"Master pin did not match");

		} else {
			// when pins are equal
			System.out.println("success");

			ArrayList<String> key = new ArrayList<String>();

			key.add("pin");

			ArrayList<String> value = new ArrayList<String>();

			value.add(et_pin.getText().toString().trim());

			d.showCustomSpinProgress(AdminConfirmLoginActivity.this);

			String url = Constants.WEBSERVICE_URL+"/webservice/adminlogin";


			callWebService = new AsyncTaskCall(
					AdminConfirmLoginActivity.this,
					AdminConfirmLoginActivity.this,
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
	}

	private void initViews() {
		// TODO Auto-generated method stub

		Typeface face = Typeface.createFromAsset(getAssets(), "Teko_Light.ttf");
		et_pin = (EditText) findViewById(R.id.et_pin);
		et_pin.setTypeface(face);
		btn_admin_confirm_login = (Button) findViewById(R.id.btn_admin_confirm_login);
		btn_admin_confirm_login.setTypeface(face);

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
				if (statuscode == 1) {
					if (requestCode == 1) {
						try {
							JSONObject obj = new JSONObject(result);
							if (obj.optString("status").equals("1")) {
								System.out.println("service login success....");

								JSONObject ob = obj.getJSONObject("admin_data");
								GlobalVariable.AdminName = ob
										.optString("firstName")
										+ " "
										+ ob.optString("lastName");
								GlobalVariable.AdminPhoto = ob
										.optString("photo");

								JSONArray obje = obj.optJSONArray("gym_data");
								for (int i = 0; i < obje.length(); i++) {
									JSONObject gobj = obje.optJSONObject(i);
									GymSetGet gs = new GymSetGet(gobj
											.optString("id"), gobj
											.optString("gymName"), gobj
											.optString("gymAddress"), gobj
											.optString("current"), gobj
											.optString("gymCity"), gobj
											.optString("gymState"), gobj
											.optString("gymZip"));

									GlobalVariable.GYM_ARRAYLIST.add(gs);
								}

								for (int i = 0; i < GlobalVariable.GYM_ARRAYLIST
										.size(); i++) {
									System.out.println("Member name: "
											+ GlobalVariable.GYM_ARRAYLIST
													.get(i).gymname);
									if (GlobalVariable.GYM_ARRAYLIST.get(i).current
											.equals("1")) {
										System.out.println("current gym"
												+ GlobalVariable.GYM_ARRAYLIST
														.get(i).current);
										GlobalVariable.AdminGymSetID = GlobalVariable.GYM_ARRAYLIST
												.get(i).current;
									}
								}

								Intent i = new Intent(
										AdminConfirmLoginActivity.this,
										AdminHomeActivity.class);
								startActivity(i);
								finish();
								overridePendingTransition(
										R.anim.open_translate,
										R.anim.close_translate);

							} else {
								// if failed
								// d.dialogShow(v,
								// AdminConfirmLoginActivity.this,
								// "Sorry", "Invalid Pin");

								d.showSingleButtonDialog(
										AdminConfirmLoginActivity.this,
										"Invalid Pin");

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							System.out.println("excp: " + e.toString());
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									// d.dialogShow(v,
									// AdminConfirmLoginActivity.this,
									// "Sorry", "Internal Error");

									d.showSingleButtonDialog(
											AdminConfirmLoginActivity.this,
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
