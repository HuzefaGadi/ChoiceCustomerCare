package com.unfc.choicecustomercare.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.unfc.choicecustomercare.R;

/**
 * Hai Nguyen - 8/31/15.
 */
public class PlaySoundService extends Service {

    private MediaPlayer mPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {

        mPlayer = MediaPlayer.create(this, R.raw.osmium);
        mPlayer.setLooping(true);
        mPlayer.setVolume(5,5);
    }

    public void onDestroy() {
        mPlayer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
