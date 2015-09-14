package com.unfc.choicecustomercare.fragments;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.adapters.ResourceAdapter;

/**
 * 16/7/15.
 */

public class RoomFragment extends BaseFragment {

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

		txtTitle.setText(R.string.room_view);
        spnSelector.setAdapter(new ResourceAdapter(getActivity(), new String[]{"Room 1","Room 2","Room 3","Room 4"}));
	}
}
