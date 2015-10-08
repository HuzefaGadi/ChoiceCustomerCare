package com.unfc.choicecustomercare.activity;

import android.app.Dialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.adapters.AssignRoomAdapter;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.Room;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;
import com.unfc.choicecustomercare.view.CustomClickTextView;
import com.unfc.choicecustomercare.view.CustomTextView;

import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AssignRoomActivity extends BaseActivity {


    boolean unassignRoomView;
    @Bind(R.id.resource_list_view_assigned_rooms)
    RecyclerView recyclerViewForAssignedRoom;

    @Bind(R.id.resource_list_view_unassigned_rooms)
    RecyclerView recyclerViewForUnAssignedRoom;

    @Bind(R.id.btn_assign_room)
    CustomClickTextView assignRoom;


    @Bind(R.id.tv_introduction)
    CustomTextView introduction;

    @Bind(R.id.btn_back)
    CustomClickTextView back;

    int responderId;

    Dialog mDialog;

    @Override
    protected int addLayoutView() {

        return R.layout.activity_assign_room;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        unassignRoomView = true;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewForAssignedRoom.setLayoutManager(mLayoutManager);
        responderId = getIntent().getIntExtra(Constants.RESPONDER_ID, 0);
        LinearLayoutManager mLayoutManagerForUnAssinedRoom = new LinearLayoutManager(this);
        recyclerViewForUnAssignedRoom.setLayoutManager(mLayoutManagerForUnAssinedRoom);


        mDialog = LoadingDialog.show(this);
        new BaseApi(false).getInterface().getAssignedRooms(responderId, 1, new Callback<List<Room>>() {
            @Override
            public void success(List<Room> room, Response response) {
                Utilities.dismissDialog(mDialog);
                displayQueueForAssignedRooms(room);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Utilities.dismissDialog(mDialog);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        assignRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.show();

                if (unassignRoomView) {
                    unassignRoomView = false;
                    new BaseApi(false).getInterface().getUnAssignedRooms(responderId, 1, new Callback<List<Room>>() {
                        @Override
                        public void success(List<Room> room, Response response) {
                            Utilities.dismissDialog(mDialog);
                            assignRoom.setText("DONE");
                            introduction.setText(R.string.intro_for_unassignedrooms_tab);
                            recyclerViewForAssignedRoom.setVisibility(View.GONE);
                            recyclerViewForUnAssignedRoom.setVisibility(View.VISIBLE);
                            displayQueueForUnAssignedRooms(room);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Utilities.dismissDialog(mDialog);
                        }
                    });
                } else {
                    unassignRoomView = true;

                    new BaseApi(false).getInterface().getAssignedRooms(getIntent().getIntExtra(Constants.RESPONDER_ID, 0), 1, new Callback<List<Room>>() {
                        @Override
                        public void success(List<Room> room, Response response) {
                            Utilities.dismissDialog(mDialog);

                            assignRoom.setText("Assign Room");
                            introduction.setText(R.string.intro_for_assignedrooms_tab);
                            recyclerViewForAssignedRoom.setVisibility(View.VISIBLE);
                            recyclerViewForUnAssignedRoom.setVisibility(View.GONE);
                            displayQueueForAssignedRooms(room);
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            Utilities.dismissDialog(mDialog);
                        }
                    });
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_staff_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayQueueForAssignedRooms(List<Room> rooms) {

        AssignRoomAdapter adapter = new AssignRoomAdapter(rooms, this, true, responderId);
        recyclerViewForAssignedRoom.setAdapter(adapter);
    }

    private void displayQueueForUnAssignedRooms(List<Room> rooms) {

        AssignRoomAdapter adapter = new AssignRoomAdapter(rooms, this, false, responderId);
        recyclerViewForUnAssignedRoom.setAdapter(adapter);
    }
}
