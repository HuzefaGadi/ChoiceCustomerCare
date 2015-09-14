package com.unfc.choicecustomercare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.models.UserTypeEntity;

import java.util.List;

/**
 * Hai Nguyen - 8/30/15.
 */
public class UserTypeAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<UserTypeEntity> mTypes;

	public UserTypeAdapter(Context ctx, List<UserTypeEntity> types) {

		this.mTypes = types;
		this.mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mTypes == null ? 0 : mTypes.size();
	}

	@Override
	public Object getItem(int i) {
		return mTypes.get(i);
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

		UserTypeEntity type = (UserTypeEntity) getItem(position);
		if (isDropDown) {

			holder.imvDown.setVisibility(View.INVISIBLE);
		} else {

			holder.imvDown.setVisibility(View.VISIBLE);
		}

		holder.txtText.setText(type.getName());
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
