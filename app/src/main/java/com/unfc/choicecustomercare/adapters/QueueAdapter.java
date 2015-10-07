package com.unfc.choicecustomercare.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.utils.RequestTypeForRooms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Hai Nguyen - 8/29/15.
 */
public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ViewHolder> {

    private List<MessageEntity> mMessages;
    private SimpleDateFormat mServerFormat, mDateFormat;
    List<String> uniqueMessageQueId;
    List<MessageEntity> messageEntities;
    public QueueAdapter(List<MessageEntity> messages) {

        this.mMessages = messages;
        messageEntities = new ArrayList<MessageEntity>();
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        mServerFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
        uniqueMessageQueId = new ArrayList<String>();
        for(MessageEntity message : mMessages)
        {
            if(!uniqueMessageQueId.contains(message.getMessageQueueId()))
            {
                uniqueMessageQueId.add(message.getMessageQueueId());
            }
        }

        for(String messageQueId : uniqueMessageQueId)
        {

            for(int i= 0;i< mMessages.size();i++)
            {
                if(mMessages.get(i).getMessageQueueId().equals(messageQueId))
                {
                    if(mMessages.get(i).getResponded()!=null &&
                            !mMessages.get(i).getResponded().isEmpty()&&mMessages.get(i).getResponderId()!=null)
                    {
                        messageEntities.add(mMessages.get(i));
                        break;
                    }
                    else if(mMessages.get(i).getResponded()==null && mMessages.get(i).getResponderId()!=null)
                    {
                        messageEntities.add(mMessages.get(i));
                        break;
                    }

                }
            }
        }

        mMessages = messageEntities;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_queue_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MessageEntity message = mMessages.get(position);


        try {

            Date sentTime = mServerFormat.parse(message.getSent());
            holder.txtTime.setText(mDateFormat.format(sentTime));

        } catch (Exception ex) {

            holder.txtTime.setVisibility(View.GONE);
        }

        String strRequest = RequestTypeForRooms.values()[Integer.parseInt(message.getMessageTypeId())].toString().toLowerCase();
        holder.txtRequest.setText(strRequest.toLowerCase() + " request");
        if(message.getResponded()!=null && !message.getResponded().isEmpty())
        {
            holder.txtRespondedName.setText("Responded by Responder"+message.getResponderId());
            holder.getTxtRespondedTime.setText(message.getResponded());
        }
        else
        {
            holder.txtRespondedName.setText("Pending");
        }
    }

    @Override
    public int getItemCount() {

        return mMessages == null ? 0 : mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        @Bind(R.id.queue_item_text)
        TextView txtRequest;

        @Bind(R.id.queue_item_time)
        TextView txtTime;

        @Bind(R.id.queue_item_text_responded_name)
        TextView txtRespondedName;

        @Bind(R.id.queue_item_text_responded)
        TextView getTxtRespondedTime;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
