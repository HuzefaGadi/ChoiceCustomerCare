package com.unfc.choicecustomercare.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.gcmservices.RegistrationIntentService;
import com.unfc.choicecustomercare.models.UserEntity;
import com.unfc.choicecustomercare.models.UserInfoEntity;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.CustomPreferences;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SplashActivity extends BaseActivity {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	@Override
	protected int addLayoutView() {

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {

			actionBar.hide();
		}

		return R.layout.activity_splash;
	}

	@Override
	protected void initComponents() {

		// Register gcm
		if (!checkPlayServices()) {

			return;
		}

		Intent intent = new Intent(this, RegistrationIntentService.class);
		startService(intent);
		String accountNum = CustomPreferences.getPreferences(Constants.PREF_USER_TOKEN, "");
		if (accountNum == null || accountNum.equals("")) {

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {

					startActivity(new Intent(SplashActivity.this, LoginActivity.class));
					finish();
				}
			}, 2000);

			return;
		}

		loginUser();
	}

	/**
	 * Login user
	 */
	private void loginUser() {

		String strToken = CustomPreferences.getPreferences(Constants.PREF_PUSH_TOKEN, "");
		String strEmail = CustomPreferences.getPreferences(Constants.PREF_USER_EMAIL, "");
		String strPass = CustomPreferences.getPreferences(Constants.PREF_USER_PASSWORD, "");
		new BaseApi(false).getInterface().doLogIn(strEmail, strPass, "password", strToken, new Callback<UserInfoEntity>() {
			@Override
			public void success(UserInfoEntity userEntity, Response response) {

				CustomPreferences.setPreferences(Constants.PREF_USER_TOKEN, userEntity.getAccess_token());
				CustomPreferences.setPreferences(Constants.PREF_FIRST_NAME, userEntity.getFirstName());
				CustomPreferences.setPreferences(Constants.PREF_LAST_NAME, userEntity.getLastName());
				CustomPreferences.setPreferences(Constants.PREF_ROLE_TYPE, Integer.parseInt(userEntity.getRole()));
				CustomPreferences.setPreferences(Constants.PREF_PROFILE_PHOTO, Constants.BASE_URL+userEntity.getProfilePhoto());
				CustomPreferences.setPreferences(Constants.PREF_BIO, userEntity.getBio());
				CustomPreferences.setPreferences(Constants.PREF_CLIENT_NAME, userEntity.getClientName());
				CustomPreferences.setPreferences(Constants.PREF_IS_CHARGED_NURSE, userEntity.getIsChargedNurse().equals("true")?true:false);
                CustomPreferences.setPreferences(Constants.PREF_RESPONDER_ID, userEntity.getResponderId());
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				intent.putExtra(Constants.INTENT_USER_TYPE, Integer.parseInt(userEntity.getRole()));
				startActivity(intent);
				finish();
			}

			@Override
			public void failure(RetrofitError retrofitError) {

				startActivity(new Intent(SplashActivity.this, LoginActivity.class));
				finish();
			}
		});
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

				GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {

				finish();
			}

			return false;
		}

		return true;
	}
}
