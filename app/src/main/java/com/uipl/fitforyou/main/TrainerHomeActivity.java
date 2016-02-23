package com.uipl.fitforyou.main;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.uipl.fitforyou.R;
import com.uipl.fitforyou.base.GlobalVariable;
import com.uipl.fitforyou.utility.AsyncTaskCall;
import com.uipl.fitforyou.utility.CallbacksFromAsynctask;
import com.uipl.fitforyou.utility.ConnectionActivity;
import com.uipl.fitforyou.utility.Constants;
import com.uipl.fitforyou.utility.DialogView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TrainerHomeActivity extends Activity {

	ImageView iv_client, iv_trainer;

	TextView tv_clientname, tv_clientplace, tv_client_session_date,
			tv_client_session_time, tv_trainername, tv_trainerplace;

	Button btn_missed;

	DialogView d;

	Date stDate, CurrentDate;
	String startDate = "";

	private AsyncTaskCall callWebService;

	View v;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.trainer_home);
		getActionBar().hide();

		initViews();

		ListenerForAsynctask();

		btn_missed.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (ConnectionActivity.isNetConnected(TrainerHomeActivity.this)) {

					makeClientMissed();

				} else {
					d.showSingleButtonDialog(TrainerHomeActivity.this,
							"Please enable your internet connection");
				}
			}
		});
	}

	protected void makeClientMissed() {
		// TODO Auto-generated method stub
		ArrayList<String> key = new ArrayList<String>();

		key.add("id");

		ArrayList<String> value = new ArrayList<String>();

		value.add(GlobalVariable.TrainerSessionId);

		System.out.println("se" + GlobalVariable.TrainerSessionId);

		d.showCustomSpinProgress(TrainerHomeActivity.this);

		String url = Constants.WEBSERVICE_URL+"/webservice/missed";

		callWebService = new AsyncTaskCall(
				TrainerHomeActivity.this,
				TrainerHomeActivity.this,
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (callWebService != null)
			callWebService.cancel(true);
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
					if (requestCode == 1) {
						try {
							JSONObject obj = new JSONObject(result);
							System.out.println("res" + result);
							if (obj.optString("is_present").equals("2")) {
								System.out.println("service login success....");

								Toast.makeText(TrainerHomeActivity.this,
										"Success", 500).show();
								Intent i = new Intent(TrainerHomeActivity.this,
										HomeActivity.class);
								startActivity(i);
								finish();
								overridePendingTransition(
										R.anim.open_translate,
										R.anim.close_translate);

							} else {
								// if failed
								Toast.makeText(
										TrainerHomeActivity.this,
										"Something went wrong, please try again later!",
										500).show();

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// d.showSingleButtonDialog(
									// TrainerHomeActivity.this,
									// "Internal Error");

									d.showSingleButtonDialog(
											TrainerHomeActivity.this,
											"Internal Error");

								}
							});
						}
					}
				}
			}
		});
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}
	}

	@SuppressLint("SimpleDateFormat")
	private void initViews() {
		// TODO Auto-generated method stub

		Typeface teko = Typeface.createFromAsset(getAssets(), "Teko_Light.ttf");
		Typeface lato = Typeface.createFromAsset(getAssets(),
				"Lato-Regular.ttf");
		Typeface teko_bold = Typeface.createFromAsset(getAssets(),
				"Teko-Bold.ttf");

		iv_client = (ImageView) findViewById(R.id.iv_client);
		iv_trainer = (ImageView) findViewById(R.id.iv_trainer);

		tv_clientname = (TextView) findViewById(R.id.tv_clientname);
		tv_clientname.setTypeface(teko_bold);
		tv_clientplace = (TextView) findViewById(R.id.tv_clientplace);
		tv_clientplace.setTypeface(lato);
		tv_client_session_date = (TextView) findViewById(R.id.tv_client_session_date);
		tv_client_session_date.setTypeface(lato);
		tv_client_session_time = (TextView) findViewById(R.id.tv_client_session_time);
		tv_client_session_time.setTypeface(lato);
		tv_trainername = (TextView) findViewById(R.id.tv_trainername);
		tv_trainername.setTypeface(teko_bold);
		tv_trainerplace = (TextView) findViewById(R.id.tv_trainerplace);
		tv_trainerplace.setTypeface(lato);
		btn_missed = (Button) findViewById(R.id.btn_missed);
		btn_missed.setTypeface(teko);
		d = new DialogView();

		tv_clientname.setText(GlobalVariable.trainerPageClientName
				.toUpperCase());
		tv_client_session_date.setText(new SimpleDateFormat("MMM dd, yyyy")
				.format(new Date()));
		tv_client_session_time.setText("demo");
		tv_trainername.setText(GlobalVariable.trainerPageTrainerName
				.toUpperCase());

		System.out.println("photo c" + GlobalVariable.trainerPageClientPhoto);
		System.out.println("photo t" + GlobalVariable.trainerPageTrainerPhoto);

		if (GlobalVariable.trainerPageClientPhoto.length() > 0) {
			System.out.println("1");
			iv_trainer.setBackgroundResource(R.drawable.test);
			new DownloadImageTask(iv_client)
					.execute(GlobalVariable.trainerPageClientPhoto);

		}

		if (GlobalVariable.trainerPageTrainerPhoto.length() > 0) {
			System.out.println("2");
			new DownloadImageTask(iv_trainer)
					.execute(GlobalVariable.trainerPageTrainerPhoto);
			iv_client.setBackgroundResource(R.drawable.test);

		}
		if (GlobalVariable.trainerPageClientPhoto.length() > 0
				&& GlobalVariable.trainerPageTrainerPhoto.length() > 0) {
			System.out.println("3");
			new DownloadImageTask(iv_client)
					.execute(GlobalVariable.trainerPageClientPhoto);

			new DownloadImageTask(iv_trainer)
					.execute(GlobalVariable.trainerPageTrainerPhoto);

		}
		if (GlobalVariable.trainerPageClientPhoto.length() == 0
				&& GlobalVariable.trainerPageTrainerPhoto.length() == 0) {
			System.out.println("4");
			iv_trainer.setBackgroundResource(R.drawable.test);
			iv_client.setBackgroundResource(R.drawable.test);
		}

		// managing start and end time
		if (GlobalVariable.trainerStartTime.equals("")
				&& GlobalVariable.trainerEndTime.equals("")) {
			tv_client_session_time.setText("No active session");
			tv_client_session_date.setText("No active session");

			btn_missed.setAlpha(0.3f);
			btn_missed.setText("NO SESSION");
			btn_missed.setEnabled(false);
		} else {

			String startTime = "", endTime = "";
			long total_time = 0;
			String st_time_text = GlobalVariable.trainerStartTime.substring(10,
					16);
			System.out.println("st time text: " + st_time_text);

			String st_date = GlobalVariable.trainerStartTime.substring(0, 10);
			System.out.println("st_date : " + st_date);

			SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm");
			SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
			SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dateFormat4 = new SimpleDateFormat("MMM dd,yyyy");
			SimpleDateFormat dateFormat5 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				Date date = dateFormat.parse(st_time_text);
				Date fulldate = dateFormat5
						.parse(GlobalVariable.trainerStartTime.substring(0, 19));
				System.out.println("full date: "
						+ GlobalVariable.trainerStartTime.substring(0, 19));

				long start_date_obj = fulldate.getTime();
				System.out.println("AA:" + start_date_obj);
				startTime = dateFormat2.format(date);
				Log.e("Time", startTime);

				System.out.println("BB:" + GlobalVariable.ytime);

				stDate = dateFormat3.parse(st_date);
				startDate = dateFormat4.format(stDate);

				// total_time = start_date_obj
				// + (Integer.parseInt(GlobalVariable.ytime) * 60 * 1000);
				total_time = start_date_obj;
				System.out.println("CC:" + total_time);
				System.out.println("DD:" + System.currentTimeMillis());

			} catch (ParseException e) {
			}

			tv_client_session_date.setText(startDate);

			String end_time_text = GlobalVariable.trainerEndTime.substring(10,
					16);
			System.out.println("end time text: " + end_time_text);

			SimpleDateFormat dtFormat = new SimpleDateFormat("kk:mm");
			SimpleDateFormat dtFormat2 = new SimpleDateFormat("hh:mm aa");
			try {
				Date date = dtFormat.parse(end_time_text);

				endTime = dtFormat2.format(date);
				Log.e("Time end", endTime);
			} catch (ParseException e) {
			}

			// set the session time
			tv_client_session_time.setText(startTime + "-" + endTime);
			System.out.println("status is:"
					+ GlobalVariable.TrainerMissedButtonStatus);

			if (GlobalVariable.TrainerMissedButtonStatus.equals("0")) {

				System.out.println("STATUS");

				btn_missed.setAlpha(0.3f);
				btn_missed.setText("NO SESSION");
				btn_missed.setEnabled(false);

			} else if (GlobalVariable.TrainerMissedButtonStatus.equals("1")) {

				// check here
				// if current time is > than start time + y mins
				Log.d("Status", GlobalVariable.TrainerMissedButtonStatus);
				System.out
						.println("current time:" + System.currentTimeMillis());
				if (System.currentTimeMillis() > total_time) {
					System.out.println("greater");
					btn_missed.setText("MISSED SESSION");
					btn_missed.setEnabled(true);
				} else {
					System.out.println("smaller");
					btn_missed.setAlpha(0.3f);
					btn_missed.setText("MISSED SESSION");
					btn_missed.setEnabled(false);
				}

			} else if (GlobalVariable.TrainerMissedButtonStatus.equals("2")) {

				btn_missed.setAlpha(0.3f);
				btn_missed.setText("SESSION CONFIRMED");
				btn_missed.setEnabled(false);

			} else if (GlobalVariable.TrainerMissedButtonStatus.equals("3")) {

				btn_missed.setAlpha(0.3f);
				btn_missed.setText("MARKED ABSENT");
				btn_missed.setEnabled(false);
			} else if (GlobalVariable.TrainerMissedButtonStatus.equals("")) {

				btn_missed.setAlpha(0.3f);
				btn_missed.setText("NO SESSION");
				btn_missed.setEnabled(false);
			}

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent i = new Intent(TrainerHomeActivity.this,
				TrainerLoginActivity.class);
		startActivity(i);
		finish();
		overridePendingTransition(R.anim.open_scale, R.anim.close_translate);
	}
}
