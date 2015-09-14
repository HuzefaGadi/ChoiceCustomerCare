package com.unfc.choicecustomercare.fragments;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.adapters.ResourceAdapter;

/**
 * 16/7/15.
 */

public class StaffFragment extends BaseFragment {

	public static StaffFragment getInstance() {

		return new StaffFragment();
	}

	@Override
	protected int addView() {

		return R.layout.fragment_resource;
	}

	@Override
	protected void initChildView() {
		super.initChildView();

		txtTitle.setText(R.string.staff_view);
		spnSelector.setAdapter(
				new ResourceAdapter(getActivity(), new String[]{"Staff 1", "Staff 2", "Staff 3", "Staff 4"}));
	}
}
