package com.unfc.choicecustomercare.fragments;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.adapters.QueueAdapter;
import com.unfc.choicecustomercare.adapters.StaffAdapter;
import com.unfc.choicecustomercare.adapters.ResourceAdapter;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;

import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 16/7/15.
 */

public class RoomFragment extends BaseFragment{


	@Bind(R.id.resource_list_view)
	RecyclerView recyclerView;
	private Dialog mDialog;
	int roomId;

	public static RoomFragment getInstance() {

		return new RoomFragment();
	}



	@Override
	protected int addView() {

		return R.layout.fragment_resource;
	}

	@Override
	protected void initChildView() {
		super.initChildView();

		//mRefreshLayout.setOnRefreshListener(this);
		mDialog = LoadingDialog.show(getActivity());
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		txtTitle.setText(R.string.room_view);
        spnSelector.setAdapter(new ResourceAdapter(getActivity(), new String[]{"Room 1","Room 2"}));
		spnSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
			//
				roomId = i+1;
				getRequestQueue();

			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {


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


		new BaseApi(false).getInterface().getRequestRoom(roomId, new Callback<List<MessageEntity>>() {
			@Override
			public void success(List<MessageEntity> messages, Response response) {

				displayQueue(messages);
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
	/**
	 * Get request queue
	 */
	private void getRequestQueue() {

		if (roomId == 0) {

			return;
		}

		new BaseApi(false).getInterface().getRequestRoom(roomId, new Callback<List<MessageEntity>>() {
			@Override
			public void success(List<MessageEntity> messages, Response response) {

				displayQueue(messages);
				Utilities.dismissDialog(mDialog);
				//mRefreshLayout.setRefreshing(false);
			}

			@Override
			public void failure(RetrofitError retrofitError) {
				Utilities.dismissDialog(mDialog);
				//mRefreshLayout.setRefreshing(false);

			}
		});
	}

	/**
	 * Display queue
	 */
	private void displayQueue(List<MessageEntity> messages) {

		QueueAdapter adapter = new QueueAdapter(messages);
		recyclerView.setAdapter(adapter);
	}


}
