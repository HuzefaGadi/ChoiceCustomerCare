package com.unfc.choicecustomercare.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.BaseEntity;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Hai Nguyen - 8/28/15.
 */
public class MessageAdapter extends BaseExpandableListAdapter {

	private Activity mActivity;
	private MessageEntity mMessage;
	private LayoutInflater mInflater;
	private SimpleDateFormat mServerFormat, mDateFormat;
	private OnResponseMessageListener mResponseListener;

	public MessageAdapter(Activity activity, OnResponseMessageListener responseListener, MessageEntity message) {

		this.mMessage = message;
		this.mActivity = activity;
		this.mResponseListener = responseListener;
        this.mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        this.mServerFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
		this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getGroupCount() {

		return 3;
	}

	@Override
	public int getChildrenCount(int i) {

		switch (i) {

			case 0 :

				return mMessage.getIncoming().size();
			case 1 :

				return mMessage.getInProgress().size();
			case 2 :

				return mMessage.getComplete().size();
		}

		return 0;
	}

	@Override
	public Object getGroup(int i) {

		return null;
	}

	@Override
	public Object getChild(int i, int i1) {

		switch (i) {

			case 0 :

				return mMessage.getIncoming().get(i1);
			case 1 :

				return mMessage.getInProgress().get(i1);
			case 2 :

				return mMessage.getComplete().get(i1);
		}

		return null;
	}

	@Override
	public long getGroupId(int i) {
		return 0;
	}

	@Override
	public long getChildId(int i, int i1) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

		SectionViewHolder holder;
		if (view == null) {

			view = mInflater.inflate(R.layout.layout_message_section, viewGroup, false);
			holder = new SectionViewHolder(view);
			view.setTag(holder);
		} else {

			holder = (SectionViewHolder) view.getTag();
		}

		switch (i) {

			case 0 :

				holder.txtSection.setText(mActivity.getString(R.string.message_incoming));
				break;
			case 1 :

				holder.txtSection.setText(mActivity.getString(R.string.message_in_progress));
				break;
			case 2 :

				holder.txtSection.setText(mActivity.getString(R.string.message_completed));
				break;
		}

		return view;
	}

	@Override
	public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

		ItemViewHolder holder;
		if (view == null) {

			view = mInflater.inflate(R.layout.layout_message_item, viewGroup, false);
			holder = new ItemViewHolder(view);
			view.setTag(holder);
		} else {

			holder = (ItemViewHolder) view.getTag();
		}

		final MessageEntity message = (MessageEntity) getChild(i, i1);
		try {

			Date sentTime = mServerFormat.parse(message.getSent());
			holder.txtTime.setText(mDateFormat.format(sentTime));
		} catch (Exception ex) {

			holder.txtTime.setVisibility(View.GONE);
		}

		holder.txtMessage.setText(message.getMessageText());

		if (i != 0) {

			holder.llButton.setVisibility(View.GONE);
		} else {

			holder.llButton.setVisibility(View.VISIBLE);
		}

		holder.btnAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				doAcceptMessage(message);
			}
		});

		holder.btnDecline.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				doDeclineMessage(message);
			}
		});

		return view;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {

		return i != 0;
	}

	class ItemViewHolder {

		@Bind(R.id.message_accept_btn)
		TextView btnAccept;

		@Bind(R.id.message_decline_btn)
		TextView btnDecline;

		@Bind(R.id.message_item_text)
		TextView txtMessage;

		@Bind(R.id.message_item_time)
		TextView txtTime;

		@Bind(R.id.message_button_ll)
		LinearLayout llButton;

		public ItemViewHolder(View view) {

			ButterKnife.bind(this, view);
		}
	}

	class SectionViewHolder {

		@Bind(R.id.message_section_text)
		TextView txtSection;

		public SectionViewHolder(View view) {

			ButterKnife.bind(this, view);
		}
	}

	/**
	 * Do accept message
	 */
	private void doAcceptMessage(MessageEntity message) {

		final Dialog dialog = LoadingDialog.show(mActivity);
		new BaseApi(true).getInterface().doAcceptMessage(message.getMessageQueueId(), message.getMessageTypeId(),
				new Callback<BaseEntity>() {
					@Override
					public void success(BaseEntity baseEntity, Response response) {

						mResponseListener.onResponse();
						Utilities.dismissDialog(dialog);
					}

					@Override
					public void failure(RetrofitError retrofitError) {

						Utilities.dismissDialog(dialog);
					}
				});
	}

	/**
	 * Do accept message
	 */
	private void doDeclineMessage(MessageEntity message) {

		final Dialog dialog = LoadingDialog.show(mActivity);
		new BaseApi(true).getInterface().doDenyMessage(message.getMessageQueueId(), message.getId(),
				new Callback<BaseEntity>() {
					@Override
					public void success(BaseEntity baseEntity, Response response) {

						mResponseListener.onResponse();
						Utilities.dismissDialog(dialog);
					}

					@Override
					public void failure(RetrofitError retrofitError) {

						Toast.makeText(mActivity, retrofitError.getMessage(), Toast.LENGTH_LONG).show();
						Utilities.dismissDialog(dialog);
					}
				});
	}

	public interface OnResponseMessageListener {

		void onResponse();
	}
}
