package org.aldomanco.wimhofmethod.ui.home;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

import org.aldomanco.wimhofmethod.R;

public class RoundPlayerService extends Service {

    //creating a mediaplayer object
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if ((int)intent.getExtras().get("remaining") == 0){
            player = MediaPlayer.create(this,
                    R.raw.round1);

        }else if ((int)intent.getExtras().get("remaining") == 1){
            player = MediaPlayer.create(this,
                    R.raw.round2edited);

        }else if ((int)intent.getExtras().get("remaining") == 2){
            player = MediaPlayer.create(this,
                    R.raw.round3edited);
        }

        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);

        //staring the player
        player.start();

        //we have some options for service
        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //stopping the player when service is destroyed
        player.stop();
    }
}
