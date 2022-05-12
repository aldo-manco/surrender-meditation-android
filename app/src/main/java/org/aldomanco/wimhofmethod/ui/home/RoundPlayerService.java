package org.aldomanco.wimhofmethod.ui.home;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

import org.aldomanco.wimhofmethod.R;

public class RoundPlayerService extends Service {

    //creating a mediaplayer object
    private MediaPlayer player;

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {

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

        return mBinder;
    }

    public class LocalBinder extends Binder {
        public RoundPlayerService getServerInstance() {
            return RoundPlayerService.this;
        }
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pauseMediaPlayer(){

        try{
            if (player.isPlaying()) {
                player.pause();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void resumeMediaPlayer(){

        try{
            if (!player.isPlaying()) {
                player.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {

        //stopping the player when service is destroyed
        player.stop();

        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //stopping the player when service is destroyed
        player.stop();
    }
}
