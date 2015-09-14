package com.unfc.choicecustomercare.activity;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.UserEntity;
import com.unfc.choicecustomercare.models.UserInfoEntity;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.CustomPreferences;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity {

	@Override
	protected int addLayoutView() {

		return R.layout.activity_login;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		CustomPreferences.setPreferences(Constants.PREF_USER_TOKEN, "");
		TextView btnLogin = (TextView) findViewById(R.id.sign_in_btn);
		TextView txtSignUp = (TextView) findViewById(R.id.sign_in_click_here);

		btnLogin.setOnClickListener(this);
		txtSignUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {

			case R.id.sign_in_btn :

				loginUser();
				break;

			case R.id.sign_in_click_here :

				startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
				break;
		}
	}

	private void loginUser() {

		String strToken = CustomPreferences.getPreferences(Constants.PREF_PUSH_TOKEN, "");
		final String strPass = ((EditText) findViewById(R.id.sign_in_pin_edt)).getText().toString().trim();
		final String strEmail = ((EditText) findViewById(R.id.sign_in_email_edt)).getText().toString().trim();
		if (strPass.equals("") || strEmail.equals("")) {

			Utilities.showAlertDialog(this, getString(R.string.app_name), getString(R.string.invalid_information),
					getString(R.string.ok), "", null, null, false);
			return;
		}

		final Dialog dialog = LoadingDialog.show(this);
		new BaseApi(false).getInterface().doLogIn(strEmail, strPass, "password", strToken, new Callback<UserInfoEntity>() {
			@Override
			public void success(UserInfoEntity userEntity, Response response) {

				Utilities.dismissDialog(dialog);


				if(userEntity.getError()==null) {
					CustomPreferences.setPreferences(Constants.PREF_USER_EMAIL, strEmail);
					CustomPreferences.setPreferences(Constants.PREF_USER_PASSWORD, strPass);
					CustomPreferences.setPreferences(Constants.PREF_USER_TOKEN, userEntity.getAccess_token());
					CustomPreferences.setPreferences(Constants.PREF_FIRST_NAME, userEntity.getFirstName());
					CustomPreferences.setPreferences(Constants.PREF_LAST_NAME, userEntity.getLastName());
					CustomPreferences.setPreferences(Constants.PREF_ROLE_TYPE, Integer.parseInt(userEntity.getRole()));
					CustomPreferences.setPreferences(Constants.PREF_PROFILE_PHOTO,Constants.BASE_URL+ userEntity.getProfilePhoto());
					CustomPreferences.setPreferences(Constants.PREF_BIO, userEntity.getBio());
					CustomPreferences.setPreferences(Constants.PREF_CLIENT_NAME, userEntity.getClientName());
					CustomPreferences.setPreferences(Constants.PREF_RESPONDER_ID, userEntity.getResponderId());
					CustomPreferences.setPreferences(Constants.PREF_IS_CHARGED_NURSE, userEntity.getIsChargedNurse().equals("true")?true:false);


					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.putExtra(Constants.INTENT_USER_TYPE, Integer.parseInt(userEntity.getRole()));
					startActivity(intent);
					finish();
				} else {
					Utilities.showAlertDialog(LoginActivity.this, getString(R.string.app_name), getString(R.string.invalid_username_password),
							getString(R.string.ok), "", null, null, false);

				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {
              try {
                  UserInfoEntity userEntity = (UserInfoEntity) retrofitError.getBody();
                  if(userEntity!=null && userEntity.getError()!=null) {
                      Utilities.showAlertDialog(LoginActivity.this, getString(R.string.app_name), userEntity.getError_description(),
                              getString(R.string.ok), "", null, null, false);
                  }
                  Log.e("Error", ""+ userEntity.getError());
              }catch (Exception e){

              }


				Utilities.dismissDialog(dialog);
			}
		});
	}
}
