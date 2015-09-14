/**
 * 
 */
package com.unfc.choicecustomercare.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.fragments.MainFragment;
import com.unfc.choicecustomercare.fragments.RoomFragment;
import com.unfc.choicecustomercare.fragments.StaffFragment;

import java.util.List;

/**
 * @author nvhaiwork
 *
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

	private Context mCtx;
	private List<String> mTitles;

	/**
	 * @param fm
	 *            Fragment manager
	 */
	public MainPagerAdapter(Context ctx, FragmentManager fm, List<String> titles) {
		super(fm);

		this.mCtx = ctx;
		this.mTitles = titles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		switch (position) {

			case 0 :

				return MainFragment.getInstance();
			case 1 :

				return StaffFragment.getInstance();
			case 2 :

				return RoomFragment.getInstance();
			default :
				return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {

		return mTitles == null ? 0 : mTitles.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {

		return mTitles.get(position);
	}

	/**
	 * Get tab view
	 */
	public View getTabView(int position) {

		TextView tv = (TextView) LayoutInflater.from(mCtx).inflate(R.layout.layout_tab_view, null);
		tv.setText(getPageTitle(position));
		if (position == 0) {

			tv.setSelected(true);
		}

		return tv;
	}
}
