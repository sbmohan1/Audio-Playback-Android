package edu.asu.bscs.sbmohan1.androidaudioplayback;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

/**
 * Copyright 2015 Sishir Mohan
 * <p/>
 * The Instructor and the TA have the right to build and evaluate this app and the code.
 * <p/>
 * Purpose:
 *
 * @author Sishir Mohan sbmohan1@asu.edu
 *         Computer Science, BS
 * @version January 24, 2015
 *          Created by sishirmohan on 1/24/15.
 */
public class AndroidAudioPlayback extends Service implements MediaPlayer.OnCompletionListener {
    MediaPlayer player;
    private boolean isPlaying = false;
    public static final String COMMAND = "CMD";
    public static final int START = 0;
    public static final int STOP = 1;
    public static final int PAUSE = 2;
    public static final int RESUME = 3;
    public static final int NONE = 4;

    @Override
    public void onCreate(){
        super.onCreate();
        android.util.Log.d(getClass().getSimpleName(), "in onCreate()");
        player = MediaPlayer.create(getApplicationContext(),R.raw.newslang);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(this);
        isPlaying = false;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        int command = intent.getIntExtra(COMMAND, NONE);
        android.util.Log.d(getClass().getSimpleName(), "in onStartCommand()" + command);
        if(command == PAUSE)
        {
            if(player!=null && isPlaying)
            {
                player.pause();
                isPlaying = false;
            }
        }
        else if(command == RESUME)
        {
            player.start();
            isPlaying = true;
        }
        else if(command == START)
        {
            if(isPlaying)
            {
                player.pause();
                player.seekTo(0);
            }
            player.start();
            isPlaying = true;
        }
        else if(command == STOP)
        {
            isPlaying = false;
            if(player != null)
            {
                player.stop();
                player.release();
            }
        }
        return (START_NOT_STICKY);
    }

    @Override
    public void onDestroy()
    {
        if(player != null)
        {
            player.stop();
            player.release();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) { return(null);}

    @Override
    public boolean onUnbind(Intent intent) { return false;}

    public void onCompletion(MediaPlayer mp)
    {
        mp.seekTo(0);
        mp.start();
        isPlaying = true;
    }

}
