/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unfc.choicecustomercare.gcmservices;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;
import com.unfc.choicecustomercare.R;
import com.unfc.choicecustomercare.activity.MainActivity;
import com.unfc.choicecustomercare.api.BaseApi;
import com.unfc.choicecustomercare.models.BaseEntity;
import com.unfc.choicecustomercare.models.MessageEntity;
import com.unfc.choicecustomercare.service.PlaySoundService;
import com.unfc.choicecustomercare.utils.Constants;
import com.unfc.choicecustomercare.utils.CustomPreferences;
import com.unfc.choicecustomercare.utils.LoadingDialog;
import com.unfc.choicecustomercare.utils.Utilities;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyGcmListenerService extends GcmListenerService {

	/**
	 * Called when message is received.
	 *
	 * @param from
	 *            SenderID of the sender.
	 * @param data
	 *            Data bundle containing message data as key/value pairs. For
	 *            Set of keys use data.keySet().
	 */
	@Override
	public void onMessageReceived(String from, Bundle data) {

		CustomPreferences.init(this);
		String message = data.getString("text");
		if (data.containsKey("emergency")) {

			Intent intent = new Intent(Constants.INTENT_EMERGENCY);
			String isEmergency = data.getString("emergency");
			if (isEmergency.equals("true")) {

				CustomPreferences.setPreferences(Constants.PREF_IS_EMERGENCY, true);
				CustomPreferences.setPreferences(Constants.PREF_EMERGENCY_MESSAGE, message);
				startService(new Intent(this, PlaySoundService.class));
				// PowerManager pm = (PowerManager)
				// this.getSystemService(Context.POWER_SERVICE);
				// PowerManager.WakeLock wl = pm
				// .newWakeLock(PowerManager.FULL_WAKE_LOCK |
				// PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
				// wl.acquire(10000);
				sendNotification(message);
			} else {

				CustomPreferences.setPreferences(Constants.PREF_EMERGENCY_MESSAGE, "");
				CustomPreferences.setPreferences(Constants.PREF_IS_EMERGENCY, false);
				stopService(new Intent(this, PlaySoundService.class));
			}

			intent.putExtra(Constants.INTENT_EMERGENCY_CLOSE, isEmergency.equals("true"));
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		} else {

			Intent intent = new Intent(Constants.INTENT_UPDATE_MESSAGE);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			sendNotification(message);
		}
	}

	/**
	 * Create and show a simple notification containing the received GCM
	 * message.
	 *
	 * @param message
	 *            GCM message received.
	 */
	private void sendNotification(String message) {

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

		Intent intentAccept = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingAccept = PendingIntent.getActivity(this, 1, intentAccept, PendingIntent.FLAG_ONE_SHOT);
//
		Intent intentDecline = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingDecline = PendingIntent.getActivity(this, 2, intentDecline, PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(message).setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent)
				.setAutoCancel(false)
				.addAction(R.drawable.accept_ico, "Accept", pendingAccept)
				.addAction(R.drawable.decline_ico, "Decline", pendingDecline);
        notificationBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
        notificationBuilder.setAutoCancel(true);


		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0, notificationBuilder.build());

	}

    /**
     * Do accept message
     */
    private void doAcceptMessage(MessageEntity message) {

        final Dialog dialog = LoadingDialog.show(getApplicationContext());
        new BaseApi(true).getInterface().doAcceptMessage(message.getMessageQueueId(), message.getMessageTypeId(),
                new Callback<BaseEntity>() {
                    @Override
                    public void success(BaseEntity baseEntity, Response response) {


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

        final Dialog dialog = LoadingDialog.show(getApplicationContext());
        new BaseApi(true).getInterface().doDenyMessage(message.getMessageQueueId(), message.getId(),
                new Callback<BaseEntity>() {
                    @Override
                    public void success(BaseEntity baseEntity, Response response) {


                        Utilities.dismissDialog(dialog);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                        Toast.makeText(getApplicationContext(), retrofitError.getMessage(), Toast.LENGTH_LONG).show();
                        Utilities.dismissDialog(dialog);
                    }
                });
    }
}
