package com.unfc.choicecustomercare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unfc.choicecustomercare.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Hai Nguyen - 8/30/15.
 */
public class ResourceAdapter extends BaseAdapter {

	private String[] mData;
	private LayoutInflater mInflater;

	public ResourceAdapter(Context ctx, String[] data) {

		this.mData = data;
		this.mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mData == null ? 0 : mData.length;
	}

	@Override
	public Object getItem(int i) {
		return mData[i];
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return getView(position, convertView, parent, false);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		return getView(position, convertView, parent, true);
	}

	/**
	 * Get view
	 */
	private View getView(int position, View convertView, ViewGroup parent, boolean isDropDown) {

		ViewHolder holder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.layout_resource_spn_tem, parent, false);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		String item = (String) getItem(position);
		if (isDropDown) {

			holder.imvDown.setVisibility(View.INVISIBLE);
		} else {

			holder.imvDown.setVisibility(View.VISIBLE);
		}

		holder.txtText.setText(item);
		return convertView;
	}

	class ViewHolder {

		@Bind(R.id.resource_item_text)
		TextView txtText;

		@Bind(R.id.resource_item_imv)
		ImageView imvDown;

		public ViewHolder(View view) {

			ButterKnife.bind(this, view);
		}
	}
}
