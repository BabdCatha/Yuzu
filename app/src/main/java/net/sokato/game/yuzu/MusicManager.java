//https://stackoverflow.com/questions/23126553/how-to-play-music-in-the-background-through-out-the-app-continuously?noredirect=1&lq=1

package net.sokato.game.yuzu;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.IOException;

public class MusicManager {
    private static MediaPlayer player;
    private static boolean keepMusicOn;
    private static boolean musicIsOn = false;
    private static float currentVolume;
    private static int maxVolume = 100;
    private static  int LinearVolume = 100;

    public static void iAmIn(Context context){
    if (player == null && musicIsOn){
        player = MediaPlayer.create(context, R.raw.main_theme);
        player.setLooping(true);

        try{
            player.prepare();
        }
            catch (IllegalStateException e){}
            catch (IOException e){}
        }

    if(musicIsOn && !player.isPlaying()){
        player.start();
    }

    keepMusicOn= false;
    }

    public static void keepMusicOn(){
        keepMusicOn= true;
    }

    public static void iAmLeaving(){

        if(player != null && !keepMusicOn){
            player.pause();
        }
    }

    public static void setVolume(float volume, boolean FromPrefs){
        if(!FromPrefs) { //So that it is always at the seekbar value
            LinearVolume = Math.round(volume);
        }
        if(!FromPrefs && musicIsOn) {
            float log1 = (float) (Math.log(maxVolume - volume) / Math.log(maxVolume));
            player.setVolume(1 - log1, 1 - log1);
            currentVolume = 1 - log1;
        }
        else if(FromPrefs && musicIsOn){
            player.setVolume(volume, volume);
        }
    }

    public static void saveVolume(Context context){
        SharedPreferences MusicPrefs = context.getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor MusicEditor = MusicPrefs.edit();
        MusicEditor.putFloat("CurrentVolume", currentVolume).apply();
        MusicEditor.putInt("LinearVolume", LinearVolume).apply();
    }

    public static void setMusicOn(boolean On, Context context){
        musicIsOn = On;
        SharedPreferences MusicPrefs = context.getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor MusicEditor = MusicPrefs.edit();
        MusicEditor.putBoolean("IsMusicOn", musicIsOn).apply();
    }

    public static void setMusicParameters(Context context){
        SharedPreferences MusicPrefs = context.getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);
        musicIsOn = MusicPrefs.getBoolean("IsMusicOn", true);
        currentVolume  = MusicPrefs.getFloat("CurrentVolume", 1.0f);
        LinearVolume = MusicPrefs.getInt("LinearVolume", 100);
    }

    public static void ResetVolume(){
        setVolume(currentVolume, true);
    }

    public static boolean isMusicActivated(){
        return musicIsOn;
    }

    public static int getCurrentVolume(Context context){
        SharedPreferences MusicPrefs = context.getSharedPreferences("MusicPreferences", Context.MODE_PRIVATE);
        return  MusicPrefs.getInt("LinearVolume", 100);
    }
}
