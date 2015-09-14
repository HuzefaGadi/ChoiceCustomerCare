package com.unfc.choicecustomercare.activity;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.adapters.UserTypeAdapter;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.BaseEntity;
import com.unfc.choicecustomercare.models.UserEntity;
import com.unfc.choicecustomercare.models.UserTypeEntity;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

	@Bind(R.id.sign_up_type)
	Spinner spnUserType;

	@Bind(R.id.sign_up_email)
	EditText edtEmail;

	@Bind(R.id.sign_up_password)
	EditText edtPassword;

	@Bind(R.id.sign_up_confirm_password)
	EditText edtConfirmPassword;

	@Bind(R.id.sign_up_btn)
	TextView btnSignUp;

	@Bind(R.id.sign_up_first_name)
	EditText edtFirstName;

	@Bind(R.id.sign_up_last_name)
	EditText edtLastName;

	private int mUserTypeId;

	@Override
	protected int addLayoutView() {

		return R.layout.activity_signup;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		List<UserTypeEntity> types = new ArrayList<>();
		UserTypeEntity type = new UserTypeEntity();
		type.setName("Tech");
		type.setId(0);
		types.add(type);

		type = new UserTypeEntity();
		type.setName("Nurse");
		type.setId(1);
		types.add(type);

		type = new UserTypeEntity();
		type.setName("Doctor");
		type.setId(2);
		types.add(type);

//		type = new UserTypeEntity();
//		type.setName("Charged Nurse");
//		type.setId(3);
//		types.add(type);

		btnSignUp.setOnClickListener(this);
		spnUserType.setOnItemSelectedListener(this);
		spnUserType.setAdapter(new UserTypeAdapter(this, types));
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {

			case R.id.sign_up_btn :

				registerUser();
				break;
		}
	}

	/**
	 * Register user
	 */
	private void registerUser() {

		String strEmail = edtEmail.getText().toString();
		String strPassword = edtPassword.getText().toString();
		String strConfirmPassword = edtConfirmPassword.getText().toString();
		String strLastName = edtLastName.getText().toString();
		String strFirstName = edtFirstName.getText().toString();
		if (strEmail.equals("") || strPassword.equals("") || strLastName.equals("") || strFirstName.equals("")) {

			Utilities.showAlertDialog(this, getString(R.string.app_name), getString(R.string.invalid_information),
					getString(R.string.ok), "", null, null, false);
			return;
		}

		if (!strConfirmPassword.equals(strPassword)) {

			Utilities.showAlertDialog(this, getString(R.string.app_name), getString(R.string.password_does_not_match),
					getString(R.string.ok), "", null, null, false);
			return;
		}

		final Dialog dialog = LoadingDialog.show(this);
		UserEntity user = new UserEntity();
		user.setEmail(strEmail);
		user.setMI("MI");
		user.setPassword(strPassword);
		user.setUserType(mUserTypeId);
		user.setLastName(strLastName);
		user.setFirstName(strFirstName);
		new BaseApi(false).getInterface().doRegister(user, new Callback<BaseEntity>() {
			@Override
			public void success(BaseEntity baseEntity, Response response) {

				Utilities.dismissDialog(dialog);
				if (baseEntity.isResult()) {
					Toast.makeText(SignUpActivity.this, "You have been successfully signed up", Toast.LENGTH_SHORT).show();

					finish();
				} else {

					Utilities.showAlertDialog(SignUpActivity.this, getString(R.string.app_name),
							baseEntity.getMessages().get(0), getString(R.string.ok), "", null, null, true);
				}
			}

			@Override
			public void failure(RetrofitError retrofitError) {

				Utilities.dismissDialog(dialog);
			}
		});
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

		mUserTypeId = i;
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}
}
