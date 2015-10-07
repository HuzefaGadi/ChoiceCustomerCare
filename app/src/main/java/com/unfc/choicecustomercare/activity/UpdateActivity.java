package com.unfc.choicecustomercare.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.UpdateResponseModel;
import com.unfc.choicecustomercare.models.UserInfoEntity;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.CustomPreferences;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdateActivity extends Activity implements ImageChooserListener {

    EditText edtBio;
    TextView btnUpdate;
	TextView btnChoosePhoto;
	CheckBox cbChargedNurse;
	TextView tvClientName;
	TextView tvPersonName;
	TextView tvRoleName;
	TextView tvClose;
	EditText edtFirstName;
	EditText edtLastName;
	ImageView ivProfilePhoto;
    private SharedPreferences preferences;
	private ImageChooserManager imageChooserManager;
	private String filePath =null;
	private int chooserType;
	private AlertDialog profile_Dailog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update);

		LinearLayout llParent = (LinearLayout) findViewById(R.id.llParent);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		llParent.setMinimumWidth(width-20);

		ivProfilePhoto = (ImageView) findViewById(R.id.imageView);
		btnChoosePhoto = (TextView) findViewById(R.id.btn_pick_photo);
        tvClientName = (TextView) findViewById(R.id.tv_client_name);
        tvPersonName = (TextView) findViewById(R.id.tv_person_name);
        tvRoleName = (TextView) findViewById(R.id.tv_role_name);
        tvClose = (TextView) findViewById(R.id.tv_close);
        cbChargedNurse = (CheckBox) findViewById(R.id.cb_charged_nurse);
        edtBio = (EditText) findViewById(R.id.edt_bio);
        edtFirstName = (EditText) findViewById(R.id.edt_first_name);
        edtLastName = (EditText) findViewById(R.id.edt_last_name);
		btnUpdate = (TextView) findViewById(R.id.btn_update);

        tvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


		String strFirstName = CustomPreferences.getPreferences(Constants.PREF_FIRST_NAME, "");
		String strLastName = CustomPreferences.getPreferences(Constants.PREF_LAST_NAME, "");
		String strBio = CustomPreferences.getPreferences(Constants.PREF_BIO, "");
		String strProfilePhoto = CustomPreferences.getPreferences(Constants.PREF_PROFILE_PHOTO, "");
		boolean isChargedNurse = CustomPreferences.getPreferences(Constants.PREF_IS_CHARGED_NURSE, false);
		String strClientName = CustomPreferences.getPreferences(Constants.PREF_CLIENT_NAME, "");
		int role = CustomPreferences.getPreferences(Constants.PREF_ROLE_TYPE, 0);
        Log.i("Bio", strBio);
		tvClientName.setText(strClientName);
		tvPersonName.setText(strFirstName +" "+strLastName);

		if(role==0) {
			tvRoleName.setText("Tech");
			cbChargedNurse.setVisibility(View.GONE);
		}
		else if(role == 1) {
			tvRoleName.setText("Nurse");
			cbChargedNurse.setVisibility(View.VISIBLE);
		}
		else if(role == 2) {
			tvRoleName.setText("Doctor");
			cbChargedNurse.setVisibility(View.GONE);
		} else if(role == 3) {
            tvRoleName.setText("Charged Nurse");
            cbChargedNurse.setVisibility(View.VISIBLE);
        }


		edtBio.setText(strBio);

		if(strProfilePhoto!=null)
			Utilities.displayImage(getApplicationContext(), strProfilePhoto, ivProfilePhoto);

		if(isChargedNurse==true)
			cbChargedNurse.setChecked(true);
		else cbChargedNurse.setChecked(false);
		edtFirstName.setText(strFirstName);
		edtLastName.setText(strLastName);


		btnChoosePhoto.setClickable(true);

		btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showImageSelectionDialog();
			}
		});


		btnUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                updateUserProfile();
			}
		});
	}

	private void showImageSelectionDialog(){

		LayoutInflater factory = LayoutInflater.from(UpdateActivity.this);
		final View imageChooseDialog = factory.inflate(R.layout.facesheet_dailog, null);
		profile_Dailog = new AlertDialog.Builder(UpdateActivity.this).create();
		profile_Dailog.setView(imageChooseDialog);
		imageChooseDialog.findViewById(R.id.Gallery).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// your business logic
				try {
					chooseImage();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		imageChooseDialog.findViewById(R.id.Camera).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					takePicture();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		imageChooseDialog.findViewById(R.id.Cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				profile_Dailog.dismiss();

			}
		});


		profile_Dailog.show();
	}




    private void updateUserProfile() {
		String encodedImage="";
		if(filePath!=null)
		{
			Bitmap bm = BitmapFactory.decodeFile(filePath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
			byte[] b = baos.toByteArray();
			encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		}


        final Dialog dialog = LoadingDialog.show(this);
        new BaseApi(false).getInterface().doUpdate(edtFirstName.getText().toString(),
				edtLastName.getText().toString(),
				CustomPreferences.getPreferences(Constants.PREF_RESPONDER_ID, ""),
				edtBio.getText().toString(),
				encodedImage,
				cbChargedNurse.isChecked() + "", new Callback<UpdateResponseModel>() {
					@Override
					public void success(UpdateResponseModel userInfoEntity, Response response) {

						if (userInfoEntity.isResult()) {
							CustomPreferences.setPreferences(Constants.PREF_FIRST_NAME, edtFirstName.getText().toString());
							CustomPreferences.setPreferences(Constants.PREF_LAST_NAME, edtLastName.getText().toString());
							CustomPreferences.setPreferences(Constants.PREF_PROFILE_PHOTO, Constants.BASE_URL + userInfoEntity.getUrl());
							CustomPreferences.setPreferences(Constants.PREF_BIO, edtBio.getText().toString());
							CustomPreferences.setPreferences(Constants.PREF_IS_CHARGED_NURSE, cbChargedNurse.isChecked());
							Log.e("Photo", CustomPreferences.getPreferences(Constants.PREF_PROFILE_PHOTO, ""));

						}
						dialog.dismiss();
						Toast.makeText(UpdateActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
						finish();
						Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);

					}

					@Override
					public void failure(RetrofitError retrofitError) {
						dialog.dismiss();
					}
				});

    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
			if (imageChooserManager == null) {
				reinitializeImageChooser();
			}
			imageChooserManager.submit(requestCode, data);
		}

		if(profile_Dailog!=null)
			profile_Dailog.dismiss();

	}
	private void chooseImage() {
		chooserType = ChooserType.REQUEST_PICK_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_PICK_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		try {

			filePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void takePicture() {
		chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
		imageChooserManager = new ImageChooserManager(this,
				ChooserType.REQUEST_CAPTURE_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		try {

			filePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onImageChosen(final ChosenImage chosenImage) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				if (chosenImage != null) {
					filePath = chosenImage.getFilePathOriginal();
					ivProfilePhoto.setImageURI(Uri.parse(new File(chosenImage
							.getFileThumbnail()).toString()));

				}
			}
		});

	}

	@Override
	public void onError(final String s) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(UpdateActivity.this, "" + s, Toast.LENGTH_SHORT).show();
			}
		});
	}

	// Should be called if for some reason the ImageChooserManager is null (Due
	// to destroying of activity for low memory situations)
	private void reinitializeImageChooser() {
		imageChooserManager = new ImageChooserManager(this, chooserType,
				"myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		imageChooserManager.reinitialize(filePath);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("chooser_type", chooserType);
		outState.putString("media_path", filePath);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("chooser_type")) {
				chooserType = savedInstanceState.getInt("chooser_type");
			}

			if (savedInstanceState.containsKey("media_path")) {
				filePath = savedInstanceState.getString("media_path");
			}
		}
		super.onRestoreInstanceState(savedInstanceState);
	}
}
