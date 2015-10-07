package com.unfc.choicecustomercare.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.activity.AssignRoomActivity;
import com.unfc.choicecustomercare.adapters.StaffAdapter;
import com.unfc.choicecustomercare.adapters.ResourceAdapter;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.models.ResponderEntity;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;
import com.unfc.choicecustomercare.view.CircularImageView;

import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 16/7/15.
 */

public class StaffFragment extends BaseFragment {

	@Bind(R.id.resource_list_view)
	RecyclerView recyclerView;

	@Bind (R.id.imageView)
	CircularImageView ivProfilePhoto;

	@Bind(R.id.tv_user_name)
	TextView tvUserName;


	@Bind(R.id.tv_person_name)
	TextView tvPersonName;

	@Bind(R.id.tv_role_name)
	TextView tvRoleName;

	@Bind(R.id.user_info)
	LinearLayout userInfoLayout;

	private Dialog mDialog;

	int responderId;

	int selectedUser = 0;

	public static StaffFragment getInstance() {

		return new StaffFragment();
	}

	@Override
	protected int addView() {

		return R.layout.fragment_staff;
	}



	@Override
	protected void initChildView() {
		super.initChildView();



		mDialog = LoadingDialog.show(getActivity());
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		txtTitle.setText(R.string.staff_view);
		new BaseApi(false).getInterface().getResponders(new Callback<List<ResponderEntity>>() {
			@Override
			public void success(List<ResponderEntity> responderEntities, Response response) {


				initalizeSpinner(responderEntities);
				Utilities.dismissDialog(mDialog);

			}

			@Override
			public void failure(RetrofitError retrofitError) {

				Utilities.dismissDialog(mDialog);


			}
		});
		userInfoLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), AssignRoomActivity.class);
				intent.putExtra(Constants.RESPONDER_ID,responderId);
				startActivity(intent);
			}
		});


	}


	/*@Override
	public void loadData(boolean isRefresh) {

		if (!getUserVisibleHint()) {

			return;
		}

		if (!isRefresh && mRefreshLayout != null) {

			mRefreshLayout.setRefreshing(true);
		}


		new BaseApi(false).getInterface().getResponders(new Callback<List<ResponderEntity>>() {
			@Override
			public void success(List<ResponderEntity> responderEntities, Response response) {


				initalizeSpinner(responderEntities);
				Utilities.dismissDialog(mDialog);
				mRefreshLayout.setRefreshing(false);
			}


			@Override
			public void failure(RetrofitError retrofitError) {

				Utilities.dismissDialog(mDialog);
				mRefreshLayout.setRefreshing(false);
			}
		});
	}*/

	private void initalizeSpinner(final List<ResponderEntity> responderEntities)
	{
		String[] responders = new String[responderEntities.size()];
		int count = 0;
		for(ResponderEntity user :responderEntities)
		{
			responders[count++] = user.getFirstName()+" "+user.getLastName();
		}
		spnSelector.setAdapter(new ResourceAdapter(getActivity(), responders));
		spnSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

				selectedUser = i;
				ResponderEntity responder = responderEntities.get(i);
				responderId = responder.getId();
				String strProfilePhoto = Constants.BASE_URL + responder.getProfilePhoto();
				if (responder.getProfilePhoto() != null) {
					Utilities.displayImage(getActivity(), strProfilePhoto, ivProfilePhoto);
				} else {
					ivProfilePhoto.setImageDrawable(null);
				}
				tvUserName.setText(responder.getUser().getEmail());
				tvPersonName.setText(responder.getFirstName() + " " + responder.getLastName());
				tvRoleName.setText(responder.getUser().getUserType());
				showResponderDetails();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}

		});

		spnSelector.setSelection(selectedUser);
	}


	/**
	 * Get request queue
	 */
	private void showResponderDetails() {

		if (responderId == 0) {

			return;
		}

		new BaseApi(false).getInterface().getResponderRequest(responderId, new Callback<List<ResponderEntity>>() {
			@Override
			public void success(List<ResponderEntity> messages, Response response) {

				displayQueue(messages);
			}

			@Override
			public void failure(RetrofitError retrofitError) {

			}
		});
	}

	/**
	 * Display queue
	 */
	private void displayQueue(List<ResponderEntity> messages) {

		StaffAdapter adapter = new StaffAdapter(messages);
		recyclerView.setAdapter(adapter);
	}
}
