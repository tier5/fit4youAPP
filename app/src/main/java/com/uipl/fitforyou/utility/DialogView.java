package com.uipl.fitforyou.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.uipl.fitforyou.R;

public class DialogView {
	static ProgressDialog mAlertDialog;

	public static Dialog customSpinProgress;

	Dialog customNoNetwork;

	// public Effectstype effect;

	public void showSingleButtonDialog(Context context, String msg) {
		AlertDialog.Builder adb = new AlertDialog.Builder(context);
		adb.setTitle("Sorry!");
		adb.setMessage(msg);
		adb.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		AlertDialog alert = adb.create();
		alert.show();
	}

	/*
	 * public void showCustomNoNetwork(Context context) { customNoNetwork = new
	 * Dialog(context, R.style.ToptAppearDialog);
	 * customNoNetwork.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * customNoNetwork.setContentView(R.layout.dialog_network_error);
	 * customNoNetwork.setCanceledOnTouchOutside(true);
	 * customNoNetwork.setCancelable(true);
	 * customNoNetwork.getWindow().setBackgroundDrawableResource(
	 * android.R.color.transparent); WindowManager.LayoutParams wlmp =
	 * customNoNetwork.getWindow() .getAttributes(); wlmp.gravity = Gravity.TOP;
	 * wlmp.width = WindowManager.LayoutParams.MATCH_PARENT; wlmp.height =
	 * WindowManager.LayoutParams.WRAP_CONTENT; customNoNetwork.show();
	 * 
	 * }
	 */

	public void showSpinProgress(Context context, String msg) {
		mAlertDialog = new ProgressDialog(context);
		mAlertDialog.setMessage(msg);
		mAlertDialog.setCancelable(true);
		mAlertDialog.setCanceledOnTouchOutside(false);
		mAlertDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mAlertDialog.show();

	}

	public void showCustomSpinProgress(Context context) {

		customSpinProgress = new Dialog(context);
		customSpinProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customSpinProgress.setContentView(R.layout.dialog_loading);
		customSpinProgress.setCanceledOnTouchOutside(false);
		customSpinProgress.setCancelable(true);
		customSpinProgress.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		WindowManager.LayoutParams wlmp = customSpinProgress.getWindow()
				.getAttributes();
		wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
		wlmp.height = WindowManager.LayoutParams.MATCH_PARENT;
		customSpinProgress.show();
	}

	public void dismissCustomSpinProgress() {
		if (customSpinProgress.isShowing())
			customSpinProgress.dismiss();
	}

	public void showHorizontalProgress(Context context, String msg) {
		mAlertDialog = new ProgressDialog(context);
		mAlertDialog.setMessage(msg);
		mAlertDialog.setCancelable(true);
		mAlertDialog.setCanceledOnTouchOutside(false);
		mAlertDialog.setIndeterminate(false);
		mAlertDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mAlertDialog.setProgress(0);
		mAlertDialog.setMax(100);
		mAlertDialog.show();
	}

	public void dismissProgress() {
		if (mAlertDialog.isShowing())
			mAlertDialog.dismiss();
	}

	public void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	// public void showCustomToast(Context context, String msg) {
	// LayoutInflater inflater = LayoutInflater.from(context);
	// View toastRoot = inflater.inflate(R.layout.toast, null);
	// TextView tv = (TextView) toastRoot.findViewById(R.id.tv);
	// tv.setText(msg);
	// Toast toast = new Toast(context);
	// toast.setView(toastRoot);
	// toast.setGravity(Gravity.BOTTOM, 0, 50);
	// toast.setDuration(Toast.LENGTH_SHORT);
	// toast.show();
	// }

	public void showCustomSingleButtonDialog(Context context, String header,
			String body) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.setContentView(R.layout.custom_single_button_dialog);
		Button dialogButton = (Button) dialog.findViewById(R.id.ok_button);
		TextView tvTitle = (TextView) dialog.findViewById(R.id.title);
		TextView tvMessage = (TextView) dialog.findViewById(R.id.toast_text);

		tvTitle.setText(header);
		tvMessage.setText(body);

		dialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// public void dialogShow(View v, Context context, String header, String
	// body) {
	// final CustomDialogBuilder dialogBuilder = CustomDialogBuilder
	// .getInstance(context);
	//
	// dialogBuilder
	// .withTitle(header)
	// .withTitleColor("#FFFFFF")
	// .withDividerColor(
	// context.getResources()
	// .getColor(R.color.divider_fitforu))
	// .withMessage(body)
	// .withMessageColor("#FFFFFFFF")
	// .withDialogColor("#66000000")
	// .withIcon(
	// context.getResources().getDrawable(R.drawable.applogo))
	// .isCancelableOnTouchOutside(true).withDuration(200)
	// .withEffect(Effectstype.SlideBottom).withButton1Text("OK")
	// .withButton2Text("Cancel")
	// .setCustomView(R.layout.custom_view, context)
	//
	// .setButton1Click(new View.OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// dialogBuilder.dismiss();
	// }
	// }).show();
	//
	// }

}
