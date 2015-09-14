package com.unfc.choicecustomercare.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.unfc.choicecustomercare.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hai Nguyen - 8/25/15.
 */
public class Utilities {


	/////////////////////////////////////////////////////////////////////////
	/// for images
	public static ImageLoaderConfiguration config;
	public static DisplayImageOptions options;

	public static void initializeImageLoaders(Context mContext){
		config = new ImageLoaderConfiguration.Builder(mContext)
				.threadPoolSize(1)
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
		options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true)  // default
				.delayBeforeLoading(0)
				.cacheInMemory(true) // default
				.cacheOnDisk(true) // default
				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.showImageForEmptyUri(R.drawable.default_user_image)
                .showImageOnLoading(R.drawable.default_user_image)
                .showImageOnFail(R.drawable.default_user_image)
				.build();
	}

	// check for internet connection
	public static boolean isConnectingToInternet(Context mContext){
		ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}
	///////////////////////////////////////////////////////////////////////////////////////////



	public static void toast(Context context, String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void toast(Context context, String message, int duration){
		Toast.makeText(context, message, duration).show();
	}

	public static void execute(Context context, AsyncTask<Void, Void,Void> asyncTask){
		if(isConnectingToInternet(context)){
			asyncTask.execute();
		} else {
			Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
		}
	}


	public static boolean hasData(String str){

		if(str!=null){
			if(!str.equals("")&&!str.equals("null"))
				return true;
			else
				return false;
		} else
			return false;
	}

	public static boolean hasData(EditText editText){

		if(editText!=null){
			if(!editText.getText().toString().equals("")&&!editText.getText().toString().equals("null"))
				return true;
			else
				return false;
		} else
			return false;
	}

	public static boolean hasData(HashMap hashMap){

		if(hashMap==null)
			return false;
		else {
			if(hashMap.isEmpty())
				return false;
			else
				return true;
		}
	}

	public static boolean hasData(ArrayList list){

		if(list!=null){
			if(list.size()>0)
				return true;
			else
				return false;
		} else
			return false;
	}


	public static String extractCharacters(String str, int numberOfChars){

		if(str.length()>0) {
			if(str.length()>numberOfChars)
				str = str.substring(0, numberOfChars);
		} else
			str = "";
		return str;
	}

	public static boolean cloneObjects(ArrayList sourceList, ArrayList destinationList){

		if(hasData(sourceList)){

			for(int i=0;i<sourceList.size();i++){
				destinationList.add(sourceList.get(i));
			}
			return  true;
		} else {
			return  false;
		}
	}

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}




	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static void displayImage(Context context, String imageURL, ImageView imageView){
		if(!ImageLoader.getInstance().isInited())
			initializeImageLoaders(context);

		if(hasData(imageURL)){
			ImageLoader.getInstance().displayImage(imageURL, imageView , options, null);
		}
	}

	// /**
	// * Save user information
	// */
	// public void saveUser(UserDetailsModel user) {
	//
	// CustomPreferences.setPreferences(Constants.PREF_R_ID, "");
	// CustomPreferences.setPreferences(Constants.PREF_USER_MI, "");
	// CustomPreferences.setPreferences(Constants.PREF_USER_EMAIL, "");
	// CustomPreferences.setPreferences(Constants.PREF_RESP_TYPE_ID, "");
	// if (user != null) {
	// if (user.getResponder_email() != null) {
	//
	// CustomPreferences.setPreferences(Constants.PREF_USER_EMAIL,
	// user.getResponder_email());
	// }
	//
	// if (user.getR_id() != null) {
	//
	// CustomPreferences.setPreferences(Constants.PREF_R_ID, user.getR_id());
	// }
	//
	// CustomPreferences.setPreferences(Constants.PREF_RESP_TYPE_ID,
	// user.getResp_type_id());
	//
	// if (user.getMi() != null) {
	//
	// CustomPreferences.setPreferences(Constants.PREF_USER_MI, user.getMi());
	// }
	// }
	// }
	//
	// /**
	// * Get saved user information
	// */
	// public UserDetailsModel getUser() {
	//
	// UserDetailsModel user = new UserDetailsModel();
	// user.setR_id(CustomPreferences.getPreferences(Constants.PREF_R_ID, ""));
	// user.setMi(CustomPreferences.getPreferences(Constants.PREF_USER_MI, ""));
	// user.setResp_type_id(CustomPreferences.getPreferences(Constants.PREF_RESP_TYPE_ID,
	// 0));
	// user.setResponder_email(CustomPreferences.getPreferences(Constants.PREF_USER_EMAIL,
	// ""));
	//
	// return user;
	// }

	/**
	 * Dismiss dialog
	 */
	public static void dismissDialog(Dialog dialog) {

		try {

			dialog.dismiss();
		} catch (Exception e) {

			LogUtil.e("Dismiss dialog");
		}
	}

	/**
	 * Show alert dialog
	 *
	 * @param title
	 *            Dialog title
	 * @param message
	 *            Dialog message
	 * @param positiveText
	 *            Positive button text
	 * @param negativeText
	 *            Negative button text
	 * @param positiveButtonClick
	 *            Positive button click listener
	 * @param negativeButtonClick
	 *            Negative button click listener
	 * @param isCancelAble
	 *            True if can cancel
	 */
	public static void showAlertDialog(Context context, String title, String message, String positiveText,
			String negativeText, DialogInterface.OnClickListener positiveButtonClick,
			DialogInterface.OnClickListener negativeButtonClick, boolean isCancelAble) {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setCancelable(isCancelAble);
		dialogBuilder.setMessage(message);
		if (!title.equals("")) {

			dialogBuilder.setTitle(title);
		}

		// Positive button
		if (!positiveText.equals("")) {

			if (positiveButtonClick != null) {

				dialogBuilder.setPositiveButton(positiveText, positiveButtonClick);
			} else {

				dialogBuilder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
			}
		}

		// Negative button
		if (!negativeText.equals("")) {

			if (negativeButtonClick != null) {

				dialogBuilder.setNegativeButton(negativeText, negativeButtonClick);
			} else {

				dialogBuilder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
			}
		}

		AlertDialog dialog = dialogBuilder.create();
		dialog.show();
	}

	/**
	 * Set recycler view layout manager
	 *
	 * @param ctx
	 *            Context
	 */
	public static RecyclerView.LayoutManager setRecyclerViewLayoutManager(Context ctx, RecyclerView view) {

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(ctx);
		view.setLayoutManager(mLayoutManager);
		return mLayoutManager;
	}
}
