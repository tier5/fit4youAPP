package com.uipl.fitforyou.main;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uipl.fitforyou.R;
import com.uipl.fitforyou.base.GlobalVariable;
import com.uipl.fitforyou.utility.AsyncTaskCall;
import com.uipl.fitforyou.utility.CallbacksFromAsynctask;
import com.uipl.fitforyou.utility.ConnectionActivity;
import com.uipl.fitforyou.utility.Constants;
import com.uipl.fitforyou.utility.DialogView;

public class AdminHomeActivity extends Activity {

	DialogView d;

	AsyncTaskCall callWebService;

	ImageView iv_admin;
	TextView tv_adminname, tv_adminpost, tv_place, tv_admin_street,
			tv_admin_country_pin, tv_admin_city, tv_admin_state, tv_admin_zip;
	Button btn_admin_set;

	ArrayList<String> placeList;

	ListView lv_items;

	Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin_home);

		getActionBar().hide();

		initViews();

		ListenerForAsynctask();

		findViewById(R.id.rl_adminplace_bg).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						openDialog();
					}
				});

		btn_admin_set.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ConnectionActivity.isNetConnected(AdminHomeActivity.this)) {

					setCurrentGym();
					//placeList.clear();

				} else {
					d.showSingleButtonDialog(AdminHomeActivity.this,
							"Please enable your internet connection");
				}
			}
		});

	}

	protected void setCurrentGym() {
		// TODO Auto-generated method stub
		ArrayList<String> key = new ArrayList<String>();

		key.add("id");

		ArrayList<String> value = new ArrayList<String>();

		value.add(GlobalVariable.AdminGymSetID);

		d.showCustomSpinProgress(AdminHomeActivity.this);

		String url = Constants.WEBSERVICE_URL+"/webservice/current-gym";

		callWebService = new AsyncTaskCall(
				AdminHomeActivity.this,
				AdminHomeActivity.this,
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
							System.out.println("res" + result);
							if (obj.optString("status").equals("1")) {
								System.out.println("service login success....");

								Toast.makeText(AdminHomeActivity.this,
										"Success", 500).show();
								Intent i = new Intent(AdminHomeActivity.this,
										HomeActivity.class);
								startActivity(i);
								finish();
								overridePendingTransition(
										R.anim.open_translate,
										R.anim.close_translate);

							} else {
								// if failed
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// d.showSingleButtonDialog(
									// AdminHomeActivity.this,
									// "Internal Error");

									d.showSingleButtonDialog(
											AdminHomeActivity.this,
											"Internal Error");

								}
							});
						}
					}
				}
			}
		});
	}

	protected void openDialog() {
		// TODO Auto-generated method stub
		// custom dialog
		dialog = new Dialog(AdminHomeActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_admin_dialog);
		dialog.getWindow().setBackgroundDrawableResource(R.color.black);
		// dialog.setTitle("Title...");

		// set the custom dialog components - text, image and button
		// TextView text = (TextView) dialog.findViewById(R.id.text);
		lv_items = (ListView) dialog.findViewById(R.id.lv_items);

		placeList = new ArrayList<String>();

		for (int i = 0; i < GlobalVariable.GYM_ARRAYLIST.size(); i++) {
			placeList.add(GlobalVariable.GYM_ARRAYLIST.get(i).gymname);
		}

		CustomAdapter adap = new CustomAdapter(AdminHomeActivity.this,
				placeList);
		lv_items.setAdapter(adap);

		dialog.show();

		lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
				tv_place.setText(tv_item.getText().toString());

				// set data

				for (int i = 0; i < GlobalVariable.GYM_ARRAYLIST.size(); i++) {
					if (tv_item
							.getText()
							.toString()
							.equals(GlobalVariable.GYM_ARRAYLIST.get(i).gymname)) {

						System.out.println("set: "
								+ tv_item.getText().toString());

						tv_admin_street.setText(GlobalVariable.GYM_ARRAYLIST
								.get(i).gymaddress);
						tv_admin_country_pin.setText("");
						tv_admin_city.setText("City: "
								+ GlobalVariable.GYM_ARRAYLIST.get(i).city);
						tv_admin_state.setText("State: "
								+ GlobalVariable.GYM_ARRAYLIST.get(i).state);
						tv_admin_zip.setText("Zip: "
								+ GlobalVariable.GYM_ARRAYLIST.get(i).zip);

						GlobalVariable.AdminGymSetID = GlobalVariable.GYM_ARRAYLIST
								.get(i).id;
					}
				}

				dialog.dismiss();
			}
		});

	}

	private void initViews() {
		// TODO Auto-generated method stub

		Typeface teko = Typeface.createFromAsset(getAssets(), "Teko_Light.ttf");
		Typeface lato = Typeface.createFromAsset(getAssets(),
				"Lato-Regular.ttf");
		Typeface teko_bold = Typeface.createFromAsset(getAssets(),
				"Teko-Bold.ttf");

		d = new DialogView();

		iv_admin = (ImageView) findViewById(R.id.iv_admin);
		tv_adminname = (TextView) findViewById(R.id.tv_adminname);
		tv_adminname.setTypeface(teko_bold);
		tv_adminpost = (TextView) findViewById(R.id.tv_adminpost);
		tv_adminpost.setTypeface(lato);
		tv_place = (TextView) findViewById(R.id.tv_place);
		tv_place.setTypeface(lato);
		tv_admin_street = (TextView) findViewById(R.id.tv_admin_street);
		tv_admin_street.setTypeface(lato);
		tv_admin_country_pin = (TextView) findViewById(R.id.tv_admin_country_pin);
		tv_admin_country_pin.setTypeface(lato);
		tv_admin_city = (TextView) findViewById(R.id.tv_admin_city);
		tv_admin_city.setTypeface(lato);
		tv_admin_state = (TextView) findViewById(R.id.tv_admin_state);
		tv_admin_state.setTypeface(lato);
		tv_admin_zip = (TextView) findViewById(R.id.tv_admin_zip);
		tv_admin_zip.setTypeface(lato);

		btn_admin_set = (Button) findViewById(R.id.btn_admin_set);
		btn_admin_set.setTypeface(teko);

		// data set
		tv_adminname.setText(GlobalVariable.AdminName.toUpperCase());

		if (GlobalVariable.AdminPhoto.length() > 0) {
			System.out.println("1");
			new DownloadImageTask(iv_admin).execute(GlobalVariable.AdminPhoto);

		} else {
			iv_admin.setBackgroundResource(R.drawable.test);
		}

		for (int i = 0; i < GlobalVariable.GYM_ARRAYLIST.size(); i++) {
			// System.out.println(GlobalVariable.GYM_ARRAYLIST.get(i).gymname);
			if (GlobalVariable.GYM_ARRAYLIST.get(i).current.equals("1")) {
				// current gym
				tv_place.setText(GlobalVariable.GYM_ARRAYLIST.get(i).gymname);
				tv_admin_street
						.setText(GlobalVariable.GYM_ARRAYLIST.get(i).gymaddress);
				tv_admin_country_pin.setText("");
				tv_admin_city.setText("City: "
						+ GlobalVariable.GYM_ARRAYLIST.get(i).city);
				tv_admin_state.setText("State: "
						+ GlobalVariable.GYM_ARRAYLIST.get(i).state);
				tv_admin_zip.setText("Zip: "
						+ GlobalVariable.GYM_ARRAYLIST.get(i).zip);

				GlobalVariable.AdminGymSetID = GlobalVariable.GYM_ARRAYLIST
						.get(i).id;
			}
		}

	}

	private class CustomAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;
		Typeface lato;
		ArrayList<String> data;

		CustomAdapter(Context c, ArrayList<String> d) {
			context = c;
			lato = Typeface.createFromAsset(context.getAssets(),
					"Lato-Regular.ttf");

			data = d;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (inflater == null)
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.listview_items, null);
				holder = createViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_item.setTypeface(lato);
			holder.tv_item.setText(data.get(position));

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tv_item;

	}

	private ViewHolder createViewHolder(View v) {
		ViewHolder holder = new ViewHolder();

		holder.tv_item = (TextView) v.findViewById(R.id.tv_item);

		return holder;
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.open_scale, R.anim.close_translate);
		GlobalVariable.GYM_ARRAYLIST.clear();
	}

}
