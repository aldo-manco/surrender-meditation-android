package org.aldomanco.wimhofmethod.ui.yoga_section;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.aldomanco.wimhofmethod.MainActivity;
import org.aldomanco.wimhofmethod.R;

public class YogaService extends Service {

    //creating a mediaplayer object
    private MediaPlayer player;

    IBinder mBinder = new LocalBinderYoga();

    @Override
    public IBinder onBind(Intent intent) {

        if (((int)intent.getExtras().get("length")) == 0){
            player = MediaPlayer.create(this,
                    R.raw.fast_back_stretch);

        }else if (((int)intent.getExtras().get("length"))==1){
            player = MediaPlayer.create(this,
                    R.raw.recovery_yoga);
        }

        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);

        //staring the player
        player.start();

        return mBinder;
    }

    public class LocalBinderYoga extends Binder {
        public YogaService getServerInstance() {
            return YogaService.this;
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
