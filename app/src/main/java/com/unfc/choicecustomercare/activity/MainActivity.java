package com.unfc.choicecustomercare.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.adapters.HistoryAdapter;
import com.unfc.choicecustomercare.adapters.MenuAdapter;
import com.unfc.choicecustomercare.adapters.MainPagerAdapter;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.BaseEntity;
import com.unfc.choicecustomercare.models.HistoryEntity;
import com.unfc.choicecustomercare.models.MenuEntity;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.service.PlaySoundService;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.CustomPreferences;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.ResponderType;
import com.unfc.choicecustomercare.utils.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity implements MenuAdapter.OnItemClickListener {

    @Bind(R.id.home_tab_pager)
    ViewPager mViewPager;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.home_tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.home_menu_button)
    ImageView mBtnOpenMenu;

    @Bind(R.id.menu_close_btn)
    LinearLayout mBtnCloseMenu;

    @Bind(R.id.menu_recycler_view)
    RecyclerView mRecyclerMenu;

    private Dialog mEmergencyDialog;

    private boolean isChargedNurse = false;

    @Override
    protected int addLayoutView() {

        return R.layout.activity_main;
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals("chargedNurse")) {
            doLogOut();
        } else {

            if(intent != null && intent.getAction() != null && intent.getAction().equals("accept"))
            {

            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(0);
            notificationManager.cancel(1);
            notificationManager.cancel(2);


            boolean isEmergency = CustomPreferences.getPreferences(Constants.PREF_IS_EMERGENCY, false);
            if (isEmergency) {

                showEmergencyDialog();
                startService(new Intent(this, PlaySoundService.class));
            }

            //	int userType = getIntent().getIntExtra(Constants.INTENT_USER_TYPE, 0);
            int userType = CustomPreferences.getPreferences(Constants.PREF_ROLE_TYPE, ResponderType.TECH);
            isChargedNurse = CustomPreferences.getPreferences(Constants.PREF_IS_CHARGED_NURSE, false);
            if (isChargedNurse) {
                userType = ResponderType.CHARGED_NURSE;
            }
            MainPagerAdapter tabAdapter = new MainPagerAdapter(getApplication(), getSupportFragmentManager(),
                    getTabs(userType));
            setupDrawerContent();
            mViewPager.setAdapter(tabAdapter);
            mViewPager.setOffscreenPageLimit(2);
            mTabLayout.setupWithViewPager(mViewPager);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {

                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                tab.setCustomView(tabAdapter.getTabView(i));
            }

            mBtnOpenMenu.setOnClickListener(this);
            mBtnCloseMenu.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.home_menu_button:

                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.menu_close_btn:

                mDrawerLayout.closeDrawers();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        CustomPreferences.getPreferences(Constants.PREF_APP_PAUSED, true);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        CustomPreferences.getPreferences(Constants.PREF_APP_PAUSED, false);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.INTENT_EMERGENCY));
    }

    /**
     * Set up drawer content
     */
    private void setupDrawerContent() {

        List<MenuEntity> menus = new ArrayList<>();
        menus.add(new MenuEntity(R.string.menu_profile, R.mipmap.ic_action_profile));
        menus.add(new MenuEntity(R.string.menu_history, R.mipmap.ic_action_history));
        menus.add(new MenuEntity(R.string.menu_take_break, R.mipmap.ic_action_break));
        menus.add(new MenuEntity(R.string.menu_change_pin, R.mipmap.ic_action_change_pin));
        menus.add(new MenuEntity(R.string.menu_logout, R.mipmap.ic_action_log_out));
        MenuAdapter adapter = new MenuAdapter(this, menus);
        Utilities.setRecyclerViewLayoutManager(this, mRecyclerMenu);
        mRecyclerMenu.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    /**
     * Do log out
     */
    private void doLogOut() {

        final Dialog dialog = LoadingDialog.show(this);
        new BaseApi(true).getInterface().doLogOut(new Callback<BaseEntity>() {
            @Override
            public void success(BaseEntity baseEntity, Response response) {

                Utilities.dismissDialog(dialog);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Utilities.dismissDialog(dialog);
            }
        });
    }

    /**
     * Get tabs base on user type
     */
    private List<String> getTabs(int userType) {

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.tab_main));
        switch (userType) {

            case ResponderType.CHARGED_NURSE:
                titles.add(getString(R.string.tab_staff));
                titles.add(getString(R.string.tab_room));
                break;

            case ResponderType.DOCTOR:
                titles.add(getString(R.string.tab_staff));
                titles.add(getString(R.string.tab_room));
                break;

            case ResponderType.NURSE:

                titles.add(getString(R.string.tab_staff));
                break;
        }

        return titles;
    }

    @Override
    public void onItemClick(MenuEntity menu) {

        mDrawerLayout.closeDrawers();
        if (menu == null) {

            return;
        }

        switch (menu.getTextResourceId()) {

            case R.string.menu_profile:

                showProfileDialog();
                break;

            case R.string.menu_history:

                getMessageHistory();
                break;

            case R.string.menu_take_break:
                getTakeABreak();
                break;

            case R.string.menu_change_pin:

                showChangePassDialog();
                break;

            case R.string.menu_logout:

                logOut();
                break;
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isClose = intent.getBooleanExtra(Constants.INTENT_EMERGENCY_CLOSE, false);
            if (!isClose) {

                Utilities.dismissDialog(mEmergencyDialog);
                return;
            }

            showEmergencyDialog();
        }
    };

    /**
     * Get message history
     */
    private void getMessageHistory() {

        final Dialog dialog = LoadingDialog.show(this);
        new BaseApi(true).getInterface().getMessageHistory(new Callback<List<HistoryEntity>>() {
            @Override
            public void success(List<HistoryEntity> messages, Response response) {

                showHistoryDialog(messages);
                Utilities.dismissDialog(dialog);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                Utilities.dismissDialog(dialog);
            }
        });
    }

    /**
     * Get take a break
     */
    private void getTakeABreak() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_take_break);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);


        dialog.show();
    }

    /**
     * Do change password
     */
    private void doChangePassword(final Dialog dialog, String oldPassword, String newPassword) {

        final Dialog loadingDialog = LoadingDialog.show(this);
        JsonObject jObject = new JsonObject();
        jObject.addProperty("oldPassword", oldPassword);
        jObject.addProperty("newPassword", newPassword);
        new BaseApi(true).getInterface().doChangePassword(jObject, new Callback<List<MessageEntity>>() {
            @Override
            public void success(List<MessageEntity> messageEntities, Response response) {

                Utilities.dismissDialog(dialog);
                Utilities.dismissDialog(loadingDialog);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

                Utilities.dismissDialog(loadingDialog);
            }
        });
    }

    /**
     * Auth dialog
     */
    private void showEmergencyDialog() {

        String strEmergencyMessage = CustomPreferences.getPreferences(Constants.PREF_EMERGENCY_MESSAGE, "");
        mEmergencyDialog = new Dialog(this);
        mEmergencyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mEmergencyDialog.setContentView(R.layout.layout_dialog_emergency);

        Window window = mEmergencyDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        mEmergencyDialog.setCanceledOnTouchOutside(false);
        mEmergencyDialog.setCancelable(false);

        TextView textView = ButterKnife.findById(mEmergencyDialog, R.id.emergency_text);
        textView.setText(strEmergencyMessage);
        mEmergencyDialog.show();
    }

    /**
     * Show history dialog
     */
    private void showHistoryDialog(List<HistoryEntity> messages) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_history);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RecyclerView historyListView = ButterKnife.findById(dialog, R.id.history_recycle_view);
        HistoryAdapter adapter = new HistoryAdapter(messages);
        Utilities.setRecyclerViewLayoutManager(this, historyListView);
        historyListView.setAdapter(adapter);

        dialog.show();
    }

    /**
     * Show update dialog
     */
    private void showProfileDialog() {

        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        startActivity(intent);
    }

    /**
     * Auth dialog
     */
    private void showChangePassDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_change_password);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        final EditText edtOldPass = ButterKnife.findById(dialog, R.id.change_password_old);
        final EditText edtNewPass = ButterKnife.findById(dialog, R.id.change_password_new);
        TextView btnCancel = ButterKnife.findById(dialog, R.id.change_password_cancel);
        TextView btnSubmit = ButterKnife.findById(dialog, R.id.change_password_submit);
        final EditText edtConfirmPass = ButterKnife.findById(dialog, R.id.change_password_confirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strOldPass = edtOldPass.getText().toString().trim();
                String strNewPass = edtNewPass.getText().toString().trim();
                String strConfirmPass = edtConfirmPass.getText().toString().trim();
                if (strOldPass.equals("") || strNewPass.equals("") || strConfirmPass.equals("")) {

                    Utilities.showAlertDialog(MainActivity.this, getString(R.string.app_name),
                            getString(R.string.invalid_information), getString(R.string.ok), "", null, null, false);
                    return;
                }

                if (!strNewPass.equals(strConfirmPass)) {

                    Utilities.showAlertDialog(MainActivity.this, getString(R.string.app_name),
                            getString(R.string.password_does_not_match), getString(R.string.ok), "", null, null, false);
                    return;
                }

                doChangePassword(dialog, strOldPass, strNewPass);
            }
        });

        dialog.show();
    }

    /**
     * Do logout part
     */
    private void logOut() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_logout);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        TextView btnNo = ButterKnife.findById(dialog, R.id.log_out_no_btn);
        TextView btnLogOut = ButterKnife.findById(dialog, R.id.log_out_btn);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                doLogOut();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // Show
        dialog.show();
    }
}
