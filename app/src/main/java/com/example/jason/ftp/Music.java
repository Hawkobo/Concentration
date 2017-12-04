package com.example.jason.ftp;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;

public class Music extends Activity
{
    private static Music sMusic;
    private MediaPlayer player;

    public static Music get(Context context)
    {
        if (sMusic == null)
        {
            sMusic = new Music(context);
        }
        return sMusic;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        player.pause();
    }

    private Music(Context context)
    {
        player = MediaPlayer.create(context, R.raw.background);
        player.setLooping(true);
        player.start();
    }
}