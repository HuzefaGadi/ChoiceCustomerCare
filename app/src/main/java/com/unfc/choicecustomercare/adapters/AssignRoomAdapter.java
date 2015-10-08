package com.unfc.choicecustomercare.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.BaseEntity;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.models.ResponderEntity;
import com.unfc.choicecustomercare.models.Room;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.RequestTypeForRooms;
import com.unfc.choicecustomercare.utils.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Rashida on 21/09/15.
 */

public class AssignRoomAdapter extends RecyclerView.Adapter<AssignRoomAdapter.ViewHolder> {



    private Dialog mDialog;
    private Context mContext;
    private boolean isAssignedRoom;
    private int responderId;
    private List<Room> rooms;
    public AssignRoomAdapter(List<Room> rooms,Context mContext,boolean isAssignedRoom,int responderId) {

        this.rooms = rooms;
        this.mContext = mContext;
        this.isAssignedRoom = isAssignedRoom;
        this.responderId = responderId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assign_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

      final  Room room = rooms.get(position);


        try {

            holder.txtRoomName.setText(room.getName());
            if(isAssignedRoom)
            {
                holder.btnUnAssign.setText("UnAssign");
            }
            else
            {
                holder.btnUnAssign.setText("Assign");
            }
            holder.btnUnAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog = LoadingDialog.show(mContext);
                    if(isAssignedRoom)
                    {
                        new BaseApi(false).getInterface().setUnassignRoom(room.getRoomId(),responderId, new Callback<BaseEntity>() {
                            @Override
                            public void success(BaseEntity entity, Response response) {
                                Utilities.dismissDialog(mDialog);
                                if(entity.isResult())
                                {
                                    //Toast.makeText(mContext, "UnAssign Success "+response.getBody(), Toast.LENGTH_LONG).show();
                                    rooms.remove(position);
                                    notifyItemRemoved(position);

                                }

                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Utilities.dismissDialog(mDialog);
                                Toast.makeText(mContext,"Unassign Fail"+retrofitError.getBody(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {new BaseApi(false).getInterface().setAssignRoom(room.getRoomId(),responderId, new Callback<BaseEntity>() {
                        @Override
                        public void success(BaseEntity entity, Response response) {
                            Utilities.dismissDialog(mDialog);
                            if(entity.isResult())
                            {
                               // Toast.makeText(mContext, "Assign Success "+response.getBody(), Toast.LENGTH_LONG).show();
                                rooms.remove(position);
                                notifyItemRemoved(position);
                            }


                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Utilities.dismissDialog(mDialog);
                            Toast.makeText(mContext, "Assign fail "+retrofitError.getBody(), Toast.LENGTH_LONG).show();

                        }
                    });

                    }

                }
            });

        } catch (Exception ex) {

            holder.txtRoomName.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {

        return rooms == null ? 0 : rooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        @Bind(R.id.tv_room_name)
        TextView txtRoomName;

        @Bind(R.id.btn_unassign)
        TextView btnUnAssign;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
