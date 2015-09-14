package com.unfc.choicecustomercare.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.models.HistoryEntity;
import com.unfc.choicecustomercare.models.MessageEntity;

/**
 * Hai Nguyen - 8/28/15.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

	private List<HistoryEntity> mMessages;
	private SimpleDateFormat mServerFormat, mDateFormat;

	public HistoryAdapter(List<HistoryEntity> messages) {

		this.mMessages = messages;
		this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		this.mServerFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		HistoryEntity history = mMessages.get(position);
		holder.txtText.setText(history.getMessage().getMessageText());
		try {

			Date sentTime = mServerFormat.parse(history.getMessage().getSent());
			String strAction;
			if (history.isDeny()) {

				strAction = "Denied at : ";
			} else {

				strAction = "Accepted at : ";
			}

			holder.txtTime.setText(strAction + mDateFormat.format(sentTime));
		} catch (Exception ex) {

			holder.txtTime.setVisibility(View.GONE);
		}
	}

	@Override
	public int getItemCount() {
		return mMessages == null ? 0 : mMessages.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@Bind(R.id.history_item_text)
		TextView txtText;

		@Bind(R.id.history_item_time)
		TextView txtTime;

		public ViewHolder(View itemView) {
			super(itemView);

			ButterKnife.bind(this, itemView);
		}
	}
}
