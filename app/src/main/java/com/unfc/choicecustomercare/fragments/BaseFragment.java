package com.unfc.choicecustomercare.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.unfc.choicecustomercare.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Hai Nguyen - 7/29/15.
 */
public class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	@Nullable
	@Bind(R.id.fragment_refresh_layout)
	SwipeRefreshLayout mRefreshLayout;

	@Nullable
	@Bind(R.id.resource_title)
	TextView txtTitle;

	@Nullable
	@Bind(R.id.resource_spinner)
	Spinner spnSelector;

	// @Nullable
	// @Bind(R.id.resource_list_view)
	// Spinner lvResource;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {

		int viewId = addView();
		View view = inflater.inflate(viewId, container, false);
		ButterKnife.bind(this, view);

		initChildView();
		return view;
	}

	protected int addView() {

		return 0;
	}

	/**
	 * Initiation child view
	 */
	protected void initChildView() {

	}

	/**
	 * Load data
	 * 
	 */
	public void loadData(boolean isRefresh) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.Fragment#setUserVisibleHint(boolean)
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {

			// if (isResumed()) {

			loadData(false);
			// return;
			// }

			// new Handler().postDelayed(new Runnable() {
			// @Override
			// public void run() {
			//
			// loadData(false);
			// }
			// }, 1000);
		}
	}

	@Override
	public void onRefresh() {

		loadData(true);
	}
}
