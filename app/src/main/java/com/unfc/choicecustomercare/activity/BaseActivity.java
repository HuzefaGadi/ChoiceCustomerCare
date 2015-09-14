package com.unfc.choicecustomercare.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.unfc.choicecustomercare.utils.CustomPreferences;

import butterknife.ButterKnife;

/**
 * Hai Nguyen - 8/24/15.
 */
@SuppressWarnings("deprecation")
public class BaseActivity extends ActionBarActivity implements View.OnClickListener {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		int layoutId = addLayoutView();
		setContentView(layoutId);
		ButterKnife.bind(this);
		CustomPreferences.init(this);
		initComponents();
	}

	/**
	 * Add layout view for activity
	 *
	 * @return Layout view id
	 */
	protected int addLayoutView() {

		return 0;
	}

	protected void initComponents() {

	}

	@Override
	public void onClick(View view) {

	}
}
