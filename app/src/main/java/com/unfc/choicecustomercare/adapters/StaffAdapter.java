package com.unfc.choicecustomercare.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.models.ResponderEntity;
import com.unfc.choicecustomercare.utils.RequestTypeForRooms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Rashida on 21/09/15.
 */

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {


    private SimpleDateFormat mServerFormat, mDateFormat;

    private List<ResponderEntity> responderEntities;
    public StaffAdapter(List<ResponderEntity> responders) {

        this.responderEntities = responders;
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        mServerFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_queue_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ResponderEntity responder = responderEntities.get(position);
        MessageEntity message = responder.getMessage();

        try {

            Date sentTime = mServerFormat.parse(message.getSent());
            holder.txtTime.setText(mDateFormat.format(sentTime));

        } catch (Exception ex) {

            holder.txtTime.setVisibility(View.GONE);
        }

        if(responder.isDeny())
        {

                holder.txtRespondedName.setText("Denied by the user");
                holder.getTxtRespondedTime.setText("");

        }
        else
        {
            if(message.getResponded()!=null && !message.getResponded().isEmpty())
            {
                holder.txtRespondedName.setText("Accepted at");
                holder.getTxtRespondedTime.setText(message.getResponded());
            }
        }
        String strRequest = RequestTypeForRooms.values()[Integer.parseInt(message.getMessageTypeId())].toString().toLowerCase();
        holder.txtRequest.setText(strRequest.toLowerCase() + " request");


    }

    @Override
    public int getItemCount() {

        return responderEntities == null ? 0 : responderEntities.size();
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
