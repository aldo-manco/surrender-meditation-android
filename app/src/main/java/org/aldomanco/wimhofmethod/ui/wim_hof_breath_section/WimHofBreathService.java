package org.aldomanco.wimhofmethod.ui.wim_hof_breath_section;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import org.aldomanco.wimhofmethod.R;

public class WimHofBreathService extends Service {

    //creating a mediaplayer object
    private MediaPlayer player;
    private boolean playOneTime = false;

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {

        if ((int)intent.getExtras().get("remaining") == 0){
            player = MediaPlayer.create(this,
                    R.raw.wim_hof_method_round_1);

        }else if ((int)intent.getExtras().get("remaining") == 1){
            player = MediaPlayer.create(this,
                    R.raw.wim_hof_method_round_2);

        }else if ((int)intent.getExtras().get("remaining") == 2){
            player = MediaPlayer.create(this,
                    R.raw.wim_hof_method_round_3);
        }

        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(false);

        if (!playOneTime){
            //staring the player
            player.start();
        }

        playOneTime = true;

        return mBinder;
    }

    public class LocalBinder extends Binder {
        public WimHofBreathService getServerInstance() {
            return WimHofBreathService.this;
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
