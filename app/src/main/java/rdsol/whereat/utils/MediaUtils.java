package rdsol.whereat.utils;

import android.content.Context;
import android.media.MediaPlayer;

import rdsol.whereat.R;

public class MediaUtils {

    private MediaUtils ( ) {

    }


    public static void accomplishedSoundEffect( Context context ){
        MediaPlayer mediaPlayer = MediaPlayer.create(  context , R.raw.accomplished);
        mediaPlayer.start();
    }
}
