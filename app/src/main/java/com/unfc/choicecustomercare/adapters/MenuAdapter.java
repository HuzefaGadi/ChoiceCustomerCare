package com.unfc.choicecustomercare.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.models.MenuEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Hai Nguyen - 8/28/15.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

	private List<MenuEntity> mMenus;
	private OnItemClickListener mItemClickListener;

	public MenuAdapter(Activity act, List<MenuEntity> menus) {

		this.mMenus = menus;
		this.mItemClickListener = (OnItemClickListener) act;
	}

	@Override
	public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_menu_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MenuAdapter.ViewHolder holder, int position) {

		final MenuEntity menu = mMenus.get(position);
		holder.txtText.setText(menu.getTextResourceId());
		holder.imvIcon.setImageResource(menu.getIconResourceId());
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				mItemClickListener.onItemClick(menu);
			}
		});
	}

	@Override
	public int getItemCount() {

		return mMenus == null ? 0 : mMenus.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@Bind(R.id.menu_item_imv)
		ImageView imvIcon;

		@Bind(R.id.menu_item_text)
		TextView txtText;

		public ViewHolder(View itemView) {
			super(itemView);

			ButterKnife.bind(this, itemView);
		}
	}

	public interface OnItemClickListener {

		void onItemClick(MenuEntity menu);
	}
}
