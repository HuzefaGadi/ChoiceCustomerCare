package com.unfc.choicecustomercare.fragments;

/**
 * 16/7/15.
 */
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ExpandableListView;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.adapters.MessageAdapter;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainFragment extends BaseFragment
		implements
			ExpandableListView.OnGroupClickListener,
			MessageAdapter.OnResponseMessageListener {

	private Dialog mDialog;

	@Bind(R.id.main_list_view)
	ExpandableListView listView;

	public static MainFragment getInstance() {

		return new MainFragment();
	}

	@Override
	protected int addView() {

		return R.layout.fragment_main;
	}

	@Override
	protected void initChildView() {
		super.initChildView();

		listView.setOnGroupClickListener(this);
		mRefreshLayout.setOnRefreshListener(this);
		mDialog = LoadingDialog.show(getActivity());
	}

	@Override
	public void loadData(boolean isRefresh) {

		if (!getUserVisibleHint()) {

			return;
		}

		if (!isRefresh && mRefreshLayout != null) {

			mRefreshLayout.setRefreshing(true);
		}

		new BaseApi(true).getInterface().getMessages(new Callback<MessageEntity>() {
			@Override
			public void success(MessageEntity messageEntity, Response response) {

				displayMessage(messageEntity);
				Utilities.dismissDialog(mDialog);
				mRefreshLayout.setRefreshing(false);
			}

			@Override
			public void failure(RetrofitError retrofitError) {

				Utilities.dismissDialog(mDialog);
				mRefreshLayout.setRefreshing(false);
			}
		});
	}

	/**
	 * Display message
	 */
	private void displayMessage(MessageEntity messageEntity) {

		MessageAdapter adapter = new MessageAdapter(getActivity(), this, messageEntity);
		listView.setAdapter(adapter);
		for (int i = 0; i < adapter.getGroupCount(); i++) {

			listView.expandGroup(i);
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
				new IntentFilter(Constants.INTENT_UPDATE_MESSAGE));
	}

	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			loadData(false);
		}
	};

	@Override
	public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {

		return true;
	}

	@Override
	public void onResponse() {

		loadData(false);
	}
}
